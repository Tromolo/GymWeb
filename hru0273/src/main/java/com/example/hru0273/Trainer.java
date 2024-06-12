package com.example.hru0273;

import com.example.hru0273.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;
@Getter
@Entity
//@Table(name = "trainer")
public class Trainer {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Getter
    private String name;
    private String photoUrl;
    private String specialization;
    private int experience;
    @Getter
    private String password;
    @OneToMany(mappedBy = "trainer")
    private List<User> users;

    public Trainer() {

    }

    public Trainer(String name, String specialization, int experience, String password) {
        this.name = name;
        this.specialization = specialization;
        this.experience = experience;
        this.password = password;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isTrainer(){
        return true;
    }
}
