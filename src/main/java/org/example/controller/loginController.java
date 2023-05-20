package org.example.controller;


import lombok.Setter;
import org.example.domain.*;
import org.example.domain.hibernate.DepartmentHibernate;
import org.example.domain.hibernate.StudentHibernate;
import org.example.domain.jdbc.StudentJdbc;
import org.example.service.LoginService;
import org.example.service.UserService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Setter
@Controller
@RequestMapping("/")
public class loginController {


    private final LoginService loginService;

    @Autowired
    UserService userService;


    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;



    @Autowired
    public loginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor =new StringTrimmerEditor(true);

        dataBinder.registerCustomEditor(String.class,stringTrimmerEditor);
    }

    @GetMapping("/login")
    public String loginPage(HttpServletRequest request) {
        HttpSession newSession= request.getSession(true);

        return "login";
    }
    // validate that we are always getting a new session after login
    @PostMapping("/login")
    public String postLogin(@RequestParam String username,
                            @RequestParam String password,
                            HttpServletRequest request) {

//        String pass_word=bCryptPasswordEncoder.encode( "admin");
//        //DepartmentHibernate departmentHibernate=sessionFactory.getCurrentSession().get(DepartmentHibernate.class,Integer.parseInt(theBindingResult.getRawFieldValue("dept_id").toString()));
//        StudentHibernate studentHibernate=new StudentHibernate("admin",
//                pass_word,"admin@gmail.com"
//                ,"admin"
//                ,""
//                ,null);
//
//        studentHibernate.setDept_id(1);
//        userService.createNewStudent(studentHibernate
//        );

        Optional<Student> possibleStudent = loginService.validateLogin(username, password);
        if(possibleStudent.isPresent()) {
//            Student admin=new Student("admin","admin","admin@gmail.com","admin","",1,true);
//            userService.createNewStudent(admin);
            HttpSession oldSession = request.getSession(false);
            // invalidate old session if it exists
            if (oldSession != null) oldSession.invalidate();

            // generate new session
            HttpSession newSession = request.getSession(true);

            // store user details in session
            newSession.setAttribute("student", possibleStudent.get());
            int limit=5;
            int page=1;
//            System.out.print(possibleStudent.get().is_admin());
           // newSession.setAttribute("professors",classes);
            if(!possibleStudent.get().is_admin()) {

                Student student= (Student) request.getSession(false).getAttribute("student");

                HttpSession curSession= request.getSession(true);
                List<Enrollment> enrollments=userService.getEnrollmentByStudentId( student.getId() );
                List<WebRegClass> classes= null;
                //userService.getClassesbyStudentIdPageLimit(student.getId(),page,limit);
                //for(WebRegClass webRegClass:classes) System.out.print(webRegClass.getId());

                List<Course> courses=null;
                List<Professor> professors=null;
                List<Semester> semesters=null;
                List<Department> departments=null;
                //List<String> enrollments=null;
                if(!(enrollments ==null))
                {
                    classes= enrollments.stream().map(c->userService.getClassByEnrollment(c)).collect(Collectors.toList());

                    courses=classes.stream().map(c->userService.getCourseByClass(c)).collect(Collectors.toList());
                           // classes.stream().map(c->userService.getCourseById(c.getCourse_id())).collect(Collectors.toList());
                    professors=classes.stream().map(c->userService.getProfessorByClass(c)).collect(Collectors.toList());
                            //classes.stream().map(c->userService.getProfessorById(c.getProfessor_id())).collect(Collectors.toList());
                    semesters=classes.stream().map(c->userService.getSemesterByClass(c)).collect(Collectors.toList());
                            //classes.stream().map(c->userService.getSemesterById(c.getSemester_id())).collect(Collectors.toList());
                    departments=professors.stream().map(c->userService.getDepartmentByProfessor(c)).collect(Collectors.toList());

                    //professors.stream().map(c->userService.getDepartmentById(c.getDept_id())).collect(Collectors.toList());
                    //enrollments=classes.stream().map(c-> userService.getEnrollmentStatus(student.getId(), c.getId())).collect(Collectors.toList());

                }


                //page=1;
                int totalPages=userService.getTotalPages(student.getId(),limit);
                System.out.print(totalPages);

                curSession.setAttribute("totalPages",totalPages);
                //curSession.setAttribute("userService",userService);
                curSession.setAttribute("limit",limit);
                curSession.setAttribute("page",page);
                curSession.setAttribute("classes",classes);
                curSession.setAttribute("courses",courses);
                curSession.setAttribute("enrollments",enrollments);
                curSession.setAttribute("professors",professors);
                curSession.setAttribute("semesters",semesters);
                curSession.setAttribute("departments",departments);


                return "home";
            }
            else {

                if(!((Student)request.getSession(false).getAttribute("student")).is_admin() ) return "login";
                Student student= (Student) request.getSession(false).getAttribute("student");
                HttpSession curSession=request.getSession(true);


                int totalPages=userService.getTotalPagesAdmin(limit);

                List<Student> students=userService.getStudentsByPageLimit(page,limit);
                List<Department> departments=

                        students.stream().map(c->userService.getDepartmentById(c.getId())).collect(Collectors.toList());
                Session session=sessionFactory.openSession();

                curSession.setAttribute("totalPages", totalPages);
                curSession.setAttribute("limit",limit);
                curSession.setAttribute("page", page);
                curSession.setAttribute("student", student);
                curSession.setAttribute("students", students);
                curSession.setAttribute("departments", departments);
                curSession.setAttribute("student", student);
                curSession.setAttribute("session", session);
//                curSession.setAttribute("totalPages", totalPages);
//                curSession.setAttribute("limit",limit);
//                curSession.setAttribute("page", page);
//                curSession.setAttribute("student", student);
//                curSession.setAttribute("students", students);
//                curSession.setAttribute("departments", departments);

                return "admin_home";

            }
        } else {
            return "login";
        }
    }


    @GetMapping("/signup")
    public String signup(Model theModel) {

        theModel.addAttribute("student", new StudentJdbc());
        return "signup";
    }

    @PostMapping("/signup")
    public String postSignup(@ModelAttribute("student") StudentHibernate theStudent, BindingResult theBindingResult) {
//        if(!theBindingResult.getRawFieldValue("password").equals(theBindingResult.getRawFieldValue("repeat password")))
//            return "signup";

        if(theBindingResult.getRawFieldValue("username").toString().equalsIgnoreCase("admin") ) return "signup";

        String pass_word=bCryptPasswordEncoder.encode( theBindingResult.getRawFieldValue("password").toString());
        //DepartmentHibernate departmentHibernate=sessionFactory.getCurrentSession().get(DepartmentHibernate.class,Integer.parseInt(theBindingResult.getRawFieldValue("dept_id").toString()));
StudentHibernate studentHibernate=new StudentHibernate((String) theBindingResult.getRawFieldValue("username"),
        pass_word,theBindingResult.getRawFieldValue("email").toString()
        ,theBindingResult.getRawFieldValue("firstName").toString()
        ,theBindingResult.getRawFieldValue("lastName").toString()
        ,null);

        studentHibernate.setDept_id(Integer.parseInt(theBindingResult.getRawFieldValue("dept_id").toString()));
        userService.createNewStudent(studentHibernate
        );


        System.out.println("Binding result: " + theBindingResult.getRawFieldValue("password") );


        if(theBindingResult.hasErrors()) return
                "signup";
        else return "login";
    }


    @GetMapping("/signout")
    public String signout(Model theModel,HttpServletRequest request) {

        HttpSession newSession=request.getSession(true);
        System.out.print(request.getRequestURI());
        return "login";
    }

}
