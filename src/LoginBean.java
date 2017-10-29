import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 * @author Rashmi
 *
 */
@ManagedBean(name = "LoginBean")
@SessionScoped
public class LoginBean {

	private String uname;
	private String password;
	private String name;
	private String session;
	private String present;

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getPresent() {
		return present;
	}

	public void setPresent(String present) {
		this.present = present;
	}


	public String login() {
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
			String sql = "SELECT Name, Password from User where UserName = ? and Password = ?";
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, this.uname);
			st.setString(2, this.password);

			// Execute the statement
			ResultSet rs = st.executeQuery();

			// Iterate through results
			if (rs.next()) {
				session =  rs.getString("Name")+ " is successfully logged in";
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("session",session);

				System.out.println("Name is: " + rs.getString("Name"));
				this.setName(rs.getString("Name"));
				return "usermainpage";
			} else {
				this.uname="";
				/*FacesContext obj = FacesContext.getCurrentInstance();
				FacesMessage message= new FacesMessage("Invalid credentials. Enter again");
				obj.addMessage("errormsg:password", message);*/
				return "login";
			}
		} catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
		} finally {
			try {
				System.out.println("before");
				con.close();
				System.out.println("After");
			} catch (SQLException e) {
			}
		}

		return "login";
	}

	
	public String logout() {
		
		//String sname =(String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("session");
		//System.out.println("before" + abc);
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		//System.out.println("after"+ (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("session")); 
		return "login";

	}

}
