import java.sql.ResultSet;
import java.sql.SQLException;

//	public Teacher(ID teacherID, String password);
//	public void insert();
//	public static boolean teacherIDExists(ID teacherID);
//	public static String getPassword(String teacherID);
//  public static void updatePassword(String teacherID , String password);
//	public static void mapCourseTeacher(String courseID, String teacherID, String semID);
//	public static Course[] getTeacherCourses(String teacherID, String semID);
//	public static void updateGrades(String courseID, String studentID[], String grades[], String semID);
//	public static void computeUpdateResult(String studentID, String semID);
//	public static Student[] getFacAdStudents(String batchID, float boundingCgpa);

//FacAd are deptt first 5 teachers, FA1, FA2,..FA5, .............FD5
class Teacher {
	ID teacherID;
	String password;
	private static Query q = new Query();	
	
	public Teacher(ID teacherID, String password) {
		super();
		this.teacherID = teacherID;
		this.password = password;
	}

	public ID getTeacherID() {
		return teacherID;
	}

	public String getPassword() {
		return password;
	}
	
	public void insert() {
		String query = "insert into Teacher(teacherID, password) values('"
			+ teacherID + "', '" + password + "')";
		q.execute(query);
	}	

	public static boolean teacherIDExists(ID teacherID) {
		String query = "select * from Teacher where teacherID = '" + teacherID + "'";
		boolean b;
		try { 
			b = q.execute(query).next(); 
		} catch(SQLException e) {
			System.out.println("teacherIDExists() : SQLException :" + "\nReturning false");
			e.printStackTrace();
			b = false;
		}
		return b;
	}

	public static String getPassword(String teacherID) {
		String query = "select * from Teacher where teacherID = '" + teacherID + "'";
		ResultSet rs = q.execute(query);		
		String password = "";
		try { 
			rs.next();
			password = rs.getString("password");
		} catch(SQLException e) {
			System.out.println("getPassword() : SQLException :" + "\nReturning empty password");
			e.printStackTrace();
		}
		return password;		
	}
	
	public static void updatePassword(String teacherID , String password) {
		String query = "update Teacher set password = '" + password + "' where teacherID = '" + teacherID + "'";
		System.out.println(query);
		q.execute(query);		
	}

	public static void mapCourseTeacher(String courseID, String teacherID, String semID) {
		String query = "insert into CourseTeacher(courseID, teacherID, semID) values('"
			+ courseID + "', '" + teacherID + "', '" + semID + "')";
		q.execute(query);		
	}
	
	public static Course[] getTeacherCourses(String teacherID, String semID) {
		Course courses[] = null;
		try{
			String query = "select count(*) from Course where courseID = " + 
						"any(select courseID from CourseTeacher where teacherID = '" + teacherID + "' and semID = '" + semID  + "')";
			ResultSet rs = q.execute(query);
			rs.next();
			int courseNo = rs.getInt(1);
			courses = new Course[courseNo];
			query = "select * from Course where courseID = " + 
			"any(select courseID from CourseTeacher where teacherID = '" + teacherID + "' and semID = '" + semID  + "')";
			rs = q.execute(query);
			int  i = 0;
			while(rs.next()) {
				courses[i] = new Course(new ID(rs.getString("courseID")), rs.getString("name"), rs.getInt("ltpc"));				
				i++;
			}
			if(i == courseNo) System.out.println("Success : getTeacherCourses(String teacherID) i = " + i);
			else System.out.println("Failure : getTeacherCourses(String teacherID) i = " + i);
		
		} catch(Exception e) {
			e.printStackTrace();
		}
		return courses;
	}
		
	//grades[i] --> studentID[i]
	public static void updateGrades(String courseID, String studentID[], String grades[], String semID) {
		String query;
		for(int i = 0; i < grades.length; i++) {
			query = "update StudentCourse set grade = '" + grades[i] + "' where courseID = '" + courseID + 
			"' and  studentID = '" + studentID[i] + "' and semID = '" + semID + "'";
			System.out.println(query);
			q.execute(query);		
		}
	}
	
