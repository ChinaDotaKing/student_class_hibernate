package org.example.domain.jdbc;

import lombok.Getter;
import lombok.Setter;
import org.example.domain.Professor;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class ProfessorJdbc extends Professor {

    private int id;
    private String firstName;
    private String lastName;
    private String email;


    private int dept_id;
}
