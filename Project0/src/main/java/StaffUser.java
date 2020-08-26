import java.io.InvalidClassException;
import java.util.*;

import org.apache.logging.log4j.Logger;

//StaffUsers can run any method an employee can. This is a superclass for Employee and Admin.

public class StaffUser extends User {

	public StaffUser(String username, String password, String legalName, int age) {
		super(username, password, legalName, age);
	}

	//Returns a string providing the user with information about all accounts.
	public String viewCustomerAccounts(DataService ds){
		String out = "";
		ArrayList<User> users = ds.getUsers();
		for(int i=0; i<users.size(); i++) {
			out += (users.get(i));
			
		}
		return out;

	}
	
	//Returns a string providing the user with information about all customers, neatly ordered in rows of 5.
	public String viewAllCustomers(DataService ds) {
		int column = 0;
		String out = "Customers:\n";
		for(User u : ds.getUsers()) {
			if(u instanceof Customer) {
				out += u.getUsername() + " ";
				if(++column % 5 == 0) {//On every fifth customer, start a new line
					out += "\n";
				}
			}
		}
		return out;
	}
	
	//Prints information on a specific customer
	public void viewCustomerInfo(DataService ds, Scanner in, Logger log) {
		try {
			System.out.println("Enter a customer to lookup:");
			User foundUser = ds.getUserByUsername(in.nextLine());
			if(foundUser == null) {//Customer not found
				System.out.println("User not found.");
				log.info(this.getClass().getSimpleName() + username + " failed to look up a customer - username not found");
				return;
			}
			if(foundUser instanceof Customer) {//Customer found
				ArrayList<BankAccount> userAccounts = ds.getAccountsOfUser(foundUser.getUsername(), true);
				System.out.println(foundUser.getPersonalInfo());
				System.out.println("Customer accounts:");
				int column = 0;
				for(BankAccount acc : userAccounts) {
					System.out.println(acc + " ");
					if(++column % 5 == 0) {//On every fifth customer, start a new line
						System.out.println("\n");
						log.info(this.getClass().getSimpleName() + username + " looked up info on customer " + foundUser.getUsername());
					}
				}
			}
			else {//Not a customer
				System.out.println("That user is not a customer.");
				log.info(this.getClass().getSimpleName() + username + " failed to look up a customer - tried to look up " 
						+ foundUser.getClass().getSimpleName() + " " + foundUser.getUsername());
			}
		}
		catch(InvalidClassException e) {
			e.printStackTrace();
		}
		
	}


	//returns a string providing the user with info on all applications
	public String viewAllApplications(DataService ds) {
		int column = 0;
		String out = "Applications:\n";
		for(BankAccount a : ds.getApplications()) {
				out += a + " ";
				if(++column % 5 == 0) {//On every fifth application, start a new line
					out += "\n";
				}
			
		}
		return out;
	}
	
	//Allows an employee or admin to approve or deny a pending application
	public boolean ApproveOrDenyApplication(DataService ds, Scanner in, Logger log) {
		System.out.println("Enter \"approve <ID>\" or \"deny <ID>\":");
		String userInput = in.nextLine();
		String[] params = userInput.split(" ");
		
		if(params.length != 2) {//The wrong amount of arguments was given to the console command
			System.out.println("Invalid number of arguments.");
			log.info(this.getClass().getSimpleName() + username + " failed to approve or deny an application - incorrect number of arguments");
			return false;
		}
		
		if(params[0].equalsIgnoreCase("approve")) {//User chose to approve an account
			if(ds.approveApplication(params[1])) {
				System.out.println("Account " + params[1] + " approved.");
				log.info(this.getClass().getSimpleName() + username + " approved application " + params[1]);
				return true;
			}
			else {
				System.out.println("Application not found.");
				log.info(this.getClass().getSimpleName() + username + " failed to approve an application - application not found");
				return false;
			}
		}
		else if(params[0].equalsIgnoreCase("deny")) {//User chose to deny an account
			if(ds.denyApplication(params[1])) {
				System.out.println("Application " + params[1] + " denied.");
				log.info(this.getClass().getSimpleName() + username + " denied applicaiton " + params[1]);
				return true;
			}
			else {
				System.out.println("Application not found.");
				log.info(this.getClass().getSimpleName() + username + " failed to deny an application - application not found");
				return false;
			}
		}
		else {
			System.out.println("Invalid command.");
			log.info(this.getClass().getSimpleName() + username + " failed to approve or deny an application - incorrect command");
			return false;
		}
	}

}




