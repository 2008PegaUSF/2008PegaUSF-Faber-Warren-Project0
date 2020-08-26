import java.io.InvalidClassException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.logging.log4j.Logger;

public class Customer extends User implements Serializable {
	
	//Takes input from a scanner and only allows an int.
	public static Integer validateInputInteger(Scanner in) {
		Integer output = null;
		while(output == null) {
			try {
				output = Integer.parseInt(in.nextLine());
			}
			catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter a number.");
				
			}
		}
		return output;
	}
	//our validation method for accepting double values
	public static Double validateInputDouble(Scanner in) {
		Double output = null;
		while(output == null) {
			try {
				output = Double.parseDouble(in.nextLine());
			}
			catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter a number with up to 2 points of decmial precision.");
				
			}
		}
		return output;
	}
	//the customer constructor
	public Customer(String username, String password, String realName, int age) {
		super(username, password, realName, age);
	}
	//the toString method
	public String toString() {
		return "Customer[" + username + "]";
	}
	//get personal info
	public String getPersonalInfo() {
		return "";
	}
	
	//Prompts the user to apply for an account. Returns true if a new application was made.
	public boolean applyForAccount(DataService ds, Scanner in, Logger log) {
		while(true) {
			System.out.println("Enter 1 to apply for a new individual account, or 2 to apply for a joint account. Enter any other number to cancel.");
			int userInput = validateInputInteger(in);
			switch(userInput) {
			case 1://Create application for an individual account
				ds.createAccountApplication(username);
				System.out.println("Account application complete. Please wait for your application to be approved.");
				log.info("Customer " + username + " has applied for an individual account");
				return true;
			case 2://Create application for a joint account
				System.out.println("Enter the username of the user you are sharing your joint account with: ");
				String otherUser = in.nextLine();
				if(!ds.isUsernameAvailable(otherUser)) {
					ds.createAccountApplication(username,otherUser);
					System.out.println("Account application complete. Please wait for your application to be approved.");
					log.info("Customer " + username + " has applied for a joint account");
					return true;
				}
				break;
			default:System.out.println("Application cancelled.");
				return false;
			}
		}
	}
	
	//Returns a string showing a customer's account data.
	public String viewAccounts(DataService ds) throws InvalidClassException {
		ArrayList<BankAccount> userAccounts = ds.getAccountsOfUser(username, false);
		int i = 0;
		String out = "Your accounts: ";
		//Display 5 user accounts per row
		for(BankAccount bac : userAccounts) {
			out += bac + " ";
			if(++i > 0 && i % 5 == 0) {
				out += "\n";
			}
		}
		out += "\n";
		return out;
	}
	//deposit method
	public boolean depositToAccount(DataService ds, Scanner in, Logger log) {
		try {
			System.out.println("Select an account to deposit to:");
			//accepts and input for the account
			BankAccount foundAccount = selectAccount(ds, in);
			if(foundAccount == null) {//No account with that ID found
				System.out.println("No account with that ID was found.");
				log.info("Customer " + username + " failed to deposit to an account - not found");
				return false;
			}
			else {//Account with that ID found
				System.out.println("Account balance: " + foundAccount.getBalance());
				System.out.println("Enter an amount to deposit: ");
				double depositAmount = validateInputDouble(in);
				if(foundAccount.doDeposit(depositAmount) == -1) {
					System.out.println("Invalid deposit amount.");
					log.info("Customer " + username + " failed to deposit to account " + foundAccount.getID() + " - invalid amount " + depositAmount);
					return false;
				}
				else {//the kick is good
					System.out.println("Deposit completed. New account balance: " + foundAccount.getBalance());
					log.info("Customer " + username + " deposited " + depositAmount + " into " + foundAccount.getID());
					return true;
				}
			}
			
		} catch (InvalidClassException e) {
			e.printStackTrace();
		}
		log.info("Customer " + username + " failed to deposit to an account - unexpected error");
		return false;
		
	}
	//Withdraw method
	public boolean withdrawFromAccount(DataService ds, Scanner in, Logger log) {
		try {
			System.out.println("Select an account to withdraw from:");
			//accepts an input for the account
			BankAccount foundAccount = selectAccount(ds, in);
			if(foundAccount == null) {//No account with that ID found
				System.out.println("No account with that ID was found.");
				log.info("Customer " + username + " failed to withdraw from an account - account not found");
				return false;
			}
			else {//Account with that ID was found
				System.out.println("Account balance: " + foundAccount.getBalance());
				System.out.println("Enter an amount to withdraw:");
				double withdrawalAmount = validateInputDouble(in);
				if(foundAccount.doWithdrawal(withdrawalAmount) == -1) {
					System.out.println("Invalid withdrawal amount.");
					log.info("Customer " + username + " failed to withdraw from account " + foundAccount.getID() + " - invalid amount " + withdrawalAmount);
					return false;
				}
				else {//the kick is good!
					System.out.println("Withdraw completed. New account balance: " + foundAccount.getBalance());
					log.info("Customer " + username + " withdrew " + withdrawalAmount + " from " + foundAccount.getID());
					return true;
				}
			}
		} catch (InvalidClassException e) {
			e.printStackTrace();
		}
		log.info("Customer " + username + " failed to withdraw from an account - unexpected error");
		return false;
	}
	//the transfer method
	public boolean transferBetweenAccounts(DataService ds, Scanner in, Logger log) {
		
		try {
			System.out.println("Enter an account to send money from: ");
			BankAccount sender = selectAccount(ds,in);//validation
			if(sender == null) {
				System.out.println("That account does not exist.");
				log.info("Customer " + username + " failed to make a transfer - sender account not found");
				return false;
			}
			//Select account to send money
			System.out.println("Enter an account to send money to:");//more validation
			BankAccount recipient = ds.getAccountByID(in.nextLine());
			if(recipient == null) {
				System.out.println("That account does not exist.");
				log.info("Customer " + username + " failed to make a transfer - recipient account not found");
				return false;
			}
			//Select account to receive transfer
			System.out.println("Sender account balance: " + sender.getBalance());
			System.out.println("Enter an amount to transfer:");//you made it! asking how much to transfer with validation attached
			double transferAmount = validateInputDouble(in);
			if(sender.doTransfer(transferAmount, recipient)) {
				System.out.println("Transfer complete. Sender's new account balance: " +  sender.getBalance());//the transfer is completed!
				log.info("Customer " + username + " transferred " + transferAmount + " from " + sender.getID() + " to " + recipient.getID());
				return true;
			}
			else {
				System.out.println("Invalid amount.");
				log.info("Customer " + username + " failed to make a transfer - invalid amount " + transferAmount);
				return false;
			}
			
		} catch (InvalidClassException e) {
			e.printStackTrace();
		}
		log.info("Customer " + username + " failed to make a transfer - unknown error");
		return false;
	}
	//the function to select an account
	private BankAccount selectAccount(DataService ds, Scanner in) throws InvalidClassException {
		ArrayList<BankAccount> userAccounts = ds.getAccountsOfUser(username, false);
		//Let user select an account to withdraw from
		String chosenAccount = in.nextLine();
		
		BankAccount foundAccount = null;
		for(BankAccount bac : userAccounts) {
			if(bac.getID().equals(chosenAccount)) {
				foundAccount = bac;
				break;
			}
		}
		return foundAccount;
	}
	
	
	
}
