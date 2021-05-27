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
	public static CountDownLatch ly;

	public static void main(String[] args) throws IOException, InterruptedException {
		ly=new CountDownLatch(4); //for leia starts after everyone finish initialize
		int LandoDuration = 0;
		int EwoksNumber = 0;
		int R2d2Duration = 0;
		Diary diary = Diary.getInstance();
		Comparator<Integer> comparator = new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o1-o2;
			}
		};

		Input json = JsonInputReader.getInputFromJson(args[0]);

		//MICROSERVICE AND RESOURCES

		LandoDuration = json.getLando();
		EwoksNumber = json.getEwoks();
		Ewoks ewoks = Ewoks.getInstance();
		R2d2Duration = json.getR2D2();
		Attack [] attacks = json.getAttacks();

		//initalize thr ewoks
		for (int i = 1 ; i <= EwoksNumber ; i++){
			ewoks.addEwok(i);
		}

		//sort the serials for the attacks
		for (int i = 0 ; i < attacks.length ; i++)
		{
			attacks[i].getSerials().sort(comparator);
		}

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


		//for print
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

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		FileWriter writer = new FileWriter(args[1]);
		gson.toJson(diary , writer);
		writer.flush();
		writer.close();


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


