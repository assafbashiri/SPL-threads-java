package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Ewoks;
import bgu.spl.mics.application.services.LeiaMicroservice;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.Reader;

/** This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
	public static void main(String[] args) throws FileNotFoundException {

		InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("input.json");
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new FileReader("input.json"));
		JsonParser parser = gson.fromJson(reader , JsonParser.class);
		//LeiaMicroservice leia = new LeiaMicroservice(); //להכניס את רשימת ההתקפות שקיבלנו
		Ewoks ewoks = Ewoks.getInstance();// עכשיו צריך להוסיף את כל האיווק שצריך לפי הקובץ שנתנו לנו..

		
	}
}