	public static void computeUpdateResult(String studentID, String semID, boolean ultimate) {
		Course c[] = Student.getStudentCourses(studentID, semID, 0);
		int grades[] = new int[c.length];
		int creditsCovered[] = new int[c.length];
		int semCred = 0, semCredPts = 0;
		int totCred = 0, totCredPts = 0;
		String query, grade;
		ResultSet rs;
		try {
			for(int i = 0; i < c.length; i++) {
				//grades
				query = "select grade from StudentCourse where courseID = '" + c[i].getCourseID() + "' and studentID = '" + studentID + 
				"' and semID = '" + semID + "'";
				rs = q.execute(query);
				rs.next();
				grade = rs.getString("grade");
				if(grade.equals("EX")) grades[i] =10;
				else if(grade.equals("A")) grades[i] =9;
				else if(grade.equals("B")) grades[i] =8;
				else if(grade.equals("C")) grades[i] =7;
				else if(grade.equals("D")) grades[i] =6;
				else if(grade.equals("P")) grades[i] =5;
				else if(grade.equals("F")) grades[i] =0;
				else {
					grades[i] =0; 
					System.out.println("ERROR computeUpdateResult(String, String, boolean),  a grade = " + grade);
				}
				//credits
				creditsCovered[i] = (grades[i] == 0)? 0 : (c[i].getLtpc() % 10);
				semCred += creditsCovered[i];
				semCredPts += creditsCovered[i] * grades[i]; 
			}
		} catch(Exception e) {
			e.printStackTrace();
			return;
		}	

		Student s = Student.getResult(studentID);
		float tmp;
		if(ultimate) {
			totCred = semCred + s.getTotalCredits();
			tmp = (float)semCredPts + (float)s.getTotalCredits() * s.getCgpa();
		} else {
			totCred = s.getTotalCredits() - s.getSemesterCredits() + semCred;
			tmp = (float)semCredPts + (float)s.getTotalCredits() * s.getCgpa() - (float)s.getSemesterCredits() * s.getSgpa();
		}
		totCredPts = Math.round(tmp);
		float cgpa = Math.round(100 * (totCredPts/(float)totCred)) / (float)100.0;
		float sgpa = Math.round(100 * (semCredPts/(float)semCred)) / (float)100.0;
		Student.updateResult(cgpa, sgpa, totCred, semCred, studentID);
		System.out.println(studentID + ": new cgpa = " + cgpa + ", new sgpa = " + sgpa);
	}
	
	public static Student[] getFacAdStudentsLow(String batchID, float boundingCgpa) {
		Student students[] = null;
		try{
			String query = "select count(*) from Student where studentID like '" + batchID + "%' and cgpa < " + boundingCgpa;
			ResultSet rs = q.execute(query);
			rs.next();
			int studentNo = rs.getInt(1);
			students = new Student[studentNo];
			query = "select studentID, name, cgpa, sgpa, totalCredits, " + 
			"semesterCredits, elective from Student where studentID like '" + batchID + "%' and cgpa < " + boundingCgpa;
			rs = q.execute(query);
			int  i = 0;
			while(rs.next()) {
				students[i] = new Student(rs.getString("name"), new ID(rs.getString("studentID")), rs.getFloat("cgpa"),
						rs.getFloat("sgpa"), rs.getInt("totalCredits"), rs.getInt("semesterCredits"), rs.getInt("elective"));
				i++;
			}
			if(i == studentNo) System.out.println("Success : getFacAdStudentsLow(String batchID)");
			else System.out.println("Failure : getFacAdStudentsLow(String batchID)");
		
		} catch(Exception e) {
			e.printStackTrace();
		}
		return students;
	}	
	
	public static Student[] getFacAdStudentsHigh(String batchID, float boundingCgpa) {
		Student students[] = null;
		try{
			String query = "select count(*) from Student where studentID like '" + batchID + "%' and cgpa >= " + boundingCgpa;
			ResultSet rs = q.execute(query);
			rs.next();
			int studentNo = rs.getInt(1);
			students = new Student[studentNo];
			query = "select studentID, name, cgpa, sgpa, totalCredits, " + 
			"semesterCredits, elective from Student where studentID like '" + batchID + "%' and cgpa >= " + boundingCgpa;
			rs = q.execute(query);
			int  i = 0;
			while(rs.next()) {
				students[i] = new Student(rs.getString("name"), new ID(rs.getString("studentID")), rs.getFloat("cgpa"),
						rs.getFloat("sgpa"), rs.getInt("totalCredits"), rs.getInt("semesterCredits"), rs.getInt("elective"));
				i++;
			}
			if(i == studentNo) System.out.println("Success : getFacAdStudentsHigh(String batchID)");
			else System.out.println("Failure : getFacAdStudentsHigh(String batchID)");
		
		} catch(Exception e) {
			e.printStackTrace();
		}
		return students;
	}	
	
}
