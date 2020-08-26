
public class JointAccount extends BankAccount {
	private String username2;
	
	public JointAccount(String username, String username2, String ID) {
		super(username, ID);
		this.username2 = username2;
	}

	public JointAccount(String ID, String user1, String user2, double balance, String status) {
		super(ID, user1, balance, status);
		this.username2 = user2;
	}
		
	public String toString() {
		return  ID + "[User 1: + " + username + " User 2: " + username2 +  " Balance: " + balance  + " Status: " + status +  "]";
	}
	
}
