package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Attack;

import java.util.List;

public class AttackEvent implements Event<Boolean> {
    protected String senderName;
    protected List <Integer> required;
    protected int duration;

    public AttackEvent(String s , Attack a){
        senderName = s;
        required = a.getSerials();
        duration=a.getDuration();

    }

    public String getSenderName() {
        return senderName;
    }

    public int getDuration(){
        return duration;
    }

    public List<Integer> workers(){
       return required;
    }
}
