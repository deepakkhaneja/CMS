import java.sql.*;

//	public Student(String name, ID studentID, int regNo, String address, String password);
//	public Student(String name, ID studentID, float cgpa, float sgpa,
//               int totalCredits, int semesterCredits, int elective);
//	public Student(String name, ID studentID);
//	public void insert();
//	public static boolean studentIDExists(String studentID);
//	public static String getPassword(String studentID);
//	public static Student getResult(String studentID);
//	public static void updateResult(float cgpa, float sgpa, int totalCredits, int semesterCredits, String studentID);
//	public static void updateElective(int elective, String studentID);
//	public static void updatePassword(String password, String studentID);
//	public static void setCoreCourses(String strStudentID, String semID);
//	public static void setElectiveCourses(String studentID, String electiveCourseID[], String semID);
//	public static Course[] getStudentCourses(String studentID, String semID);
//	public static Student[] getCourseStudents(String courseID, String semID);

//nobody fails
class Student {

	public static final int B_TECH = 1, DUAL = 2; 
	public static final int TOTAL_DEPTT = 4; 
	public static final char DEPTT_A = 'A', DEPTT_B = 'B', DEPTT_C = 'C', DEPTT_D = 'D'; 
	public static final char DEFAULT_GRADE = 'A'; 
	private String name;
	private ID studentID;
	private int regNo;
	private String address;
	private String password;
	private float cgpa = 0, sgpa = 0;
	private int totalCredits = 0, semesterCredits = 0;
	private int elective = 1;	
	private static Query q = new Query();
	
	public Student(String name, ID studentID) {
		super();
		this.name = name;
		this.studentID = studentID;
	}


	public Student(String name, ID studentID, float cgpa, float sgpa,
			int totalCredits, int semesterCredits, int elective) {
		super();
		this.name = name;
		this.studentID = studentID;
		this.cgpa = cgpa;
		this.sgpa = sgpa;
		this.totalCredits = totalCredits;
		this.semesterCredits = semesterCredits;
		this.elective = elective;
	}


	public Student(String name, ID studentID, int regNo, String address,
			String password) {
		super();
		this.name = name;
		this.studentID = studentID;
		this.regNo = regNo;
		this.address = address;
		this.password = password;
	}
	
	public void insert() {
		String query = "insert into Student(studentID, password, regno, address, name," 
			+ " cgpa, sgpa, totalCredits, semesterCredits, elective) values('"
			+ studentID + "', '" + password + "', " + regNo +  ", '" + address + "', '" + name + "', " + cgpa
			+ ", " + sgpa + ", " + totalCredits + ", " + semesterCredits + ", " + elective + ")";
		q.execute(query);
	}

	public static boolean studentIDExists(String studentID) {
		String query = "select * from Student where studentID = '" + studentID + "'";
		boolean b;
		try { 
			b = q.execute(query).next(); 
		} catch(SQLException e) {
			System.out.println("studentIDExists() : SQLException :" + "\nReturning false");
			e.printStackTrace();
			b = false;
		}
		return b;
	}
	
