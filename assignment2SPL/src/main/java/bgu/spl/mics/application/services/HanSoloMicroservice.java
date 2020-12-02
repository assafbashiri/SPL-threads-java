package bgu.spl.mics.application.services;


import bgu.spl.mics.Callback;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.FinishAttackBroadcast;
import bgu.spl.mics.application.messages.FinishBombDestroyerBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewok;
import bgu.spl.mics.application.passiveObjects.Ewoks;

import java.util.List;

/**
 * HanSoloMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class HanSoloMicroservice extends MicroService {
    private Ewoks ewoks = Ewoks.getInstance();
    Diary diary = Diary.getInstance();

    public HanSoloMicroservice() {
        super("Han");
    }


    @Override
    protected void initialize() {
        Callback<AttackEvent> attackEventCallback = c -> {
            List<Integer> list = c.workers();
            Ewok e;
            boolean use = ewoks.useResource(list);
            while (!use){
                use = ewoks.useResource(list);
            }
            Thread.sleep(c.getDuration());
            ewoks.release(list);
            //FinishAttackBroadcast b = new FinishAttackBroadcast();
            //sendBroadcast(b);
            diary.addAttack();
            messageBus.complete(c ,true);

        };
        this.subscribeEvent(AttackEvent.class, attackEventCallback);
        Callback<FinishBombDestroyerBroadcast> finishBombDestroyerBroadcast = c -> {
            terminate();
        };
        this.subscribeBroadcast(FinishBombDestroyerBroadcast.class, finishBombDestroyerBroadcast);
    }
    }


