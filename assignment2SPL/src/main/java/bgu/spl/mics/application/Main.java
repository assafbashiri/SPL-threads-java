package bgu.spl.mics.application;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewok;
import bgu.spl.mics.application.passiveObjects.Ewoks;
import bgu.spl.mics.application.services.*;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/** This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
	public static CountDownLatch ly = new CountDownLatch(4);

	public static void main(String[] args) throws FileNotFoundException {
		int LandoDuration = 0;
		int EwoksNumber = 0;
		int R2d2Duration = 0;
		List attacksList = new ArrayList();
		Comparator<Integer> comparator = new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o1-o2;
			}
		};
		File input = new File("/Users/assafbashiri/Desktop/SPL-threads-java-main/assignment2SPL/src/main/java/bgu/spl/mics/application/input.json");
			JsonElement fileElement = JsonParser.parseReader(new FileReader(input));
			JsonObject fileObject = fileElement.getAsJsonObject();
			//extract the data

			//MICROSERVICE AND RESOURCES

			LandoDuration = fileObject.get("Lando").getAsInt();
			EwoksNumber = fileObject.get("Ewoks").getAsInt();
			R2d2Duration = fileObject.get("R2D2").getAsInt();

			//Process the attacks

			JsonArray jsonArrayAttacks = fileObject.get("attacks").getAsJsonArray();
			for (JsonElement attackElement : jsonArrayAttacks){
				List list = new ArrayList();//FOR THE ATTACKS

				//Get the JsonObject:
				JsonObject attackJsonObject = attackElement.getAsJsonObject();
				//Extract data
				int duration = attackJsonObject.get("duration").getAsInt();
				//Extract serials array
				JsonArray lis = attackJsonObject.get("serials").getAsJsonArray();
				for (JsonElement serialElement : lis){
					list.add(serialElement.getAsInt());
				}
				//sorting the list
				list.sort(comparator);
				System.out.println(list.toString());
				Attack attack = new Attack(list , duration);//new attack
				attacksList.add(attack);

			}
			Ewoks ewoks = Ewoks.getInstance();
			for (int i = 1 ; i <= EwoksNumber ; i++){
				ewoks.addEwok(i);
			}
		Attack[] attacks = new Attack[attacksList.size()];
		for (int i=0 ; i<attacksList.size() ; i++){
			attacks[i] = (Attack) attacksList.get(i);
		}
		//initial the program
		Thread c3PO = new Thread(new C3POMicroservice());
		Thread leia = new Thread(new LeiaMicroservice(attacks));
		Thread hanSolo = new Thread(new HanSoloMicroservice());
		Thread lando = new Thread(new LandoMicroservice(LandoDuration));
		Thread r2D2 = new Thread(new R2D2Microservice(R2d2Duration));
		c3PO.start();
		hanSolo.start();
		r2D2.start();
		lando.start();
		leia.start();
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


