import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//export CLASSPATH=$CLASSPATH:/usr/share/java/mysql-connector-java.jar
class Query {
	private static Connection con;
	private static Statement stmt;
	private static ResultSet rs;
	private static boolean b;
	
	public ResultSet execute(String query) {
		String dburl = "jdbc:mysql://localhost/cms";//constant
		String dbclass = "com.mysql.jdbc.Driver";//constant
		try {
			if(rs != null) rs.close();
			if(stmt != null) stmt.close();
			if(con != null) con.close();
			rs = null; 
			Class.forName(dbclass);
			con = DriverManager.getConnection(dburl, "root", "wannalive");
			stmt = con.createStatement();
			b = stmt.execute(query);
			if(b) rs = stmt.getResultSet();
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return rs;
	}
	
	
}