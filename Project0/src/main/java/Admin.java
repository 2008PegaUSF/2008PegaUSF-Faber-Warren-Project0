import java.io.InvalidClassException;
import java.util.*;

import org.apache.logging.log4j.Logger;

public class Admin extends StaffUser {
	//the constructor for the admin class
	public Admin(String username, String password, String legalName, int age) {
		super(username, password, legalName, age);
	
	}

	//the toString method which prints the Admins username
	public String toString() {
		return "Admin[" + username + "]";
	}

	//allows an admin to deposit to a customer account.
	public boolean depositToAccount( DataService ds, Scanner in, Logger log) {
		//prompting for an account then accepting an input.
			System.out.println("Please enter an account to deposit to:");
			String s=in.nextLine();			
			//creating a bank account object for our input
			BankAccount foundAccount = ds.getAccountByID(s);
			//finding the bank account among our data
			if(foundAccount == null) {//No account with that ID found
				System.out.println("No account with that ID was found.");
				log.info("Admin " + username + " failed to deposit to an account - ID not found");
				return false;				
			}
			//if the account is found entering the amount
			else {//Account with that ID found
				System.out.println("Account balance: " + foundAccount.getBalance());
				System.out.println("Enter an amount to deposit: ");
				double depositAmount = validateInputDouble(in);
				//if the number is wrong, don't deposit
				if(foundAccount.doDeposit(depositAmount) == -1) {
					System.out.println("Invalid deposit amount.");
					log.info("Admin " + username + " failed to deposit to an account - invalid deposit amount " + depositAmount);
					return true;
				}
				else {//and the kick is good!
					System.out.println("Deposit completed. New account balance: " + foundAccount.getBalance());
					log.info("Admin " + username + " deposited " + depositAmount + " into account " + foundAccount.getID());
					return true;
				}
			}
		}

	//Allows the admin to withdraw from a customer account.
	public boolean withdrawFromAccount(DataService ds, Scanner in, Logger log) {
		//accepting input and searching for the appropraite account in the data
			System.out.println("Please enter an account id to withdraw from:");
			String s=in.nextLine();
			BankAccount foundAccount = ds.getAccountByID(s);
			//the account doesn't exist
			if(ds.getAccountByID(s) == null) {//No account with that ID found
				System.out.println("No account with that ID was found.");
				log.info("Admin " + username + " failed to withdraw from an account - ID not found");
				return false;
			}
			else {//Account with that ID was found
				System.out.println("Account balance: " + foundAccount.getBalance());
				System.out.println("Enter an amount to withdraw:");
				double withdrawalAmount = validateInputDouble(in);
				//validation that the withdrawal is legal
				if(foundAccount.doWithdrawal(withdrawalAmount) == -1) {
					System.out.println("Invalid withdrawal amount.");
					log.info("Admin " + username + " failed to withdraw from an account - invalid withdrawal amount " + withdrawalAmount);
					return false;
				}
				else {//its super effective
					System.out.println("Withdraw completed. New account balance: " + foundAccount.getBalance());
					log.info("Admin " + username + " withdrew " + withdrawalAmount + " from account " + foundAccount.getID());
					return true;
				}
			}
	}

	//Allows the admin to make a transfer between two customer accounts.
	public boolean transferBetweenAccounts(DataService ds, Scanner in, Logger log) {
			//accepting input finding the account
			System.out.println("Enter an account to send money from: ");
			String s=in.nextLine();
			BankAccount sender = ds.getAccountByID(s);
			if(sender == null) {
				System.out.println("That account does not exist.");
				log.info("Admin " + username + " failed to make a transfer - sender account not found");
				return false;
			}
			System.out.println("Enter an account to send money to:");
			BankAccount recipient = ds.getAccountByID(in.nextLine());
			if(recipient == null) {
				System.out.println("That account does not exist.");
				log.info("Admin " + username + " failed to make a transfer - recipient account not found");
				return false;
			}
			//alerting balances then sending the money
			System.out.println("Sender account balance: " + sender.getBalance());
			System.out.println("Enter an amount to transfer:");
			double transferAmount = validateInputDouble(in);
			if(sender.doTransfer(transferAmount, recipient)) {//Your princess is in this castle
				System.out.println("Transfer complete. Sender's new account balance: " +  sender.getBalance());
				log.info("Admin " + username + " made a transfer between account " + sender.getID() + " and " + recipient.getID());
				return true;
			}
			else {//Your princess is not in this castle
				System.out.println("Invalid amount.");
				log.info("Admin " + username + " failed to make a transfer between " + sender.getID() + " to " + recipient.getID() + " - invalid amount " + transferAmount);
				return false;
			}
			
	}

//our validation method to make sure the user can only enter a double
	public static Double validateInputDouble(Scanner in) {
		Double output = null;
		while(output == null) {
			try {
				output = Double.parseDouble(in.nextLine());
			}
			catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter a number with up to 2 points of decimal precision.");
				output = null;
			}
		}
		return output;
	}

//our view account method, which asks for a current user than spits out their accounts
	public void viewAccounts(DataService ds) throws InvalidClassException {
		ArrayList<BankAccount> userAccounts = ds.getAccountsOfUser(username, false);
		int i = 0;
		System.out.println("Your accounts: ");
		//Display 5 user accounts per row
		for(BankAccount bac : userAccounts) {
			System.out.print(bac.getID() + " ");
			if(++i > 0 && i % 5 == 0) {
				System.out.println();
			}
		}
	}
//open and close account method
	public boolean openOrCloseAccount(DataService ds, Scanner in, Logger log) {
	System.out.println("Enter \"open <ID>\" or \"close <ID>\": ");
	//asking which account to close or open with validaton
	String userInput = in.nextLine();
	String[] params = userInput.split(" ");
	
	if(params.length != 2) {
		System.out.println("Incorrect number of arguments.");
		log.info("Admin " + username + " failed to open or close an account - incorrect number of arguments");
		return false;
	}
	//Check if the account exists or not before continuing
	BankAccount toManage = ds.getAccountByID(params[1]);
	if(toManage == null) {
		System.out.println("Account not found.");
		log.info("Admin " + username + " failed to open or close an account - account " + params[1] + " not found");
		return false;
	}
	// open the account
	if(params[0].equalsIgnoreCase("open")) {
		toManage.open();
		System.out.println("Account " + toManage.getID() + "opened.");
		log.info("Admin " + username + "opened account " + toManage.getID());
		return true;
	}
	// close the account
	else if(params[0].equalsIgnoreCase("close")) {
		toManage.close();
		System.out.println("Account " + toManage.getID() + "closed.");
		log.info("Admin " + username + "closed account " + toManage.getID());
		return true;
	}
	return false;
}

}






