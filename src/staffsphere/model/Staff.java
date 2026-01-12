package staffsphere.model;

import java.sql.Timestamp;

public class Staff {
    private int id;
    private String empCode;
    private String name;
    private String email;
    private String phone;
    private String department;
    private String designation;
    private double salary;
    private String status;
    private Integer createdBy;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Staff(){
    }

    public Staff(int id, String empCode, String name, String email, String phone, 
                 String department, String designation, double salary, String status) {
        this.id = id;
        this.empCode = empCode;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.designation = designation;
        this.salary = salary;
        this.status = status;
    }

    public int getId(){
        return this.id;
    }
    public void setId(int id){
        this.id = id;
    }

    public String getEmpCode(){
        return this.empCode;
    }
    public void setEmpCode(String empCode){
        this.empCode = empCode;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getEmail(){
        return this.email;
    }
    public void setEmail(String email){
        this.email = email;
    }

    public String getPhone(){
        return this.phone;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }

    public String getDepartment(){
        return this.department;
    }
    public void setDepartment(String department){
        this.department = department;
    }

    public String getDesignation(){
        return this.designation;
    }
    public void setDesignation(String designation){
        this.designation = designation;
    }

    public String getRole(){
        return this.designation;
    }
    public void setRole(String role){
        this.designation = role;
    }

    public double getSalary(){
        return this.salary;
    }
    public void setSalary(double salary){
        this.salary = salary;
    }

    public String getStatus(){
        return this.status;
    }
    public void setStatus(String status){
        this.status = status;
    }

    public Integer getCreatedBy(){
        return this.createdBy;
    }
    public void setCreatedBy(Integer createdBy){
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedAt(){
        return this.createdAt;
    }
    public void setCreatedAt(Timestamp createdAt){
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt(){
        return this.updatedAt;
    }
    public void setUpdatedAt(Timestamp updatedAt){
        this.updatedAt = updatedAt;
    }
}

