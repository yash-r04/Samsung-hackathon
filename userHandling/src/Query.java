
public class Query {
    public static String insert="insert into user (id,name,email) values (?,?,?)";
    public static String updateName ="update user set name=?,email=? where id=?";
    public static String deleteTable ="delete from user";
    public static String deleteId="delete from user where id=?";
    public static String select="select * from user";
}
