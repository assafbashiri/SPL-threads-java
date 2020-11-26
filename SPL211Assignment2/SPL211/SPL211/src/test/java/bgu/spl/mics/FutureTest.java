package bgu.spl.mics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class FutureTest {
    private Future<String> future;
    @BeforeEach
    void setUp() {
        future = new Future<>();
    }

    @Test
    void get() {
        assertFalse(future.isDone());
        future.resolve("");
        future.get();
        assertTrue(future.isDone());
    }

    @Test
    void resolve() {
        String str = "Result";
        future.resolve(str);
        assertTrue(future.isDone());
        assertTrue(str.equals(future.get()));
    }

    @Test
    void isDone() {
        String str = "Result";
        assertFalse(future.isDone());
        future.resolve(str);
        assertTrue(future.isDone());
    }

    @Test
    void testGet() {
        assertFalse(future.isDone());
        String result = future.get(100,TimeUnit.MILLISECONDS);
        assertFalse(future.isDone());
        assertEquals(null,result);
        future.resolve("hello");
        assertEquals("hello",future.get(100,TimeUnit.MILLISECONDS));

    }
}