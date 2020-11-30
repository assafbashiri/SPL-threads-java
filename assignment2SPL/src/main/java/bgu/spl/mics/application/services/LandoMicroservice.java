package bgu.spl.mics.application.services;

import bgu.spl.mics.Event;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.example.messages.ExampleEvent;

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice  extends MicroService {

    public LandoMicroservice(long duration) {

        super("Lando");
    }

    @Override
    protected void initialize() {
        //DeactivationEvent deactivationEvent = new DeactivationEvent();
        //Event ;
        messageBus.subscribeEvent(BombDestroyerEvent.class, this);

       
    }
}
