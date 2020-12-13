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
    final ConcurrentHashMap<Class<? extends Message>, ConcurrentLinkedQueue<MicroService>> eventType; // map: key = TYPE (event), value = queue of MicroService's that subscribed to this Event
    final ConcurrentHashMap<Class<? extends Message>, ConcurrentLinkedQueue<MicroService>> broadcastType; //map: key = TYPE (broadcast), value = queue of MicroService's that subscribed to this Broadcast
    ConcurrentHashMap<Event, Future> futureOfEvent; // map: key = event, value = future connected this event

    private MessageBusImpl(){
        this.messagesQueues = new ConcurrentHashMap<MicroService, LinkedBlockingQueue<Message>>();
        this.eventType = new ConcurrentHashMap<Class<? extends Message>, ConcurrentLinkedQueue<MicroService>>();
        this.broadcastType = new ConcurrentHashMap<Class<? extends Message>, ConcurrentLinkedQueue<MicroService>>();
        this.futureOfEvent = new  ConcurrentHashMap<Event, Future> ();

    }
    @Override
    public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
        synchronized (eventType) {
            if (messagesQueues.containsKey(m)) { //Checks that the microservis is already registered for the message bus
                if (!eventType.containsKey(type)) {//checks that this type of event already registered in the system
                    ConcurrentLinkedQueue<MicroService> queue = new ConcurrentLinkedQueue<MicroService>();
                    queue.add(m);
                    eventType.put(type, queue);
                } else {
                    eventType.get(type).add(m);
                }
            }
        }
    }

    @Override
    public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
        synchronized (broadcastType) {
            if (messagesQueues.containsKey(m)) {  //Checks that the microservis is already registered for the message bus
                if (!broadcastType.containsKey(type)) { //checks that this type of event already registered in the system
                    ConcurrentLinkedQueue<MicroService> queue = new ConcurrentLinkedQueue<MicroService>();
                    queue.add(m);
                    broadcastType.put(type, queue);
                } else {
                    broadcastType.get(type).add(m);
                }
            }
        }
    }

    @Override
    public <T> void complete(Event<T> e, T result) {
        //if(futureOfEvent.containsKey(e)){ //להתייעץ עם אסף

            Future<T> future = futureOfEvent.get(e);
            future.resolve(result); //Update the future
        //}

    }

    @Override
    public void sendBroadcast(Broadcast b) {
        synchronized (broadcastType) {
            ConcurrentLinkedQueue<MicroService> queue = broadcastType.get(b.getClass());
            if(queue!=null) {
                if (queue.size() > 0) {
                    for (MicroService m : queue) { //Send to all microservices
                        messagesQueues.get(m).add(b);
                    }
                }
            }
        }

    }


    @Override
    public <T> Future<T> sendEvent(Event<T> e) {
        synchronized (eventType) {
            ConcurrentLinkedQueue<MicroService> queue = eventType.get(e.getClass());
            if (queue != null) {
                if (queue.size() > 0) {
                    MicroService m = queue.remove(); //for the round rubin
                    Future<T> future = new Future<>();
                    futureOfEvent.put(e, future);
                    messagesQueues.get(m).add(e);
                    queue.add(m); //for the round rubin
                    return future;
                }
            }
        }
        return null;
    }

    @Override
    public void register(MicroService m) {
        LinkedBlockingQueue<Message> queue = new LinkedBlockingQueue<Message>();
        messagesQueues.put(m,queue);
    }

    @Override
    public void unregister(MicroService m) {


        for (ConcurrentLinkedQueue<MicroService> queue: broadcastType.values()){ // delete from broadcastType
            queue.remove(m);
        }
        for(ConcurrentLinkedQueue<MicroService> queue: eventType.values()){ // delete from eventType
            queue.remove(m);
        }
        LinkedBlockingQueue<Message> queue = messagesQueues.remove(m); //delete from messagesQueues
        while (queue.size()>0){
            queue.remove();
        }
        queue.clear();

    }

    @Override
    public Message awaitMessage(MicroService m) throws InterruptedException { //blocking function
        BlockingQueue<Message> blockingQueue=  messagesQueues.get(m);
        if (blockingQueue != null) {
            return blockingQueue.take(); //block
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
