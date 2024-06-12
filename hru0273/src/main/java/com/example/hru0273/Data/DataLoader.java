package com.example.hru0273.Data;

import com.example.hru0273.Trainer;
import com.example.hru0273.TrainingPlan;
import com.example.hru0273.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final RestTemplate restTemplate;

    @Autowired
    public DataLoader(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        loadUsers();
        loadTrainers();
        loadTrainingPlans();
    }

    private void loadUsers() {
        String usersUrl = "http://localhost:8080/users/batch";
        List<User> users = Arrays.asList(
                new User("John Doe", "john.doe@example.com", "pass123", 15, "male"),
                new User("Tomas Hruby", "tomas.hruby@example.com", "pass123", 22, "male"),
                new User("Tomas Duraj", "tomas.duraj@example.com", "pass123", 21, "male")
        );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<User>> request = new HttpEntity<>(users, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(usersUrl, request, String.class);
        System.out.println("Users loaded: " + response.getBody());
    }

    private void loadTrainers() {
        String trainersUrl = "http://localhost:8080/trainers/batch";
        List<Trainer> trainers = Arrays.asList(
                new Trainer("Tomas Krisicka", "Cutting", 5, "password"),
                new Trainer("Kubo Backa", "Bulking", 5, "password")
        );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<Trainer>> request = new HttpEntity<>(trainers, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(trainersUrl, request, String.class);
        System.out.println("Trainers loaded: " + response.getBody());
    }

    private void loadTrainingPlans() {
        String trainingPlansUrl = "http://localhost:8080/training-plans/batch";
        List<TrainingPlan> trainingPlans = Arrays.asList(
                new TrainingPlan("Beginner Workout", "This workout plan is designed for beginners to get started with their fitness journey.", "Easy"),
                new TrainingPlan("Intermediate Workout", "This workout plan is for users who have some experience with training and want to push themselves further.", "Intermediate"),
                new TrainingPlan("Advanced Workout", "This workout plan is for advanced users who are looking for a challenging workout to test their limits.", "Advanced"),
                new TrainingPlan("Weight Loss Program", "This program focuses on helping users lose weight through a combination of cardio and strength training exercises.", "Medium")
        );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<TrainingPlan>> request = new HttpEntity<>(trainingPlans, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(trainingPlansUrl, request, String.class);
        System.out.println("Training plans loaded: " + response.getBody());
    }
}
