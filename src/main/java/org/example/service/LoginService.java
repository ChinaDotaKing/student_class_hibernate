package org.example.service;

import org.example.dao.InterF.StudentDao;
import org.example.domain.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {
    private final StudentDao studentHibernateDao;

    @Autowired
    public LoginService(StudentDao studentHibernateDao) {this.studentHibernateDao = studentHibernateDao; }

    public Optional<Student> validateLogin(String username, String password) {
        return studentHibernateDao.validateLogin(username, password);
    }

}
