package com.example.hru0273.Controllers;

import com.example.hru0273.Repositories.TrainerRepository;
import com.example.hru0273.Repositories.TrainingPlanRepository;
import com.example.hru0273.Repositories.UserRepository;
import com.example.hru0273.Repositories.UserTrainerRepository;
import com.example.hru0273.Trainer;
import com.example.hru0273.TrainingPlan;
import com.example.hru0273.User;
import com.example.hru0273.UserTrainer;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/trainers")
public class TrainerController {

    private final TrainerRepository trainerRepository;
    private final UserTrainerRepository userTrainerRepository;
    private final UserRepository userRepository;
    private final TrainingPlanRepository trainingPlanRepository;
    private final HttpSession session;

    @Autowired
    public TrainerController(TrainerRepository trainerRepository, UserTrainerRepository userTrainerRepository, UserRepository userRepository, TrainingPlanRepository trainingPlanRepository, HttpSession session) {
        this.trainerRepository = trainerRepository;
        this.userTrainerRepository = userTrainerRepository;
        this.userRepository = userRepository;
        this.trainingPlanRepository = trainingPlanRepository;
        this.session = session;
    }


    @GetMapping
    public String showAllTrainers(Model model) {
        List<Trainer> trainers = trainerRepository.findAll();
        List<User> users = userRepository.findAll();
        model.addAttribute("trainers", trainers);
        model.addAttribute("users", users);
        return "trainers";
    }


    @PostMapping("/batch")
    public ResponseEntity<String> createTrainers(@RequestBody List<Trainer> trainers) {
        try {
            trainerRepository.saveAll(trainers);
            trainerRepository.flush();
            return ResponseEntity.ok("Trainers created successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create trainers: " + e.getMessage());
        }
    }

    @PostMapping("/training-plans/create")
    public String assignPlan(@RequestParam("userId") Long userId, @RequestParam("planId") Long planId, Model model) {
        User user = userRepository.findById(userId).orElse(null);
        TrainingPlan trainingPlan = trainingPlanRepository.findById(planId).orElse(null);
        Trainer trainer = (Trainer) session.getAttribute("loggedInUser");

        if (user != null && trainingPlan != null && trainer != null) {
            UserTrainer userTrainer = new UserTrainer(user, trainer, trainingPlan);
            userTrainerRepository.save(userTrainer);
            model.addAttribute("message", "Training plan assigned successfully!");
        } else {
            model.addAttribute("error", "Failed to assign training plan.");
        }

        return "redirect:/trainers";
    }


}

