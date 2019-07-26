/* When using separate controller for Security and MainContent,
 * be sure to update MainContact controller with
 * proper UserService functions
 *
 * You need to create an object of UserService class,
 * then be sure to assign a user to each instatiation of the Java Bean
 *
 * over and out-- */

package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.LinkedHashSet;
import java.util.Set;

@Controller
public class SecurityController {
    @Autowired
    CourseRepository courseRepository;

    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationPage(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/register")
    public String processRegistrationPage(@Valid
                                          @ModelAttribute("user") User user, BindingResult result,
                                          Model model) {
        model.addAttribute("user", user);
        if (result.hasErrors()) {
            return "registration";
        } else {
            userService.saveUser(user);
            model.addAttribute("message", "User Account Created");
        }
        return "index";
    }

    /* taken from:
     * https://www.baeldung.com/get-user-in-spring-security */
    @GetMapping("/username")
    @ResponseBody
    public String currentUsername(Principal principal) {
        return principal.getName();
    }
//    @GetMapping("/username")
//    @ResponseBody
//    public String currentUsernameSimple(HttpServletRequest request){
//        Principal principal = request.getUserPrincipal();
//        return principal.getName();
//    }

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/secure")
    public String secure(Principal principal, Model model) {
        User myuser = ((CustomUserDetails)
                ((UsernamePasswordAuthenticationToken) principal)
                        .getPrincipal()).getUser();
        model.addAttribute("myuser", myuser);
        return "secure";
    }

    /* Addition for separate log out page */
    @RequestMapping("/logoutconfirm")
    public String logoutconfirm() {
        return "logoutconfirm";
    }

    //This is the part I put in ---------------------------------------------------------------------------------------
    @RequestMapping("/afterlogin")
    public String courseList(Model model) {
        model.addAttribute("courses", courseRepository.findAllByUsers(userService.getUser()));
        model.addAttribute("subjects", subjectRepository.findAll());
        return "homepage";
    }

    @GetMapping("/add")
    public String courseForm(Model model) {

        model.addAttribute("course", new Course());
        model.addAttribute("subjects", subjectRepository.findAll());
        return "courseform";
    }

    @PostMapping("/process")
    public String processForm(@Valid Course course, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("subjects", subjectRepository.findAll());
            return "courseform";
        }
        User currentUser = userService.getUser();
        Set<User> usersSet = new LinkedHashSet<>();
        usersSet.add(currentUser);
        course.setUsers(usersSet);

        courseRepository.save(course);
        return "redirect:/afterlogin";
    }

    @RequestMapping("/addsubject")
    public String processSubject(Model model) {

        model.addAttribute("subject", new Subject());
        return "subjectform";
    }

    @PostMapping("/processsubject")
    public String processForm(@Valid Subject subject, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "subjectform";
        }
        if (subjectRepository.findBysubjectName(subject.getSubjectName()) != null) {

            model.addAttribute("message", "You already have a subject");
            return "subjectform";
        }



        subjectRepository.save(subject);
        return "redirect:/afterlogin";
    }

    @RequestMapping("/detail/{id}")
    public String showCoursesOfASubjectDetail(@PathVariable("id") long id, Model model) {

        model.addAttribute("subjects", subjectRepository.findById(id).get());
        return "CoursesOfASubjectDetail";
    }

    @RequestMapping("/detail/course/{id}")
    public String showOneCourseDetail(@PathVariable("id") long id, Model model) {
//        model.addAttribute("catergory", catergoryRepository.findById(id).get());
        model.addAttribute("course", courseRepository.findById(id).get());
        return "OneCourseDetail";
    }

//    @RequestMapping("/update/{id}")
//    public String updateCar(@PathVariable("id") long id, Model model) {
//        model.addAttribute("course", courseRepository.findById(id).get());
//        model.addAttribute("subjects", subjectRepository.findAll());
//
//        return "changecourseinfo";
//    }
//
//    @PostMapping("/update/changecourse/{id}")
//    public String changeCarForm(@Valid Course course, BindingResult result, Model model) {
//        if (result.hasErrors()) {
//            model.addAttribute("subjects", subjectRepository.findAll());
//            return "changecourseinfo";
//        }
//        courseRepository.save(course);
//        return "redirect:/afterlogin";
//    }

}
