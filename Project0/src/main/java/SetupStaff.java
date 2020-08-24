import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

//This class should only be run to load in the staff and admin accounts. This will clear all customers
public class SetupStaff {
	public static void main(String[] args) throws IOException {
		Employee e1 = new Employee("employee1","password","Worker Man", 40);
		Admin a1 = new Admin("jfaber","password","Jacob Faber",23);
		Admin a2 = new Admin("jwarren","password","Jesse Warren",27);
		
		FileOutputStream fout = new FileOutputStream("src/main/resources/Users.txt");
		ObjectOutputStream oout = new ObjectOutputStream(fout);
		
		oout.writeObject(e1);
		oout.writeObject(a1);
		oout.writeObject(a2);
		
		oout.close();
		fout.close();
	}
}
