package org.example.service;

import org.example.dao.InterF.*;
//import org.example.dao.jdbcDao.*;
import org.example.domain.*;
import org.example.domain.hibernate.StudentHibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

//    @Autowired
//    SessionFactory sessionFactory;
//
//
//    @Autowired
//            Session session;


    StudentDao studentDao;


    WebRegClassDao webRegClassDao;


    SemesterDao semesterDao;


    ProfessorDao professorDao;


    ClassroomDao classroomDao;


    CourseDao courseDao;


    EnrollmentDao enrollmentDao;


    DepartmentDao departmentDao;


    LectureDao lectureDao;


    PrerequisiteDao prerequisiteDao;


    ApplicationDao applicationDao;


@Autowired
    public UserServiceImpl( @Qualifier("ormSwitch") boolean ormSwitch,

                           // @Qualifier("ApplicationDaoJdbc") ApplicationDao applicationDaoJdbc,
                            @Qualifier("ApplicationDaoHibernate") ApplicationDao applicationHibernateDao,
                            //@Qualifier("StudentDaoJdbc") StudentDao studentDaoJdbc,
                            @Qualifier("StudentDaoHibernate") StudentDao studentHibernateDao,

                           // @Qualifier("ClassroomDaoJdbc") ClassroomDao classroomDaoJdbc,
                            @Qualifier("ClassroomDaoHibernate") ClassroomDao classroomHibernateDao,

                           // @Qualifier("CourseDaoJdbc") CourseDao courseDaoJdbc,
                            @Qualifier("CourseDaoHibernate") CourseDao courseHibernateDao,
                          //  @Qualifier("DepartmentDaoJdbc") DepartmentDao departmentDaoJdbc,
                            @Qualifier("DepartmentDaoHibernate") DepartmentDao departmentHibernateDao,
                           // @Qualifier("EnrollmentDaoJdbc") EnrollmentDao enrollmentDaoJdbc,
                            @Qualifier("EnrollmentDaoHibernate") EnrollmentDao enrollmentHibernateDao,

                           // @Qualifier("LectureDaoJdbc") LectureDao lectureDaoJdbc,
                            @Qualifier("LectureDaoHibernate") LectureDao lectureHibernateDao,

                           // @Qualifier("PrerequisiteDaoJdbc") PrerequisiteDao prerequisiteDaoJdbc,
                            @Qualifier("PrerequisiteDaoHibernate") PrerequisiteDao prerequisiteHibernateDao,
                           // @Qualifier("ProfessorDaoJdbc") ProfessorDao professorDaoJdbc,
                            @Qualifier("ProfessorDaoHibernate") ProfessorDao professorHibernateDao,

                           // @Qualifier("SemesterDaoJdbc") SemesterDao semesterDaoJdbc,
                            @Qualifier("SemesterDaoHibernate") SemesterDao semesterHibernateDao,

                          //  @Qualifier("WebRegClassDaoJdbc") WebRegClassDao webRegClassDaoJdbc,
                            @Qualifier("WebRegClassDaoHibernate") WebRegClassDao webRegClassHibernateDao

                            ) {
        this.studentDao =   studentHibernateDao;
    this.applicationDao =   applicationHibernateDao;
    this.classroomDao =   classroomHibernateDao;
    this.courseDao =   courseHibernateDao;
    this.departmentDao =   departmentHibernateDao;
    this.enrollmentDao =   enrollmentHibernateDao;
    this.lectureDao =  lectureHibernateDao;
    this.prerequisiteDao =   prerequisiteHibernateDao;
    this.professorDao =   professorHibernateDao;
    this.semesterDao =   semesterHibernateDao;
    this.webRegClassDao =   webRegClassHibernateDao;

    //session=sessionFactory.getCurrentSession();

    }

    @Override
    public void createNewStudent(StudentHibernate student) {
            studentDao.createNewStudent(student);

    }

    @Override
    public List<WebRegClass> getClassesbyCourseId(int id) {
        return webRegClassDao.getWebRegClassesByCourseId(id);
    }

    @Override
    public List<WebRegClass> getClassesbyStudentId(int id) {

        return webRegClassDao.getWebRegClassesByStudentId(id);
    }



    public Course getCourseById(int id) {

        return courseDao.getCourseById(id);
    }

    public Professor getProfessorById(int id) {

        return professorDao.getProfessorById(id);
    }

    public Semester getSemesterById(int id) {

        return semesterDao.getSemesterById(id);
    }

    @Override
    public Department getDepartmentById(int id) {
        return departmentDao.getDepartmentById(id);
    }

    @Override
    public WebRegClass getWebRegClassById(int id) {
        return webRegClassDao.getWebRegClassById(id);
    }

    @Override
    public List<Enrollment> getActiveEnrollmentsByClassId(int id) {
        return enrollmentDao.getActiveEnrollmentsByClassId(id);
    }



    @Override
    public Lecture getLectureByClass(WebRegClass theClass) {
        return lectureDao.getLectureByClass(theClass);
    }

    @Override
    public Classroom getClassroomById(int id) {
        return classroomDao.getClassroomById(id);
    }



    @Override
    public List<WebRegClass> getAllActiveClasses() {
        return webRegClassDao.getAllActiveClasses();
    }

    @Override
    public boolean getEnrollmentStatus(int studentId, int classId) {
        return enrollmentDao.getEnrollmentStatus(studentId,classId);
    }

    @Override
    public String getClassStatus(int studentId, int classId) {
        return enrollmentDao.getStatus(studentId,classId);
    }

    @Override
    public Student getStudentById(int studentId) {
        return studentDao.getStudentById(studentId);
    }

    @Override
    public boolean checkIfStudentEligible(Student student,WebRegClass theClass) {
        if(!student.is_active()) {
            System.out.print("not active student");
            return false;
        }
        if(!theClass.is_active()){
            System.out.print("not active class");
            return false;

        }
        List<Prerequisite> prerequisites=getPrerequisitesByClass(theClass);
        // getPrerequisitesByCourseId(theClass.getCourse_id());
        if (!(prerequisites==null))
        for(Prerequisite prerequisite:prerequisites){
            if(!checkIfStudentPassCourseById(student,prerequisite)) {
//                System.out.print(prerequisite.getPre_req_id());
//                System.out.print("not pass pre-req");
                return false;
            }
        }

        if(checkIfWithdrawn(student.getId(),theClass.getId())) {
            System.out.print("withdrawn");
            return false;
        }

        if(checkIfStudentPassCourseById(student,theClass.getCourse_id())) {
            System.out.print("pass the course");
            return false;
        }

        if(checkTimeConflict(student,theClass)) {
            System.out.print("time conflict");
            return false;
        }


        return true;
    }

    @Override
    public boolean checkIfStudentPassCourseById(Student student, int courseId) {
        List<Enrollment> Enrollments= enrollmentDao.getClassesByStudentId(student.getId());
        if(Enrollments==null) return false;


        for(Enrollment enrollment: Enrollments) if(enrollment.getStatus().equals("pass")&&getCourseByClass(webRegClassDao.getClassByEnrollment(enrollment)).getId()==courseId)return true;

        return false;
    }

    private List<Prerequisite> getPrerequisitesByClass(WebRegClass theClass) {

    Course course=getCourseByClass(theClass);
    List<Prerequisite> prerequisites=prerequisiteDao.getPrerequisiteByCourse(course);
    return prerequisites;
    }

    @Override
    public boolean checkIfStudentPassCourseById(Student student, Prerequisite prerequisite) {
        List<Enrollment> Enrollments= enrollmentDao.getClassesByStudentId(student.getId());
       // System.out.print(Enrollments.size());
        if(Enrollments==null) return false;
        int courseId=prerequisiteDao.getPrereqByPre(prerequisite);
        List<WebRegClass> classes=Enrollments.stream().map(c->webRegClassDao.getClassByEnrollment(c)).collect(Collectors.toList());

        if(classes==null) return false;

        for(Enrollment enrollment: Enrollments) if(enrollment.getStatus().equals("pass")&&getCourseByClass(webRegClassDao.getClassByEnrollment(enrollment)).getId()==courseId)return true;


        return false;
    }

    @Override
    public boolean checkTimeConflict(Student student, WebRegClass theClass) {
        return false;
//        List<Enrollment> enrollments= enrollmentDao.getClassesByStudentId(student.getId());
//        List<Enrollment> Enrollments=enrollments.stream().filter(c->c.getStatus().equals("ongoing")).collect(Collectors.toList());
//        if(Enrollments==null) return false;
//
//        List<WebRegClass> classes=Enrollments.stream().map(c->webRegClassDao.getClassByEnrollment(c)).collect(Collectors.toList());
//        if(classes==null) return false;
//
//        List<Lecture> lectures=classes.stream().map(c->lectureDao.getLectureByClass(c)).collect(Collectors.toList());
//        if(lectures==null) return false;
//
//        Lecture theLecture=lectureDao.getLectureByClass(theClass);
//        if(theLecture==null) return false;
//
//        for(int i=0;i<classes.size();i++){
//            //if(classes.get(i).getSemester_id()==theClass.getSemester_id()) return false;
//
//            if(lectures.get(i).getEnd_time().before(theLecture.getStart_time())  ||
//                    lectures.get(i).getStart_time().after(theLecture.getEnd_time())||
//                    lectures.get(i).getDay_of_week()!=theLecture.getDay_of_week()
//            ) continue;
//            else return false;
//        }
//        return true;
    }

    @Override
    public List<Course> getAllCourses() {
        return courseDao.getAllCourses();
    }

    @Override
    public void createNewCourse(Course course) {
        courseDao.createNewCourse(course);
    }

    @Override
    public boolean checkIfClassIsFull(WebRegClass theClass) {
        return enrollmentDao.getEnrollmentNumByClassId(theClass.getId())==theClass.getCapacity()?true:false;

    }

    public boolean checkIfDroppable(int studentId,WebRegClass theClass){
        Semester semester=semesterDao.getSemesterByClass(theClass);
        if(enrollmentDao.getStatus(studentId,theClass.getId())==null||!enrollmentDao.getStatus(studentId,theClass.getId()).equals("ongoing")) return false;

        Calendar c = Calendar.getInstance();
        c.setTime(semester.getStart_date());
        c.add(Calendar.DATE,14);

        Date date=c.getTime();

        Date d=new java.sql.Date(System.currentTimeMillis());

        if(!d.before(date)) return false;

        return true;



    }
    public boolean checkIfWithdrawable(int studentId,WebRegClass theClass){

        Semester semester=semesterDao.getSemesterByClass(theClass);
        if(enrollmentDao.getStatus(studentId,theClass.getId())==null||!enrollmentDao.getStatus(studentId,theClass.getId()).equals("ongoing")) return false;

        Calendar c = Calendar.getInstance();
        c.setTime(semester.getStart_date());
        c.add(Calendar.DATE,14);

        Date date=c.getTime();

        Date d=new java.sql.Date(System.currentTimeMillis());

        if(!d.after(date)) return false;

        return true;
    }

    @Override
    public boolean checkIfWithdrawn(int studentId, int classId) {

        List<Enrollment> enrollments=enrollmentDao.getEnrollmentsByStudentClassId(studentId,classId);

        if(enrollments==null) return false;

        for(Enrollment enrollment:enrollments){
            if(enrollment.getStatus().equals("withdraw")) return true;
        }

        return false;
    }

    @Override
    public List<Student> getAllStudents() {
        return studentDao.getAllStudents();
    }

    @Override
    public List<WebRegClass> getAllStatusClassesbyStudentId(int studentId) {
        return webRegClassDao.getAllStatusClassesByStudentId(studentId);
    }

    @Override
    public void createNewClass(WebRegClass theClass) {
        webRegClassDao.createNewClass(theClass);
    }

    @Override
    public List<WebRegClass> getAllClasses() {
        return webRegClassDao.getAllClasses();
    }

    @Override
    public void deactivateClassById(int classId) {
        webRegClassDao.deactivateClassById(classId);
    }

    @Override
    public void activateClassById(int classId) {
        webRegClassDao.activateClassById(classId);
    }

    @Override
    public void createApplicationByStudentClassId(int id, int classId)  {
        applicationDao.createApplicationByStudentClassId(id,classId);
    }

    @Override
    public List<Application> getApplicationsByStudentId(int id) {
        return applicationDao.getApplicationByStudentId(id);
    }

    @Override
    public List<Application> getAllPendingApplications() {
        return applicationDao.getPendingApplications();
    }

    @Override
    public void rejectOrApproveByIdFeedback(String status, int applicationId, String f) {
        applicationDao.rejectOrApproveByIdFeedback(status,applicationId,f);
    }

    @Override
    public void activateStudentById(int studentId) {
        studentDao.activateStudentById(studentId);
    }

    @Override
    public void deactivateStudentById(int studentId) {
        studentDao.deactivateStudentById(studentId);
    }

    @Override
    public void passStudentByClassId(int studentId, int classId) {
        enrollmentDao.passStudentByClassId(studentId,classId);
    }

    @Override
    public void failStudentByClassId(int studentId, int classId) {
        enrollmentDao.failStudentByClassId(studentId,classId);
    }

    @Override
    public String getRequestByApplicationId(int applicationId) {
        return applicationDao.getRequestByApplicationId(applicationId);
    }

    @Override
    public int getStudentIdByApplicationId(int applicationId) {
        return applicationDao.getStudentIdByApplicationId(applicationId);
    }

    @Override
    public int getClassIdByApplicationId(int applicationId) {
        return applicationDao.getClassIdByApplicationId(applicationId);
    }

    @Override
    public int getTotalPages(int studentId,int limit) {

            List<WebRegClass> classes=getClassesbyStudentId(studentId);
            if(classes==null) return 0;
            if(classes.size()%limit==0) return classes.size()/limit;
            else return classes.size()/limit+1;

    }

    @Override
    public List<WebRegClass> getClassesbyStudentIdPageLimit(int id, int page, int limit) {
        List<WebRegClass> webRegClasses=webRegClassDao.getWebRegClassesByStudentId(id);

        return webRegClasses.subList((page-1)*limit,Math.min((page)*limit,webRegClasses.size()));
                //webRegClassDao.getWebRegClassesByStudentIdPageLimit(id,  page, limit);
    }

    @Override
    public int getTotalPagesAdmin(int limit) {
        List<Student> students=studentDao.getAllStudents();
        if(students==null) return 0;
        if(students.size()%limit==0) return students.size()/limit;
        else return students.size()/limit+1;
                //getTotalPagesAdmin(limit);
    }

    @Override
    public List<Student> getStudentsByPageLimit(int page, int limit) {
        List<Student> items = studentDao.getAllStudents();

        return items.subList((page-1)*limit,Math.min((page)*limit,items.size()));
    }

    @Override
    public String getCourseNameById(int courseId) {
        return courseDao.getCourseNameById(courseId);
    }

    @Override
    public Lecture getLectureById(int lecture_id) {
        return lectureDao.getLectureById(lecture_id);
    }

    @Override
    public void withdrawApplication(int applicationId) {
        applicationDao.withdrawApplication(applicationId);
    }

    @Override
    public Application getApplicationById(int applicationId) {
        return applicationDao.getApplicationById(applicationId);
    }

    @Override
    public void resubmitApplication(int applicationId) {
        applicationDao.resubmitApplication(applicationId);
    }

    @Override
    public Department getDepartmentByStudent(Student c) {
        return departmentDao.getDepartmentByStudent(c);
    }

    @Override
    public Course getCourseByClass(WebRegClass c) {
    return courseDao.getCourseByClass(c);
       // return null;
    }

    @Override
    public Professor getProfessorByClass(WebRegClass c) {
        return professorDao.getProfessorByClass(c);
    }

    @Override
    public Semester getSemesterByClass(WebRegClass c) {
        return semesterDao.getSemesterByClass(c);
    }

    @Override
    public Department getDepartmentByProfessor(Professor c) {
        return departmentDao.getDepartmentByProfessor(c);
    }

    @Override
    public List<Enrollment> getEnrollmentByStudentId(int id) {
        return enrollmentDao.getEnrollmentByStudentId(id);
    }
    @Override
    public List<Enrollment> getEnrollmentByStudentIdPageLimit(int id, int page, int limit) {
    List<Enrollment> items=enrollmentDao.getEnrollmentByStudentId(id);
        return items.subList((page-1)*limit,Math.min((page)*limit,items.size()));
    }

    @Override
    public WebRegClass getClassByEnrollment(Enrollment c) {

        return webRegClassDao.getClassByEnrollment(c);
    }

    @Override
    public Integer getActiveEnrollmentsByClass(WebRegClass c) {

        return enrollmentDao.getEnrollmentsByClass(c).stream().filter(s->s.getStatus().equals("ongoing")).collect(Collectors.toList()).size();
    }

    @Override
    public Department getDepartmentByCourse(Course course) {
        return departmentDao.getDepartmentByCourse(course);
    }

    @Override
    public Classroom getClassroomByClass(WebRegClass theWebRegClass) {
        return classroomDao.getClassroomByClass(theWebRegClass);
    }

    @Override
    public List<Prerequisite> getPrerequisiteByCourse(Course course) {
        return prerequisiteDao.getPrerequisiteByCourse(course);
    }

    @Override
    public Student getStudentByEnrollment(Enrollment c) {
        return studentDao.getStudentByEnrollment(c);
    }

    @Override
    public Student getStudentByApplication(Application c) {
        return studentDao.getStudentByApplication(c);

    }

    @Override
    public WebRegClass getWebRegClassByApplication(Application application) {
        return webRegClassDao.getWebRegClassByApplication(application);
    }


//    @Override
//    public Enrollment getEnrollmentByClass(WebRegClass c) {
//        return enrollmentDao.;
//    }


    @Override
    public void dropEnrollments(int studentId, int classId) {
        enrollmentDao.dropEnrollment(studentId,classId);
    }

    @Override
    public void withdrawEnrollment(int studentId, int classId) {



        enrollmentDao.withdrawEnrollment(studentId,classId);
    }

    @Override
    public void createEnrollmentApplication(int studentId, int classId) {
        applicationDao.createEnrollmentApplication(studentId,classId);
    }

    @Override
    public void addEnrollment(int studentId, int classId) {
            enrollmentDao.addEnrollment(studentId, classId);
    }


}
