package bgu.spl.mics.application.passiveObjects;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EwokTest {
    Ewok E;
    @BeforeEach
    void setUp() {
        E=new Ewok(0);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void acquire() {
        if(E.available==false){
            try {
                E.acquire();
            } catch (IllegalArgumentException e){}

        }else{
            E.acquire();
        }
        assertFalse(E.available);
    }

    @Test
    void release() {
        if(E.available==true){
            try {
                E.release();
            } catch (IllegalArgumentException e){}

        }else{
            E.release();
        }
        assertTrue(E.available);
    }

}