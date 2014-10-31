import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SemRegisterPage.java
 *
 * Created on 5 Feb, 2012, 12:33:46 AM
 */

/**
 *
 * @author deepak
 */
class SemRegisterPage extends javax.swing.JPanel {

	MainFrame mainFrame;
	PageListener pageListener;
	String semID;
	Student stu;
	String[][] coreV, electiveV;
	
    // Variables declaration - do not modify
    private javax.swing.JLabel cgpa;
    private javax.swing.JTable core;
    private javax.swing.JButton done;
    private javax.swing.JTable elective;
    private javax.swing.JLabel instruction;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel name;
    private javax.swing.JButton password;
    private javax.swing.JLabel sem;
    // End of variables declaration

	/** Creates new form SemRegisterPage */
    public SemRegisterPage(PageListener pageListener, MainFrame mainFrame) {
        this.pageListener = pageListener;
        this.mainFrame = mainFrame;
        
       initComponents();
       
       password.addActionListener(
       		new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					JPasswordField passwordField = new JPasswordField();
					passwordField.setEchoChar('*');
					Object[] obj = {"Please enter the password:\n\n", passwordField};
					Object stringArray[] = {"OK","Cancel"};
					if (JOptionPane.showOptionDialog(null, obj, "Need password",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, stringArray, obj) == JOptionPane.YES_OPTION) {
						Student.updatePassword(passwordField.getText().toString(), stu.getStudentID().toString()); 
					}
				}
			});
       
       	done.addActionListener( 
       			new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						int rows[] = elective.getSelectedRows();
						if(rows.length != stu.getElective()) {
							JOptionPane.showMessageDialog(null, "Select exactly " + stu.getElective() + " number of Electives");
							return;
						}
						String elected[] = new String[rows.length];
						for(int i = 0; i < rows.length; i++) 
							elected[i] = electiveV[rows[i]][0];
						Student.setElectiveCourses(stu.getStudentID().toString(), elected, semID);
						Student.setCoreCourses(stu.getStudentID().toString(), semID);
						
						Teacher.computeUpdateResult(stu.getStudentID().toString(), semID, true);
					
						SemRegisterPage.this.mainFrame.remove(SemRegisterPage.this);
						SemRegisterPage.this.pageListener.done();
					}
				});
    }

    public void setSem(String studentID) {
        semID = this.pageListener.getSemID();
        char c[] = semID.toCharArray();

        stu = Student.getResult(studentID);
        
        sem.setText(((c[2]=='1')? "Autumn" : "Spring") + " 20" + (char)c[0] + (char)c[1] + " Registration");
        name.setText("Name : " + stu.getName() + "    Roll No. : " + studentID);
        DecimalFormat d = new DecimalFormat("0.00");        
        cgpa.setText("CGPA : " + d.format(stu.getCgpa()) + "    SGPA : " + d.format(stu.getSgpa()) + "    SemCredits : " + stu.getSemesterCredits()
        		+ "    TotCredits : " + stu.getTotalCredits());
        instruction.setText("\nSelect " + stu.getElective() + " row(s) choosing your ELECTIVE Courses from the Electives offered and Click"
        		+ " \"Done\" button");
        
        String batchID;
        char studentIDarray[] = studentID.toCharArray();
        batchID = new String(studentIDarray, 0, 4);
        Course cores[] = Course.getDegCourses(batchID, Course.CORE, semID);
        Course electives[] = Course.getDegCourses(batchID, Course.ELECTIVE, semID);

        coreV = new String[cores.length][3];
        electiveV = new String[electives.length][3];

        for(int i = 0; i < cores.length; i++) {
        	coreV[i][0] = cores[i].getCourseID().toString();
        	coreV[i][1] = cores[i].getName();
        	coreV[i][2] = "" + cores[i].getLtpc();
        }
        
        for(int i = 0; i < electives.length; i++) {
        	electiveV[i][0] = electives[i].getCourseID().toString();
        	electiveV[i][1] = electives[i].getName();
        	electiveV[i][2] = "" + electives[i].getLtpc();
        }

        core = new javax.swing.JTable(coreV, new String [] { "CourseID", "Course Name", "LTPC" } );
        jScrollPane1.setViewportView(core);

        elective = new javax.swing.JTable(electiveV, new String [] { "CourseID", "Course Name", "LTPC" } );
        jScrollPane2.setViewportView(elective);
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        core = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        elective = new javax.swing.JTable();
        password = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        sem = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        name = new javax.swing.JLabel();
        cgpa = new javax.swing.JLabel();
        done = new javax.swing.JButton();
        instruction = new javax.swing.JLabel();


        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "CORE", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 14))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(85, 85, 85)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(65, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "ELECTIVE", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 14))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(107, 107, 107)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(43, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51))
        );

        password.setText("Change Password");

        sem.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        sem.setText("DDDDDD SDFG DFGHJKLASDFHJJ                                                                          ");

        name.setText("name rollno");

        cgpa.setText("cgpa");

        done.setText("Done");
        done.setActionCommand("jButton1");

        instruction.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        instruction.setForeground(java.awt.Color.red);
        instruction.setText("Choose  x  number   of   ELECTIVE  Courses  from  the Courses offered");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(instruction)
                        .addContainerGap())
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(77, 77, 77)
                                .addComponent(done, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE))
                            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(name, javax.swing.GroupLayout.DEFAULT_SIZE, 614, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 614, Short.MAX_VALUE)
                            .addComponent(sem, javax.swing.GroupLayout.DEFAULT_SIZE, 614, Short.MAX_VALUE)
                            .addComponent(cgpa, javax.swing.GroupLayout.DEFAULT_SIZE, 614, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(87, 87, 87))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(sem, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(name)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cgpa)
                .addGap(13, 13, 13)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(instruction, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(password)
                    .addComponent(done))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(317, 317, 317)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(352, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(215, Short.MAX_VALUE))
        );

    }// </editor-fold>

}
