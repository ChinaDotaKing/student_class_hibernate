package org.example.dao.InterF;

import org.example.domain.Course;
import org.example.domain.WebRegClass;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface CourseDao {


    Course getCourseById(int id);

    List<Course> getAllCourses();

    void createNewCourse(Course course);

    String getCourseNameById(int courseId);

    Course getCourseByClass(WebRegClass c);
}