	public static String getPassword(String studentID) {
		String query = "select * from Student where studentID = '" + studentID + "'";
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
		
	public static Student getResult(String studentID) {
		String query = "select studentID, name, cgpa, sgpa, totalCredits, " + 
		"semesterCredits, elective from Student where studentID = '" + studentID + "'";
		ResultSet rs = q.execute(query);		
		Student s = null;
		try { 
			rs.next();
			s = new Student(rs.getString("name"), new ID(rs.getString("studentID")), rs.getFloat("cgpa"),
					rs.getFloat("sgpa"), rs.getInt("totalCredits"), rs.getInt("semesterCredits"), rs.getInt("elective"));
		} catch(SQLException e) {
			System.out.println("getResult() : SQLException :" + "\nReturning null");
			e.printStackTrace();
		}
		return s;		
	}	

	public String getName() {
		return name;
	}


	public ID getStudentID() {
		return studentID;
	}


	public float getSgpa() {
		return sgpa;
	}


	public int getSemesterCredits() {
		return semesterCredits;
	}


	public float getCgpa() {
		return cgpa;
	}

	public int getTotalCredits() {
		return totalCredits;
	}

	public int getElective() {
		return elective;
	}

	public static void updateResult(float cgpa, float sgpa, int totalCredits, int semesterCredits, String studentID) {
		String query = "update Student set sgpa = " + sgpa + " where studentID = '" + studentID + "'";
		q.execute(query);		
		query = "update Student set cgpa = " + cgpa + " where studentID = '" + studentID + "'";
		q.execute(query);		
		query = "update Student set totalCredits = " + totalCredits + " where studentID = '" + studentID + "'";
		q.execute(query);		
		query = "update Student set semesterCredits = " + semesterCredits + " where studentID = '" + studentID + "'";
		q.execute(query);		
	}

	public static void updateElective(int elective, String studentID) {
		String query = "update Student set elective = " + elective + " where studentID = '" + studentID + "'";
		q.execute(query);		
	}
	
	public static void updatePassword(String password, String studentID) {
		String query = "update Student set password = '" + password + "' where studentID = '" + studentID + "'";
		System.out.println(query);
		q.execute(query);		
	}

	public static void setCoreCourses(String strStudentID, String semID) {
		ID studentID = new ID(strStudentID);
		Course core[] = Course.getDegCourses("" + studentID.getT1() + studentID.getT2() + studentID.getT3(), Course.CORE, semID);
		String query ;
		for(int i = 0; i < core.length; i++) {
			query = "insert into StudentCourse(studentID, courseID, grade, subtype, semID) values('"
				+ studentID + "', '" + core[i].getCourseID() + "','" + Student.DEFAULT_GRADE + "'," + "1, '" + semID + "')";
			q.execute(query);					
		}
	}
	
	public static void setElectiveCourses(String studentID, String electiveCourseID[], String semID) {
		String query ;
		for(int i = 0; i < electiveCourseID.length; i++) {
			query = "insert into StudentCourse(studentID, courseID, grade, subtype, semID) values('"
				+ studentID + "', '" + electiveCourseID[i] + "','" + Student.DEFAULT_GRADE + "'," + "2, '" + semID + "')";
			q.execute(query);					
		}
	}
	
	public static Course[] getStudentCourses(String studentID, int subtype, String semID) {
		Course courses[] = null;
		try{
			String query = "select count(*) from Course where courseID = " + 
						"any(select courseID from StudentCourse where studentID = '" + studentID 
						+ "' and subtype = " + subtype + " and semID = '" + semID  + "')";
			ResultSet rs = q.execute(query);
			rs.next();
			int courseNo = rs.getInt(1);
			courses = new Course[courseNo];
			query = "select * from Course where courseID = " + 
			"any(select courseID from StudentCourse where studentID = '" + studentID + "' and subtype = " + subtype + " and semID = '" + semID  + "')";
			rs = q.execute(query);
			int  i = 0;
			while(rs.next()) {
				courses[i] = new Course(new ID(rs.getString("courseID")), rs.getString("name"), rs.getInt("ltpc"));				
				i++;
			}
			if(i == courseNo) System.out.println("Success : getStudentCourses(String studentID)");
			else System.out.println("Failure : getStudentCourses(String studentID)");
		
		} catch(Exception e) {
			e.printStackTrace();
		}
		return courses;
	}

	public static String getGrade(String studentID, String courseID, String semID) {
		String query = "select grade from StudentCourse where studentID = '" + studentID + 
						"' and courseID = '" + courseID + "' and semID = '" + semID + "'";
		String g = "A";
		ResultSet rs = q.execute(query);
		if(rs == null) {
			System.out.println("rs = null in getGrade(studentID, courseID, semID) returning \"A\" grade");
			return g;
		}
		try {
			if(!rs.next()) {
				System.out.println("rs = empty in getGrade(studentID, courseID, semID) returning \"A\" grade");
				return g;
			}
			g = rs.getString("grade");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return g;
	}
	
	public static Course[] getStudentCourses(String studentID, String semID, int subtype) {
		Course courses[] = null;
		try{
			String query;
			if(subtype == 0) {
				query = "select count(*) from Course where courseID = " + 
				"any(select courseID from StudentCourse where studentID = '" + studentID + "' and semID = '" + semID  + "')";
			} else {
				query = "select count(*) from Course where courseID = " + 
				"any(select courseID from StudentCourse where studentID = '" + studentID + "' and semID = '" + semID  + "' and subtype = "
				+ subtype + ")";
			}
			ResultSet rs = q.execute(query);
			rs.next();
			int courseNo = rs.getInt(1);
			courses = new Course[courseNo];
			if(subtype == 0) {
				query = "select * from Course where courseID = " + 
				"any(select courseID from StudentCourse where studentID = '" + studentID + "' and semID = '" + semID  + "')";
			} else {
				query = "select * from Course where courseID = " + 
				"any(select courseID from StudentCourse where studentID = '" + studentID + "' and semID = '" + semID  + "' and subtype = "
				+ subtype + ")";
			}
			rs = q.execute(query);
			int  i = 0;
			while(rs.next()) {
				courses[i] = new Course(new ID(rs.getString("courseID")), rs.getString("name"), rs.getInt("ltpc"));				
				i++;
			}
			if(i == courseNo) System.out.println("Success : getStudentCourses(String studentID)");
			else System.out.println("Failure : getStudentCourses(String studentID)");
		
		} catch(Exception e) {
			e.printStackTrace();
		}
		return courses;
	}
	

	
	public static Student[] getCourseStudents(String courseID, String semID) {
		Student students[] = new Student[0];
		try{
			String query = "select count(*) from Student where studentID = " + 
						"any(select studentID from StudentCourse where courseID = '" + courseID + "' and semID = '" + semID  + "')";
			ResultSet rs = q.execute(query);
			rs.next();
			if(rs == null) return students;
			int studentNo = rs.getInt(1);
			students = new Student[studentNo];
			query = "select * from Student where studentID = " + 
			"any(select studentID from StudentCourse where courseID = '" + courseID + "' and semID = '" + semID  + "')";
			rs = q.execute(query);
			int  i = 0;
			while(rs.next()) {
				students[i] = new Student(rs.getString("name"), new ID(rs.getString("studentID")));				
				i++;
			}
			if(i == studentNo) System.out.println("Success : getCourseStudents(String courseID) i = " + i);
			else System.out.println("Failure : getCourseStudents(String courseID) i = " + i);
		
		} catch(Exception e) {
			e.printStackTrace();
		}
		return students;
	}
	

	public static Student[] getDegIDStudents(String degID, String semID) {
		Student students[] = null;
		try{
			char degIDc[] = degID.toCharArray();
			int year;
			char semIDc[] = semID.toCharArray();
						
			year = new Integer("" + semIDc[0] + semIDc[1]).intValue();
			
			if(semIDc[2] == '1') year -= (new Integer("" + degIDc[0]).intValue() - 1);
			else year -= new Integer("" + degIDc[0]).intValue();
			
			System.out.println("year = " + year);

			if(year < 12) return null;

			String batchID = "" + year + new String(degIDc, 1, degIDc.length - 1);
			
			String query = "select count(*) from Student where studentID like '" + batchID + "%'"; 
			ResultSet rs = q.execute(query);
			rs.next();
			int studentNo = rs.getInt(1);
			students = new Student[studentNo];
			query = "select name, studentID from Student where studentID like '" + batchID + "%'"; 
			rs = q.execute(query);
			int  i = 0;
			while(rs.next()) {
				students[i] = new Student(rs.getString("name"), new ID(rs.getString("studentID")));				
				i++;
			}
			if(i == studentNo) System.out.println("Success : getDegIDStudents(String degID, String semID)");
			else System.out.println("Failure : getDegIDStudents(String degID, String semID))");
		
		} catch(Exception e) {
			e.printStackTrace();
		}
		return students;
	}

	public static int getBatchMaxStudentNumber(String batchID) {
		int studentNo = 0;
		try{
			String query = "select count(*) from Student where studentID like '" + batchID + "%'";
			ResultSet rs = q.execute(query);
			rs.next();
			studentNo = rs.getInt(1);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return studentNo;
		
	}	
	
}