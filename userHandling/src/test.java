
import java.util.Scanner;

public class test {
    public static void main(String[] args) throws Exception {
        

        Scanner sc=new Scanner(System.in);
        System.out.println("Enter the database name u wanna work with:");
        String dbName=sc.next();
        System.out.println("WORKING WITH : "+dbName);
        while (true) {
            System.out.println("Enter operation u wanna perform: ");
            System.out.println(
                "1 for adding new user\n2 for removing user\n3 for clearing data from table 'user'\n4 for updating user data\n5 for  displayingData "
                
            );
            int choice=sc.nextInt();
            if (choice>=1&&choice<=5) {
                userFunc func=new userFunc();
                switch (choice) {
                    case 1://add
                        System.out.println("Enter in format: id name email : ");
                        int id=sc.nextInt();String name=sc.next();String email=sc.next();
                        user newEmployee=new user(id, name, email);
                        func.addUser(newEmployee, dbName);
                        break;
                
                    case 2://remove
                        System.out.println("Enter the id of employee u wanna remove:");
                        int remid=sc.nextInt();
                        func.deleteId(remid, dbName);
                        break;
                
                    case 3://clear
                        func.deleteTable(dbName);
                        System.out.println("deleting employee table..");
                        break;
                
                    case 4://update
                        System.out.println("Enter in format: id updatedName updatedEmail :");
                        int oldId=sc.nextInt();String upName=sc.next();String upEmail=sc.next();
                        func.updateName(oldId, upName,upEmail, dbName);
                        break;
                
                    case 5://display
                        System.out.println(func.showData(dbName));
                        break;
                
                    default:
                        break;
                }
            }else{
                System.out.println("Invalid choice");
                sc.close();
            }
        }
    }
}
