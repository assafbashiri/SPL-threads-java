package bgu.spl.mics;

import bgu.spl.mics.application.services.C3POMicroservice;
import bgu.spl.mics.application.services.HanSoloMicroservice;
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

    }

    @Test
    void subscribeEvent() {//כשאדם רוצה להירשם לסוג מסויים של אירוע

        messageBus.subscribeEvent(bgu.spl.mics.example.messages.ExampleEvent.class, microService);
        Event<String> testEvent = new ExampleEvent(microService.getName());
        messageBus.sendEvent(testEvent);
        try{
            assertEquals(testEvent,messageBus.awaitMessage(microService)); }
        catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    @Test
    void subscribeBroadcast() {//כשאדם רוצה להירשם לסוג מסויים של הודעות
        messageBus.subscribeBroadcast(ExampleBroadcast.class, microService);
        Broadcast testBroadcast = new ExampleBroadcast(microService.getName());
        messageBus.sendBroadcast(testBroadcast);
        try {
            assertEquals(testBroadcast,messageBus.awaitMessage(microService)); }
        catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    @Test
    void complete() {//מעדכנים אותנו כשהמשימה הושלמה
        String result = "result";
        Event<String> testEvent = new ExampleEvent(microService.getName());
        Future<String> excepted = messageBus.sendEvent(testEvent);
        assertNull(excepted);
        messageBus.complete(testEvent,result);
        assertTrue(excepted.isDone());
    }

    @Test
    void sendBroadcast() {//כשאנשים רוצים לשלוח הודעה לרשימת תפוצה
        MicroService subscriberTest1 = new C3POMicroservice();
        messageBus.subscribeBroadcast(ExampleBroadcast.class, microService);
        messageBus.subscribeBroadcast(ExampleBroadcast.class, subscriberTest1);
        Broadcast testBroadcast = new ExampleBroadcast(microService.getName());
        messageBus.sendBroadcast(testBroadcast);
        try {
            assertEquals(messageBus.awaitMessage(subscriberTest1),messageBus.awaitMessage(microService)); }
        catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    @Test
    void sendEvent() {//כשאנשים רוצים ליצור אירוע חדש ולהוסיף אותו לרשימת המתנה של האירועים שלא טופלו. היא מתווספת לאדם שהסוג אירוע מתאים לו ואם יש כמה אנשים אז לפי רובין.
        MicroService subscriberTest1 = new C3POMicroservice();
        messageBus.subscribeEvent(ExampleEvent.class, microService);
        messageBus.subscribeEvent(ExampleEvent.class, subscriberTest1);
        Event<String> testEvent = new ExampleEvent(microService.getName());
        Future<String> excepted = messageBus.sendEvent(testEvent);
        assertNull(excepted);
        try {
            assertEquals(messageBus.awaitMessage(subscriberTest1),messageBus.awaitMessage(microService)); }
        catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    @Test
    void register() {//האנשים קוראים לפונקציה הזאת כדי להירשם
        messageBus.register(microService);
        messageBus.subscribeEvent(ExampleEvent.class, microService);
        Event<String> testEvent = new ExampleEvent(microService.getName());
        Future<String> excepted = messageBus.sendEvent(testEvent);
        assertNull(excepted);
        try {
            assertEquals(testEvent,messageBus.awaitMessage(microService)); }
        catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    @Test
    void unregister() {//האנשים קוראים לפונקציה הזאת כדי לבטל הרשמה-בדרכ כשנסיים תמשימה
        MicroService subscriberTest1 = new C3POMicroservice();
        messageBus.unregister(subscriberTest1);
        messageBus.subscribeEvent(ExampleEvent.class, microService);
        Event<String> testEvent = new ExampleEvent(microService.getName());
        Future<String> excepted = messageBus.sendEvent(testEvent);
        assertNull(excepted);
        assertThrows(IllegalStateException.class, () -> {
            messageBus.awaitMessage(microService);
        });
    }

    @Test
    void awaitMessage() {//הודעה חוסמת עד שיהיה לי מסאג לתת לו
        messageBus.subscribeEvent(ExampleEvent.class, microService);
        Event<String> testEvent = new ExampleEvent(microService.getName());
        Future<String> excepted = messageBus.sendEvent(testEvent);
        assertNull(excepted);
        try {
            assertEquals(testEvent, messageBus.awaitMessage(microService));
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }
}