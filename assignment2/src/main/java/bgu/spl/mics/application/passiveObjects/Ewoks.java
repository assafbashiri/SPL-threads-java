package bgu.spl.mics.application.passiveObjects;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Passive object representing the resource manager.
 * <p>
 * This class must be implemented as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class Ewoks {
   private Vector<Ewok> list;
   private int size;
   public Ewoks(){
       size=0;
       list=new Vector<Ewok>();
   }
   public void addEwok (int serialNumber){
       list.add(new Ewok(serialNumber));
       size=size+1;
   }
    public Ewok getEwok(int serialNumber){
       for(int i=0;i<size;i++){
           if(list.elementAt(i).getSerialNumber()==serialNumber)
               return list.elementAt(i);
       }
        throw new IllegalArgumentException("no such Ewok");

    }

}
