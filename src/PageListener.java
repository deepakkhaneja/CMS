import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


class PageListener {
	
	MainFrame mainFrame;
	private String semID = Course.currentSemID();
	
	public String getSemID() {
		return semID;
	}

	public PageListener(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		System.out.println("semID = " + semID);
	}

	public void done() {
		mainFrame.startPage.getUserID().setText("");
		mainFrame.startPage.getPassword().setText("");
		mainFrame.add(mainFrame.startPage);
		mainFrame.pack();
	}
	
	public void displayResult(String studentID) {
		mainFrame.remove(mainFrame.startPage);
		mainFrame.resultPage.setSem(studentID);
		mainFrame.add(mainFrame.resultPage);
		mainFrame.pack();
	}
	
	public void newStudent() {
		if(semID.toCharArray()[2] == '2') {
			JOptionPane.showMessageDialog(null, "You cannot register initially in the Spring semester");
			mainFrame.remove(mainFrame.startPage);
			done();
			return;
		}	
		mainFrame.remove(mainFrame.startPage);
		mainFrame.newStudentPage.setSem();
		mainFrame.add(mainFrame.newStudentPage);
		mainFrame.pack();
	}

	public void deanEntered() {
		mainFrame.remove(mainFrame.startPage);
		semID = Course.nextSemID();
		System.out.println("semID = " + semID);
		mainFrame.deanPage.setSem();
		mainFrame.add(mainFrame.deanPage);
		mainFrame.pack();
	}

	public void teacherEntered(String teacherID) {
		mainFrame.remove(mainFrame.startPage);
		mainFrame.teacherPage.setSem(teacherID);
		mainFrame.add(mainFrame.teacherPage);
		mainFrame.pack();
	}

	public void facAdEntered(String facAdID) {
		mainFrame.remove(mainFrame.startPage);
		mainFrame.facAdPage1.setSem(facAdID);
		mainFrame.add(mainFrame.facAdPage1);
		mainFrame.pack();
	}

	public void studentEntered(String studentID) {
		mainFrame.remove(mainFrame.startPage);
		mainFrame.semRegisterPage.setSem(studentID);
		mainFrame.add(mainFrame.semRegisterPage);
		mainFrame.pack();
	}
	
}
