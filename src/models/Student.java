package models;

import java.sql.Date;

public class Student {
    private int id;
    private String name;
    private Date birth;
    private String address;
    private String email;

    public Student(int id, String name, Date birth, String address, String email) {
        this.id = id;
        this.name = name;
        this.birth = birth;
        this.address = address;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getBirth() {
        return birth;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }
}
