package bgu.spl.mics.application;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Ewoks;
import bgu.spl.mics.application.services.LandoMicroservice;
import bgu.spl.mics.application.services.R2D2Microservice;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class readFromJson {
    public static void read(String path) throws FileNotFoundException {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader(path));
        JsonParser parser = gson.fromJson(reader, JsonParser.class);
        int R2D2 = gson.fromJson("R2D2" , int.class);
        int Lando = gson.fromJson("Lando" , int.class);
        int ewoks = gson.fromJson("Ewoks" , int.class);
        Attack[] attack = gson.fromJson("[\"attacks\"]" , Attack[].class);

    }
}
