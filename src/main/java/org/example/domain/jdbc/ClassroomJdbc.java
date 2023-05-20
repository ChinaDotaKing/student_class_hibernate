package org.example.domain.jdbc;

import lombok.Getter;
import lombok.Setter;
import org.example.domain.Classroom;
import org.springframework.stereotype.Component;



@Getter
@Setter
@Component
public class ClassroomJdbc extends Classroom {


    private int id;
    private String name;
    private String building;
    private int capacity;

}
