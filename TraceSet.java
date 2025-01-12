/*
 * TraceSet.java
 * holds an entire seg2 file in usable form
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
import org.jfree.data.*;
public class TraceSet {
    Trace[] traceArray;
    int number;
    /** Creates a new instance of PingSet */
    public TraceSet(int n) {
        number = n;
        traceArray = new Trace[n];
        //for(int i=0; i<n; i++){
        //  pingArray[i] = new Ping();
        //}
    }
    public void setTrace(int n, Trace t){
traceArray[n] = t;
    }
    public Trace getTrace(int n){
        return traceArray[n];
    }
public XYSeries getPickSeries(double o){
    XYSeries s = new XYSeries("");
    for (int i=0; i < number; i++){
        s.add(traceArray[i].getPick(),o*(i+1));
    }
    return s;
}
public XYSeries getTtSeries(){
    XYSeries s = new XYSeries("");
    for (int i=0; i < number; i++){
        double pl = traceArray[i].getPhoneLocation();
        //double sl = traceArray[i].getShotLocation();
        
        s.add(pl,traceArray[i].getPick());
    }
    return s;
}
}
