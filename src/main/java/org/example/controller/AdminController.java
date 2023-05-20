package org.example.controller;


import org.example.domain.*;
import org.example.domain.hibernate.ApplicationHibernate;
import org.example.domain.jdbc.CourseJdbc;
import org.example.domain.jdbc.WebRegClassJdbc;
import org.example.service.UserService;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    UserService userService;



    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor =new StringTrimmerEditor(true);

        dataBinder.registerCustomEditor(String.class,stringTrimmerEditor);
    }
    @GetMapping("/home/{page}/{limit}")
    public String home(@PathVariable int page,@PathVariable int limit, HttpServletRequest request) {
        if(!((Student)request.getSession(false).getAttribute("student")).is_admin() ) return "login";
        Student student= (Student) request.getSession(false).getAttribute("student");
        HttpSession curSession=request.getSession(true);


        int totalPages=userService.getTotalPagesAdmin(limit);

        List<Student> students=userService.getStudentsByPageLimit(page,limit);
        List<Department> departments=

                students.stream().map(c->userService.getDepartmentById(c.getId())).collect(Collectors.toList());
        //Session session=sessionFactory.openSession();

        curSession.setAttribute("totalPages", totalPages);
        curSession.setAttribute("limit",limit);
        curSession.setAttribute("page", page);
        curSession.setAttribute("student", student);
        curSession.setAttribute("students", students);
        curSession.setAttribute("departments", departments);
        curSession.setAttribute("student", student);

        return "admin_home";
    }

    @GetMapping("/course/all")
    public String courseManagement(Model theModel, HttpServletRequest request) {
        if(!((Student)request.getSession(false).getAttribute("student")).is_admin() ) return "login";
        theModel.addAttribute("course", new CourseJdbc());
        theModel.addAttribute("theClass", new WebRegClassJdbc());
        Student student= (Student) request.getSession(false).getAttribute("student");

        HttpSession curSession=request.getSession(false);
        List<Course> courses=userService.getAllCourses();
       List<Department> departments=courses.stream().map(c->userService.getDepartmentByCourse(c)).collect(Collectors.toList());
        curSession.setAttribute("student", student);
        curSession.setAttribute("courses", courses);
        curSession.setAttribute("departments", departments);

        return "course_management";
    }

    @PostMapping("/course")
    public String postSignup(@ModelAttribute("course") CourseJdbc course,  BindingResult theBindingResult,HttpServletRequest request) {
        if(!((Student)request.getSession(false).getAttribute("student")).is_admin() ) return "login";
        //HttpSession curSession=request.getSession(false);
        userService.createNewCourse(new CourseJdbc( theBindingResult.getRawFieldValue("course_name").toString(),
                theBindingResult.getRawFieldValue("course_code").toString()

                ,Integer.parseInt(theBindingResult.getRawFieldValue("dept_id").toString())
                ,theBindingResult.getRawFieldValue("description").toString()

        ));

//        Student student= (Student) request.getSession(false).getAttribute("student");
//
//        HttpSession curSession=request.getSession(true);
//        List<Course> courses=userService.getAllCourses();
//        List<Department> departments=courses.stream().map(c->userService.getDepartmentByCourse(c)).collect(Collectors.toList());
//        curSession.setAttribute("student", student);
//        curSession.setAttribute("courses", courses);
//        curSession.setAttribute("departments", departments);
//
//        return "course_management";
        return "success";
    }

    @PostMapping("/class")
    public String addClass(@ModelAttribute("theClass") WebRegClassJdbc theClass, BindingResult theBindingResult,HttpServletRequest request) {
        if(!((Student)request.getSession(false).getAttribute("student")).is_admin() ) return "login";

        userService.createNewClass(new WebRegClassJdbc( Integer.parseInt(theBindingResult.getRawFieldValue("course_id").toString()),
                Integer.parseInt(theBindingResult.getRawFieldValue("semester_id").toString())

                ,Integer.parseInt(theBindingResult.getRawFieldValue("professor_id").toString())
                ,Integer.parseInt(theBindingResult.getRawFieldValue("classroom_id").toString())
                ,Integer.parseInt(theBindingResult.getRawFieldValue("lecture_id").toString()),
                Integer.parseInt(theBindingResult.getRawFieldValue("capacity").toString())

        )
        );
        return "success";

    }

    @PostMapping("/class/{classId}")
    public String addClass(@PathVariable int classId,HttpServletRequest request){
        if(!((Student)request.getSession(false).getAttribute("student")).is_admin() ) return "login";
        HttpSession curSession = request.getSession(false);
        String button = request.getParameter("button");
        System.out.print(button);
        try {
            Student student = (Student) curSession.getAttribute("student");
            if(!((Student)request.getSession(false).getAttribute("student")).is_admin() ) return "login";

            if(button.equals("deactivate")) userService.deactivateClassById(classId);
            else if(button.equals("activate")) userService.activateClassById(classId);
            List<WebRegClass> classes = userService.getAllClasses();
            List<Course> courses = classes.stream().map(c -> userService.getCourseByClass(c)).collect(Collectors.toList());
            List<Professor> professors = classes.stream().map(c -> userService.getProfessorByClass(c)).collect(Collectors.toList());
            List<Semester> semesters = classes.stream().map(c -> userService.getSemesterByClass(c)).collect(Collectors.toList());
            List<Department> departments = professors.stream().map(c -> userService.getDepartmentByProfessor(c)).collect(Collectors.toList());
            List<Integer> en_num = classes.stream().map(c -> userService.getActiveEnrollmentsByClass(c) == null ? 0 : userService.getActiveEnrollmentsByClassId(c.getId()).size()).collect(Collectors.toList());
            //List<Enrollment> enrollments = classes.stream().map(c -> userService.getEnrollmentByClassStudentId(c,student.getId())).collect(Collectors.toList());

//            List<String> en_status = classes.stream().map(c -> userService.getEnrollmentStatus(student.getId(), c.getId()) ? "YES" : "NO").collect(Collectors.toList());


            curSession.setAttribute("classes", classes);
            curSession.setAttribute("courses", courses);
            curSession.setAttribute("professors", professors);
            curSession.setAttribute("semesters", semesters);

            curSession.setAttribute("departments", departments);
            curSession.setAttribute("en_num", en_num);
        }




        catch (NullPointerException e){
            e.printStackTrace();
        }
        return "admin_class_management";
    }


