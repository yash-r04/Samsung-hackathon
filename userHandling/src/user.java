
public class user {
    private  int id;
    private  String name;
    private  String email;


    public user(int id,String name,String email){
        this.id=id;
        this.name=name;
        this.email=email;
      
    }
    public int getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getEmail(){
        return email;
    }
   
    public String showData(){
        return ("\n[id="+id+" name="+name+" email="+email+"]");
    }
}
