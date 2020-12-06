package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.FinishBombDestroyerBroadcast;
import bgu.spl.mics.application.messages.FinishDeactivationBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;

/**
 * R2D2Microservices is in charge of the handling {@link DeactivationEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link DeactivationEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class R2D2Microservice extends MicroService {
    public long duration;
    public R2D2Microservice(long duration) {
        super("R2D2");
        this.duration=duration;

    }

    @Override
    protected void initialize() {
        Callback<DeactivationEvent> deactivationEventCallback = c -> {
            Thread.sleep(duration);
            messageBus.complete(c , true);
        };
       this.subscribeEvent(DeactivationEvent.class,deactivationEventCallback);
       Callback<FinishBombDestroyerBroadcast> finishBombDestroyerBroadcast = c -> {
           terminate();
       };
       this.subscribeBroadcast(FinishBombDestroyerBroadcast.class, finishBombDestroyerBroadcast);

    }

}
