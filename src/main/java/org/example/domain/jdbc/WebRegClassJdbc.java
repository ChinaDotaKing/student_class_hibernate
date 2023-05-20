package org.example.domain.jdbc;


import lombok.Getter;
import lombok.Setter;
import org.example.domain.WebRegClass;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class WebRegClassJdbc extends WebRegClass {

    private int id;
    private int course_id;
    private int semester_id;
    private int professor_id;
    private int classroom_id;
    private int lecture_id;
    private int capacity;
    private boolean is_active=true;

    public WebRegClassJdbc(){}
    public WebRegClassJdbc(int course_id, int semester_id, int professor_id, int classroom_id, int lecture_id, int capacity) {
        this.course_id=course_id;
        this.semester_id=semester_id;
        this.professor_id=professor_id;
        this.classroom_id=classroom_id;
        this.lecture_id=lecture_id;
        this.capacity=capacity;
        this.is_active=true;
    }
}
