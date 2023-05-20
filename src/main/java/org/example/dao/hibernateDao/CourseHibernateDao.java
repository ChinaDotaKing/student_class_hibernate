package org.example.dao.hibernateDao;


import lombok.Getter;
import lombok.Setter;
import org.example.dao.AbstractHibernateDao;
import org.example.dao.InterF.ApplicationDao;
import org.example.dao.InterF.CourseDao;
import org.example.domain.Application;
import org.example.domain.Classroom;
import org.example.domain.Course;
import org.example.domain.WebRegClass;
import org.example.domain.hibernate.*;
import org.example.mapper.ApplicationRowMapper;
import org.example.mapper.CourseRowMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@Repository("CourseDaoHibernate")
@Transactional
public class CourseHibernateDao extends AbstractHibernateDao<CourseHibernate> implements CourseDao {


    @Override
    public Course getCourseById(int id) {

        CourseHibernate courseHibernate=sessionFactory.getCurrentSession().get(CourseHibernate.class,id);
        return  courseHibernate;
    }

    @Override
    public List<Course> getAllCourses() {
        Session session=sessionFactory.getCurrentSession();

//        Transaction transaction = session.beginTransaction();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        // result class
        CriteriaQuery<CourseHibernate> criteriaQuery = criteriaBuilder.createQuery(CourseHibernate.class);

        criteriaQuery.from(CourseHibernate.class);
//        Root<CourseHibernate> aRoot=criteriaQuery.from(CourseHibernate.class);
//        aRoot.join("Department", JoinType.INNER);
//
//
//        criteriaQuery.multiselect(
//                aRoot.get("id"),
//                aRoot.get("course_name"),
//                aRoot.get("course_code"),
//                aRoot.get("Department").get("id"),
//
//
//                aRoot.get("description")
//        );


//        Predicate predicateA = criteriaBuilder.equal(aRoot.get("Student").get("status"), "Pending");

//        criteriaQuery.where(predicateA);
        List<Course> items = session.createQuery(criteriaQuery).getResultList().stream().collect(Collectors.toList());
//        transaction.commit();
//        session.close();
        return items;
    }

    @Override
    public void createNewCourse(Course course) {
        Session session=sessionFactory.getCurrentSession();

//        Transaction transaction = session.beginTransaction();



        CourseHibernate courseHibernate=new CourseHibernate();



        courseHibernate.setCourse_code(course.getCourse_code());
        courseHibernate.setCourse_name(course.getCourse_name());
        courseHibernate.setDepartmentHibernate(session.load(DepartmentHibernate.class,course.getDept_id()));
        courseHibernate.setDescription(course.getDescription());


        session.save(courseHibernate);
//        transaction.commit();
//        session.close();
    }

    @Override
    public String getCourseNameById(int courseId) {
        Session session=sessionFactory.getCurrentSession();

//        Transaction transaction = session.beginTransaction();

//        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
//        // result class
//        CriteriaQuery<String> criteriaQuery = criteriaBuilder.createQuery(String.class);
//        Root<CourseHibernate> aRoot=criteriaQuery.from(CourseHibernate.class);
//        aRoot.join("Department", JoinType.INNER);
//
//
//        criteriaQuery.multiselect(
//
//                aRoot.get("course_name")
//
//        );
//
//
//        Predicate predicateA = criteriaBuilder.equal(aRoot.get("id"), courseId);
//
//        criteriaQuery.where(predicateA);
//        List<String> items = session.createQuery(criteriaQuery).getResultList();
        CourseHibernate courseHibernate=session.load(CourseHibernate.class,courseId);


//        transaction.commit();
//        session.close();
        return courseHibernate.getCourse_name();
    }

    @Override
    public Course getCourseByClass(WebRegClass c) {
        Session session=sessionFactory.getCurrentSession();

        WebRegClassHibernate webRegClassHibernate=session.get(WebRegClassHibernate.class,c.getId());

        return webRegClassHibernate.getCourseHibernate();
    }


}
