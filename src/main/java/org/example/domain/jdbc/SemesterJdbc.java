package org.example.domain.jdbc;


import lombok.Getter;
import lombok.Setter;
import org.example.domain.Semester;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Getter
@Setter
@Component
public class SemesterJdbc extends Semester {
    private int id;
    private String name;
    private Date start_date;
    private Date end_date;
}
