package org.example.mapper;


import org.example.domain.Prerequisite;
import org.example.domain.jdbc.PrerequisiteJdbc;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PrerequisiteRowMapper implements RowMapper<Prerequisite> {
    @Override
    public Prerequisite mapRow(ResultSet rs, int rowNum) throws SQLException {
        Prerequisite prerequisite = new PrerequisiteJdbc();
        prerequisite.setId(rs.getInt("id"));
        prerequisite.setCourse_id(rs.getInt("course_id"));
        prerequisite.setPre_req_id(rs.getInt("pre_req_id"));



        return prerequisite;
    }
}

