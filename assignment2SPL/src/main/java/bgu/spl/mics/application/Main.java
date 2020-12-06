package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;
import bgu.spl.mics.application.services.LeiaMicroservice;
import bgu.spl.mics.application.services.R2D2Microservice;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/** This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
	public static void main(String[] args) throws FileNotFoundException {
		File input = new File("/Users/assafbashiri/Desktop/SPL-threads-java-main/assignment2SPL/src/main/java/bgu/spl/mics/application/input.json");
		try{
			JsonElement fileElement = JsonParser.parseReader(new FileReader(input));
			JsonObject fileObject = fileElement.getAsJsonObject();
			//extract the data

			//MICROSERVICE AND RESOURCES

			int LandoDuration = fileObject.get("Lando").getAsInt();
			int EwoksNumber = fileObject.get("Ewoks").getAsInt();
			int R2d2Duration = fileObject.get("R2D2").getAsInt();

			//Process the attacks

			List attacksList = new ArrayList();
			JsonArray jsonArrayAttacks = fileObject.get("attacks").getAsJsonArray();
			List list = new ArrayList();//FOR THE ATTACKS
			for (JsonElement attackElement : jsonArrayAttacks){
				//Get the JsonObject:
				JsonObject attackJsonObject = attackElement.getAsJsonObject();
				//Extract data
				int duration = attackJsonObject.get("duration").getAsInt();
				//Extract serials array
				JsonArray lis = attackJsonObject.get("serials").getAsJsonArray();
				for (JsonElement serialElement : lis){
					list.add(serialElement.getAsInt());
				}
				Attack attack = new Attack(list , duration);//new attack
				attacksList.add(attack);
			}

		}catch (FileNotFoundException e){

		}
	}

	public  static void readInput(String path, List <Attack> attacks , int R2D2Duration , int LandoDuration , int ewoksNumber) throws FileNotFoundException {

		Gson gson = new Gson();
//		JsonReader reader = new JsonReader(new FileReader("input.json"));
//		JsonParser parser = gson.fromJson(reader , JsonParser.class);
		try (		JsonReader reader = new JsonReader(new FileReader("/input.json"))
		) { attacks = gson.fromJson( reader , attacks.getClass());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}


