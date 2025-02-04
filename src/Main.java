package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
	public static void main(String[] args) {
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

			Consumer c1 = new Consumer(cub1, thread2, p);
			Consumer c2 = new Consumer(cub2, thread3, p);

			c1.start();
			c2.start();
			p.start();

			System.out.println("The End");
		}

	}
}

class Consumer extends Thread {
	private final String name;
	private CubbyHole cubbyhole;
	private char character;
	private int count = 0;
	private Producer p;

	public Consumer(CubbyHole c, String name, Producer p) {
		cubbyhole = c;
		this.name = name;
		this.p = p;
	}

	public void run() {
		int value = 0;
		while (!p.isDone()) {
			value = cubbyhole.get();
			count++;
			char c = Character.toChars(value)[0];
			System.out.println(name + " got: " + c);
		}
		System.out.println(name + " got: " + count + " characters");
	}
}

class Producer extends Thread {
	private final String name;
	private CubbyHole cubbyhole1;
	private CubbyHole cubbyhole2;
	private String fileName;
	private int count = 0;
	private boolean done = false;

	public Producer(CubbyHole c1, CubbyHole c2,String fileName, String name) {
		this.cubbyhole1 = c1;
		this.cubbyhole2 = c2;
		this.fileName = fileName;
		this.name = name;
	}

	public void run() {
		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
			int character;
			while ((character = reader.read()) != -1) {
				count++;
				if(character == 'a' || character == 'e' || character == 'i' || character == 'o' || character == 'u' || character == 'A' || character == 'E' || character == 'I' || character == 'O' || character == 'U'){
					cubbyhole1.put(character);
				}
				else if (character == 'b' || character == 'B' || character == 'c' || character == 'C' || character == 'd' || character == 'D' || character == 'f' || character == 'F' || character == 'g' || character == 'G' || character == 'h' || character == 'H' || character == 'j' || character == 'J' || character == 'k' || character == 'K' || character == 'l' || character == 'L' || character == 'm' || character == 'M' || character == 'n' || character == 'N' || character == 'p' || character == 'P' || character == 'q' || character == 'Q' || character == 'r' || character == 'R' || character == 's' || character == 'S' || character == 't' || character == 'T' || character == 'v' || character == 'V' || character == 'w' || character == 'W' || character == 'x' || character == 'X' || character == 'y' || character == 'Y' || character == 'z' || character == 'Z'){
					cubbyhole2.put(character);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		char c = '.';
		cubbyhole1.put(c);
		cubbyhole2.put(c);
		done = true;
		System.out.println(name + " put: " + count + " characters");
	}

	public boolean isDone(){
		return done;
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
		contents = (char) value;
		available = true;
		notifyAll();
	}
}