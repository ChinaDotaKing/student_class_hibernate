package org.example.domain.jdbc;


import lombok.Getter;
import lombok.Setter;
import org.example.domain.Department;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class DepartmentJdbc extends Department {

    private int id;
    private String name;
    private String school;

}