//    @GetMapping("/class/{classId}/{is_active}")
//    public String flipAlive(@PathVariable int classId, @PathVariable boolean is_active,@RequestParam("button") String button, HttpServletRequest request){
//        HttpSession curSession = request.getSession(false);
//
//        try {
//            Student student = (Student) curSession.getAttribute("student");
//            if(!((Student)request.getSession(false).getAttribute("student")).is_admin() ) return "login";
//
//            if(is_active) userService.deactivateClassById(classId);
//            else if(!is_active) userService.activateClassById(classId);
//            List<WebRegClass> classes = userService.getAllClasses();
//            List<Course> courses = classes.stream().map(c -> userService.getCourseById(c.getCourse_id())).collect(Collectors.toList());
//            List<Professor> professors = classes.stream().map(c -> userService.getProfessorById(c.getProfessor_id())).collect(Collectors.toList());
//            List<Semester> semesters = classes.stream().map(c -> userService.getSemesterById(c.getSemester_id())).collect(Collectors.toList());
//            List<Department> departments = professors.stream().map(c -> userService.getDepartmentById(c.getDept_id())).collect(Collectors.toList());
//            List<Integer> en_num = classes.stream().map(c -> userService.getActiveEnrollmentsByClassId(c.getId()) == null ? 0 : userService.getActiveEnrollmentsByClassId(c.getId()).size()).collect(Collectors.toList());
//            curSession.setAttribute("classes", classes);
//            curSession.setAttribute("courses", courses);
//            curSession.setAttribute("professors", professors);
//            curSession.setAttribute("semesters", semesters);
//
//            curSession.setAttribute("departments", departments);
//            curSession.setAttribute("en_num", en_num);
//        }
//
//        catch (NullPointerException e){
//            e.printStackTrace();
//        }
//        return "admin_class_management";
//    }

    @GetMapping("/class/all")
    public String class_management_Page(HttpServletRequest request) {
        if(!((Student)request.getSession(false).getAttribute("student")).is_admin() ) return "login";
        HttpSession curSession = request.getSession();

        List<WebRegClass> classes = userService.getAllClasses();
        try {
            Student student = (Student) curSession.getAttribute("student");
            if(!((Student)request.getSession(false).getAttribute("student")).is_admin() ) return "login";

            List<Course> courses = classes.stream().map(c -> userService.getCourseByClass(c)).collect(Collectors.toList());
            List<Professor> professors = classes.stream().map(c -> userService.getProfessorByClass(c)).collect(Collectors.toList());
            List<Semester> semesters = classes.stream().map(c -> userService.getSemesterByClass(c)).collect(Collectors.toList());
            List<Department> departments = professors.stream().map(c -> userService.getDepartmentByProfessor(c)).collect(Collectors.toList());
            List<Integer> en_num = classes.stream().map(c -> userService.getActiveEnrollmentsByClass(c) == null ? 0 : userService.getActiveEnrollmentsByClassId(c.getId()).size()).collect(Collectors.toList());
            //List<Enrollment> enrollments = classes.stream().map(c -> userService.getEnrollmentByClassStudentId(c,student.getId())).collect(Collectors.toList());

//            List<String> en_status = classes.stream().map(c -> userService.getEnrollmentStatus(student.getId(), c.getId()) ? "YES" : "NO").collect(Collectors.toList());


            curSession.setAttribute("classes", classes);
            curSession.setAttribute("courses", courses);
            curSession.setAttribute("professors", professors);
            curSession.setAttribute("semesters", semesters);

            curSession.setAttribute("departments", departments);
            curSession.setAttribute("en_num", en_num);
            //curSession.setAttribute("en_status", en_status==null? null:en_status);
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
        return "admin_class_management";
    }

        @GetMapping("/student/{studentId}")
    public String studentPage(@PathVariable int studentId,
                            HttpServletRequest request) {
            if(!((Student)request.getSession(false).getAttribute("student")).is_admin() ) return "login";
        HttpSession curSession= request.getSession(false);
        Student student= (Student) request.getSession(false).getAttribute("student");

        if(!student.is_admin()) return "no_access";


        Student theStudent=userService.getStudentById(studentId);

        Department department=userService.getDepartmentById(theStudent.getDept_id());
            List<WebRegClass> classes=userService.getAllStatusClassesbyStudentId(studentId);
            List<Course> courses=null;
            List<Professor> professors=null;
            List<Semester> semesters=null;
            List<Department> departments=null;
            List<String> en_status=null;
            if(!(classes ==null)) {
                courses = classes.stream().map(c -> userService.getCourseByClass(c)).collect(Collectors.toList());
                professors = classes.stream().map(c -> userService.getProfessorByClass(c)).collect(Collectors.toList());
                semesters = classes.stream().map(c -> userService.getSemesterByClass(c)).collect(Collectors.toList());
                departments = professors.stream().map(c -> userService.getDepartmentByProfessor(c)).collect(Collectors.toList());
                en_status = classes.stream().map(c -> userService.getClassStatus(theStudent.getId(), c.getId())).collect(Collectors.toList());
            }
            curSession.setAttribute("theStudent", theStudent);
        curSession.setAttribute("department", department);
        curSession.setAttribute("classes", classes);
        curSession.setAttribute("courses", courses);
        curSession.setAttribute("professors", professors);
        curSession.setAttribute("semesters", semesters);

        curSession.setAttribute("departments", departments);

        curSession.setAttribute("en_status", en_status==null? null:en_status);

        return "student_profile";

    }

    @PostMapping("/student/{studentId}")
    public String AcDeacStudent(@PathVariable int studentId,HttpServletRequest request){
        if(!((Student)request.getSession(false).getAttribute("student")).is_admin() ) return "login";
        HttpSession curSession = request.getSession(false);
        String button = request.getParameter("button");
        System.out.print(button);

            if(button.equals("activate"))
            userService.activateStudentById(studentId);

            else if(button.equals("deactivate"))
                userService.deactivateStudentById(studentId);

        Student student= (Student) request.getSession(false).getAttribute("student");
        int limit=Integer.parseInt(request.getSession(false).getAttribute("limit").toString());
        int page=Integer.parseInt(request.getSession(false).getAttribute("page").toString());
        HttpSession newSession=request.getSession(true);
        List<Student> students=userService.getStudentsByPageLimit(page,limit);
        List<Department> departments=students.stream().map(c->userService.getDepartmentById(c.getId())).collect(Collectors.toList());
        newSession.setAttribute("student", student);
        newSession.setAttribute("students", students);
        newSession.setAttribute("departments", departments);
            return "admin_home";
    }
    @GetMapping("/application/all")
    public String applicationManagement(Model theModel,
            HttpServletRequest request) {
        if(!((Student)request.getSession(false).getAttribute("student")).is_admin() ) return "login";
        HttpSession curSession = request.getSession(false);
        Student student = (Student) curSession.getAttribute("student");


        HttpSession newSession = request.getSession(true);
        theModel.addAttribute("application", new ApplicationHibernate());
        newSession.setAttribute("student",student);

        List<Application> applications= userService.getAllPendingApplications();

        List<Course> courses=null;
        List<Professor> professors=null;
        List<Semester> semesters=null;
        List<Department> departments=null;
        List<WebRegClass> classes=null;

        List<Student> students=null;

        if(!(applications ==null))
        {
            classes=applications.stream().map(c->userService.getWebRegClassByApplication(c)).collect(Collectors.toList());

            students=applications.stream().map(c->userService.getStudentByApplication(c)).collect(Collectors.toList());

            courses=classes.stream().map(c->userService.getCourseByClass(c)).collect(Collectors.toList());
            semesters=classes.stream().map(c->userService.getSemesterByClass(c)).collect(Collectors.toList());
            professors=classes.stream().map(c->userService.getProfessorByClass(c)).collect(Collectors.toList());
            departments=professors.stream().map(c->userService.getDepartmentByProfessor(c)).collect(Collectors.toList());
        }
        String s1="Pending";
        String s2="Reject";
        String s3="Withdraw";
        newSession.setAttribute("s1",s1);
        newSession.setAttribute("s2",s2);
        newSession.setAttribute("s3",s3);
        newSession.setAttribute("applications",applications);
        newSession.setAttribute("classes",classes);
        newSession.setAttribute("students",students);
        newSession.setAttribute("courses",courses);
        newSession.setAttribute("semesters",semesters);
        newSession.setAttribute("departments",departments);



        return "admin_application_management";

    }

    @PostMapping("/application/{applicationId}")
    public String RejectOrApprove(Model theModel,
            //@ModelAttribute("") String s,
                                  @PathVariable int applicationId,
                                        HttpServletRequest request) {
        if(!((Student)request.getSession(false).getAttribute("student")).is_admin() ) return "login";
        String f=request.getParameter("text-input");
        //String feedback=((Application)theModel.getAttribute("application")).getFeedback();
        String button=request.getParameter("submit-button");


        if(button.equals("Reject") ||button.equals("Approve")) {

            if(button.equals("Approve")&&userService.getRequestByApplicationId(applicationId).equals("Add"))
                userService.addEnrollment(userService.getStudentIdByApplicationId(applicationId),userService.getClassIdByApplicationId(applicationId));

           else if(button.equals("Approve")&&userService.getRequestByApplicationId(applicationId).equals("Withdraw"))
            userService.withdrawEnrollment(userService.getStudentIdByApplicationId(applicationId),userService.getClassIdByApplicationId(applicationId));



            String status=button;
            userService.rejectOrApproveByIdFeedback(status,applicationId,f);
        }
        System.out.print(f);




        return "success";
    }


    @PostMapping("/student/{studentId}/class/{classId}")
    public String passOrFail(@PathVariable int studentId,
                             @PathVariable int classId,
                                  //@ModelAttribute("") String s,

                                  HttpServletRequest request) {
        if(!((Student)request.getSession(false).getAttribute("student")).is_admin() ) return "login";
        HttpSession curSession = request.getSession(false);
        String button = request.getParameter("button");
        System.out.print(button);

        if(button.equals("pass"))
            userService.passStudentByClassId(studentId,classId);

        else if(button.equals("fail"))
            userService.failStudentByClassId(studentId,classId);;

        Student theStudent=userService.getStudentById(studentId);

        Department department=userService.getDepartmentById(theStudent.getDept_id());
        List<WebRegClass> classes=userService.getAllStatusClassesbyStudentId(studentId);
        List<Course> courses=null;
        List<Professor> professors=null;
        List<Semester> semesters=null;
        List<Department> departments=null;
        List<String> en_status=null;
        if(!(classes ==null)) {
            courses = classes.stream().map(c -> userService.getCourseByClass(c)).collect(Collectors.toList());
            professors = classes.stream().map(c -> userService.getProfessorByClass(c)).collect(Collectors.toList());
            semesters = classes.stream().map(c -> userService.getSemesterByClass(c)).collect(Collectors.toList());
            departments = professors.stream().map(c -> userService.getDepartmentByProfessor(c)).collect(Collectors.toList());
            en_status = classes.stream().map(c -> userService.getClassStatus(theStudent.getId(), c.getId())).collect(Collectors.toList());
        }
        curSession.setAttribute("theStudent", theStudent);
        curSession.setAttribute("department", department);
        curSession.setAttribute("classes", classes);
        curSession.setAttribute("courses", courses);
        curSession.setAttribute("professors", professors);
        curSession.setAttribute("semesters", semesters);

        curSession.setAttribute("departments", departments);

        curSession.setAttribute("en_status", en_status==null? null:en_status);

        return "student_profile";

    }
}
