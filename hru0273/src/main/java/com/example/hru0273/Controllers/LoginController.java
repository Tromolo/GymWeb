package com.example.hru0273.Controllers;

import com.example.hru0273.Repositories.TrainerRepository;
import com.example.hru0273.Repositories.UserRepository;
import com.example.hru0273.Trainer;
import com.example.hru0273.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.List;

@Controller
public class LoginController {

    private HttpSession session;
    private final TrainerRepository trainerRepository;
    private final UserRepository userRepository;

    @Autowired
    public LoginController(TrainerRepository trainerRepository, UserRepository userRepository, HttpSession session) {
        this.trainerRepository = trainerRepository;
        this.userRepository = userRepository;
        this.session = session;
    }

    @GetMapping(value = "/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping(value = "/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, Model model) {


        Trainer trainer = trainerRepository.findByName(username);
        if (trainer != null) {
            if (password.equals(trainer.getPassword())) {
                session.setAttribute("loggedInUser", trainer);
                List<User> users = userRepository.findAll();
                model.addAttribute("users", users);
                return "redirect:/users";
            } else {
                model.addAttribute("error", "Username or password is incorrect.");
                return "login";
            }
        } else {
            User user = userRepository.findByName(username);
            if (user != null && password.equals(user.getPassword())) {
                session.setAttribute("loggedInUser", user);
                List<Trainer> trainers = trainerRepository.findAll();
                model.addAttribute("trainers", trainers);
                return "redirect:/trainers";
            } else {
                model.addAttribute("error", "Username or password is incorrect.");
                return "login";
            }
        }
    }

    @PostMapping("/addUser")
    public String addUser(@RequestParam("name") String name,
                          @RequestParam("email") String email,
                          @RequestParam("password") String password,
                          @RequestParam("age") int age,
                          @RequestParam("gender") String gender,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        try {
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            user.setAge(age);
            user.setGender(gender);

            userRepository.save(user);
            redirectAttributes.addFlashAttribute("success", "User successfully registered!");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "User registration failed. Please try again.");
            return "redirect:/login";
        }
    }
}
