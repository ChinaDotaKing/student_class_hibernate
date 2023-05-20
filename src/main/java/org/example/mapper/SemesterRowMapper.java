package org.example.mapper;

import org.example.domain.Semester;
import org.example.domain.jdbc.SemesterJdbc;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;




@Component
public class SemesterRowMapper implements RowMapper<Semester> {
    @Override
    public Semester mapRow(ResultSet rs, int rowNum) throws SQLException {
        Semester semester = new SemesterJdbc();
        semester.setId(rs.getInt("id"));
        semester.setName(rs.getString("name"));
        semester.setStart_date(rs.getDate("start_date"));
        semester.setEnd_date(rs.getDate("end_date"));




        return semester;
    }
}
