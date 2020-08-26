import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TestCases {
	
	@BeforeAll
	public static void setupTests() {
		try {
		//Single bank accounts to be loaded in tests:
		BankAccount bacc1 = new BankAccount("C1", "customer1", 100f, "active");
		BankAccount bacc2 = new BankAccount("C2", "customer2", 100f, "active");
		BankAccount bacc3 = new BankAccount("C3", "customer3", 100f, "active");
		BankAccount bacc4 = new BankAccount("C4", "customer4", 100f, "active");
		BankAccount bacc5 = new BankAccount("C5","customer1",100f, "cancelled");
		
		//Joint bank accounts to be loaded in tests:
		JointAccount jacc1 = new JointAccount("J1","customer1","customer2",100f,"active");
		JointAccount jacc2 = new JointAccount("J2","customer1","customer3",100f,"active");
		JointAccount jacc3 = new JointAccount("J3","customer2","customer3",100f,"active");
		JointAccount jacc4 = new JointAccount("J4","customer3","customer4",100f,"active");
		
		//Users to be loaded in tests:
		Customer cust1 = new Customer("customer1", "password1", "Guy One", 10);
		Customer cust2 = new Customer("customer2", "password2", "Guy Two", 20);
		Customer cust3 = new Customer("customer3", "password3", "Guy Three", 30);
		Customer cust4 = new Customer("customer4", "password4", "Guy Four", 40);
		Employee emp1 = new Employee("employee1", "Worker Man", "1234567895", 50);
		Admin adm1 = new Admin("admin1", "passwordA", "Admin Boss", 60);
		//Bank account applications to be loaded in tests:
		BankAccount appl1 = new BankAccount("C6", "customer1", 100f, "pending");
		BankAccount appl2 = new BankAccount("C7", "customer2", 100f, "pending");
		BankAccount appl3 = new JointAccount("J5", "customer3", "customer4", 100f, "pending");
		BankAccount appl4 = new JointAccount("J6", "customer4", "customer3", 100f, "pending");
		//Arrays that can be quickly edited as test data is rewritten:
		BankAccount[] accounts = {bacc1, bacc2, bacc3, bacc4, bacc5, jacc1, jacc2, jacc3, jacc4};
		User[] users = {cust1,cust2,cust3,cust4,emp1,adm1};
		BankAccount[] applications = {appl1,appl2,appl3,appl4};
		//Initial data saving:
		FileOutputStream fout = new FileOutputStream("src/test/resources/Accounts.txt");
		ObjectOutputStream oout = new ObjectOutputStream(fout);
		
		for(BankAccount account : accounts) {
			oout.writeObject(account);
		}
		
		oout.close();
		fout.close();
		
		
		FileOutputStream fout2 = new FileOutputStream("src/test/resources/Users.txt");
		ObjectOutputStream oout2 = new ObjectOutputStream(fout2);
		
		for(User u : users) {
			oout2.writeObject(u);
		}
		
		oout2.close();
		fout2.close();
		
		
		
		FileOutputStream fout3 = new FileOutputStream("src/test/resources/IDCounts.txt");
		ObjectOutputStream oout3 = new ObjectOutputStream(fout3);
		
		oout3.writeInt(7);
		oout3.writeInt(6);
		
		oout3.close();
		fout3.close();
		
		FileOutputStream fout4 = new FileOutputStream("src/test/resources/Applications.txt");
		ObjectOutputStream oout4 = new ObjectOutputStream(fout4);
		
		for(BankAccount a : applications) {
			oout4.writeObject(a);
		}
		
		oout4.close();
		fout4.close();
		
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	//=== BankAccount Tests ===
	
	//Test if an updated account balance was returned by the correct amount after a successful deposit
	@Test
	public void TestDepositAllow() {
		BankAccount bac = new BankAccount("C0001","customer1",100f, "active");
		Assert.assertEquals(120f, bac.doDeposit(20f),0f);
	}
	
	//Test if -1f was returned on a failed deposit
	@Test
	public void TestDepositDenyNegativeValue() {
		BankAccount bac = new BankAccount("C0001","customer1",100f, "active");
		Assert.assertEquals(-1f, bac.doDeposit(-10f),0f);
	}
	
	//Test if the correct amount was returned after a withdrawal
	@Test
	public void TestWithdrawAllow() {
		BankAccount bac = new BankAccount("C0001","customer1",100f, "active");
		Assert.assertEquals(50f, bac.doWithdrawal(50f),0f);
	}
	
	//Test if -1f is returned when there was an attempt to withdraw a negative value
	@Test
	public void TestWithdrawDenyNegativeValue() {
		BankAccount bac = new BankAccount("C0001","customer1",100f, "active");
		Assert.assertEquals(-1f, bac.doWithdrawal(-1f),0f);
	}
	
	//Test if -1f is returned after an attempt to withdraw more than an account's available funds
	@Test
	public void TestWithdrawDenyOverdraw() {
		BankAccount bac = new BankAccount("C0001","customer1",100f, "active");
		Assert.assertEquals(-1f, bac.doWithdrawal(120f),0f);
	}
	
	//Test if a transfer is approved given a proper amount to send
	@Test
	public void TestTransferAllow() {
		BankAccount bac1 = new BankAccount("C0001","customer1",100f, "active");
		BankAccount bac2 = new BankAccount("C0002","customer1",100f, "active");
		Assert.assertTrue(bac1.doTransfer(50f, bac2));
	}
	
	//Test if a transfer is denied after sending more than an account's available funds
	@Test
	public void TestTransferDenyInsufficientFunds() {
		BankAccount bac1 = new BankAccount("C0001","customer1",100f, "active");
		BankAccount bac2 = new BankAccount("C0002","customer1",100f, "active");
		Assert.assertFalse(bac1.doTransfer(101f, bac2));
	}
	
	//Test if a transfer is denied after sending a negative amount
	@Test
	public void TestTransferDenyNegativeAmount() {
		BankAccount bac1 = new BankAccount("C0001","customer1",100f, "active");
		BankAccount bac2 = new BankAccount("C0002","customer1",100f, "active");
		Assert.assertFalse(bac1.doTransfer(-1f, bac2));
	}
	
	//Test if a transfer is denied when made to the same account
	@Test
	public void TestTransferDenySameAccount() {
		BankAccount bac1 = new BankAccount("C0001","customer1",100f, "active");
		Assert.assertFalse(bac1.doTransfer(1f, bac1));
	}
	
	//Test if the correct amount of money is given to the recipient of a transfer
	@Test
	public void TestTransferCorrectAmountSent() {
		BankAccount bac1 = new BankAccount("C0001","customer1",100f, "active");
		BankAccount bac2 = new BankAccount("C0002","customer1",100f, "active");
		bac1.doTransfer(50f, bac2);
		Assert.assertEquals(150f, bac2.getBalance(),0f);
	}
	
	//Test if the correct amount of money is taken from the sender during a transfer
	@Test
	public void TestTransferCorrectAmountSubtracted() {
		BankAccount bac1 = new BankAccount("C0001","customer1",100f, "active");
		BankAccount bac2 = new BankAccount("C0002","customer1",100f, "active");
		bac1.doTransfer(50f, bac2);
		Assert.assertEquals(50f, bac1.getBalance(),0f);
	}
	
	//Test if no money was sent on a failed transaction
	@Test
	public void TestTransferNoMoneySent() {
		BankAccount bac1 = new BankAccount("C0001","customer1",100f, "active");
		BankAccount bac2 = new BankAccount("C0002","customer1",100f, "active");
		bac1.doTransfer(-1f, bac2);
		Assert.assertEquals(100f, bac2.getBalance(),0f);
	}
	
	//Test if no money was subtracted from the sender on a failed transaction
	@Test
	public void TestTransferNoMoneySubtracted() {
		BankAccount bac1 = new BankAccount("C0001","customer1",100f, "active");
		BankAccount bac2 = new BankAccount("C0002","customer1",100f, "active");
		bac1.doTransfer(-1f, bac2);
		Assert.assertEquals(100f, bac1.getBalance(),0f);
	}
	
	// === DataService test methods ===
	
	//Test loaded accounts against initial saved data
	@Test
	public void TestLoadAccounts() {
		DataService ds = new DataService();
		ds.loadBankAccounts("src/test/resources/Accounts.txt");
		
		BankAccount bacc1 = new BankAccount("C1", "customer1", 100f, "active");
		BankAccount bacc2 = new BankAccount("C2", "customer2", 100f, "active");
		BankAccount bacc3 = new BankAccount("C3", "customer3", 100f, "active");
		BankAccount bacc4 = new BankAccount("C4", "customer4", 100f, "active");
		BankAccount bacc5 = new BankAccount("C5", "customer1",100f, "cancelled");
		
		JointAccount jacc1 = new JointAccount("J1","customer1","customer2",100f,"active");
		JointAccount jacc2 = new JointAccount("J2","customer1","customer3",100f,"active");
		JointAccount jacc3 = new JointAccount("J3","customer2","customer3",100f,"active");
		JointAccount jacc4 = new JointAccount("J4","customer3","customer4",100f,"active");
		
		
		BankAccount[] accounts = {bacc1, bacc2, bacc3, bacc4, bacc5, jacc1, jacc2, jacc3, jacc4};
		boolean isSame = true;
		for(int i = 0; i < accounts.length; i++) {
			if(!accounts[i].equals(ds.getAccounts().get(i))) {
				isSame = false;
			}
		}
		Assert.assertTrue(isSame);
	}
	
	//Test loaded users against initial saved data
	@Test
	public void TestLoadUsers() {
		DataService ds = new DataService();
		ds.loadUsers("src/test/resources/Users.txt");
		
		Customer cust1 = new Customer("customer1", "password1", "Guy One", 10);
		Customer cust2 = new Customer("customer2", "password2", "Guy Two", 20);
		Customer cust3 = new Customer("customer3", "password3", "Guy Three", 30);
		Customer cust4 = new Customer("customer4", "password4", "Guy Four", 40);
		Employee emp1 = new Employee("employee1", "Worker Man", "1234567895", 50);
		Admin adm1 = new Admin("admin1", "passwordA", "Admin Boss", 60);
		
		System.out.println(ds.getUsers());
		
		User[] users = {cust1, cust2, cust3, cust4, emp1, adm1};
		boolean isSame = true;
		for(int i = 0; i < users.length; i++) {
			if(!users[i].equals(ds.getUsers().get(i))) {
				isSame = false;
			}
		}
		Assert.assertTrue(isSame);
	}
	
	//Test loaded ID counts against initial saved data
	@Test
	public void TestLoadNumAccounts() {
		DataService ds = new DataService();
		ds.loadNumAccounts("src/test/resources/IDCounts.txt");
		Assert.assertTrue(ds.getNumBankAccounts() == 7 && ds.getNumJointAccounts() == 6);
	}
	
	//Test loaded account applications against initial saved data
	@Test
	public void TestLoadApplications() {
		DataService ds = new DataService();
		ds.loadApplications("src/test/resources/Applications.txt");
		
		BankAccount appl1 = new BankAccount("C6", "customer1", 100f, "pending");
		BankAccount appl2 = new BankAccount("C7", "customer2", 100f, "pending");
		BankAccount appl3 = new JointAccount("J5", "customer3", "customer4", 100f, "pending");
		BankAccount appl4 = new JointAccount("J6", "customer4", "customer3", 100f, "pending");
		
		BankAccount[] applications = {appl1,appl2,appl3,appl4};
		
		boolean isSame = true;
		for(int i = 0; i < applications.length; i++) {
			if(!applications[i].equals(ds.getApplications().get(i))) {
				isSame = false;
			}
		}
		Assert.assertTrue(isSame);
	}
	
	//Test to make sure after saving and loading, no changes are made to the test data for accounts
	//Requires loadAccounts to be functional, though all loading methods has already been tested to work properly.
	@Test
	public void TestSaveAccounts() {
		DataService ds1 = new DataService();
		DataService ds2 = new DataService();
		
		ds1.loadBankAccounts("src/test/resources/Accounts.txt");
		ds1.saveBankAccounts("src/test/resources/Accounts.txt");
		
		ds2.loadBankAccounts("src/test/resources/Accounts.txt");
		
		BankAccount bacc1 = new BankAccount("C1", "customer1", 100f, "active");
		BankAccount bacc2 = new BankAccount("C2", "customer2", 100f, "active");
		BankAccount bacc3 = new BankAccount("C3", "customer3", 100f, "active");
		BankAccount bacc4 = new BankAccount("C4", "customer4", 100f, "active");
		BankAccount bacc5 = new BankAccount("C5", "customer1",100f, "cancelled");
		
		JointAccount jacc1 = new JointAccount("J1","customer1","customer2",100f,"active");
		JointAccount jacc2 = new JointAccount("J2","customer1","customer3",100f,"active");
		JointAccount jacc3 = new JointAccount("J3","customer2","customer3",100f,"active");
		JointAccount jacc4 = new JointAccount("J4","customer3","customer4",100f,"active");
		
		
		BankAccount[] accounts = {bacc1, bacc2, bacc3, bacc4, bacc5, jacc1, jacc2, jacc3, jacc4};
		boolean isSame = true;
		for(int i = 0; i < accounts.length; i++) {
			if(!accounts[i].equals(ds2.getAccounts().get(i))) {
				isSame = false;
			}
		}
		Assert.assertTrue(isSame);
	}
	
	//Test to make sure after saving and loading, no changes are made to the test data for users
	@Test
	public void TestSaveUsers() {
		DataService ds1 = new DataService();
		DataService ds2 = new DataService();
		
		ds1.loadUsers("src/test/resources/Users.txt");
		ds1.saveUsers("src/test/resources/Users.txt");
		
		ds2.loadUsers("src/test/resources/Users.txt");
		
		Customer cust1 = new Customer("customer1", "password1", "Guy One", 10);
		Customer cust2 = new Customer("customer2", "password2", "Guy Two", 20);
		Customer cust3 = new Customer("customer3", "password3", "Guy Three", 30);
		Customer cust4 = new Customer("customer4", "password4", "Guy Four", 40);
		Employee emp1 = new Employee("employee1", "Worker Man", "1234567895", 50);
		Admin adm1 = new Admin("admin1", "passwordA", "Admin Boss", 60);
		
		System.out.println(ds2.getUsers());
		
		User[] users = {cust1, cust2, cust3, cust4, emp1, adm1};
		boolean isSame = true;
		for(int i = 0; i < users.length; i++) {
			if(!users[i].equals(ds2.getUsers().get(i))) {
				isSame = false;
			}
		}
		Assert.assertTrue(isSame);
	}
	
	//Test to make sure after saving and loading, no changes are made to the test data for the number of BankAccounts and JointAccounts
	@Test
	public void TestSaveNumAccounts() {
		DataService ds1 = new DataService();
		DataService ds2 = new DataService();
		
		ds1.loadNumAccounts("src/test/resources/IDCounts.txt");
		ds1.saveNumAccounts("src/test/resources/IDCounts.txt");
		
		ds2.loadNumAccounts("src/test/resources/IDCounts.txt");
		Assert.assertTrue(ds2.getNumBankAccounts() == 7 && ds2.getNumJointAccounts() == 6);
	}
	
	//Test to make sure after saving and loading, no changes are made to the test data for applications
	@Test
	public void TestSaveApplications() {
		DataService ds1 = new DataService();
		DataService ds2 = new DataService();
		
		ds1.loadApplications("src/test/resources/Applications.txt");
		ds1.saveApplications("src/test/resources/Applications.txt");
		
		ds2.loadApplications("src/test/resources/Applications.txt");
		
		BankAccount appl1 = new BankAccount("C6", "customer1", 100f, "pending");
		BankAccount appl2 = new BankAccount("C7", "customer2", 100f, "pending");
		BankAccount appl3 = new JointAccount("J5", "customer3", "customer4", 100f, "pending");
		BankAccount appl4 = new JointAccount("J6", "customer4", "customer3", 100f, "pending");
		
		BankAccount[] applications = {appl1,appl2,appl3,appl4};
		
		boolean isSame = true;
		for(int i = 0; i < applications.length; i++) {
			if(!applications[i].equals(ds2.getApplications().get(i))) {
				isSame = false;
			}
		}
		Assert.assertTrue(isSame);
	}
	
	//When getAccountsOfUser is run, accounts that are cancelled should not be returned when IncludeInactive is false
	@Test
	public void TestGetAccountsOfUser_NoCancelledAccounts() throws InvalidClassException {
		DataService ds = new DataService();
		ds.loadBankAccounts("src/test/resources/Accounts.txt");
		ds.loadUsers("src/test/resources/Users.txt");
		
		
		BankAccount bacc1 = new BankAccount("C1", "customer1", 100f, "active");
		JointAccount jacc1 = new JointAccount("J1","customer1","customer2",100f,"active");
		JointAccount jacc2 = new JointAccount("J2","customer1","customer3",100f,"active");
		
		ArrayList<BankAccount> foundAccounts = ds.getAccountsOfUser("customer1", false);
		BankAccount accs[] = {bacc1, jacc1, jacc2};
		
		System.out.println("Found accounts of customer1: " + foundAccounts);
		
		boolean isSame = true;
		for(int i = 0; i < accs.length; i++) {
			if(!accs[i].equals(foundAccounts.get(i))) {
				isSame = false;
			}
		}
		Assert.assertTrue(isSame);
		
	}
	
	//When getAccountsOfUser is run, accounts that are cancelled should be returned when IncludeInactive is true
	@Test
	public void TestGetAccountsOfUserWithCancelled() throws InvalidClassException {
		DataService ds = new DataService();
		ds.loadBankAccounts("src/test/resources/Accounts.txt");
		ds.loadUsers("src/test/resources/Users.txt");
		
		
		BankAccount bacc1 = new BankAccount("C1", "customer1", 100f, "active");
		BankAccount bacc5 = new BankAccount("C5","customer1",100f, "cancelled");
		JointAccount jacc1 = new JointAccount("J1","customer1","customer2",100f,"active");
		JointAccount jacc2 = new JointAccount("J2","customer1","customer3",100f,"active");
		
		
		ArrayList<BankAccount> foundAccounts = ds.getAccountsOfUser("customer1", true);
		BankAccount accs[] = {bacc1, bacc5, jacc1, jacc2};
		
		System.out.println("(WithCancelled)Found accounts of customer1: " + foundAccounts);
		
		boolean isSame = true;
		for(int i = 0; i < accs.length; i++) {
			if(!accs[i].equals(foundAccounts.get(i))) {
				isSame = false;
			}
		}
		Assert.assertTrue(isSame);
		
	}
	
	//Given the initial data, createAccountApplication should create a specific BankAccount application while not causing other changes in the applications list.
	@Test
	public void TestCreateAccountApplicationSingle() {
		DataService ds = new DataService();
		ds.loadApplications("src/test/resources/Applications.txt");
		ds.loadNumAccounts("src/test/resources/IDCounts.txt");
		
		ds.createAccountApplication("customer6");
		
		BankAccount appl1 = new BankAccount("C6", "customer1", 100f, "pending");
		BankAccount appl2 = new BankAccount("C7", "customer2", 100f, "pending");
		BankAccount appl3 = new JointAccount("J5", "customer3", "customer4", 100f, "pending");
		BankAccount appl4 = new JointAccount("J6", "customer4", "customer3", 100f, "pending");
		BankAccount expectedAccount = new BankAccount("C8","customer6", 0f, "pending");
		
		BankAccount expectedAccounts[] = {appl1,appl2,appl3,appl4, expectedAccount};
		
		System.out.println("Retrieved applications: " + ds.getApplications());
		
		boolean isSame = true;
		for(int i = 0; i < expectedAccounts.length; i++) {
			if(!expectedAccounts[i].equals(ds.getApplications().get(i))) {
				isSame = false;
			}
		}
		Assert.assertTrue(isSame);
	}
	
	//Given the initial data, createAccountApplication, when given two string arguments, should create a specific JointAccount application while not causing other changes in the applications list.
	//Preventing a joint account owner who does not exist is the responsibility of implementation.
	@Test
	public void TestCreateAccountApplicationJoint() {
		DataService ds = new DataService();
		ds.loadApplications("src/test/resources/Applications.txt");
		ds.loadNumAccounts("src/test/resources/IDCounts.txt");
		
		ds.createAccountApplication("customer6","customer7");
		
		BankAccount appl1 = new BankAccount("C6", "customer1", 100f, "pending");
		BankAccount appl2 = new BankAccount("C7", "customer2", 100f, "pending");
		BankAccount appl3 = new JointAccount("J5", "customer3", "customer4", 100f, "pending");
		BankAccount appl4 = new JointAccount("J6", "customer4", "customer3", 100f, "pending");
		BankAccount expectedAccount = new JointAccount("J7","customer6", "customer7", 0f, "pending");
		
		BankAccount expectedAccounts[] = {appl1,appl2,appl3,appl4, expectedAccount};
		
		System.out.println("Retrieved applications: " + ds.getApplications());
		
		boolean isSame = true;
		for(int i = 0; i < expectedAccounts.length; i++) {
			if(!expectedAccounts[i].equals(ds.getApplications().get(i))) {
				isSame = false;
			}
		}
		Assert.assertTrue(isSame);
	}

	//isUsernameAvailable should return true when a name is not taken.
	@Test
	public void TestIsUsernameAvailableTrue() {
		DataService ds = new DataService();
		ds.loadUsers("src/test/resources/Users.txt");
		Assert.assertTrue(ds.isUsernameAvailable("namethatisnottaken"));
	}

	//isUsernameAvailable should return false when a name is already taken.
	@Test
	public void TestIsUsernameAvailableFalse() {
		DataService ds = new DataService();
		ds.loadUsers("src/test/resources/Users.txt");
		
		Assert.assertFalse(ds.isUsernameAvailable("customer1"));
	}
	
	//getUserByUsername should return a corresponding user if a user with that username exists.
	@Test
	public void testGetUserByUsernameFound() {
		DataService ds = new DataService();
		ds.loadUsers("src/test/resources/Users.txt");
		
		Customer cust1 = new Customer("customer1", "password1", "Guy One", 10);
		
		Assert.assertTrue(ds.getUserByUsername("customer1").equals(cust1));
	}
	
	//getUserByUsername should return a user if a user with that username exists.
	@Test
	public void testGetUserByUsernameNotFound() {
		DataService ds = new DataService();
		ds.loadUsers("src/test/resources/Users.txt");
		Assert.assertNull(ds.getUserByUsername("NameThatDoesNotExistHere"));
	}
	
	//getAccountByID should return a specific BankAccount when given the corresponding ID.
	@Test
	public void testGetAccountByIDSingle() {
		DataService ds = new DataService();
		ds.loadBankAccounts("src/test/resources/Accounts.txt");
		
		BankAccount bacc1 = new BankAccount("C1", "customer1", 100f, "active");
		
		Assert.assertTrue(ds.getAccountByID("C1").equals(bacc1));
		
	}
	
	//getAccountByID should return a specific JointAccount when given the corresponding ID.
	@Test
	public void testGetAccountByID_Joint() {
		DataService ds = new DataService();
		ds.loadBankAccounts("src/test/resources/Accounts.txt");
		
		JointAccount jacc1 = new JointAccount("J1","customer1","customer2",100f,"active");
		
		Assert.assertTrue(ds.getAccountByID("J1").equals(jacc1));
		
	}
	
	//getApplicationByID should return a specific single account application given the corresponding ID.
	@Test
	public void TestGetApplicationByID_Single() {
		DataService ds = new DataService();
		ds.loadApplications("src/test/resources/Applications.txt");
		
		BankAccount appl1 = new BankAccount("C6", "customer1", 100f, "pending");
		
		Assert.assertTrue(appl1.equals(ds.getApplicationByID("C6")));
	}
	
	//getApplicationByID should return a specific joint account application given the corresponding ID.
	@Test
	public void TestGetApplicationByID_Joint() {
		DataService ds = new DataService();
		ds.loadApplications("src/test/resources/Applications.txt");
		
		BankAccount appl3 = new JointAccount("J5", "customer3", "customer4", 100f, "pending");
		
		Assert.assertTrue(appl3.equals(ds.getApplicationByID("J5")));
	}
	
	//After an application is approved, the Accounts ArrayList should be properly updated.
	@Test
	public void TestApproveApplication_AccountsChanged() {
		DataService ds = new DataService();
		ds.loadBankAccounts("src/test/resources/Accounts.txt");
		ds.loadApplications("src/test/resources/Applications.txt");
		
		ds.approveApplication("C6");
		
		BankAccount bacc1 = new BankAccount("C1", "customer1", 100f, "active");
		BankAccount bacc2 = new BankAccount("C2", "customer2", 100f, "active");
		BankAccount bacc3 = new BankAccount("C3", "customer3", 100f, "active");
		BankAccount bacc4 = new BankAccount("C4", "customer4", 100f, "active");
		BankAccount bacc5 = new BankAccount("C5","customer1",100f, "cancelled");
		
		JointAccount jacc1 = new JointAccount("J1","customer1","customer2",100f,"active");
		JointAccount jacc2 = new JointAccount("J2","customer1","customer3",100f,"active");
		JointAccount jacc3 = new JointAccount("J3","customer2","customer3",100f,"active");
		JointAccount jacc4 = new JointAccount("J4","customer3","customer4",100f,"active");
		
		BankAccount appl1 = new BankAccount("C6", "customer1", 100f, "active");
		
		BankAccount expectedAccounts[] = {bacc1,bacc2,bacc3,bacc4,bacc5,jacc1,jacc2,jacc3,jacc4,appl1};
		
		System.out.println("(TestApproveApplication) Accounts: "+ds.getAccounts());
		
		boolean isSame = true;
		for(int i = 0; i < expectedAccounts.length; i++) {
			if(!expectedAccounts[i].equals(ds.getAccounts().get(i))) {
				isSame = false;
			}
		}
		Assert.assertTrue(isSame);
	}
	
	//After an application is approved, the application ArrayList should be properly updated.
	@Test
	public void TestApproveApplication_ApplicationsChanged() {
		DataService ds = new DataService();
		ds.loadBankAccounts("src/test/resources/Accounts.txt");
		ds.loadApplications("src/test/resources/Applications.txt");
		
		ds.approveApplication("C6");
		
		BankAccount appl2 = new BankAccount("C7", "customer2", 100f, "pending");
		BankAccount appl3 = new JointAccount("J5", "customer3", "customer4", 100f, "pending");
		BankAccount appl4 = new JointAccount("J6", "customer4", "customer3", 100f, "pending");
		
		BankAccount expectedAccounts[] = {appl2,appl3,appl4};
		
		System.out.println("(TestApproveApplication) Accounts: "+ds.getAccounts());
		
		boolean isSame = true;
		for(int i = 0; i < expectedAccounts.length; i++) {
			if(!expectedAccounts[i].equals(ds.getApplications().get(i))) {
				isSame = false;
			}
		}
		Assert.assertTrue(isSame);
	}
	
	//After an application is denied, the application ArrayList should be properly updated.
	@Test
	public void TestDenyApplication() {
		DataService ds = new DataService();
		ds.loadBankAccounts("src/test/resources/Accounts.txt");
		ds.loadApplications("src/test/resources/Applications.txt");
		
		ds.denyApplication("C6");
		
		BankAccount appl2 = new BankAccount("C7", "customer2", 100f, "pending");
		BankAccount appl3 = new JointAccount("J5", "customer3", "customer4", 100f, "pending");
		BankAccount appl4 = new JointAccount("J6", "customer4", "customer3", 100f, "pending");
		
		BankAccount expectedAccounts[] = {appl2,appl3,appl4};
		
		System.out.println("(TestApproveApplication) Accounts: "+ds.getAccounts());
		
		boolean isSame = true;
		for(int i = 0; i < expectedAccounts.length; i++) {
			if(!expectedAccounts[i].equals(ds.getApplications().get(i))) {
				isSame = false;
			}
		}
		Assert.assertTrue(isSame);
	}

}
