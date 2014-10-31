import java.sql.*;

//	public Course(ID courseID, String name, int ltpc);
//	public void insert();
//  public static void mapCourseDeg(String degID, String courseID, int subtype, String semID);
//	public static void setDegElective(String degID, int elective, String semID);
//	public static int getDegElective(String degID, String semID);
//	public static Course[] getDegCourses(String degID, int subtype, String semID);

class Course {
	public static final int CORE = 1, ELECTIVE = 2, NA = 0;//subType of a course for a degree 
	public static final int DEFAULT_SEM_COURSES = 35;
	private ID courseID;//sem + deptt + courseNo
	private String name;
	private int ltpc;
	private static Query q = new Query();	
	

	public Course(ID courseID, String name, int ltpc) {
		super();
		this.courseID = courseID;
		this.name = name;
		this.ltpc = ltpc;
	}

	public ID getCourseID() {
		return courseID;
	}

	public String getName() {
		return name;
	}

	public int getLtpc() {
		return ltpc;
	}

	public void insert() {
		String query = "insert into Course(courseID, name, ltpc) values('"
			+ courseID + "', '" + name + "', " + ltpc + ")";
		q.execute(query);

	}
	
	public static void mapCourseDeg(String degID, String courseID, int subtype, String semID) {
		String query = "insert into CourseDeg(courseID, degID, subtype, semID) values('"
			+ courseID + "', '" + degID + "', " + subtype + ", '" + semID + "')";
		q.execute(query);		
	}
	
	public static void setDegElective(String degID, int elective, String semID) {
		String query = "insert into DegID(degID, elective, semID) values('"
			+ degID + "', " + elective + ", '" + semID + "')";		
		q.execute(query);		
	}
	
	public static String nextSemID() {
		String semIDs[] = null;
		int semIDNo = 0;
		try {
			String query = "select count(*) from DegID";
			ResultSet rs = q.execute(query);
			rs.next();
			semIDNo = rs.getInt(1);
			semIDs = new String[semIDNo];
			query = "select semID from DegID";
			rs = q.execute(query);
			int  i = 0;
			while(rs.next()) {
				semIDs[i] = rs.getString("semID");//, rs.getString("name"), rs.getInt("ltpc"));				
				i++;
			}
			if(i == semIDNo) System.out.println("Success : nextSemID()");
			else System.out.println("Failure : nextSemID()");
		} catch(Exception e) {
			e.printStackTrace();
		}
		if(semIDNo == 0) return "121";
		char []c;
		int []wt = new int[semIDNo];
		int maxWt = 0;
		String maxSemID = null;
		for(int k  = 0; k < semIDNo; k++) {
			c = semIDs[k].toCharArray();
			wt[k] = (int)c[0] * 1000000 + (int)c[1] * 10000 + ((c[2] == '1')? 100 : 0); 
			if(maxWt < wt[k]) { maxWt = wt[k]; maxSemID = semIDs[k]; }
		}
		c = maxSemID.toCharArray();
		
		if(c[2] == '2') return "" + (char)c[0] + (char)c[1] + "1";
		else if(c[1] == '9') return "" + (char)((int)c[0] + 1) + "02";
		else return "" + (char)c[0] + (char)((int)c[1] + 1) + "2";
		
	} 
	
	public static String currentSemID() {
		String semIDs[] = null;
		int semIDNo = 0;
		try {
			String query = "select count(*) from DegID";
			ResultSet rs = q.execute(query);
			rs.next();
			semIDNo = rs.getInt(1);
			semIDs = new String[semIDNo];
			query = "select semID from DegID";
			rs = q.execute(query);
			int  i = 0;
			while(rs.next()) {
				semIDs[i] = rs.getString("semID");//, rs.getString("name"), rs.getInt("ltpc"));				
				i++;
			}
			if(i == semIDNo) System.out.println("Success : currentSemID()");
			else System.out.println("Failure : currentSemID()");
		} catch(Exception e) {
			e.printStackTrace();
		}
		if(semIDNo == 0) return "121";
		char []c;
		int []wt = new int[semIDNo];
		int maxWt = 0;
		String maxSemID = null;
		for(int k  = 0; k < semIDNo; k++) {
			c = semIDs[k].toCharArray();
			wt[k] = (int)c[0] * 1000000 + (int)c[1] * 10000 + ((c[2] == '1')? 100 : 0); 
			if(maxWt < wt[k]) { maxWt = wt[k]; maxSemID = semIDs[k]; }
		}
		return maxSemID;
	} 
	
	public static int getDegElective(String batchID, String semID) {
		char batchIDc[] = batchID.toCharArray();
		char semIDc[] = semID.toCharArray();

		int currentYear, batchYear;
		currentYear = new Integer("" + semIDc[0] + semIDc[1]).intValue();
		batchYear   = new Integer("" + batchIDc[0] + batchIDc[1]).intValue();
		
		int degYear;
		if(semIDc[2] == '1') degYear = currentYear - batchYear + 1;
		else degYear = currentYear - batchYear;
		
		System.out.println("degYear = " + degYear);

		if(degYear > 5 || degYear < 1) return 0;

		String degID = "" + degYear + new String(batchIDc, 2, batchIDc.length - 2);
		String query = "select elective from DegID where degID = '" + degID  + "' and semID = '" + semID + "'";
		ResultSet rs = q.execute(query);
		int elective = 1;
		try{
			if(rs != null && rs.next()) {
				elective = rs.getInt("elective");
			}	
		} catch(Exception e) {
			e.printStackTrace();
		}
		return elective;
	}	
	
	public static Course[] getDegCourses(String batchID, int subtype, String semID) {
		char batchIDc[] = batchID.toCharArray();
		char semIDc[] = semID.toCharArray();

		int currentYear, batchYear;
		currentYear = new Integer("" + semIDc[0] + semIDc[1]).intValue();
		batchYear   = new Integer("" + batchIDc[0] + batchIDc[1]).intValue();
		
		int degYear;
		if(semIDc[2] == '1') degYear = currentYear - batchYear + 1;
		else degYear = currentYear - batchYear;
		
		System.out.println("degYear = " + degYear);

		if(degYear > 5 || degYear < 1) return null;

		String degID = "" + degYear + new String(batchIDc, 2, batchIDc.length - 2);

		Course courses[] = new Course[0];
		try {
			String query = "select count(*) from Course where courseID = " + 
			"any(select courseID from CourseDeg where degID = '" + degID + "' and subtype = " + subtype + " and semID = '" + semID + "')";
			ResultSet rs = q.execute(query);
			if(rs == null) return courses;
			rs.next();
			int coreNo = rs.getInt(1);
			courses = new Course[coreNo];
			query = "select * from Course where courseID = " + 
			"any(select courseID from CourseDeg where degID = '" + degID + "' and subtype = " + subtype + " and semID = '" + semID + "')";
			rs = q.execute(query);
			int  i = 0;
			while(rs.next()) {
				courses[i] = new Course(new ID(rs.getString("courseID")), rs.getString("name"), rs.getInt("ltpc"));				
				i++;
			}
			if(i == coreNo) System.out.println("Success : getDegCourses(String batchID, int subtype, String semID)");
			else System.out.println("Failure : getDegCourses(String batchID, int subtype, String semID)");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return courses;
	}	
	
	
}
