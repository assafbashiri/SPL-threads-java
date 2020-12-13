package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.Main;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.FinishBombDestroyerBroadcast;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;

import java.util.concurrent.ConcurrentHashMap;


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
        Main.ly.await(); //wait for everyone else
        for (Attack a  : attacks){ //send all the attacks
            AttackEvent attack = new AttackEvent(this.name , a);
            Future <Boolean> future = sendEvent(attack);
            attackStatus.put(attack , future);

        }
        Callback<FinishBombDestroyerBroadcast> finishBombDestroyerBroadcast = c -> { //finish and terminate
            terminate();
            diary.setLeiaTerminate();
        };
        this.subscribeBroadcast(FinishBombDestroyerBroadcast.class, finishBombDestroyerBroadcast); //subsribe finish and terminate

        for (Future f : attackStatus.values()){ //wait for all the attack complete
            f.get();
        }
        //System.out.println("finish attack");
        DeactivationEvent deactivationEvent = new DeactivationEvent(); //for R2D2
        Future future = this.sendEvent(deactivationEvent);
        future.get(); //wait to R2D2
        //System.out.println("finish deactivation");
        BombDestroyerEvent bombDestroyerEvent = new BombDestroyerEvent(); //for lando
        Future future1 = this.sendEvent(bombDestroyerEvent);
        future1.get(); // wait to lando
        FinishBombDestroyerBroadcast b = new FinishBombDestroyerBroadcast(); //subsribe that everyone need to terminate
        this.sendBroadcast(b); //tell everyone to terminate
        System.out.println("finish bomb");
        //terminate();

    	
    }
}
