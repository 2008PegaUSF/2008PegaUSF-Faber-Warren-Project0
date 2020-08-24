import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TestCases {
	
	@BeforeAll
	public static void setupTests() {
		try {
		BankAccount bacc1 = new BankAccount("C1", "customer1", 100f, "active");
		BankAccount bacc2 = new BankAccount("C2", "customer2", 100f, "active");
		BankAccount bacc3 = new BankAccount("C3", "customer3", 100f, "active");
		BankAccount bacc4 = new BankAccount("C4", "customer4", 100f, "active");
		
		JointAccount jacc1 = new JointAccount("J1","customer1","customer2",100f,"active");
		JointAccount jacc2 = new JointAccount("J2","customer1","customer3",100f,"active");
		JointAccount jacc3 = new JointAccount("J1","customer2","customer3",100f,"active");
		JointAccount jacc4 = new JointAccount("J2","customer3","customer4",100f,"active");
		
		Customer cust1 = new Customer("customer1", "password1", "Guy One", 10);
		Customer cust2 = new Customer("customer2", "password2", "Guy Two", 20);
		Customer cust3 = new Customer("customer3", "password3", "Guy Three", 30);
		Customer cust4 = new Customer("customer4", "password4", "Guy Four", 40);
		
		Employee emp1 = new Employee("employee1", "Worker Man", "1234567895", 50);
		
		Admin adm1 = new Admin("admin1", "passwordA", "Admin Boss", 60);
		
		BankAccount[] accounts = {bacc1, bacc2, bacc3, bacc4, jacc1, jacc2, jacc3, jacc4};
		User[] users = {cust1,cust2,cust3,cust4,emp1,adm1};
		
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
		
		
		
		FileOutputStream fout3 = new FileOutputStream("src/test/resources/Applications.txt");
		ObjectOutputStream oout3 = new ObjectOutputStream(fout3);
		
		oout3.writeInt(4);
		oout3.writeInt(4);
		
		oout3.close();
		fout3.close();
		
		
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
	@Test
	public void TestLoadAccounts() {
		DataService ds = new DataService();
		ds.loadBankAccounts("src/test/resources/Accounts.txt");
		
		BankAccount bacc1 = new BankAccount("C1", "customer1", 100f, "active");
		BankAccount bacc2 = new BankAccount("C2", "customer2", 100f, "active");
		BankAccount bacc3 = new BankAccount("C3", "customer3", 100f, "active");
		BankAccount bacc4 = new BankAccount("C4", "customer4", 100f, "active");
		
		JointAccount jacc1 = new JointAccount("J1","customer1","customer2",100f,"active");
		JointAccount jacc2 = new JointAccount("J2","customer1","customer3",100f,"active");
		JointAccount jacc3 = new JointAccount("J1","customer2","customer3",100f,"active");
		JointAccount jacc4 = new JointAccount("J2","customer3","customer4",100f,"active");
		
		BankAccount[] accounts = {bacc1, bacc2, bacc3, bacc4, jacc1, jacc2, jacc3, jacc4};
		boolean isSame = true;
		for(int i = 0; i < accounts.length; i++) {
			if(!accounts[i].equals(ds.getAccounts().get(i))) {
				isSame = false;
			}
		}
		Assert.assertTrue(isSame);
	}
}
