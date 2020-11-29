package bgu.spl.mics;

import bgu.spl.mics.application.services.C3POMicroservice;
import bgu.spl.mics.application.services.HanSoloMicroservice;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import bgu.spl.mics.example.messages.ExampleEvent;
import bgu.spl.mics.example.messages.ExampleBroadcast;


import static org.junit.jupiter.api.Assertions.*;

class MessageBusImplTest {
    private MessageBusImpl messageBus;
    MicroService microService;

    @BeforeEach
    void setUp() {
        microService = new HanSoloMicroservice();
        messageBus = MessageBusImpl.getInstance();
        messageBus.register(microService);

    }
    @AfterEach
    void tearDown(){
        messageBus.unregister(microService);
    }

    @Test
    void subscribeEvent() {//כשאדם רוצה להירשם לסוג מסויים של אירוע

        messageBus.subscribeEvent(ExampleEvent.class, microService);
        Event<String> event = new ExampleEvent(microService.getName());
        messageBus.sendEvent(event);
        try{
            assertEquals(event,messageBus.awaitMessage(microService)); }
        catch (InterruptedException exception) {
            //exception.printStackTrace();
        }
    }

    @Test
    void subscribeBroadcast() {//כשאדם רוצה להירשם לסוג מסויים של הודעות
        messageBus.subscribeBroadcast(ExampleBroadcast.class, microService);
        Broadcast broadcast = new ExampleBroadcast(microService.getName());
        messageBus.sendBroadcast(broadcast);
        try {
            assertEquals(broadcast,messageBus.awaitMessage(microService)); }
        catch (InterruptedException exception) {
           // exception.printStackTrace();
        }
    }

    @Test
    void complete() {//מעדכנים אותנו כשהמשימה הושלמה
        String result = "result";
        Event<String> event = new ExampleEvent("test1");
        messageBus.subscribeEvent(ExampleEvent.class , microService);
        Future<String> future = messageBus.sendEvent(event);
        assertNull(future.getResult());
        messageBus.complete(event,result);
        assertTrue(future.isDone());
    }

    @Test
    void sendBroadcast() {//כשאנשים רוצים לשלוח הודעה לרשימת תפוצה
        MicroService C3PO = new C3POMicroservice();
        messageBus.register(C3PO);
        messageBus.subscribeBroadcast(ExampleBroadcast.class, microService);
        messageBus.subscribeBroadcast(ExampleBroadcast.class, C3PO);
        Broadcast broadcast = new ExampleBroadcast(microService.getName());
        messageBus.sendBroadcast(broadcast);
        try {
            assertEquals(messageBus.awaitMessage(C3PO),messageBus.awaitMessage(microService)); }
        catch (InterruptedException exception) {
            //exception.printStackTrace();
        }
        messageBus.unregister(C3PO);
    }

    @Test
    void sendEvent() {//כשאנשים רוצים ליצור אירוע חדש ולהוסיף אותו לרשימת המתנה של האירועים שלא טופלו. היא מתווספת לאדם שהסוג אירוע מתאים לו ואם יש כמה אנשים אז לפי רובין.
        MicroService tehila = new HanSoloMicroservice();
        MicroService C3PO = new C3POMicroservice();
        messageBus.register(C3PO);
        messageBus.subscribeEvent(ExampleEvent.class, microService);
        messageBus.subscribeEvent(ExampleEvent.class, C3PO);
        Event<String> event1 = new ExampleEvent("number1");
        Event<String> event2 = new ExampleEvent("number2");
        Event<String> event3 = new ExampleEvent("number3");
        Future<String> future1 = messageBus.sendEvent(event1);
        Future<String> future2 = messageBus.sendEvent(event2);
        Future<String> future3 = messageBus.sendEvent(event3);
        assertNull(future1.getResult());
        assertNull(future2.getResult());
        assertNull(future3.getResult());
        try {
            assertEquals(event1 , messageBus.awaitMessage(microService)); }
        catch (InterruptedException exception) {
            //exception.printStackTrace();
        }
        try {
            assertEquals(event2 , messageBus.awaitMessage(C3PO)); }
        catch (InterruptedException exception) {
            //exception.printStackTrace();
        }
        try {
            assertEquals(event3 , messageBus.awaitMessage(microService)); }
        catch (InterruptedException exception) {
            //exception.printStackTrace();
        }
        messageBus.unregister(C3PO);
    }

    @Test
    void register() {//האנשים קוראים לפונקציה הזאת כדי להירשם
        MicroService C3PO = new C3POMicroservice();
        messageBus.register(C3PO);
        messageBus.subscribeEvent(ExampleEvent.class, C3PO);
        Event<String> testEvent = new ExampleEvent(C3PO.getName());
        Future<String> excepted = messageBus.sendEvent(testEvent);
        //assertNull(excepted.getResult());
        try {
            assertEquals(testEvent,messageBus.awaitMessage(C3PO)); }
        catch (InterruptedException exception) {
           // exception.printStackTrace();
        }
        messageBus.unregister(C3PO);
    }


    @Test
    void awaitMessage() {//הודעה חוסמת עד שיהיה לי מסאג לתת לו
        messageBus.subscribeEvent(ExampleEvent.class, microService);
        Event<String> testEvent = new ExampleEvent(microService.getName());
        Future<String> future = messageBus.sendEvent(testEvent);
        //assertNull(future);
        try {
            assertEquals(testEvent, messageBus.awaitMessage(microService));
        } catch (InterruptedException exception) {
           // exception.printStackTrace();
        }
    }
}