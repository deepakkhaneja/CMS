import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.InputMismatchException;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * FacAdPage2.java
 *
 * Created on 7 Feb, 2012, 12:28:13 AM
 */

/**
 *
 * @author deepak
 */
public class FacAdPage2 extends javax.swing.JPanel {

	MainFrame mainFrame;
	PageListener pageListener;
	String semID;
	Student stusLow[], stusHigh[];
	String facAdID;
	String values[][];

    // Variables declaration - do not modify
    private javax.swing.JButton done;
    private javax.swing.JLabel facad;
    private javax.swing.JTable facadstus;
    private javax.swing.JLabel instruct;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel sem;
    // End of variables declaration

    /** Creates new form FacAdPage2 */
	   public FacAdPage2(PageListener pageListener, MainFrame mainFrame) {
	        this.pageListener = pageListener;
	        this.mainFrame = mainFrame;
	        
	       initComponents();
	       
	       
	       	done.addActionListener( 
	       			new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent arg0) {
							// TODO Auto-generated method stub
							if(facadstus.isEditing()) {
								JOptionPane.showMessageDialog(null, "End editing the Table and Click \"Done\" button");
								return;
							}
							
							int i = 0;	
							try {
							//validity
								for(i = 0; i < stusLow.length; i++) 
									if(0 > new Integer(facadstus.getValueAt(i, 3).toString()).intValue() || 
											new Integer(facadstus.getValueAt(i, 3).toString()).intValue() > stusLow[i].getElective()) {
										JOptionPane.showMessageDialog(null, "Invalid Electives set at row " + 
												(i+1) + " StudentID " + stusLow[i].getStudentID().toString() + ": Electives can only be reduced upto 0");
										return;
									}
							} catch(InputMismatchException e) {
								JOptionPane.showMessageDialog(null, "Invalid Electives set at row " + 
										(i+1) + " StudentID " + stusLow[i].getStudentID().toString() + ": Electives must be a digit");
								return;
							} catch(NumberFormatException e) {
								JOptionPane.showMessageDialog(null, "Invalid Electives set at row " + 
										(i+1) + " StudentID " + stusLow[i].getStudentID().toString() + ": Electives must be a digit");
								return;
							}
							
							try {
								for(i = 0; i < stusHigh.length; i++) 
									if(new Integer(facadstus.getValueAt(i + stusLow.length, 3).toString()).intValue() != stusHigh[i].getElective()) {
										JOptionPane.showMessageDialog(null, "Invalid Electives set at row " + (i+1 + stusLow.length) +
												" StudentID " + stusHigh[i].getStudentID().toString() + 
										": Electives cannot be changed for students with CGPA >= 7.0");
										return;
									}
							} catch(InputMismatchException e) {
								JOptionPane.showMessageDialog(null, "Invalid Electives set at row " + 
										(i+stusLow.length +1) + " StudentID " + stusHigh[i].getStudentID().toString() + ": Electives must be a digit");
								return;
							} catch(NumberFormatException e) {
								JOptionPane.showMessageDialog(null, "Invalid Electives set at row " + 
										(i+stusLow.length +1) + " StudentID " + stusHigh[i].getStudentID().toString() + ": Electives must be a digit");
								return;
							}

							//after validity check, set electives of students with cgpa < 7.0
							for(i = 0; i < stusLow.length; i++) 
								Student.updateElective(new Integer(facadstus.getValueAt(i, 3).toString()).intValue(), 
										stusLow[i].getStudentID().toString());
								
							FacAdPage2.this.mainFrame.remove(FacAdPage2.this);
							FacAdPage2.this.pageListener.done();
						}
					});
	    }

	    public void setSem(String facAdID) {
	        semID = this.pageListener.getSemID();
	        char c[] = semID.toCharArray();

	        this.facAdID = facAdID;

	        String year;
	        String batchID;
	        char fac[] = facAdID.toCharArray();
	        
	        if(fac[2] == '1') year = "First";
	        else  if(fac[2] == '2') year = "Second";
	        else  if(fac[2] == '3') year = "Third";
	        else  if(fac[2] == '4') year = "Fourth";
	        else year = "Fifth";
	        	
	        //find batchID
	        int y;
			y = new Integer("" + c[0] + c[1]).intValue();
			
			if(c[2] == '1') y -= (new Integer("" + fac[2]).intValue() - 1);
			else y -= new Integer("" + fac[2]).intValue();
			
			System.out.println("y = " + y);

			if(y < 12) {
		        sem.setText(((c[2]=='1')? "Autumn" : "Spring") + " 20" + (char)c[0] + (char)c[1] + " Results");
		        facad.setText("Faculty Advisor ID: " + facAdID + "    Department: " + fac[1] + "    Batch: " + year + " Year     Batch Strength: 0");
		        values = new String[0][7];
		        facadstus = new javax.swing.JTable(values, 
		                new String [] {
		                "StudentID", "Student Name", "CGPA", "Electives", "SGPA", "Total Credits", "Sem Credits"
		            }
		        );
		        
		        jScrollPane1.setViewportView(facadstus);
		        stusLow = new Student[0];
		        stusHigh = new Student[0];
				return;
			}

			batchID = "" + y + "" + (char)fac[1];
	        stusLow = Teacher.getFacAdStudentsLow(batchID, (float)7.0);
	        if(stusLow == null) stusLow = new Student[0];
	        stusHigh = Teacher.getFacAdStudentsHigh(batchID, (float)7.0);
	        if(stusHigh == null) stusHigh = new Student[0];

	        sem.setText(((c[2]=='1')? "Autumn" : "Spring") + " 20" + (char)c[0] + (char)c[1] + " Results");
	        facad.setText("Faculty Advisor ID: " + facAdID + "    Department: " + fac[1] + "    Batch: " + year + " Year     Batch Strength: (CGPA < 7)" + 
	        				stusLow.length + " + (CGPA >= 7)" + stusHigh.length + "    Total: " + (stusLow.length + stusHigh.length));

	        values = new String[stusLow.length + stusHigh.length][7];
	        DecimalFormat decif = new DecimalFormat("0.00");
	        int i;
	        for(i = 0; i < stusLow.length; i++) {
	        	values[i][0] = stusLow[i].getStudentID().toString();
	        	values[i][1] = stusLow[i].getName();
	        	values[i][2] = decif.format((double)stusLow[i].getCgpa());
	        	values[i][3] = "" + stusLow[i].getElective();
	        	values[i][4] = decif.format((double)stusLow[i].getSgpa());
	        	values[i][5] = "" + stusLow[i].getTotalCredits();
	        	values[i][6] = "" + stusLow[i].getSemesterCredits();
	        }
	        
	        for(i = 0; i < stusHigh.length; i++) {
	        	values[i + stusLow.length][0] = stusHigh[i].getStudentID().toString();
	        	values[i + stusLow.length][1] = stusHigh[i].getName();
	        	values[i + stusLow.length][2] = decif.format((double)stusHigh[i].getCgpa());
	        	values[i + stusLow.length][3] = "" + stusHigh[i].getElective();
	        	values[i + stusLow.length][4] = decif.format((double)stusHigh[i].getSgpa());
	        	values[i + stusLow.length][5] = "" + stusHigh[i].getTotalCredits();
	        	values[i + stusLow.length][6] = "" + stusHigh[i].getSemesterCredits();
	        }
	        
	        facadstus = new javax.swing.JTable(values, 
	                new String [] {
	                "StudentID", "Student Name", "CGPA", "Electives", "SGPA", "Total Credits", "Sem Credits"
	            }
	        );
	        
	        jScrollPane1.setViewportView(facadstus);
	        return;
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
        jScrollPane1 = new javax.swing.JScrollPane();
        facadstus = new javax.swing.JTable();
        sem = new javax.swing.JLabel();
        facad = new javax.swing.JLabel();
        instruct = new javax.swing.JLabel();
        done = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        sem.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        sem.setText("Autumn ");

        facad.setText("facad");

        instruct.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        instruct.setForeground(java.awt.Color.red);
        instruct.setText("Semester Tailoring : Can reduce Electives for CGPA < 7.0 on a case by case basis");

        done.setText("Done");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1307, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sem, javax.swing.GroupLayout.DEFAULT_SIZE, 1307, Short.MAX_VALUE)
                    .addComponent(facad, javax.swing.GroupLayout.PREFERRED_SIZE, 990, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(instruct)
                        .addGap(256, 256, 256)
                        .addComponent(done, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(sem)
                .addGap(12, 12, 12)
                .addComponent(facad)
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(instruct)
                    .addComponent(done))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 598, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(77, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>

}
