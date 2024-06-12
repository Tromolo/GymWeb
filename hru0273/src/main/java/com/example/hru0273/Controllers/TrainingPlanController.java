package com.example.hru0273.Controllers;

import com.example.hru0273.Repositories.UserRepository;
import com.example.hru0273.Repositories.UserTrainerRepository;
import com.example.hru0273.Trainer;
import com.example.hru0273.TrainingPlan;
import com.example.hru0273.UserTrainer;
import com.example.hru0273.Repositories.TrainingPlanRepository;
import com.example.hru0273.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/training-plans")
public class TrainingPlanController {
    private static final Logger logger = LogManager.getLogger(TrainingPlanController.class);
    private final TrainingPlanRepository trainingPlanRepository;
    private final UserRepository userRepository;
    private final HttpSession session;

    private final UserTrainerRepository userTrainerRepository;


    @Autowired
    public TrainingPlanController(TrainingPlanRepository trainingPlanRepository, UserRepository userRepository,UserTrainerRepository userTrainerRepository, HttpSession session) {
        this.trainingPlanRepository = trainingPlanRepository;
        this.userRepository = userRepository;
        this.session = session;
        this.userTrainerRepository = userTrainerRepository;
    }



    @PostMapping("/batch")
    public ResponseEntity<String> createTrainingPlans(@RequestBody List<TrainingPlan> trainingPlans) {
        try {
            trainingPlanRepository.saveAll(trainingPlans);
            trainingPlanRepository.flush();
            return ResponseEntity.ok("trainingPlans created successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create trainingPlans: " + e.getMessage());
        }
    }

    @GetMapping
    public String showAllTrainingPlans(Model model) {
        Object loggedInUser = session.getAttribute("loggedInUser");

        if (loggedInUser instanceof Trainer trainer) {
            logger.info("Fetched Trainer from session: {}", trainer.getName());
            List<TrainingPlan> trainingPlans = trainingPlanRepository.findAll();
            model.addAttribute("trainingplans", trainingPlans);
        } else if (loggedInUser instanceof User user) {
            logger.info("Fetched User from session: {}", user.getName());
            List<UserTrainer> userTrainers = userTrainerRepository.findByUser(user);
            List<TrainingPlan> trainingPlans = userTrainers.stream()
                    .map(UserTrainer::getTrainingPlan)
                    .collect(Collectors.toList());
            model.addAttribute("trainingplans", trainingPlans);
        }

        return "trainingplans";
    }


    @GetMapping("/create")
    public String showCreateForm(Model model) {
        Trainer trainer = (Trainer) session.getAttribute("loggedInUser");
        List<User> users = userRepository.findByTrainer(trainer);
        model.addAttribute("trainingPlan", new TrainingPlan());
        model.addAttribute("users", users);
        return "create-training-plan";
    }

    @PostMapping("/create")
    public String createTrainingPlan(@ModelAttribute TrainingPlan trainingPlan, @RequestParam("userId") Long userId) {
        logger.debug("createTrainingPlan called with userId: {}", userId);

        User user = userRepository.findById(userId).orElse(null);
        logger.info("Fetched User: {}", user);

        Trainer trainer = (Trainer) session.getAttribute("loggedInUser");
        logger.info("Fetched Trainer from session: {}", trainer);
        //logger.info("User's Trainer: {}", user.getTrainer());
        logger.info("Logged-in Trainer: {}", trainer);

        trainingPlanRepository.save(trainingPlan);
        UserTrainer userTrainer = new UserTrainer(user, trainer, trainingPlan);
        userTrainerRepository.save(userTrainer);


        logger.info("Training plan created successfully for user: {}", user.getName());

        return "redirect:/training-plans";

    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        TrainingPlan trainingPlan = trainingPlanRepository.findById(id).orElse(null);
        model.addAttribute("trainingPlan", trainingPlan);
        return "update-training-plan";
    }

    @PostMapping("/update/{id}")
    public String updateTrainingPlan(@PathVariable("id") Long id, @ModelAttribute TrainingPlan trainingPlan) {
        TrainingPlan existingTrainingPlan = trainingPlanRepository.findById(id).orElse(null);

        if (existingTrainingPlan != null) {
            existingTrainingPlan.setName(trainingPlan.getName());
            existingTrainingPlan.setDescription(trainingPlan.getDescription());
            existingTrainingPlan.setDifficulty(trainingPlan.getDifficulty());

            trainingPlanRepository.save(existingTrainingPlan);
        }

        return "redirect:/training-plans";
    }


}
