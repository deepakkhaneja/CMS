//Project CMS: 3305 Lines in total
import javax.swing.*;

public class Main {

	public static void main(String args[]) {
		
		//Dean
		new Teacher(new ID("D"), "D").insert();		
		
		//FacAds
		for(int dep = 0; dep < 4; dep++) 
			for(int i = 1; i <= 5; i++)
				new Teacher(new ID("F" + (char)((int)'A' + dep) + i), "facad").insert();
		
		//Non-FacAd Teachers
		for(int dep = 0; dep < 4; dep++) 
			for(int i = 1; i <= 10; i++)
				new Teacher(new ID("T" + (char)((int)'A' + dep) + i), "teacher").insert();

		MainFrame cms = new MainFrame();
 	
	 	cms.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}	

}
