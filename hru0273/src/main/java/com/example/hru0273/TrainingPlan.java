package com.example.hru0273;

import com.example.hru0273.UserTrainer;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
@Entity
public class TrainingPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Getter
    private String name;
    private String description;
    private String difficulty;
    @OneToMany(mappedBy = "trainingPlan")
    private List<UserTrainer> userTrainers = new ArrayList<>();


    public TrainingPlan() {

    }
    public TrainingPlan(String name, String description, String difficulty){
        this.name= name;
        this.description = description;
        this.difficulty = difficulty;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setName(String name) {
        this.name = name;
    }
}
