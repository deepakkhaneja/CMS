import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTable;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * TeacherPage.java
 *
 * Created on 5 Feb, 2012, 9:31:16 AM
 */

/**
 *
 * @author deepak
 */
public class TeacherPage extends javax.swing.JPanel {

	MainFrame mainFrame;
	PageListener pageListener;
	String teacherID;
	String semID;
	String values[][];
	Course[] course;
	Student[] stus;
	
    // Variables declaration - do not modify
    private javax.swing.JButton done;
    private javax.swing.JLabel instruct;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton password;
    private javax.swing.JLabel sem;
    private javax.swing.JLabel teacher;
    // End of variables declaration

	
	/** Creates new form TeacherPage */
    public TeacherPage(PageListener pageListener, MainFrame mainFrame) {
        this.pageListener = pageListener;
        this.mainFrame = mainFrame;

        initComponents();
        
        done.addActionListener( 
        	new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					//isGrade valid
					if(jTable1.isEditing()) {
						JOptionPane.showMessageDialog(null, "End editing the Table and Click \"Done\" button");
						return;
					}
					String studentID[] = new String[stus.length];
					String grade[]   = new String[stus.length];

					for(int i = 0; i < stus.length; i++)
						if(!isGrade(jTable1.getValueAt(i, 2).toString())) {
							JOptionPane.showMessageDialog(null, "Invalid grade at row number " + (i + 1) + 
									"\nSet again and Click \"Done\" button");
							return;
						} else {
							studentID[i] = stus[i].getStudentID().toString();
							grade[i] = jTable1.getValueAt(i, 2).toString();
							System.out.println(jTable1.getValueAt(i, 0).toString() + "  " + 
									jTable1.getValueAt(i, 1).toString() + "  " + jTable1.getValueAt(i, 2).toString());
						}
					
					if(course.length > 0)
						Teacher.updateGrades(course[0].getCourseID().toString(), studentID, grade, semID);

					for(int i = 0; i < stus.length; i++)
						Teacher.computeUpdateResult(stus[i].getStudentID().toString(), semID, false);

					TeacherPage.this.mainFrame.remove(TeacherPage.this);
			        TeacherPage.this.pageListener.done();
				}
        	});

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
    						System.out.println("password about to update");
    						Teacher.updatePassword(teacherID, passwordField.getText().toString()); 
    					}
    				}
    			});
           
    }

    public void setSem(String teacherID) {
        semID = this.pageListener.getSemID();
        char c[] = semID.toCharArray();
        this.teacherID = teacherID;
        stus = new Student[0];
        course = new Course[0];
        
        String grd[] = {"EX", "A", "B", "C", "D", "P", "F"};

        sem.setText(((c[2]=='1')? "Autumn" : "Spring") + " 20" + (char)c[0] + (char)c[1] + " Grade List");
        course = Teacher.getTeacherCourses(teacherID, semID);
        if(course == null || course.length == 0) {
            teacher.setText("TeacherID : " + teacherID + " has no courses");
            instruct.setText("No students shown");
            values = new String[0][3];
            stus = new Student[0];
            jTable1 = new JTable(values, new String [] { "StudentID", "Student Name", "Grade" } );
            jScrollPane1.setViewportView(jTable1);
            return;
        }
        
        teacher.setText("TeacherID : " + teacherID + "   CourseID : " + course[0].getCourseID().toString());
        
        stus = Student.getCourseStudents(course[0].getCourseID().toString(), semID);        
        if(stus == null || stus.length == 0) {
            instruct.setText("No students in this course");
            values = new String[0][3];
            jTable1 = new JTable(values, new String [] { "StudentID", "Student Name", "Grade" } );
            jScrollPane1.setViewportView(jTable1);
            return;
        } else {
        	instruct.setText(stus.length + " students in this course, Set the grades");
        }
        
        values = new String[stus.length][3];
        
        for(int i = 0; i < stus.length; i++) {
        	values[i][0] = stus[i].getStudentID().toString();
        	values[i][1] = stus[i].getName().toString();
        	values[i][2] = Student.getGrade(stus[i].getStudentID().toString(), course[0].getCourseID().toString(), semID);
        }
        
        jTable1 = new JTable(values, new String [] { "StudentID", "Student Name", "Grade" } );
        jScrollPane1.setViewportView(jTable1);
        return;
    }
    
    public boolean isGrade(String g) {
    	return (g.equals("EX") || g.equals("A") || g.equals("B") || g.equals("C") || g.equals("D") || g.equals("P") || g.equals("F"));
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        sem = new javax.swing.JLabel();
        teacher = new javax.swing.JLabel();
        instruct = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        password = new javax.swing.JButton();
        done = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        sem.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        sem.setText("SSSSSSSSDDDDDDDDDDD");

        teacher.setText("tttttttttttttggggghhhhhhhhhh");

        instruct.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        instruct.setForeground(java.awt.Color.red);
        instruct.setText("Set the grades of students enrolled");


        password.setText("Change Password");

        done.setText("Done");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(273, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(teacher, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sem, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(275, 275, 275))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(255, 255, 255)
                .addComponent(instruct)
                .addContainerGap(284, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(143, 143, 143)
                .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(117, 117, 117)
                .addComponent(done, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(189, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(92, 92, 92)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 588, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(115, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(sem)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(teacher)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(instruct)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(done)
                    .addComponent(password))
                .addGap(23, 23, 23))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(229, 229, 229)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(265, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 634, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(76, Short.MAX_VALUE))
        );
    }// </editor-fold>


}
