package bgu.spl.mics;
import java.util.concurrent.BlockingQueue;
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
    ConcurrentHashMap<Class<? extends Message>, ConcurrentLinkedQueue<MicroService>> eventType; // map: key = TYPE (event/broadcast), value = linked queue of MicroService's that subscribed to this message
    ConcurrentHashMap<Class<? extends Message>, ConcurrentLinkedQueue<MicroService>> broadcastType;
    ConcurrentHashMap<Event, Future> futureOfEvent; // map: key = event, value = future connected this event

    private MessageBusImpl(){
        this.messagesQueues = new ConcurrentHashMap<MicroService, LinkedBlockingQueue<Message>>();
        this.eventType = new ConcurrentHashMap<Class<? extends Message>, ConcurrentLinkedQueue<MicroService>>();
        this.broadcastType = new ConcurrentHashMap<Class<? extends Message>, ConcurrentLinkedQueue<MicroService>>();
        this.futureOfEvent = new  ConcurrentHashMap<Event, Future> ();

    }
    @Override
    public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {//כשאדם רוצה להירשם לסוג מסויים של אירוע
        if (messagesQueues.containsKey(m)) {
            if (!eventType.containsKey(type)) {
                ConcurrentLinkedQueue<MicroService> queue = new ConcurrentLinkedQueue<MicroService>();
                queue.add(m);
                eventType.put(type,queue);
            } else {
                eventType.get(type).add(m);
            }
        }
    }

    @Override
    public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) { //כשאדם רוצה להירשם לסוג מסויים של אירוע
        if(messagesQueues.containsKey(m)){
            if(!broadcastType.containsKey(type)){
                ConcurrentLinkedQueue<MicroService> queue = new ConcurrentLinkedQueue<MicroService>();
                queue.add(m);
                broadcastType.put(type,queue);
            } else {
                broadcastType.get(type).add(m);
            }
        }
    }

    @Override @SuppressWarnings("unchecked")
    public <T> void complete(Event<T> e, T result) {
        if(futureOfEvent.containsKey(e)){
            Future<T> future = futureOfEvent.get(e);
            future.resolve(result);
        }

    }

    @Override
    public void sendBroadcast(Broadcast b) {
        ConcurrentLinkedQueue<MicroService> queue = broadcastType.get(b.getClass());
        if(queue.size()>0){
            for(MicroService m :queue) {
                messagesQueues.get(m).add(b);
            }
        }

    }


    @Override
    public <T> Future<T> sendEvent(Event<T> e) {
        ConcurrentLinkedQueue<MicroService> queue = eventType.get(e.getClass());
        if(queue.size()>0){
            MicroService m =queue.remove();
            Future<T> future = new Future<>();
            futureOfEvent.put(e,future);
            messagesQueues.get(m).add(e);
            queue.add(m);
            return future;
        }
        return null;
    }

    @Override
    public void register(MicroService m) {//האנשים קוראים לפונקציה הזאת כדי להירשם
        LinkedBlockingQueue<Message> queue = new LinkedBlockingQueue<Message>();
        messagesQueues.put(m,queue);
    }

    @Override
    public void unregister(MicroService m) {//האנשים קוראים לפונקציה הזאת כדי לבטל הרשמה-בדרכ כשנסיים תמשימה


        for (ConcurrentLinkedQueue<MicroService> queue: broadcastType.values()){ // delete from broadcastType
            queue.remove(m);
        }
        for(ConcurrentLinkedQueue<MicroService> queue: eventType.values()){ // delete from eventType
            queue.remove(m);
        }
        LinkedBlockingQueue<Message> queue = messagesQueues.remove(m);

    }

    @Override
    public Message awaitMessage(MicroService m) throws InterruptedException {
        BlockingQueue<Message> blockingQueue;
        blockingQueue = messagesQueues.get(m);
        if (blockingQueue != null) {
            return blockingQueue.take();
        }

        return null;
    }

    private static class SingletonHolder {
        private static MessageBusImpl instance = new MessageBusImpl();
    }

    public static MessageBusImpl getInstance(){
        return SingletonHolder.instance;
    }


}
