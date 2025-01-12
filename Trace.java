/*
 * Trace.java
 * holds data pertaining to a single trace
 * 
 */

/**
 *
 * Copyright 2004  Will Brunner
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
import java.util.*;
import org.jfree.data.*;

public class Trace {
    Filter filter;
    int number;
    double[] value;
    double[] filtered;
    double maxValue;
    int length;
    double windowLength;
    double sampleInterval = 0;
    double shotLocation = 0;
    double phoneLocation = 0;
    double pick = 0;
    boolean isPicked;
    StringBuffer sb = new StringBuffer();
    StringTokenizer st;
    Range plotRange;
    /** Creates a new instance of Trace */
    public Trace(int n) {
        value = new double[n];
        filtered = new double[n];
        maxValue = 0;
        number = 0;
        length = n;
    }
    public void set(int i, double val){
        value[i]=val;
        filtered[i] = val;
    }
    public double get(int i){
        return value[i];
    }
    public double getFiltered(int i){
        return filtered[i];
    }
    public void setPick(double p){
        pick = p;
        isPicked = true;
    }
    public double getPick(){
        return pick;
    }
    public double getSampleInterval(){
        return sampleInterval;
    }
    public double getShotLocation(){
        return shotLocation;
    }
    public double getPhoneLocation(){
        return phoneLocation;
    }
    public void setMaxValue(double v){
        maxValue = v;
    }
    public double getMaxValue(){
        return maxValue;
    }
    public void setRange(Range r){
        plotRange = r;
    }
    public Range getRange(){
        return plotRange;
    }
    public void setNormalized(int i, double val){
        filtered[i]=(double)(val*maxValue);
    }
    public double getNormalized(int i){
        return filtered[i]/maxValue;
    }
    
    public void setNum(int n){
        number = n;
    }
    public boolean isPicked(){
        return isPicked;
    }
    public void restore(){
        for(int i = 0; i < length; i++){
            filtered[i] = value[i];
        }

        
    }
     public Filter getFilter(){
	    return filter;
    }
    public void setFilter(Filter f){
	    filter = f;
    }
    public void applyFilter(){
	    double powerOfTwo = Math.log(length)/Math.log(2);
	    int newLength;
	    Complex[] c;
	    
        if (Math.floor(powerOfTwo) != powerOfTwo) { 
        //pre-pad with zeroes if not an integer power of two
            int order = (int)Math.floor(powerOfTwo)+1;
            newLength = (int)Math.pow(2,order);
            //System.out.println(newLength+"   "+length);
	    c = new Complex[newLength*2];
	    for (int i = 0; i < (newLength-length); i++){
                c[i] = new Complex();
		c[newLength*2-1-i] = new Complex();
            }
	    for (int i = (newLength-length); i < newLength; i++){
                //System.out.println(i);
		    c[i] = new Complex(value[i-(newLength-length)],0);
		    c[newLength*2-i-1] = new Complex(value[i-(newLength-length)],0);//make an even function of the trace
            }
	    
	}
	else{
		newLength = length;
		c = new Complex[length*2];
		for (int i = 0; i < length; i++){
		    c[i] = new Complex(value[i],0);
		    c[length*2-i-1] = new Complex(value[i],0);//make an even function of the trace
	    }
	}
            
            
	    
	    
	    
	    double[] f = filter.getFilterArray(1/sampleInterval,newLength);
	    
	    
	    Complex[] fft = FFT.fft(c);
	    for (int i = 0; i < newLength; i++){
		    
		    fft[i] = fft[i].times(f[i]);
		    fft[newLength*2-1-i] = fft[newLength*2-1-i].times(f[i]);
		   // System.out.println(i+"   "+fft[i].getReal()+"    "+f[i]);
	    }
	    Complex[] r = FFT.ifft(fft);
	    for (int i = 0; i < length; i++){
		    filtered[i] = r[i+(newLength-length)].getReal();
		    //System.out.println(filtered[i]);
	    }
    }

   public void buildMetadata(){
       String delim = " "+(char)0;
       st = new StringTokenizer(sb.toString(),delim);
       
       while (st.hasMoreTokens()){
         String t = st.nextToken();
         //System.out.println(t);
         if (t.equals("SAMPLE_INTERVAL")) {
             sampleInterval = Double.valueOf(st.nextToken()).doubleValue();
             //System.out.println(number+"   "+t+"   "+sampleInterval);
         }
         if (t.equals("SOURCE_LOCATION")) {
             shotLocation = Double.valueOf(st.nextToken()).doubleValue();
             //System.out.println(t+"   "+shotLocation);
         }
         if (t.equals("RECEIVER_LOCATION")) {
             phoneLocation = Double.valueOf(st.nextToken()).doubleValue();
             //System.out.println(t+"   "+phoneLocation);
         }
       }
       windowLength = length*sampleInterval*1000;
        plotRange = new Range(0,windowLength);
   }
    public XYSeries getSeries(){//get filtered trace data
        XYSeries r = new XYSeries("trace data");
       
        
            
            for (int i = 0; i < length; i++){
      
                r.add(((double)i)*sampleInterval*1000,(double)getFiltered(i));
        
            }
        
        
       
        return r;
    }
   public XYSeries getSeries(int o){//get normalized trace data
        XYSeries r = new XYSeries("trace data");
       
        
            
            for (int i = 0; i < length; i+= 8){
      
                r.add((double)i*sampleInterval*1000,(double)getNormalized(i)+o);
        
            }
        
        
       
        return r;
    } 
    public XYSeries getSeries(int o,double s){//get gained, normalized trace data
        XYSeries r = new XYSeries("trace data");
       
        
            
            for (int i = 0; i < length; i++){
      
                r.add((double)i*sampleInterval*1000,((double)getNormalized(i)+o)*s);
        
            }
        
        
       
        return r;
    } 
}
