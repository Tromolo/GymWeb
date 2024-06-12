package com.example.hru0273;

import lombok.Getter;

import jakarta.persistence.*;

@Getter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    private int age;
    private String gender;
    @Getter
    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    public User() {
    }

    public User(String name, String email, String password, int age, String gender) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.age = age;
        this.gender = gender;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isTrainer(){
        return false;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }
}
