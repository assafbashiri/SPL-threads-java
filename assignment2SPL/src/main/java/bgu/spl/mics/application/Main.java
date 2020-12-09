package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;
import bgu.spl.mics.application.passiveObjects.Input;
import bgu.spl.mics.application.passiveObjects.JsonInputReader;

import bgu.spl.mics.application.services.*;
import com.google.gson.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/** This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
	public static CountDownLatch ly = new CountDownLatch(4);

	public static void main(String[] args) throws IOException, InterruptedException {
		int LandoDuration = 0;
		int EwoksNumber = 0;
		int R2d2Duration = 0;
		List attacksList = new ArrayList();
		Diary diary = Diary.getInstance();
		Comparator<Integer> comparator = new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o1-o2;
			}
		};
		//Input json = JsonInputReader.
		Input json = JsonInputReader.getInputFromJson("/Users/assafbashiri/Desktop/SPL-threads-java-main/assignment2SPL/input.json");
	//	File input = new File(args[0]);
	//	JsonElement fileElement = JsonParser.parseReader(new FileReader(input));
//		JsonObject fileObject = fileElement.getAsJsonObject();
		//extract the data

		//MICROSERVICE AND RESOURCES

		LandoDuration = json.getLando();
		EwoksNumber = json.getEwoks();
		Ewoks ewoks = Ewoks.getInstance();
		R2d2Duration = json.getR2D2();
		Attack [] attacks = json.getAttacks();

		//initalize thr ewoks
		for (int i = 1 ; i <= EwoksNumber ; i++)
		{
			ewoks.addEwok(i);
		}
		//finish initialize


		//sort the serials for the attacks
		for (int i = 0 ; i < attacks.length ; i++)
		{
			attacks[i].getSerials().sort(comparator);
		}
		//finish sorting

		//initial the program
		Thread c3PO = new Thread(new C3POMicroservice());
		Thread leia = new Thread(new LeiaMicroservice(attacks));
		Thread hanSolo = new Thread(new HanSoloMicroservice());
		Thread lando = new Thread(new LandoMicroservice(LandoDuration));
		Thread r2D2 = new Thread(new R2D2Microservice(R2d2Duration));

		//start the attack
		hanSolo.start();
		c3PO.start();
		r2D2.start();
		lando.start();
		leia.start();

		hanSolo.join();
		c3PO.join();
		r2D2.join();
		lando.join();
		leia.join();


		//create the output

		//for json01 , json02
		long helper = diary.getHanSoloFinish()-diary.getC3POFinish();
		long result = diary.getHanSoloFinish();
		if (helper < 0) {
			helper = helper * (-1);
			result = diary.getC3POFinish();
		}
		/*
		FileOutputStream fileOutputStream = new FileOutputStream(args[1]);
		String json0 = gson.toJson("There are " + diary.getTotalAttack() + " attacks. \n");
		String json01 = gson.toJson("HanSolo and CSPO finished there attacks in " +(helper)+".\n");
		String json02 = gson.toJson("All threads terminates " + (diary.maxTreminate()-result));
		/*
		String json = gson.toJson("HanSolo finish attack in: " + diary.getHanSoloFinish());
		String json1 = gson.toJson("HanSolo terminate: " + diary.getHanSoloTerminate());
		String json2 = gson.toJson("C3PO finish attack in: " + diary.getC3POFinish());
		String json3 = gson.toJson("C3PO terminate: " + diary.getC3POTerminate());
		String json4 = gson.toJson("R2D2 finish attack in: " + diary.getR2D2Deactivate());
		String json5 = gson.toJson("R2D2 terminate: " + diary.getR2D2Terminate());
		String json6 = gson.toJson("Lando finish attack in: " + diary.getLandoDestroy());
		String json7 = gson.toJson("Lando terminate: " + diary.getLandoTerminate());
		//String json8 = gson.toJson("hansolo finish attack in: " + diary.getHanSoloFinish());
		String json8 = gson.toJson("Leia terminate: " + diary.getLeiaTerminate());

		FileWriter writer = new FileWriter(args[1]);
		fileOutputStream.write(json0.getBytes());
		fileOutputStream.write(json01.getBytes());
		fileOutputStream.write(json02.getBytes());
		/*
		fileOutputStream.write(json3.getBytes());
		fileOutputStream.write(json4.getBytes());
		fileOutputStream.write(json5.getBytes());
		fileOutputStream.write(json6.getBytes());
		fileOutputStream.write(json7.getBytes());
		fileOutputStream.write(json8.getBytes());
		*/
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		FileWriter writer = new FileWriter("/Users/assafbashiri/Desktop/SPL-threads-java-main/assignment2SPL/Output.json");
		gson.toJson(diary , writer);
		writer.flush();
		writer.close();

		//Gson gson5 = new GsonBuilder().setPrettyPrinting().create();

	}
	public static void printToFile(String filename,Object... objs2print){
		try {
			FileOutputStream fos = new FileOutputStream(filename);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();;
			for (int i = 0;  i< objs2print.length; i++) {
				String str=gson.toJson(objs2print[i]);
				fos.write(str.getBytes());
			}
			fos.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}


