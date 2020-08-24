import java.io.InvalidClassException;
import java.util.*;

//StaffUsers can run any method an employee can. This is a superclass for Employee and Admin.

public class StaffUser extends User {

	public StaffUser(String username, String password, String legalName, int age) {
		super(username, password, legalName, age);
	}

	public String viewCustomerAccounts(DataService ds){
		String out = "";
		ArrayList<User> users = ds.getUsers();
		for(int i=0; i<users.size(); i++) {
			out += (users.get(i));
			
		}
		return out;

	}
	
	public String viewAllCustomers(DataService ds) {
		int column = 0;
		String out = "Customers:\n";
		ArrayList<Customer> customers = new ArrayList<Customer>();
		for(User u : ds.getUsers()) {
			if(u instanceof Customer) {
				out += u.getUsername() + " ";
				if(++column % 5 == 0) {
					out += "\n";
				}
			}
		}
		return out;
	}
	
	public void viewCustomerInfo(DataService ds, Scanner in) {
		try {
			System.out.println("Enter a customer to lookup:");
			User foundUser = ds.getUserByUsername(in.nextLine());
			if(foundUser == null) {//Customer not found
				System.out.println("User not found.");
				return;
			}
			if(foundUser instanceof Customer) {//Customer found
				ArrayList<BankAccount> userAccounts = ds.getAccountsOfUser(foundUser.getUsername(), true);
				System.out.println(foundUser.getPersonalInfo());
				System.out.println("Customer accounts:");
				int column = 0;
				for(BankAccount acc : userAccounts) {
					System.out.println(acc + " ");
					if(++column % 5 == 0) {
						System.out.println("\n");
					}
				}
			}
			else {//Not a customer
				System.out.println("That user is not a customer.");
			}
		}
		catch(InvalidClassException e) {
			e.printStackTrace();
		}
		
	}

	public void viewAccountInfo(DataService ds, Scanner scan){
		System.out.println("Please enter an account ID");
		String s=scan.nextLine();
		for (BankAccount b: ds.getAccounts()){
			if (s==b.getID()){
				System.out.println("Account balance: " +b.getBalance() + "\n" + "Account balance: " +b.getBalance() + "\n");
			}
		}//end for
		System.out.println("Invalid username or there is no account associated with that username");
	}

	public String viewAllApplications(DataService ds) {
		int column = 0;
		String out = "Applications:\n";
		for(BankAccount a : ds.getApplications()) {
				out += a.getID() + " ";
				if(++column % 5 == 0) {
					out += "\n";
				}
			
		}
		return out;
	}
	
	public boolean ApproveOrDenyApplication(DataService ds, Scanner in) {
		System.out.println("Enter \"approve <ID>\" or \"deny <ID>\":");
		String userInput = in.nextLine();
		String[] params = userInput.split(" ");
		
		if(params.length != 2) {
			System.out.println("Invalid command.");
			return false;
		}
		
		if(params[0].equalsIgnoreCase("approve")) {
			if(ds.approveApplication(params[1])) {
				System.out.println("Account " + params[1] + " approved.");
				return true;
			}
			else {
				System.out.println("Application not found.");
				return false;
			}
		}
		else if(params[0].equalsIgnoreCase("deny")) {
			if(ds.denyApplication(params[1])) {
				System.out.println("Application " + params[1] + " denied.");
				return true;
			}
			else {
				System.out.println("Application not found.");
				return false;
			}
		}
		else {
			System.out.println("Invalid command.");
			return false;
		}
	}

}




