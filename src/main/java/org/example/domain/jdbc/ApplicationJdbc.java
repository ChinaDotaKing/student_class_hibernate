package org.example.domain.jdbc;


import lombok.Getter;
import lombok.Setter;
import org.example.domain.Application;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Getter
@Setter
@Component
public class ApplicationJdbc extends Application {

    private int id;
    private int student_id;
    private int class_id;
    private Timestamp creation_time;
    private String request;

    private String status;
    private String feedback;

}
