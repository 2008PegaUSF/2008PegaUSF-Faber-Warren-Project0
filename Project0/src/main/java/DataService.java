import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class DataService {
	//Declaring the arraylists which hold our users and account information
	private ArrayList<User> users;
	private ArrayList<BankAccount> accounts;
	private ArrayList<BankAccount> applications;
	int numBankAccounts;
	int numJointAccounts;
	//the constructor for our dataservice class
	public DataService() {
		users = new ArrayList<User>();
		accounts = new ArrayList<BankAccount>();
		applications = new ArrayList<BankAccount>();
	}
	//Getter method for bank accounts
	public ArrayList<BankAccount> getAccounts() {
		return accounts;
	}
	//Getter method for applications
	public ArrayList<BankAccount> getApplications() {
		return applications;
	}
	//Getter method for users
 	public ArrayList<User> getUsers() {
		return users;
	}
	
 	public int getNumBankAccounts() {
 		return numBankAccounts;
 	}
 	
 	public int getNumJointAccounts() {
 		return numJointAccounts;
 	}
	
	//Read how many BankAccounts and how many JointAccounts have been made from a file.
	public void loadNumAccounts(String filepath) {
		FileInputStream fin;
		ObjectInputStream oin;
		
		try {
			fin = new FileInputStream(filepath);
			if(fin.available() == 0) {
				numBankAccounts = 0;
				numJointAccounts = 0;
				fin.close();
				return;
			}
			else {
				oin = new ObjectInputStream(fin);
				
				if(fin.available() > 0) {
					numBankAccounts = oin.readInt();
					numJointAccounts = oin.readInt();
					
				}
			}
			
			oin.close();
			fin.close();
			
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//Saves an account to a file
	public void saveNumAccounts(String filepath) {
		FileOutputStream fout;
		ObjectOutputStream oout;
		
		try {
			fout = new FileOutputStream(filepath);
			oout = new ObjectOutputStream(fout);
			
			oout.writeInt(numBankAccounts);
			oout.writeInt(numJointAccounts);
			
			oout.close();
			fout.close();
			
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Loads all bank accounts from a file.
	public void loadBankAccounts(String filepath) {
		
		FileInputStream fin_bank;
		ObjectInputStream oin_bank;
		try {
			fin_bank = new FileInputStream(filepath);
			if(fin_bank.available() == 0) {
				fin_bank.close();
				return;
			}
			oin_bank = new ObjectInputStream(fin_bank);
			while(fin_bank.available() > 0) {
				BankAccount nextAccount = (BankAccount)oin_bank.readObject();
				if(nextAccount instanceof BankAccount) {
					accounts.add(nextAccount);
				}
				else if(nextAccount instanceof JointAccount) {
					accounts.add(nextAccount);
				}
				else {
					System.out.println("Invalid object loaded from " + filepath);
				}
			}
			
			oin_bank.close();
			fin_bank.close();
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	//Loads all users from a file.
	//This method looks different from the ones that use instanceof because I was trying to understand a loading-related bug.
	/**Be sure to run SetupStaff.java to update the Employee/Admin classes if this function throws an error.*/
	public void loadUsers(String filepath) {
		
		FileInputStream fin;
		ObjectInputStream oin;
		try {
			fin = new FileInputStream(filepath);
			if(fin.available() == 0) {
				fin.close();
				return;
			}
			oin = new ObjectInputStream(fin);
			
			while(fin.available() > 0) {
				Object nextUser = oin.readObject();
				String userClass = nextUser.getClass().getSimpleName();
				if(userClass.equals("Customer")) {
					users.add((Customer)nextUser);
				}
				else if(userClass.equals("Employee")) {
					users.add((Employee)nextUser);
				}
				else if(userClass.equals("Admin")) {
					users.add((Admin)nextUser);
				}
				else {
					System.out.println("Invalid object loaded from " + filepath);
				} 
			}
			
			oin.close();
			fin.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Loads all account applicatons from a file.
	public void loadApplications(String filepath) {
		
		FileInputStream fin_bank;
		ObjectInputStream oin_bank;
		try {
			fin_bank = new FileInputStream(filepath);
			if(fin_bank.available() == 0) {
				fin_bank.close();
				return;
			}
			oin_bank = new ObjectInputStream(fin_bank);
			while(fin_bank.available() > 0) {
				Object nextAccount = oin_bank.readObject();
				if(nextAccount instanceof BankAccount) {
					applications.add((BankAccount)nextAccount);
				}
				else if(nextAccount instanceof JointAccount) {
					applications.add((JointAccount)nextAccount);
				}
				else {
					System.out.println("Invalid object loaded from " + filepath);
				}
			}
			
			oin_bank.close();
			fin_bank.close();
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	//Saves all accounts to a file.
	public void saveBankAccounts(String filepath) {
		FileOutputStream fout;
		ObjectOutputStream oout;
		try {
			fout = new FileOutputStream(filepath);
			oout = new ObjectOutputStream(fout);
			
			for(BankAccount a : accounts) {
				oout.writeObject(a);
			}
			
			oout.close();
			fout.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//Saves all accounts to a file.
	public void saveApplications(String filepath) {
		FileOutputStream fout;
		ObjectOutputStream oout;
		try {
			fout = new FileOutputStream(filepath);
			oout = new ObjectOutputStream(fout);
			
			for(BankAccount a : applications) {
				oout.writeObject(a);
			}
			
			oout.close();
			fout.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//Saves all users to a file.
	public void saveUsers(String filepath) {
		FileOutputStream fout;
		ObjectOutputStream oout;
		try {
			fout = new FileOutputStream(filepath);
			oout = new ObjectOutputStream(fout);
			
			for(User u : users) {
				oout.writeObject(u);
			}
			
			oout.close();
			fout.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		
	}
	
	//Returns all accounts belonging to a customer. Will include inactive accounts if includeInactive is true.
	public ArrayList<BankAccount> getAccountsOfUser(String username, boolean includeInactive) throws InvalidClassException {
		ArrayList<BankAccount> customerAccounts = new ArrayList<BankAccount>();
		for(int i = 0; i < accounts.size(); i++) {
			Object currentAccount = accounts.get(i);
			if(currentAccount instanceof BankAccount) {
				if(((BankAccount) currentAccount).getUsername().equals(username) && !(((BankAccount) currentAccount).getStatus() != "active" && !includeInactive)) {
					customerAccounts.add((BankAccount) currentAccount);
				}
			}
			else if(currentAccount instanceof JointAccount) {
				if(((JointAccount) currentAccount).getUsername().equals(username) && !(((JointAccount) currentAccount).getStatus() != "active" && !includeInactive)) {
					customerAccounts.add((JointAccount) currentAccount);
				}
			}
			else {
				throw new InvalidClassException("Invalid object");
			}
		}
		return customerAccounts;
	}
	
	//One username: create new BankAccount
	public void createAccountApplication(String username) {
		BankAccount newAccount;
		String newID = "C" + ++numBankAccounts;
		newAccount = new BankAccount(newID, username, 0f, "pending");
		applications.add(newAccount);
	}
	
	//Two usernames: create new JointAccount
	public void createAccountApplication(String username, String username2) {
		BankAccount newAccount;
		String newID = "J" + ++numJointAccounts;
		newAccount = new JointAccount(newID, username, username2, 0f, "pending");
		applications.add(newAccount);
	}
	
	public boolean createCustomer(String username, String password, String phoneNumber, int age) {
		for(User u : users) {
			if(username.equals(u.getUsername())) {
				return false;
			}//end if
		}//end for
		//If this line is reached, the username is not taken
		Customer newCustomer = new Customer(username, password, phoneNumber, age);
		users.add(newCustomer);
		return true;
	}
	
	public boolean isUsernameAvailable(String username) {
		for(User u : users) {
			if(username.equals(u.getUsername())) {
				return false;
			}
		}
		return true;
	}
	
	//Returns the user with that given username. Returns null if no user with that name was found.
	public User getUserByUsername(String username) {
		for(User u : getUsers()) {
			if(u.getUsername().equals(username))
				return u;
		}
		return null;
	}
	
	//Returns the account with that given ID. Returns null if no account with that name was found.
	public BankAccount getAccountByID(String id) {
		for(BankAccount account : accounts) {
			if(account.getID().equals(id))
				return account;
		}
		return null;
	}
	
	public BankAccount getApplicationByID(String id) {
		for(BankAccount application : applications) {
			if(application.getID().equals(id))
				return application;
		}
		return null;
	}
	
	public boolean approveApplication(String id) {
		BankAccount toApprove = getApplicationByID(id);
		if(toApprove == null) {
			return false;
		}
		else {//Application found, approve
			applications.remove(toApprove);
			accounts.add(toApprove);
			toApprove.open();
			return true;
		}
	}
	
	public boolean denyApplication(String id) {
		BankAccount toDeny = getApplicationByID(id);
		if(toDeny == null) {
			return false;
		}
		else {//Application found, approve
			applications.remove(toDeny);
			return true;
		}
	}

	
}
