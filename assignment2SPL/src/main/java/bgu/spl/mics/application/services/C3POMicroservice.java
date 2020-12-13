package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.FinishBombDestroyerBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewok;
import bgu.spl.mics.application.passiveObjects.Ewoks;
import bgu.spl.mics.application.Main;


import java.util.List;
import java.util.concurrent.CountDownLatch;


/**
 * C3POMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class C3POMicroservice extends MicroService {
    private Ewoks ewoks = Ewoks.getInstance();
    Diary diary = Diary.getInstance();

    public C3POMicroservice() {
        super("C3PO");
    }

    @Override
    protected void initialize() {
        Callback<AttackEvent> attackEventCallback = c -> { //what to do when we get a attack
            List<Integer> list = c.workers();
            Ewok e;
            ewoks.useResource(list);
            Thread.sleep(c.getDuration());
            ewoks.release(list);
            diary.addAttack();
            messageBus.complete(c ,true);
            diary.setC3POFinish();
        };
        this.subscribeEvent(AttackEvent.class, attackEventCallback); //subscribe attack event
        Callback<FinishBombDestroyerBroadcast> finishBombDestroyerBroadcast = c -> { //finish and terminate
            terminate();
            diary.setC3POTerminate();
            //System.out.println(this.name+ "  finish");
        };
        this.subscribeBroadcast(FinishBombDestroyerBroadcast.class, finishBombDestroyerBroadcast);
        Main.ly.countDown();  //one less tread that leia need to wait for
    }
}
