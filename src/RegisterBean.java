import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
/**
 * @author Rashmi
 *
 */
@ManagedBean(name="RegisterBean")
@SessionScoped
public class RegisterBean {
	private String name;
	private String email;
	private String uname;
	private String password;
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String username) {
		this.uname = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String register() {
		Connection con = null;
		try {
			// Setup the DataSource object
			com.mysql.jdbc.jdbc2.optional.MysqlDataSource ds = new com.mysql.jdbc.jdbc2.optional.MysqlDataSource();
			ds.setServerName(System.getenv("ICSI518_SERVER"));
			ds.setPortNumber(Integer.parseInt(System.getenv("ICSI518_PORT")));
			ds.setDatabaseName(System.getenv("ICSI518_DB"));
			ds.setUser(System.getenv("ICSI518_USER"));
			ds.setPassword(System.getenv("ICSI518_PASSWORD"));

			// Get a connection object
			con = ds.getConnection();

			// Get a prepared SQL statement
			String sql = "SELECT Name from User where UserName = ?";
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, this.uname);

			// Execute the statement
			ResultSet rs = st.executeQuery();

			// Iterate through results
			if (rs.next()) {
				this.name = rs.getString("Name");
				System.out.println(rs.getString("Name") + ", user already exists.");
				this.name ="";
				this.email="";
				this.uname="";
				this.password="";
				return "register";
			}
			else
			{
				// Get a prepared SQL statement
				String sql1 ="INSERT INTO User(Name, Email, Password, UserName) VALUES(?,?,?,?)";
				PreparedStatement st1 = con.prepareStatement(sql1);
				st1.setString(1,this.name);
				st1.setString(2,this.email);
				st1.setString(3,this.password);
				st1.setString(4,this.uname);
				st1.executeUpdate();
				st1.close();
			}
		} 
		catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
		} 
		finally {
			try {
				con.close();
			} catch (SQLException e) 
			{
			}
		}
		return "login";
	}
}
