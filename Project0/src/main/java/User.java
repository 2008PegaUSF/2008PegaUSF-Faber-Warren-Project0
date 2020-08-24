import java.io.Serializable;

//Superclass for all Users, including Customer, Employee, and Admin.
public abstract class User implements Serializable {
	
	protected String username;
	protected String password;
	protected String legalName;
	protected int age;
	
	public User(String username, String password, String realName, int age) {
		this.username = username;
		this.password = password;
		this.legalName = realName;
		this.age = age;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getLegalName() {
		return legalName;
	}
	
	public int getAge() {
		return age;
	}
	
	public String toString() {
		return "User[" + username + "]";
	}
	
	public String getPersonalInfo() {
		return "Username: " + username
				+ "\nLegal Name: " + legalName
				 +"\nAge: " + age;
	}
}
