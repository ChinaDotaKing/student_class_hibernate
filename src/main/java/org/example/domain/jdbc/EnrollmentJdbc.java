package org.example.domain.jdbc;


import lombok.Getter;
import lombok.Setter;
import org.example.domain.Enrollment;
import org.springframework.stereotype.Component;


@Getter
@Setter
@Component
public class EnrollmentJdbc extends Enrollment {

    private int id;
    private int student_id;
    private int class_id;
    private String status;


}
