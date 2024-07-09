
import java.sql.*;

public class connection {
    static Connection connection=null;

    public static Connection connect(String dataBaseName){
        try {
            String url="jdbc:mysql://localhost:3306/"+dataBaseName;
            String username="root";
            String password="__";//your password
            connection=DriverManager.getConnection(url, username, password);
            System.out.println("Connection succesfull");
            
        } catch (Exception e) {
            System.out.println("Unable to connect");
        }
        return connection;
    } 
}
