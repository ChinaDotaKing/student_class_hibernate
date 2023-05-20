package org.example.domain.jdbc;

import lombok.Getter;
import lombok.Setter;
import org.example.domain.Student;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class StudentJdbc extends Student {
    private  int id;
    private String username;
    private String password;

    private String email;

    private String encrypted_password;


    private String firstName;

    private String lastName;

    private int dept_id;

    private boolean is_active = true;
    private boolean is_admin =false;

    public StudentJdbc(String username, String password, String email, String firstName, String lastName, int dept_id, boolean is_admin) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dept_id = dept_id;
        this.is_admin = is_admin;
    }

    public StudentJdbc(String username, String encrypted_password, String email, String firstName, String lastName, int dept_id) {
        this.username = username;
        this.encrypted_password = encrypted_password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dept_id = dept_id;
    }

    public StudentJdbc(int id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public StudentJdbc(String username, String encrypted_password, String email) {
        this.username = username;
        this.encrypted_password = encrypted_password;
        this.email = email;
    }

    public StudentJdbc() {

    }
}
