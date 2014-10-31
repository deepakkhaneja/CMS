import javax.swing.*;

import java.awt.*;

import java.awt.event.*;

class MainFrame extends JFrame {
	public StartPage startPage;
	public DeanPage deanPage;	
	public NewStudentPage newStudentPage;
	public SemRegisterPage semRegisterPage;
	public TeacherPage teacherPage;
	public ResultPage resultPage;
	public FacAdPage1 facAdPage1;
	public FacAdPage2 facAdPage2;
	private PageListener pageListener;
	
	public MainFrame() {
		setUp();
		getContentPane().add(startPage);
		Toolkit tk = Toolkit.getDefaultToolkit();  
		int xSize = ((int) tk.getScreenSize().getWidth());  
		int ySize = ((int) tk.getScreenSize().getHeight()); 
		setSize(xSize,ySize);
		setVisible(true);
	}
	
	public void setUp() {
	 	pageListener = new PageListener(this);
	 	startPage = new StartPage(pageListener, this);
	 	newStudentPage = new NewStudentPage(pageListener, this);
	 	semRegisterPage = new SemRegisterPage(pageListener, this);
	 	deanPage = new DeanPage(pageListener, this);
	 	teacherPage = new TeacherPage(pageListener, this);
	 	facAdPage1 = new FacAdPage1(pageListener, this);
	 	facAdPage2 = new FacAdPage2(pageListener, this);
	 	resultPage = new ResultPage(pageListener, this);
	}	
	
} 