/*
 * OutputFile.java
 *saves an ascii pick file
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
public class OutputFile {
    PrintWriter pw;
    /** Creates a new instance of OutputFile */
    public OutputFile(File file) {
        try{
            pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
          
        }
        catch(Exception e){
            System.err.println(e);
        }
    }
    public void writeOut(TraceSet ts){
        try{
     for (int i = 0; i < ts.number; i++){
         
         //do windows-style carriage return
         pw.println(ts.getTrace(i).getPhoneLocation()+"          "+ts.getTrace(i).getPick()+"\r");
     }
     pw.close();
        }
        catch(Exception e){
          System.err.println(e);
        }  
    }
}
