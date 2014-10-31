/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * StartPage.java
 *
 * Created on 3 Feb, 2012, 11:43:35 PM
 */
import java.awt.*;
import java.awt.event.*;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author deepak
 */


class StartPage extends javax.swing.JPanel {

	   // Variables declaration - do not modify
    private javax.swing.JButton register;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField userID;
	private javax.swing.JPasswordField password;
    private PageListener pageListener;    
    private MainFrame mainFrame;    
    
    /** Creates new form StartPage */
    public StartPage(PageListener pageListener, MainFrame mainFrame) {
        initComponents();
		this.mainFrame = mainFrame; 
        loginListener l = new loginListener();
        this.pageListener = pageListener;
        
        register.addActionListener(
        		new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						JTextField id = new JTextField();
						Object[] obj = {"Enter Roll No.(StudentID) to see result\n\n", id};
						Object stringArray[] = {"New Student Register","Display Result"};
						if (JOptionPane.showOptionDialog(null, obj, "Registeration or, Display Results",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, stringArray, obj) == JOptionPane.YES_OPTION) {
							stringArray[0] = "Single Student Register";
							stringArray[1] = "Randomly register many students";
							if (JOptionPane.showOptionDialog(null, null, "Registeration",
									JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, stringArray, null) == JOptionPane.YES_OPTION) 
								StartPage.this.pageListener.newStudent();						
							else {
								//register many students randomly
						        String semID = StartPage.this.pageListener.getSemID();
						        char semIDc[] = semID.toCharArray();
						        int year = (new Integer(new String(semIDc, 0, 2))).intValue();
								
						        //initial reg for autumn sem
						        if(semIDc[2] == '1') 
									currentSemRandomInitialReg(year, semID);
						        
						        //semReg for currentSem and for all randomly generated batch year students
						        int minBatchYear = 12;
						        int maxBatchYear = (semIDc[2] == '1')? year : year - 1;
						        for(int i = minBatchYear; i <= maxBatchYear; i++) {
						        	Student stus[] = getRandomBatchStudents(i);
						        
						        	for(int k = 0; k < stus.length; k++) 
						        		randomStudentReg(semID, stus[k]);
						        }
								
							}
							
						} else {
			       			if(Student.studentIDExists(id.getText()))
			       				StartPage.this.pageListener.displayResult(id.getText());
			       			else JOptionPane.showMessageDialog(null, "Invalid Roll No.(StudentID) entered");
						}						
					}
				});
        userID.addActionListener(l);
        password.addActionListener(l);
    }

    public javax.swing.JTextField getUserID() {
		return userID;
	}

	public javax.swing.JPasswordField getPassword() {
		return password;
	}
	
	public void currentSemRandomInitialReg(int year, String semID) {
		ID studentID;
		String batchID;
		char []stuc;
		int elective;
		
		int deptt, deg, bound, number;
		for(deptt = 0; deptt < 4; deptt++) 
    		for(deg = 1; deg <= 2; deg++) {
    			if(deg == 1) bound = 10; else bound = 5;
    			for(number = 1; number <= bound; number++) {
    				//initial registration
    				studentID = new ID("" + year + (char)(deptt + (int)'A') + deg + number);
    				new Student(studentID.toString() + "name", studentID, 
    						(int)(1000000 + Math.random() * 9000000), "address", "student").insert();
    				
    				stuc = studentID.toString().toCharArray();
    				batchID = new String(stuc, 0, 4);
    				elective = Course.getDegElective(batchID, semID);
    				Student.updateElective(elective, studentID.toString());
    			}
    		} 
	}
	
	public void randomStudentReg(String semID, Student stu) {
        String batchID;
        char studentIDarray[] = stu.getStudentID().toString().toCharArray();
        batchID = new String(studentIDarray, 0, 4);

        Course electives[] = Course.getDegCourses(batchID, Course.ELECTIVE, semID);

        int elective = stu.getElective();
        String elected[] = new String[elective];

        for(int k = 0; k < elective; k++) {
        	if(k >= electives.length) break;
        	elected[k] = electives[k].getCourseID().toString();
        }
        
		Student.setElectiveCourses(stu.getStudentID().toString(), elected, semID);
		Student.setCoreCourses(stu.getStudentID().toString(), semID);
		
		Teacher.computeUpdateResult(stu.getStudentID().toString(), semID, true);
	} 
	
	public Student[] getRandomBatchStudents(int year) {
		Student []stus = new Student[60];

		int o = 0;
		int deptt, deg, bound, number;
		for(deptt = 0; deptt < 4; deptt++) 
    		for(deg = 1; deg <= 2; deg++) {
    			if(deg == 1) bound = 10; else bound = 5;
    			for(number = 1; number <= bound; number++) {
    				stus[o++] = Student.getResult("" + year + (char)(deptt + (int)'A') + deg + number);
    			}
    		}	
		return stus;
	}

    class loginListener implements ActionListener {

    	public void actionPerformed(ActionEvent e) {
    		String userId = StartPage.this.userID.getText();
    		String password = StartPage.this.password.getText();
    		char[] userID = userId.toCharArray();
    		boolean valid = false;
    		
    		if(userID[0] == 'D' || userID[0] == 'F' || userID[0] == 'T') {
    			if(Teacher.teacherIDExists(new ID(userId)) && 
    					password.equals(Teacher.getPassword(userId)) )
    					valid = true;
    		} else {
       			if(Student.studentIDExists(userId) && 
    					password.equals(Student.getPassword(userId)) )
    					valid = true;
    		}

			if(valid) {
				if(userID[0] == 'D') 	  pageListener.deanEntered();
				else if(userID[0] == 'F') pageListener.facAdEntered(userId);
				else if(userID[0] == 'T') pageListener.teacherEntered(userId);
				else					  pageListener.studentEntered(userId);
			} else {
				JOptionPane.showMessageDialog(null, "Invalid UserID or, Password");
			}
    	}
    	
    } 

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        userID = new javax.swing.JTextField();
        password = new javax.swing.JPasswordField();
        register = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setBorder(new javax.swing.border.MatteBorder(null));
        setFont(new java.awt.Font("Ubuntu", 1, 17)); // NOI18N
        setPreferredSize(new java.awt.Dimension((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth()- 100, 
        		(int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 100));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Login", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 15))); // NOI18N

        jLabel2.setText("UserID");

        jLabel3.setText("Password");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabel2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(password)
                    .addComponent(userID, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(userID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21))
        );

        register.setText("Register or Display Result");

        jLabel1.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabel1.setText("IIT Academic Course Management Software");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel1)
                .addContainerGap(939, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(461, 461, 461)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(register, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(481, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(227, 227, 227)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(register)
                .addContainerGap(323, Short.MAX_VALUE))
        );
    }// </editor-fold>


 
}
