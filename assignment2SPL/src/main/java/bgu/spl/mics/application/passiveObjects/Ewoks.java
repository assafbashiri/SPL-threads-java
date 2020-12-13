package bgu.spl.mics.application.passiveObjects;


import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Passive object representing the resource manager.
 * <p>
 * This class must be implemented as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class Ewoks {
    private ConcurrentHashMap<Integer, Ewok> list;

    public Ewoks()
    {
        list=new ConcurrentHashMap<>();
    }

    public void addEwok (int serialNumber){ //new ewok
        if(list.get(serialNumber)==null)
            list.put(serialNumber,new Ewok(serialNumber));
    }

    public  Ewok  getEwok(int serialNumber) {
        Ewok ewok = list.get(serialNumber);
        synchronized (ewok) { //ewok lock
            while (!ewok.available) {
                try {
                    ewok.wait(); //release key until notify
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                }
            }
            ewok.acquire(); //not available
            return ewok;
        }
    }

    private static class SingletonHolder {
        private static Ewoks instance = new Ewoks();
    }
    public static Ewoks  getInstance(){
        return SingletonHolder.instance;
    }

    public void useResource ( List<Integer> list) {
        Ewok e;
        for(int i=0 ; i<list.size();i++){
            e = getEwok(list.get(i));
        }
    }


    public void release(List<Integer> list) { //check
        Ewok e;
        for (int i = 0; i < list.size() ; i++) {
            e = this.list.get(list.get(i));
            synchronized (e) { //lock ewok ,the same lock like get ewok
                e.release();
                e.notifyAll(); //wake tread that wait in get ewok
            }
        }


    }


}
