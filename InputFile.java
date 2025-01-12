/*
 * InputFile.java
 * Seg-2 file reader (currently 32-bit only)
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
import java.io.*;
import java.util.*;
import org.jfree.data.*;

public class InputFile {
    public RandomAccessFile fileInput;
    public File f;
    public int number;
    public int pointer[];
    public InputFile(File file) throws java.util.zip.DataFormatException {
        
        try {
            f = file;
            fileInput = new RandomAccessFile(file,"r");
            
            //check if really seg2
            fileInput.seek(0);
            int b1 = fileInput.readUnsignedByte();
            int b2 = fileInput.readUnsignedByte();
            int b3 = 0;
            int b4 = 0;
            if (((b2 << 8)+ b1) != 0x3a55) throw new java.util.zip.DataFormatException();
            
            //find number of traces in file
            fileInput.seek(6);
            b1 = fileInput.readUnsignedByte();
            b2 = fileInput.readUnsignedByte();
            number = (b2 << 8) + b1;
            pointer = new int[number];
          
            //find beginning of each trace
            fileInput.seek(32);
            for(int i = 0; i<number; i++){
            b1 = fileInput.readUnsignedByte();
            b2 = fileInput.readUnsignedByte();
            b3 = fileInput.readUnsignedByte();
            b4 = fileInput.readUnsignedByte();
                
            int res = ((b3 << 16) | (b2 << 8)  |(b4 << 24)) + b1;
                pointer[i] = res;
            }
            
            
        }
        catch (FileNotFoundException fnf){
            System.err.println(fnf);
        }
        
        catch (EOFException eof) {
            System.out.println( "End of File");
        }
        catch (IOException e) {
            System.out.println( "IO error: " + e );
        }
    }
    Trace getTrace(int traceNum){
        Trace p = new Trace(0);
        try {
            long beginPos = pointer[traceNum];
            
            //read size of trace header
            fileInput.seek(beginPos+2);
            int b1 = fileInput.readUnsignedByte();
            int b2 = fileInput.readUnsignedByte();
            int b3 = 0;
            int b4 = 0;
            int sizeHeader = (b2 << 8) + b1;
            
            //read size of data block
            fileInput.seek(beginPos+8);
            b1 = fileInput.readUnsignedByte();
            b2 = fileInput.readUnsignedByte();
            int sizeData = (b2 << 8) + b1;
            
  
            p = new Trace(sizeData);
            p.setNum(traceNum);
            
            //read header into a string contained in the Trace object
            fileInput.seek(beginPos+32);
            for(int i = 32; i < sizeHeader; i++){
                b1 = fileInput.readByte();
                char c = (char)b1;
                //System.out.println(c);
                p.sb.append(c);
            }
            
            //decode the header string
            p.buildMetadata();
            
            //read in the data
            fileInput.seek(beginPos+sizeHeader);
            for(int i = 0; i<sizeData-1; i++){
            b1 = fileInput.readUnsignedByte();
            b2 = fileInput.readUnsignedByte();
            b3 = fileInput.readUnsignedByte();
            b4 = fileInput.readUnsignedByte();
                
            int res = ((b3 << 16) | (b2 << 8)  |(b4 << 24)) + b1;
            double f = Float.intBitsToFloat(res);
            
            //keep track of largest value for normalization later
            if (f > p.getMaxValue()) p.setMaxValue(f);
                p.set(i,f);
            }
            
        }
        catch(Exception e){
            System.err.println(e);
        }
       // System.out.println("done reading trace");
        return p;
    }
    
    public TraceSet getTraceSet(){
        TraceSet s = new TraceSet(number);
        for (int i = 0; i< number; i++){
            s.setTrace(i,getTrace(i));
        }
        return s;
    }
    public void close(){
        try{
        fileInput.close();
        }
        catch(Exception e){
            System.err.println(e);
        }
    }
}
