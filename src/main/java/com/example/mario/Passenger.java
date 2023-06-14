package com.example.mario;

import com.example.mario.enums.Embarked;
import com.example.mario.enums.PassengerClass;
import com.example.mario.enums.Sex;
import lombok.Data;

@Data
public class Passenger {
    private boolean isSurvived;
    private PassengerClass passengerClass;
    private String name;
    private Sex sex;
    private Double age;
    private double fare;
    private Embarked embarked;
}
