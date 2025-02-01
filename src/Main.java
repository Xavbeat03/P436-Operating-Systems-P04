package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
	public static void Main(String[] args) {
		if (args.length != 4){
			System.out.println("Usage: java src.Main <input file> <thread 1 name> <thread 2 name> <thread 3 name>");
			System.exit(1);
		}
		else{
			final String inputFile = args[0];
			final String thread1 = args[1];
			final String thread2 = args[2];
			final String thread3 = args[3];

			CubbyHole cub1 = new CubbyHole();
			CubbyHole cub2 = new CubbyHole();

			Producer p = new Producer(cub1, cub2, inputFile, thread1);

			Consumer c1 = new Consumer(cub1, thread2);
			Consumer c2 = new Consumer(cub2, thread3);


		}

	}
}

class Consumer extends Thread {
	private final String name;
	private CubbyHole cubbyhole;
	private char character;

	public Consumer(CubbyHole c, String name) {
		cubbyhole = c;
		this.name = name;
	}

	public void run() {
		int value = 0;
		while (true) {
			value = cubbyhole.get();
			char c = Character.toChars(value)[0];
			System.out.println(name + " got: " + c);
		}
	}
}

class Producer extends Thread {
	private final String name;
	private CubbyHole cubbyhole1;
	private CubbyHole cubbyhole2;
	private String fileName;

	public Producer(CubbyHole c1, CubbyHole c2,String fileName, String name) {
		this.cubbyhole1 = c1;
		this.cubbyhole2 = c2;
		this.fileName = fileName;
		this.name = name;
	}

	public void run() {
		try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
			int character;
			while ((character = reader.read()) != -1) {
				if(character == 'a' || character == 'e' || character == 'i' || character == 'o' || character == 'u'){
					cubbyhole1.put(character);
				}
				else{
					cubbyhole2.put(character);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		char c = '.';
		cubbyhole1.put(c);
		cubbyhole2.put(c);
	}
}

class CubbyHole {
	private char contents;
	private boolean available = false;

	public synchronized int get() {
		while (available == false) {
			try {
				wait();
			} catch (InterruptedException e) { }
		}
		available = false;
		notifyAll();
		return contents;
	}

	public synchronized void put(int value) {
		while (available == true) {
			try {
				wait();
			} catch (InterruptedException e) { }
		}
		contents = value;
		available = true;
		notifyAll();
	}
}