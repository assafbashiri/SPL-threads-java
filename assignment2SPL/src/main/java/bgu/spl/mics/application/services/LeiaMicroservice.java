package bgu.spl.mics.application.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.Main;


/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as  {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LeiaMicroservice extends MicroService {
	private Attack[] attacks;
	ConcurrentHashMap <AttackEvent , Future> attackStatus = new ConcurrentHashMap<>();
    Diary diary = Diary.getInstance();



    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
		this.attacks = attacks;
    }

    @Override
    protected void initialize() throws InterruptedException {
        Main.ly.await();
        for (Attack a  : attacks){
            AttackEvent attack = new AttackEvent(this.name , a);
            Future <Boolean> future = sendEvent(attack);
            attackStatus.put(attack , future);

        }
        Callback<FinishBombDestroyerBroadcast> finishBombDestroyerBroadcast = c -> {
            terminate();
        };
        this.subscribeBroadcast(FinishBombDestroyerBroadcast.class, finishBombDestroyerBroadcast);
        //Callback<FinishAttackBroadcast> finishAttackBroadcastCallback = c -> {
//        };

        for (Future f : attackStatus.values()){
            f.get();
        }
        System.out.println("finish attack");
        DeactivationEvent deactivationEvent = new DeactivationEvent();
        Future future = this.sendEvent(deactivationEvent);
        future.get();
        System.out.println("finish deactivation");
        BombDestroyerEvent bombDestroyerEvent = new BombDestroyerEvent();
        Future future1 = this.sendEvent(bombDestroyerEvent);
        future1.get();
        FinishBombDestroyerBroadcast b = new FinishBombDestroyerBroadcast();
        this.sendBroadcast(b);
        System.out.println("finish bomb");
        //terminate();

    	
    }
}
