package bgu.spl.mics.application.passiveObjects;


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

    public void addEwok (int serialNumber)
    {
        list.put(serialNumber,new Ewok(serialNumber));
    }

    public  Ewok  getEwok(int serialNumber)
    {
        Ewok ewok = list.get(serialNumber);
        ewok.acquire();
        return ewok;
    }

    private static class SingletonHolder {
        private static Ewoks instance = new Ewoks();
    }
    public static Ewoks getInstance(){
        return SingletonHolder.instance;
    }

}
