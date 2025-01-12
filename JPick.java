/*
* JPick.java
 *
 * JPick is a free tool for picking first arrivals from SEG-2
 * seismic data.  JPick uses the JFreeChart library from http://www.jfree.org/jfreechart/
 */

/**
*
* Copyright 2004, Will Brunner
* will@larkinsoap.com
*
* 117 grove school road
* Brooktondale NY 14817
*
*This file is part of the JPick first arrival picker.

JPick is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

JPick is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with JPick; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/
import java.io.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.*;
import org.jfree.chart.axis.*;
import org.jfree.data.*;

public class JPick extends javax.swing.JFrame {

    public JPick() {
        initComponents();
    }
   
    private void initComponents() {
        loaded = false;
        fileChooser = new javax.swing.JFileChooser();
        homeDir = fileChooser.getCurrentDirectory().getAbsolutePath();
        try{
            File file = new File(homeDir+File.separator+".JPick");
            if(file.exists()){
                BufferedReader br = new BufferedReader(new FileReader(file));
                fileChooser.setCurrentDirectory(new File(br.readLine()));
            }
        }
        catch(Exception e){}
        messageDialog = new javax.swing.JDialog();
        pickFrame = new javax.swing.JFrame();
        traceLabel = new javax.swing.JLabel();
        travelTimeFrame = new javax.swing.JFrame();
        filterFrame = new javax.swing.JFrame();
        filterButton = new javax.swing.JButton();
        mainMenuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        openMenuItem = new javax.swing.JMenuItem();
        exportMenuItem = new javax.swing.JMenuItem();
        quitMenuItem = new javax.swing.JMenuItem();
        filterMenu = new javax.swing.JMenu();
        applyFilterMenuItem = new javax.swing.JMenuItem();
        removeFilterMenuItem = new javax.swing.JMenuItem();
        lowField = new javax.swing.JTextField();
        lowCornerField = new javax.swing.JTextField();
        highCornerField = new javax.swing.JTextField();
        highField = new javax.swing.JTextField();
        applyToAllCheckBox = new javax.swing.JCheckBox();
        bandpassCheckBox = new javax.swing.JCheckBox();
        pickFrame.setTitle("Pick Window");
        pickFrame.setLocationRelativeTo(this);
        pickFrame.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pickFrameKeyPressed(evt);
            }
        });

        traceLabel.setBackground(new java.awt.Color(255, 255, 255));
        traceLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        traceLabel.setText("Trace 1");
        pickFrame.getContentPane().add(traceLabel, java.awt.BorderLayout.NORTH);


        filterFrame.getContentPane().setLayout(new java.awt.GridLayout(7, 2));
        filterFrame.getContentPane().add(lowField);
        filterFrame.getContentPane().add(new javax.swing.JLabel("low cutoff"));
        filterFrame.getContentPane().add(lowCornerField);
        filterFrame.getContentPane().add(new javax.swing.JLabel("low corner"));
        filterFrame.getContentPane().add(highCornerField);
        filterFrame.getContentPane().add(new javax.swing.JLabel("high corner"));
        filterFrame.getContentPane().add(highField);
        filterFrame.getContentPane().add(new javax.swing.JLabel("high cutoff"));
        filterFrame.getContentPane().add(bandpassCheckBox);
        filterFrame.getContentPane().add(new javax.swing.JLabel("Bandpass"));
        filterFrame.getContentPane().add(applyToAllCheckBox);
        filterFrame.getContentPane().add(new javax.swing.JLabel("Apply filter globally"));
        filterFrame.getContentPane().add(filterButton);
        filterFrame.pack();
        filterFrame.setSize(300,200);
        filterFrame.setTitle("Filter parameters");
       
        filterButton.setText("Apply");
        filterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterButtonActionPerformed(evt);
            }
        });

        filterFrame.getContentPane().add(filterButton);

        travelTimeFrame.setTitle("Travel Time");
        travelTimeFrame.setLocationRelativeTo(this);
        travelTimeFrame.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                travelTimeFrameKeyPressed(evt);
            }
        });
        
        setTitle("JPick--A free first arrival picker");
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        fileMenu.setText("File");
        openMenuItem.setText("Open");
        openMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMenuItemActionPerformed(evt);
            }
        });

        fileMenu.add(openMenuItem);

        exportMenuItem.setText("Export Picks");
        exportMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportMenuItemActionPerformed(evt);
            }
        });

        fileMenu.add(exportMenuItem);

        quitMenuItem.setText("Quit");
        quitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitMenuItemActionPerformed(evt);
            }
        });

        fileMenu.add(quitMenuItem);

        mainMenuBar.add(fileMenu);

        filterMenu.setText("Filter");
        applyFilterMenuItem.setText("Apply Filter");
        applyFilterMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyFilterMenuItemActionPerformed(evt);
            }
        });

        filterMenu.add(applyFilterMenuItem);

        removeFilterMenuItem.setText("Remove Filter");
        removeFilterMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeFilterMenuItemActionPerformed(evt);
            }
        });

        filterMenu.add(removeFilterMenuItem);

        mainMenuBar.add(filterMenu);

        setJMenuBar(mainMenuBar);

        pack();
    }

    private void quitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        // call cleanup and exit method
        quit();
    }
    private void removeFilterMenuItemActionPerformed(java.awt.event.ActionEvent evt) {                                                                                      //remove all filters
        if(loaded){
            ts[currentFile].getTrace(currentTrace).restore();
            updateTracePlot();
        }
    }

    private void filterButtonActionPerformed(java.awt.event.ActionEvent evt) {

        if(loaded){
            Filter filter = new Filter(Double.parseDouble(lowField.getText()),
                                       Double.parseDouble(lowCornerField.getText()),
                                       Double.parseDouble(highCornerField.getText()),
                                       Double.parseDouble(highField.getText()),
                                       bandpassCheckBox.isSelected());

            if(applyToAllCheckBox.isSelected()){
                for(int i = 0; i < numFiles; i++){
                    for(int j = 0; j < ts[i].number; j++){
                        Trace nt = ts[i].getTrace(j);
                        nt.setFilter(filter);
                        nt.applyFilter();
                    }
                }
            }
            else{
                Trace nt = ts[currentFile].getTrace(currentTrace);
                nt.setFilter(filter);
                nt.applyFilter();
            }


            //cleanup
            updateTracePlot();
            filterFrame.dispose();

        }
        else{
            showMessageDialog("No files loaded yet!");
        }
        
    }

    private void applyFilterMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterMenuItemActionPerformed

        if(loaded){
            Trace nt = ts[currentFile].getTrace(currentTrace);
            Filter gvf = nt.getFilter();
            if (gvf != null){
                lowField.setText(""+gvf.getLow());
                lowCornerField.setText(""+gvf.getLowCorner());
                highCornerField.setText(""+gvf.getHighCorner());
                highField.setText(""+gvf.getHigh());
                bandpassCheckBox.setSelected(gvf.isBandpass());
            }
            filterFrame.show();
        }
        else{
            showMessageDialog("No files loaded yet!");
        }

    }

    private void travelTimeFrameKeyPressed(java.awt.event.KeyEvent evt) {
        // if a key is pressed in another window, pass it to main window
        formKeyPressed(evt);
    }

    private void pickFrameKeyPressed(java.awt.event.KeyEvent evt) {
        // if a key is pressed in another window, pass it to main window
        formKeyPressed(evt);
    }

    private void formKeyPressed(java.awt.event.KeyEvent evt) {
        //handle key presses
        if(loaded){
        //"p" goes to previous file
        if(evt.getKeyCode() == java.awt.event.KeyEvent.VK_P && currentFile > 0){
            currentFile--;
            showPlot();
        }

        //"n" goes to next file
        if(evt.getKeyCode() == java.awt.event.KeyEvent.VK_N && currentFile < numFiles-1){
            currentFile++;
            showPlot();
        }

        //"+" increases gain in pick window 
        if(evt.getKeyCode()== java.awt.event.KeyEvent.VK_EQUALS){
            scale *= 1.5;
            updateTracePlot();
        }

        //"-" decreases gain in pick window
        if(evt.getKeyCode()== java.awt.event.KeyEvent.VK_MINUS){
            scale /= 1.5;
            updateTracePlot();
        }

        //left arrow puts trace to left in pick window
        if(evt.getKeyCode()== java.awt.event.KeyEvent.VK_LEFT && currentTrace > 0){
            currentTrace--;
            //if this trace hasn't been picked yet, move pick cursor even with that of last trace
            //and resize pick view to match
            if(!ts[currentFile].getTrace(currentTrace).isPicked()){
                double pk = ts[currentFile].getTrace(currentTrace+1).getPick();
                double lb = ts[currentFile].getTrace(currentTrace+1).getRange().getLowerBound();
                double len = ts[currentFile].getTrace(currentTrace+1).getRange().getLength();
                ts[currentFile].getTrace(currentTrace).setPick(pk);
                ts[currentFile].getTrace(currentTrace).setRange(new Range(lb,lb+len));
            }
            updateMarker();
            updateTracePlot();
            updatePicks();
            traceLabel.setText("Trace "+(currentTrace+1));
        }

        //right arrow puts trace to right in pick window
        if(evt.getKeyCode()== java.awt.event.KeyEvent.VK_RIGHT && currentTrace < ts[currentFile].number-1){
            currentTrace++;
            //if this trace hasn't been picked yet, (and we're beyond trace 2) extrapolate last two picks (to left) and move cursor
            //and resize view
            if(!ts[currentFile].getTrace(currentTrace).isPicked()){
                if(currentTrace >=2){
                    double pk1 = ts[currentFile].getTrace(currentTrace-1).getPick();
                    double pk2 = ts[currentFile].getTrace(currentTrace-2).getPick();
                    ts[currentFile].getTrace(currentTrace).setPick(pk1+(pk1-pk2));
                    double lb = ts[currentFile].getTrace(currentTrace-1).getRange().getLowerBound();
                    double len = ts[currentFile].getTrace(currentTrace-1).getRange().getLength();
                    ts[currentFile].getTrace(currentTrace).setRange(new Range(lb+(pk1-pk2),lb+len+(pk1-pk2)));
                }
                else{
                    double pk = ts[currentFile].getTrace(currentTrace-1).getPick();
                    double lb = ts[currentFile].getTrace(currentTrace-1).getRange().getLowerBound();
                    double len = ts[currentFile].getTrace(currentTrace-1).getRange().getLength();
                    ts[currentFile].getTrace(currentTrace).setPick(pk);
                    ts[currentFile].getTrace(currentTrace).setRange(new Range(lb,lb+len));
                }
            }
            updateMarker();
            updateTracePlot();
            updatePicks();
            traceLabel.setText("Trace "+(currentTrace+1));
        }
        double lb = ts[currentFile].getTrace(currentTrace).getRange().getLowerBound();
        double len = ts[currentFile].getTrace(currentTrace).getRange().getLength();
        double cv = ts[currentFile].getTrace(currentTrace).getRange().getCentralValue();
        double pk = ts[currentFile].getTrace(currentTrace).getPick();
        //"e" expands pick view in time
        if(evt.getKeyCode()== java.awt.event.KeyEvent.VK_E){
            ts[currentFile].getTrace(currentTrace).setRange(new Range(cv-(len*1.5)/2,cv+(len*1.5/2)));
            updateTracePlot();
        }
        //"c" contracts it
        if(evt.getKeyCode()== java.awt.event.KeyEvent.VK_C){
            ts[currentFile].getTrace(currentTrace).setRange(new Range(pk-(len/1.5)/2,pk+((len/1.5)/2)));
            updateTracePlot();
        }
        //page down moves the pick view down by it's length.  Not very useful
        if(evt.getKeyCode()== java.awt.event.KeyEvent.VK_PAGE_DOWN){
            ts[currentFile].getTrace(currentTrace).setRange(new Range(lb+len,lb+2*len));
            cv = ts[currentFile].getTrace(currentTrace).getRange().getCentralValue();
            ts[currentFile].getTrace(currentTrace).setPick(cv);
            updateMarker();
            updateTracePlot();
        }

        //moves view up
        if(evt.getKeyCode()== java.awt.event.KeyEvent.VK_PAGE_UP){
            ts[currentFile].getTrace(currentTrace).setRange(new Range(lb-len,lb));
            cv = ts[currentFile].getTrace(currentTrace).getRange().getCentralValue();
            ts[currentFile].getTrace(currentTrace).setPick(cv);
            updateMarker();
            updateTracePlot();
        }

        //moves pick cursor up
        if(evt.getKeyCode()== java.awt.event.KeyEvent.VK_UP){
            ts[currentFile].getTrace(currentTrace).setPick(ts[currentFile].getTrace(currentTrace).getPick()-len/100);
            updateMarker();
        }
        //moves pick cursor down
        if(evt.getKeyCode()== java.awt.event.KeyEvent.VK_DOWN){
            ts[currentFile].getTrace(currentTrace).setPick(ts[currentFile].getTrace(currentTrace).getPick()+len/100);
            updateMarker();
        }
        }

    }

    private void exportMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        // saves picks in a .pic file
        if(loaded){
            try {
                fileChooser.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);
                fileChooser.setDialogTitle("Choose directory for pick files");
                int returnVal = fileChooser.showSaveDialog(this);
                if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION){
                    for(int i = 0; i < numFiles; i++){
                        File file = new File(fileChooser.getSelectedFile().getAbsolutePath() ,  inFile[i].f.getName()+".pic");
                        OutputFile outFile = new OutputFile(file);
                        outFile.writeOut(ts[i]);
                    }
                }
            }
            catch (java.awt.HeadlessException e1) {
                e1.printStackTrace();
            }
        }
        else{
            showMessageDialog("No files loaded yet!");
        }
    }
    private void updateMarker(){

        //update pick cursor
        pPlot.clearDomainMarkers();
        pPlot.addDomainMarker(new ValueMarker(ts[currentFile].getTrace(currentTrace).getPick(),java.awt.Color.RED,new java.awt.BasicStroke(0.5f),java.awt.Color.BLACK,new java.awt.BasicStroke(0.5f),0.8f));

        //update marker on travel time plot
        ttPlot.clearDomainMarkers();
        ttPlot.addDomainMarker(new ValueMarker(ts[currentFile].getTrace(currentTrace).getPhoneLocation(),java.awt.Color.BLUE, new java.awt.BasicStroke(0.5f),java.awt.Color.BLACK,new java.awt.BasicStroke(0.5f),0.8f));

        //update marker on main plot
        plot.clearRangeMarkers();
        plot.addRangeMarker(new ValueMarker(currentTrace+1,java.awt.Color.BLUE,new java.awt.BasicStroke(0.5f),java.awt.Color.BLACK,new java.awt.BasicStroke(0.5f),0.8f));
    }

    private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        // open one or more seg-2 files

       
        fileChooser.setMultiSelectionEnabled(true);

        try {
            int returnVal = fileChooser.showOpenDialog(this);
            if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION){
                File[] file = fileChooser.getSelectedFiles();
                numFiles = file.length;
                inFile = new InputFile[numFiles];
                ts = new TraceSet[numFiles];
                for(int i=0; i < numFiles; i++){
                    inFile[i] = new InputFile(file[i]);
                }
                showPlot();
                loaded = true;

            }
        }
        catch (java.awt.HeadlessException e1) {
            e1.printStackTrace();
        }
        catch (java.util.zip.DataFormatException df){

            showMessageDialog("bad seg-2 file");
        }
           }

    private void showMessageDialog(String s){
        messageDialog.setSize(200,100);

        messageDialog.getContentPane().add(new javax.swing.JLabel(s));
        messageDialog.setModal(true);
        messageDialog.show();

    }
    public void showPlot(){
        //plot everything

        //put the chart panel on the main window
        getContentPane().add(cp, java.awt.BorderLayout.CENTER);
        getContentPane().validate();

        //set travel time plots right-way up, the other ones sideways
        PlotOrientation ms = PlotOrientation.VERTICAL;
        ttPlot.setOrientation(ms);
        ms = PlotOrientation.HORIZONTAL;
        plot.setOrientation(ms);
        pPlot.setOrientation(ms);
         pCp.setVerticalAxisTrace(true);
        pCp.addChartMouseListener(new ChartMouseListener(){
            public void chartMouseClicked(ChartMouseEvent event){
                pickClick(event);
            }
            public void chartMouseMoved(ChartMouseEvent event){
                //int y = event.getTrigger().getY();
                //mousePos = pDomainAxis.java2DToValue(y, pCp.getScaledDataArea(),org.jfree.ui.RectangleEdge.BOTTOM);
        //System.out.println(gvdc.mousePos);
            }
        });
        
        //set main window title to current file name
        this.setTitle("JPick "+ inFile[currentFile].f.getName());

        //put picks on main plot
        plot.setDataset(1,mainPickCollection);
        plot.setRenderer(1,mainPickRenderer);

        //load data if haven't already
        if(ts[currentFile] == null) ts[currentFile] = inFile[currentFile].getTraceSet();

        //make an array for all the data series
        series = new XYSeries[ts[currentFile].number];

        //plot with time increasing downward
        domainAxis.setInverted(true);


        rangeAxis.setTickLabelsVisible(false);
        renderer.setPaint(java.awt.Color.BLACK);
        pDomainAxis.setInverted(true);
        pRangeAxis.setRange(new Range(-1,1));
        pRangeAxis.setTickLabelsVisible(false);
        pRenderer.setPaint(java.awt.Color.BLACK);


        //plot pick plot and travel time plot
        pickFrame.getContentPane().add(pCp, java.awt.BorderLayout.CENTER);
        pickFrame.setSize(300,400);
        pickFrame.show();
        travelTimeFrame.getContentPane().add(ttCp, java.awt.BorderLayout.CENTER);
        travelTimeFrame.setSize(400,300);
        travelTimeFrame.show();
        updateMainPlot();
        updateTracePlot();
        updatePicks();
    }
    public void updateMainPlot(){
        collection.removeAllSeries();


        for(int i=0; i<ts[currentFile].number; i++){
            series[i] = ts[currentFile].getTrace(i).getSeries(i*OFFSET+1);

            collection.addSeries(series[i]);
        }


    }
    public void pickClick(ChartMouseEvent event){
        //int x = event.getTrigger().getX();
        int y = event.getTrigger().getY();
        double yy = pDomainAxis.java2DToValue(y, pCp.getScaledDataArea(),org.jfree.ui.RectangleEdge.LEFT);
        ts[currentFile].getTrace(currentTrace).setPick(yy);
        updateMarker();
        //System.out.println(xx+"\t"+y);

    }
    
    private void updatePicks(){
        mainPickCollection.removeAllSeries();
        mainPickSeries = ts[currentFile].getPickSeries(OFFSET);
        mainPickCollection.addSeries(mainPickSeries);
        ttCollection.removeAllSeries();
        ttSeries = ts[currentFile].getTtSeries();
        ttCollection.addSeries(ttSeries);
    }
    private void updateTracePlot(){
        pCollection.removeAllSeries();
        pDomainAxis.setRange(ts[currentFile].getTrace(currentTrace).getRange());
        pSeries = ts[currentFile].getTrace(currentTrace).getSeries(0,scale);
        pCollection.addSeries(pSeries);
    }
    private void quit(){
if(loaded){
        for(int i = 0; i< numFiles; i++){
            inFile[i].close();
        }
        try{
            File file = new File(homeDir+File.separator+".JPick");
            System.out.println(file.getAbsolutePath());
            if (file.exists()) {
                boolean b = file.delete();
            }
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            String dir = fileChooser.getCurrentDirectory().getAbsolutePath();
            pw.println(dir);
            System.out.println(dir);
            pw.close();
        }
        catch(Exception e){}
        }
        System.exit(0);
    }
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        quit();
    }

    public static void main(String args[]) {

        JPick jp = new JPick();
        jp.setSize(400,300);
        jp.show();
    }

    private javax.swing.JButton filterButton;
    private javax.swing.JDialog messageDialog;  //pops up to show messages
    private javax.swing.JFileChooser fileChooser; 
    private javax.swing.JFrame pickFrame;  //frame with single trace
    private javax.swing.JFrame travelTimeFrame; //travel time frame
    private javax.swing.JFrame filterFrame;  //frame for choosing filter options
    private javax.swing.JLabel traceLabel;  //has name of current trace
    private javax.swing.JMenu fileMenu;  //various menu parts:
    private javax.swing.JMenu filterMenu;
    private javax.swing.JMenuBar mainMenuBar;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JMenuItem exportMenuItem;
    private javax.swing.JMenuItem quitMenuItem;
    private javax.swing.JMenuItem applyFilterMenuItem;
    private javax.swing.JMenuItem removeFilterMenuItem;
    private boolean loaded; //whether or not any files have been loaded
    private String homeDir;//user's home directory
    private int OFFSET =1;  //amount each trace is offset in main spread window
    private int currentTrace = 0; //trace displayed in pick window
    private int numFiles = 0;    //number of files currently loaded
    private int currentFile = 0; //shot file shown in main window
    private double scale = 1;    //relative gain of trace shown in pick window
    private InputFile[] inFile;  //array of seg-2 file readers
    private TraceSet[] ts;       //each TraceSet in this array holds an entire shot 
    private boolean[] ft;       //whether or not to filter a given order of wavelet
    private javax.swing.JCheckBox bandpassCheckBox;//checkbox to set bandpass
    private javax.swing.JCheckBox applyToAllCheckBox;//checkbox to filter all traces
    private javax.swing.JTextField lowField;//low cutoff of filter
    private javax.swing.JTextField lowCornerField;
    private javax.swing.JTextField highCornerField;
    private javax.swing.JTextField highField;
    private XYSeries[] series; //each XYSeries is a trace in the main window
    private XYSeries mainPickSeries;//series in pick window
    private XYSeriesCollection collection = new XYSeriesCollection();//series collection for main window
    private XYSeriesCollection mainPickCollection = new XYSeriesCollection();//series collection for picks in main window
    private NumberAxis domainAxis = new NumberAxis("Time/ms");//vertical axis in main window
    private NumberAxis rangeAxis = new NumberAxis("Station");//horizontal axis in main window
    private DefaultXYItemRenderer renderer = new DefaultXYItemRenderer();//renderer for main window
    private StandardXYItemRenderer mainPickRenderer = new StandardXYItemRenderer(StandardXYItemRenderer.SHAPES);//renderer for pick window
    private XYPlot plot = new XYPlot(collection,domainAxis,rangeAxis,renderer);//main window plot
    private NumberAxis ttDomainAxis = new NumberAxis("Distance");//horizontal axis in travel time window
    private NumberAxis ttRangeAxis = new NumberAxis("Time/ms");//vertical axis in travel time window
    private XYSeries ttSeries;//series in travel time window
    private XYSeriesCollection ttCollection = new XYSeriesCollection();//travel time series collection
    private StandardXYItemRenderer ttRenderer = new StandardXYItemRenderer(StandardXYItemRenderer.SHAPES);//travel time renderer
    private XYPlot ttPlot = new XYPlot(ttCollection,ttDomainAxis,ttRangeAxis,ttRenderer);//travel time plot
    private JFreeChart chart = new JFreeChart(plot);//main chart
    private ChartPanel cp = new ChartPanel(chart);//main chart panel
    private JFreeChart ttChart = new JFreeChart(ttPlot);//travel time chart
    private ChartPanel ttCp = new ChartPanel(ttChart);//travel time chart panel
    private XYSeries pSeries;//series for pick window
                                                                                                                 //XYSeries pPickSeries;
    private XYSeriesCollection pCollection = new XYSeriesCollection();//collection for pick window
    private NumberAxis pDomainAxis = new NumberAxis("Time/ms");//axes for pick window
    private NumberAxis pRangeAxis = new NumberAxis("");
    private DefaultXYItemRenderer pRenderer = new DefaultXYItemRenderer();//renderer for pick window
    private XYPlot pPlot = new XYPlot(pCollection,pDomainAxis,pRangeAxis,pRenderer);//plot for pick window
    private JFreeChart pChart = new JFreeChart(pPlot);//chart for pick window
    private ChartPanel pCp = new ChartPanel(pChart);//chart panel for pick window

                                                                                                                    
}
