package org.example.domain.jdbc;


import lombok.Getter;
import lombok.Setter;
import org.example.domain.Prerequisite;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class PrerequisiteJdbc extends Prerequisite {
    private int id;
    private int course_id;
    private int pre_req_id;
}
