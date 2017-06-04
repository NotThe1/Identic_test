package sideMenu;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayDeque;
import java.util.NoSuchElementException;

public class TestTwoThreads {
	private static ArrayDeque<Integer> q1 = new ArrayDeque<Integer>();
	private static final Integer count = 2000;
	int mainCount = 0;

	public static void main(String[] args) {
		new TestTwoThreads().doWork();
	}// main

	private void doWork() {
		q1.clear();
		T1 t1 = new T1(q1, count);
		Thread thread1 = new Thread(t1);
		thread1.start();
		
		T2 t2 = new T2(q1,thread1);
		Thread thread2 = new Thread(t2);
		thread2.start();
		try{
			thread1.join();
			thread2.join();
		}catch (InterruptedException e){
			e.printStackTrace();
		}//try
		
		
	}// doWork

	class T1 implements Runnable {
		private ArrayDeque<Integer> q1;
		public int count;
		 Path path = Paths.get("C:\\Temp\\jarStuff");

		public T1(ArrayDeque<Integer> q1, int count) {
			this.count = count;
			this.q1 = q1;
		}// Constructor

		@Override
		public void run() {
			Integer i = 0;
			 Path path = Paths.get("C:\\Temp\\jarStuff");
			try {
				Files.walkFileTree(path, new MyWalker1());
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.printf("[T1.run] Integer i %d%n", i);
		}// run

	}// class T1

	class T2 implements Runnable {
		private ArrayDeque<Integer> q1;
		private Thread priorThread;
		

		public T2(ArrayDeque<Integer> q1, Thread priorThread) {
			this.priorThread =priorThread;
			this.q1 = q1;
		}// Constructor

		@Override
		public void run() {
			Integer currentValue = 0;
			while (true) {
				try {
					currentValue = q1.remove();
				} catch (NoSuchElementException ex) {
					if (priorThread.getState().equals(Thread.State.TERMINATED)) {
						System.out.printf("[T2.run] currentValue = %d%n", currentValue);
						return;
					} // if
				} // try
			} // while
		}// run

	}// class T1
	
	class MyWalker1 implements FileVisitor<Path> {

		@Override
		public FileVisitResult postVisitDirectory(Path arg0, IOException arg1) throws IOException {
			// TODO Auto-generated method stub
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path arg0, BasicFileAttributes arg1) throws IOException {
			// TODO Auto-generated method stub
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path arg0, BasicFileAttributes arg1) throws IOException {
			q1.add(1);
			mainCount++;
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path arg0, IOException arg1) throws IOException {
			// TODO Auto-generated method stub
			return FileVisitResult.CONTINUE;
		}
		
	}
}// class TestTwoThreads
