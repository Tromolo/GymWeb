package com.example.hru0273.Repositories;

import com.example.hru0273.Trainer;
import com.example.hru0273.TrainingPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingPlanRepository extends JpaRepository<TrainingPlan, Long> {
    TrainingPlan findByName(String name);
}
