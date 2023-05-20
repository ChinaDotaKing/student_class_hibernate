package org.example.domain.jdbc;


import lombok.Getter;
import lombok.Setter;
import org.example.domain.Course;
import org.springframework.stereotype.Component;

import javax.persistence.ManyToOne;

@Getter
@Setter
@Component
public class CourseJdbc extends Course {

    private int id;
    private String course_name;
    private String course_code;


    private int dept_id;
    private String description;
    public CourseJdbc(){

    }
    public CourseJdbc(String course_name, String course_code, int dept_id, String description) {
        this.course_name = course_name;
        this.course_code = course_code;
        this.dept_id = dept_id;
        this.description = description;
    }
}
