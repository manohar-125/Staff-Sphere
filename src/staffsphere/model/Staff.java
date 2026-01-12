package staffsphere.model;

public class Staff {
    private int id;
    private String name;
    private String email;
    private String role;
    private double salary;

    public Staff(){
    }

    public Staff(int id, String name, String email, String role, double salary) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.salary = salary;
    }

    public int getId(){
        return this.id;
    }
    public void setId(int id){
        this.id = id;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getRole(){
        return this.role;
    }
    public void setRole(String role){
        this.role = role;
    }

    public String getEmail(){
        return this.email;
    }
    public void setEmail(String email){
        this.email = email;
    }

    public double getSalary(){
        return this.salary;
    }
    public void setSalary(double salary){
        this.salary = salary;
    }
}

