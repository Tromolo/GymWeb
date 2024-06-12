package com.example.hru0273.Repositories;

import com.example.hru0273.Trainer;
import com.example.hru0273.User;
import com.example.hru0273.UserTrainer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserTrainerRepository  extends JpaRepository<UserTrainer,Long> {
    UserTrainer findByUserId(Long userId);

    List<UserTrainer> findByTrainer(Trainer trainer);

    List<UserTrainer> findByUser(User user);
}
