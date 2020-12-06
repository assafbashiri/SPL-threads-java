package bgu.spl.mics.application.passiveObjects;


import bgu.spl.mics.MessageBusImpl;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive data-object representing a Diary - in which the flow of the battle is recorded.
 * We are going to compare your recordings with the expected recordings, and make sure that your output makes sense.
 * <p>
 * Do not add to this class nothing but a single constructor, getters and setters.
 */
public class Diary {
    private AtomicInteger totalAttack = new AtomicInteger(0);

   /* private long HanSoloFinish;
    private long C3POFinish;
    private long R2D2Deactivate;
    private long LeiaTerminate;
    private long HanSoloTerminate;
    private long C3POTerminate;
    private long R2D2Terminate;
    private long LandoTerminate;
*/

    public AtomicInteger getTotalAttack() {
        return totalAttack;
    }

    private static class SingletonHolder {
        private static Diary instance = new Diary();
    }

    public static Diary getInstance(){
        return Diary.SingletonHolder.instance;
    }

    public synchronized void addAttack(){
        totalAttack.addAndGet(1);
    }

}
