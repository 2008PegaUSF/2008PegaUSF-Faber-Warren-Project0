import java.io.Serializable;

//ID username balance status
public class BankAccount implements Serializable {
	protected String username;
	protected String ID;
	protected double balance;
	protected String status;

	
	//Simple constructor to use when a new account is applied for
	public BankAccount(String ID, String user) {
		this.username = user;
		this.ID = ID;
		balance = 0;
		status = "pending";
	}
	
	public BankAccount(String ID, String user, double balance, String status) {
		this.username = user;
		this.ID = ID;
		this.balance = balance;
		this.status = status;
	}
	//Sets the account's status to active
	public void open() {
		status = "active";
	}
	//Sets the account's status to cancelled, marking it as closed
	public void close() {
		status = "cancelled";
	}
	
	public String toString() {
		return  ID + "[User: " + username + " Balance: " + balance  + " Status: " + status +  "]";
	}
	
	public String getID() {
		return ID;
	}
	
	public String getUsername() {
		return username;
	}

	public double getBalance() {
		return balance;
	}
	
	public String getStatus() {
		return status;
	}

	//Negative values are denied when setting the balance
	public double setBalance(double d){
		if (d>=0){
		balance=d;
		return balance;}
		else return -1;	
	}
	//Negative values are denied for deposits
	public double doDeposit(double d){
		if (d>=0){
			balance+=d;
			return balance;
		}
		else {
			return -1;
		}
	}
	//Withdrawals are denied if the requested amount is negative or would result in an overdraw
	public double doWithdrawal(double d){
		if (balance-d >=0 && d >= 0){
			balance-=d;
			return balance;
		}
		else {
			return -1;
		}
	} 
	//Transfers are denied if the mount is negative or would cause an overdraw, or if the transfer is being made to the same account,
	//	because we liveyth in a civilized society that doesn't allow something unholy like that.
	public boolean doTransfer(double amount, BankAccount other) {
		if (balance>=amount && amount > 0 && this != other){
			balance-=amount;
			other.balance+=amount;
			System.out.println("Funds Transfered");
			return true;
		} else {
			System.out.println("Insufficient Funds");
			return false;
		}
	}
	//Primarily used for testing purposes as assertEquals does not work for these accounts.
	public boolean equals(BankAccount other) {
		return this.username.equals(other.getUsername()) && this.ID.equals(other.getID()) && this.balance == other.getBalance() && this.status.equals(other.getStatus());
	}
}


