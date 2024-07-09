
import java.sql.*;
import java.util.*;



public class userFunc {
    public  void addUser(user e,String dataBaseName){
        //make conn first
        Connection c=connection.connect(dataBaseName);
        //query with ? makrs:
        String query=Query.insert;
        //Transform query:
        try {
            PreparedStatement st=c.prepareStatement(query);
            //set st:
            st.setInt(1, e.getId());
            st.setString(2, e.getName());
            st.setString(3, e.getEmail());

            //execute:
            st.execute();
            System.out.println("----------Added successfulyy----------");
            st.close();

        } catch (SQLException e1) {
            System.out.println("----------Coluldnt add user----------");
        }
    }
    public void updateName(int id,String newName,String newEmail,String dataBaseName){
        Connection c=connection.connect(dataBaseName);
        String query=Query.updateName;
        try {
            PreparedStatement st=c.prepareStatement(query);
            st.setString(1, newName);//name is 1st questionmark
            st.setString(2, newEmail);//email is 2st questionmark
            st.setInt(3, id);//id is 5th question mark
            st.execute();
            System.out.println("----------Updated succesfully----------");
            st.close();

        } catch (Exception ex) {
            System.out.println("----------Coluldnt update ----------");
        }
    }
    public void deleteId(int id,String dataBaseName){
        Connection c=connection.connect(dataBaseName);
        String query=Query.deleteId;
        try {
            PreparedStatement st=c.prepareStatement(query);
            st.setInt(1, id);
            st.execute();
            System.out.println("----------Deleted succesfully----------");
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("----------Coluldnt delete----------");
        }
    }
    public void deleteTable(String dataBaseName){
        Connection c=connection.connect(dataBaseName);
        String query=Query.deleteTable;
        try {
            Statement st=c.createStatement();
            st.execute(query);
            System.out.println("----------Cleared table successfully----------");
            st.close();
        } catch (SQLException e) {
           System.out.println("----------Coluldnt delete table----------");
        }
    }
    public ArrayList<String> showData(String dataBaseName){//diplay all data till date:
        Connection c=connection.connect(dataBaseName);
        ArrayList<String>arr=new ArrayList<String>();
        String query=Query.select;
        try {
            Statement st=c.prepareStatement(query);
            ResultSet set=st.executeQuery(query);
            while (set.next()) {
                user e=new user(set.getInt(1), set.getString(2), set.getString(3));
                arr.add(e.showData());
            }
            st.close();
            
        } catch (Exception e) {
            System.out.println("----------u have not entered data yet----------");
        }

        return arr;

    }
    public ArrayList<user> getData(String dataBaseName){//diplay all data till date:
        Connection c=connection.connect(dataBaseName);
        ArrayList<user>arr=new ArrayList<user>();
        String query=Query.select;
        try {
            Statement st=c.prepareStatement(query);
            ResultSet set=st.executeQuery(query);
            while (set.next()) {
                user e=new user(set.getInt(1), set.getString(2), set.getString(3));
                arr.add(e);
            }
            st.close();
            
        } catch (Exception e) {
            System.out.println("----------u have not entered data yet----------");
        }

        return arr;

    }
}
