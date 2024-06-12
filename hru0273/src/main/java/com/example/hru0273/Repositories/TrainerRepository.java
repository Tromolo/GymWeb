package com.example.hru0273.Repositories;

import com.example.hru0273.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {

    Trainer findByName(String name);

}
