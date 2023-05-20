package org.example.mapper;

import org.example.domain.Department;
import org.example.domain.jdbc.DepartmentJdbc;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;



@Component
public class DepartmentRowMapper implements RowMapper<Department> {
    @Override
    public Department mapRow(ResultSet rs, int rowNum) throws SQLException {
        Department department = new DepartmentJdbc();
        department.setId(rs.getInt("id"));
        department.setName(rs.getString("name"));
        department.setSchool(rs.getString("school"));



        return department;
    }
}

