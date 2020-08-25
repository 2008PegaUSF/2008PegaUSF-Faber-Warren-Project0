import java.io.Serializable;

//ID username balance status
public class BankAccount implements Serializable {
	protected String username;
	protected String ID;
	protected double balance;
	protected String status;

	
	//Simple constructor to use when a new account is applied for
	public BankAccount(String username, String ID) {
		this.username = username;
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
	
	public void open() {
		status = "active";
	}
	
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

	public double setBalance(double d){
		if (d>=0){
		balance=d;
		return balance;}
		else return -1;	
	}
	public double doDeposit(double d){
		if (d>=0){
			balance+=d;
			return balance;
		}
		else {
			return -1;
		}
	}
	
	public double doWithdrawal(double d){
		if (balance-d >=0 && d >= 0){
			balance-=d;
			return balance;
		}
		else {
			return -1;
		}
	} 
	
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
	
	public boolean equals(BankAccount other) {
		return this.username.equals(other.getUsername()) && this.ID.equals(other.getID()) && this.balance == other.getBalance() && this.status.equals(other.getStatus());
	}
}


