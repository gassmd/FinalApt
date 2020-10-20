package Model;

public class User {
    private int userId;
    private String userName;
    private String password;
    private int active;

    public User(int userId, String userName, String password){
        this.userId = userId;
        this.userName = userName;
        this.password = password;
    }


    public User(){

    }

    public int getUserId(){
        return userId;
    }

    public void setUserId(int userId){
        this.userId = userId;
    }

    public String getUserName(){
        return userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public int getActive(){
        return active;
    }

    public void setActive(){
        this.active = active;
    }

}

