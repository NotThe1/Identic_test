package sideMenu;

import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.NoSuchElementException;

import javax.swing.JTextArea;

public class ShowRejects implements Runnable {

	private ArrayDeque<Path> rejects = new ArrayDeque<Path>();
	private JTextArea txtLog;
	private Thread priorThread;
	
	public ShowRejects(Thread priorThread,ArrayDeque<Path> subjects,JTextArea txtLog){
		this.priorThread = priorThread;
		this.rejects= subjects;
		this.txtLog = txtLog;
	}//Constructor
	
	@Override
	public void run() {
		Path path = null;
		String name = null;
		while (true){
			try{
			path = rejects.remove();
			System.out.println(path.toString());
			}catch (NoSuchElementException ex){
				if(priorThread.getState().equals(Thread.State.TERMINATED)){
					return;
				}//if - done ?
			}//try
		}//while

	}//run

}//class ShowSubjects
