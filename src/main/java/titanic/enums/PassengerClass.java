package titanic.enums;

public enum PassengerClass{
    FIRST(1),
    SECOND(2),
    THIRD(3);

    private final Integer value;

    PassengerClass(Integer value){
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
