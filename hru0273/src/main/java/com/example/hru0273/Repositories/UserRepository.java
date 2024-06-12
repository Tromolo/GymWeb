package com.example.hru0273.Repositories;

import com.example.hru0273.Trainer;
import com.example.hru0273.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByName(String name);


    List<User> findByTrainer(Trainer trainer);
}
