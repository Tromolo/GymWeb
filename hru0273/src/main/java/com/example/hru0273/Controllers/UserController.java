package com.example.hru0273.Controllers;

import com.example.hru0273.Repositories.TrainerRepository;
import com.example.hru0273.Repositories.TrainingPlanRepository;
import com.example.hru0273.Repositories.UserRepository;
import com.example.hru0273.Repositories.UserTrainerRepository;
import com.example.hru0273.Trainer;
import com.example.hru0273.User;
import com.example.hru0273.UserTrainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
@Controller
@RequestMapping("/users")
public class UserController {
    private static final Logger logger = LogManager.getLogger(TrainingPlanController.class);
    private final UserRepository userRepository;
    private final UserTrainerRepository userTrainerRepository;
    private final TrainerRepository trainerRepository;

    @Autowired
    public UserController(UserRepository userRepository, UserTrainerRepository userTrainerRepository,TrainerRepository trainerRepository) {
        this.userRepository = userRepository;
        this.userTrainerRepository = userTrainerRepository;
        this.trainerRepository = trainerRepository;
    }

    @PostMapping("/batch")
    public ResponseEntity<String> createUsers(@RequestBody List<User> users) {
        try {
            userRepository.saveAll(users);
            userRepository.flush();
            return ResponseEntity.ok("Users created successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create users: " + e.getMessage());
        }
    }

    @GetMapping
    public String showAllUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/{userId}/trainer")
    public String showTrainer(@PathVariable("userId") Long userId, Model model) {
        UserTrainer userTrainer = userTrainerRepository.findByUserId(userId);

        if (userTrainer != null) {
            Trainer trainer = userTrainer.getTrainer();
            model.addAttribute("trainer", trainer);
        } else {
            model.addAttribute("error", "No trainer found for this user.");
        }

        return "userTrainer";
    }

    @PostMapping("/assignTrainer")
    public String assignTrainer(@RequestParam("userId") Long userId, @RequestParam("trainerId") Long trainerId, Model model) {
        User user = userRepository.findById(userId).orElse(null);
        Trainer trainer = trainerRepository.findById(trainerId).orElse(null);

        if (user != null && trainer != null) {
            user.setTrainer(trainer);
            userRepository.save(user);
            userRepository.flush();
            logger.info("User {} assigned to trainer {}", user.getName(), trainer.getName());
            model.addAttribute("message", "User assigned to trainer successfully!");
        } else {
            logger.warn("Failed to assign user to trainer. User or trainer is null.");
            model.addAttribute("error", "Failed to assign user to trainer.");
        }

        return "redirect:/trainers";
    }

    @PostMapping("/trainer/{id}")
    public String showTrainerDetails(@PathVariable("id") Long id, Model model) {
        Trainer trainer = trainerRepository.findById(id).orElse(null);
        model.addAttribute("trainer", trainer);
        return "trainerDetails";
    }

}
