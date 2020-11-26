package bgu.spl.mics;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {
    ConcurrentHashMap<MicroService, LinkedBlockingQueue<Message>> messagesQueues; // map: key = MicroService, value = message queue of this MicroService
    ConcurrentHashMap<Class<? extends Message>, ConcurrentLinkedQueue<MicroService>> subscribersForMessages; // map: key = message (event/broadcast), value = linked queue of MicroService's that subscribed to this message
    ConcurrentHashMap<Event, Future> futureOfEvent; // map: key = event, value = future connected this event


    @Override
    public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {//כשאדם רוצה להירשם לסוג מסויים של אירוע

    }

    @Override
    public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) { //כשאדם רוצה להירשם לסוג מסויים של אירוע

    }

    @Override @SuppressWarnings("unchecked")
    public <T> void complete(Event<T> e, T result) {


    }

    @Override
    public void sendBroadcast(Broadcast b) {

    }


    @Override
    public <T> Future<T> sendEvent(Event<T> e) {

        return null;
    }

    @Override
    public void register(MicroService m) {//האנשים קוראים לפונקציה הזאת כדי להירשם


    }

    @Override
    public void unregister(MicroService m) {//האנשים קוראים לפונקציה הזאת כדי לבטל הרשמה-בדרכ כשנסיים תמשימה

    }

    @Override
    public Message awaitMessage(MicroService m) throws InterruptedException {

        return null;
    }
    private static class SingletonHolder {
        private static MessageBusImpl instance = new MessageBusImpl();
    }

    public static MessageBusImpl getInstance(){
        return SingletonHolder.instance;
    }


}
