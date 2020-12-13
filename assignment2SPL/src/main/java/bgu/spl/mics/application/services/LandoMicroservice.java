package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Event;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.FinishBombDestroyerBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.example.messages.ExampleEvent;
import bgu.spl.mics.application.Main;

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice  extends MicroService {
    private long duration;
    Diary diary = Diary.getInstance();
    public LandoMicroservice(long duration) {

        super("Lando");
        this.duration = duration;
    }

    @Override
    protected void initialize() {
        Callback<BombDestroyerEvent> bombDestroyerEventCallback = c -> { //bombDestroy
            Thread.sleep(duration);
            messageBus.complete( c , true);
            diary.setLandoDestroy();
        };
        this.subscribeEvent(BombDestroyerEvent.class, bombDestroyerEventCallback); //subscribe bombDestroy

        Callback<FinishBombDestroyerBroadcast> finishBombDestroyerBroadcast = c -> { //finish and terminate
            terminate();
            diary.setLandoTerminate();
            //System.out.println(this.name+ "  finish");
        };
        this.subscribeBroadcast(FinishBombDestroyerBroadcast.class, finishBombDestroyerBroadcast);
        Main.ly.countDown();//one less tread to wait for
    }
}
