import java.util.ArrayList;

public class Employee extends StaffUser {


	public Employee(String username, String password, String legalName, int age) {
		super(username, password, legalName, age);
	}

	public String toString() {
		return "Employee[" + username + "]";
	}

	
	
}
