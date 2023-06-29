package titanic;

import titanic.enums.Embarked;
import titanic.enums.PassengerClass;
import titanic.enums.Sex;
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
