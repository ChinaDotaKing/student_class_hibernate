package org.example.domain.jdbc;

import lombok.Getter;
import lombok.Setter;
import org.example.domain.Lecture;
import org.springframework.stereotype.Component;

import java.sql.Time;


@Getter
@Setter
@Component
public class LectureJdbc extends Lecture {


    private int id;
    private int day_of_week;
    private Time start_time;
    private Time end_time;
}
