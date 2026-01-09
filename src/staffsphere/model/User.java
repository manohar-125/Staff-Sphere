package staffsphere.model;

public class User {
    private int userId;
    private String username;
    private String password;
    private String role;
    private String status;

    public User(){
    }

    public User(int uid, String uname, String password, String role, String status){
        this.userId = uid;
        this.username = uname;
        this.password = password;
        this.role = role;
        this.status = status;
    }

    public void setUserId(int uid){
        this.userId = uid;
    }

    public int getUserId(){
        return this.userId;
    }

    public void setUsername(String uname){
        this.username = uname;
    }
    public String getUsername(){
        return this.username;
    }

    public void setPassword(String password){
        this.password = password;
    }
    public String getPassword(){
        return this.password;
    }

    public void setRole(String role){
        this.role = role;
    }
    public String getRole(){
        return this.role;
    }

    public void setStatus(String status){
        this.status = status;
    }
    public String getStatus(){
        return this.status;
    }
}

