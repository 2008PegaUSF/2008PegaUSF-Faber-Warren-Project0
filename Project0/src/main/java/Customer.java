import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

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

	public Customer(String username, String password, String realName, int age) {
		super(username, password, realName, age);
	}

	public String toString() {
		return "Customer[" + username + "]";
	}
	
	public String getPersonalInfo() {
		return "";
	}
	
	//Prompts the user to apply for an account. Returns true if a new application was made.
	public boolean applyForAccount(DataService ds, Scanner in) {
		while(true) {
			System.out.println("Enter 1 to apply for a new individual account, or 2 to apply for a joint account. Enter any other number to cancel.");
			int userInput = validateInputInteger(in);
			switch(userInput) {
			case 1:
				ds.createAccountApplication(username);
				System.out.println("Account application complete. Please wait for your application to be approved.");
				return true;
			case 2:
				System.out.println("Enter the username of the user you are sharing your joint account with: ");
				String otherUser = in.nextLine();
				if(!ds.isUsernameAvailable(otherUser)) {
					ds.createAccountApplication(username,otherUser);
					System.out.println("Account application complete. Please wait for your application to be approved.");
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
	
	public boolean depositToAccount(DataService ds, Scanner in) {
		try {
			System.out.println("Select an account to deposit to:");
			BankAccount foundAccount = selectAccount(ds, in);
			if(foundAccount == null) {//No account with that ID found
				System.out.println("No account with that ID was found.");
				return false;
			}
			else {//Account with that ID found
				System.out.println("Account balance: " + foundAccount.getBalance());
				System.out.println("Enter an amount to deposit: ");
				double depositAmount = validateInputDouble(in);
				if(foundAccount.doDeposit(depositAmount) == -1) {
					System.out.println("Invalid deposit amount.");
					return false;
				}
				else {
					System.out.println("Deposit completed. New account balance: " + foundAccount.getBalance());
					return true;
				}
			}
			
		} catch (InvalidClassException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean withdrawFromAccount(DataService ds, Scanner in) {
		try {
			System.out.println("Select an account to withdraw from:");
			BankAccount foundAccount = selectAccount(ds, in);
			if(foundAccount == null) {//No account with that ID found
				System.out.println("No account with that ID was found.");
				return false;
			}
			else {//Account with that ID was found
				System.out.println("Account balance: " + foundAccount.getBalance());
				System.out.println("Enter an amount to withdraw:");
				double withdrawalAmount = validateInputDouble(in);
				if(foundAccount.doWithdrawal(withdrawalAmount) == -1) {
					System.out.println("Invalid withdrawal amount.");
					return false;
				}
				else {
					System.out.println("Deposit completed. New account balance: " + foundAccount.getBalance());
					return true;
				}
			}
		} catch (InvalidClassException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean transferBetweenAccounts(DataService ds, Scanner in) {
		
		try {
			System.out.println("Enter an account to send money from: ");
			BankAccount sender = selectAccount(ds,in);
			if(sender == null) {
				System.out.println("That account does not exist.");
				return false;
			}
			System.out.println("Enter an account to send money to:");
			BankAccount recipient = ds.getAccountByID(in.nextLine());
			if(recipient == null) {
				System.out.println("That account does not exist.");
				return false;
			}
			System.out.println("Sender account balance: " + sender.getBalance());
			System.out.println("Enter an amount to transfer:");
			double transferAmount = validateInputDouble(in);
			if(sender.doTransfer(transferAmount, recipient)) {
				System.out.println("Transfer complete. Sender's new account balance: " +  sender.getBalance());
				return true;
			}
			else {
				System.out.println("Invalid amount.");
				return false;
			}
			
		} catch (InvalidClassException e) {
			e.printStackTrace();
		}
		return false;
	}
	
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
