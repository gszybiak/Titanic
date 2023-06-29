package titanic;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import titanic.enums.Embarked;
import titanic.enums.PassengerClass;
import titanic.enums.Sex;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

public class MainController {

    @FXML
    private Button chooseFile;
    @FXML
    private TextField passengerName;

    private List<Passenger> passengerList = new ArrayList<>();

    @FXML
    protected void onChooseFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(chooseFile.getScene().getWindow());
        loadData(selectedFile, passengerList);

        getInformationAboutPassenger();
        getInformationAboutPassengerFirstTask();
        getInformationAboutPassengerSecondTask();
        getInformationAboutPassengerThirdTask();
        getInformationAboutPassengernewTask();
    }

    @FXML
    protected void countButtonClick(){
        if(StringUtils.isEmpty(passengerName.getText()))
            return;
        String name = passengerName.getText();
        double averageAge = passengerList.stream().filter(passenger -> passenger.getName().contains("\"" + name)).collect(Collectors.averagingDouble(Passenger::getAge));
        System.out.println("Średni wiek pasażerów: " + averageAge);
        System.out.println("Nazwiska dużymi literami: ");
        passengerList
                .stream()
                .filter(passenger -> passenger.getName().contains("\"" + name))
                .map(Passenger::getName)
                .map(surname -> {
                            return surname.substring(1, surname.length() - 1);
                })
                .forEach(System.out::println);

        double firstClassAverangeFare = passengerList.stream().filter(passenger -> passenger.getPassengerClass().equals(PassengerClass.FIRST) &&
                        passenger.getName().contains("\"" + name)).collect(Collectors.averagingDouble(Passenger::getFare));
        System.out.println("Średnia cena biletu z pierwszej klasy: " + firstClassAverangeFare);

        double thirdClassAverangeFare = passengerList.stream().filter(passenger -> passenger.getName().contains("\"" + name) &&
                passenger.getPassengerClass().equals(PassengerClass.THIRD)).collect(Collectors.averagingDouble(Passenger::getFare));
        System.out.println("Średnia cena biletu z trzeciej klasy: " + thirdClassAverangeFare);
    }

    private void loadData(File selectedFile, List<Passenger> passengerList) throws IOException {
        if (selectedFile == null)
            return;

        String pathFile = selectedFile.getAbsolutePath();
        Path path = Paths.get(pathFile);
        Scanner scanner = new Scanner(path);
        List<String> readPassengerList = new ArrayList<>();

        while (scanner.hasNextLine())
            readPassengerList.add(scanner.nextLine());

        if (readPassengerList.isEmpty())
            return;

        for (int i = 1; i < readPassengerList.size(); i++) {
             Passenger passenger = new Passenger();
             String[] elements = readPassengerList.get(i).split("\t");

             passenger.setSurvived("1".equals(elements[1]));
             String elements2 = elements[2];
             if(elements2.equals(PassengerClass.FIRST.getValue().toString()))
                 passenger.setPassengerClass(PassengerClass.FIRST);
             else if(elements2.equals(PassengerClass.SECOND.getValue().toString()))
                 passenger.setPassengerClass(PassengerClass.SECOND);
             else
                 passenger.setPassengerClass(PassengerClass.THIRD);

             passenger.setName(elements[3]);
             String elements4 = elements[4];
             passenger.setSex(elements4.equalsIgnoreCase("FEMALE") ? Sex.FEMALE : Sex.MALE);
             passenger.setAge(elements[5].equals("") ? null : Double.parseDouble(elements[5]));
             passenger.setFare(Double.parseDouble(elements[9]));
             passengerList.add(passenger);
        }
    }

    private void getInformationAboutPassenger(){
        long allPassengerWithNullAge = passengerList.stream().filter(passenger -> passenger.getAge() == null).count();
        int allPassenger = passengerList.size();

        System.out.println("Liczba wszystkich pasażerów: " + allPassenger);
        System.out.println("Liczba pasażerów bez wieku: " + allPassengerWithNullAge);
    }

    private void getInformationAboutPassengerFirstTask(){
        System.out.println("Liczba dorosłych mężczyzn: " + countPassengersDependFromSexAndAge(Sex.MALE, 18.0, Double.MAX_VALUE));
        System.out.println("Liczba dorosłych kobiet: " + countPassengersDependFromSexAndAge(Sex.FEMALE, 18.0, Double.MAX_VALUE));
        System.out.println("Liczba chłopców: " + countPassengersDependFromSexAndAge(Sex.MALE, 0.0, 17.0));
        System.out.println("Liczba dziewczynek: " + countPassengersDependFromSexAndAge(Sex.FEMALE, 0.0, 17.0));
    }

    private long countPassengersDependFromSexAndAge(Sex sex, Double startAge, Double endAge){
        return passengerList.stream().filter(passenger -> passenger.getSex().equals(sex) && passenger.getAge() != null
                && passenger.getAge() >= startAge && passenger.getAge() <= endAge).count();
    }

    private void getInformationAboutPassengerSecondTask() {
        double firstClassSurvived = countPassengersDependFromSurvivedAndPassengerClass(true, PassengerClass.FIRST);
        System.out.println("Liczba osób które przeżyły z 1 klasy: " + firstClassSurvived);
        double firstClass = countPassengersDependFromSurvivedAndPassengerClass(null, PassengerClass.FIRST);
        System.out.println("Liczba osób  z 1 klasy: " + firstClass);
        double firstClassPercent = firstClassSurvived / firstClass * 100;
        System.out.println(Math.round(firstClassPercent) + "%");

        double secondClassSurvived = countPassengersDependFromSurvivedAndPassengerClass(true, PassengerClass.SECOND);
        System.out.println("Liczba osób które przeżyły z 1 klasy: " + secondClassSurvived);
        double secondClass = countPassengersDependFromSurvivedAndPassengerClass(null, PassengerClass.SECOND);
        System.out.println("Liczba osób  z 2 klasy: " + secondClass);
        double secondClassPercent  = secondClassSurvived/secondClass * 100;
        System.out.println(Math.round(secondClassPercent) + "%");

        double thirdClassSurvived = countPassengersDependFromSurvivedAndPassengerClass(true, PassengerClass.THIRD);
        System.out.println("Liczba osób które przeżyły z 3 klasy: " + thirdClassSurvived);
        double thirdClass = countPassengersDependFromSurvivedAndPassengerClass(null, PassengerClass.THIRD);
        System.out.println("Liczba osób  z 3 klasy: " + thirdClass);
        double thirdClassPercent  = thirdClassSurvived/thirdClass * 100;
        System.out.println(Math.round(thirdClassPercent) + "%");
    }

    private long countPassengersDependFromSurvivedAndPassengerClass(Boolean isSurvived, PassengerClass passengerClass){
        if(isSurvived == null)
            return passengerList.stream().filter(passenger -> passenger.getPassengerClass().equals(passengerClass)).count();
        else
            return passengerList.stream().filter(passenger -> passenger.isSurvived() && passenger.getPassengerClass().equals(passengerClass)).count();
    }

    private void getInformationAboutPassengerThirdTask() {
        double firstClassSurvivedWoman = countPassengersDependFromSurvivedSexAndPassengerClass(Sex.FEMALE, true, PassengerClass.FIRST);
        System.out.println("Liczba kobiet które przeżyły z 1 klasy: " + firstClassSurvivedWoman);
        double firstClassWomen = countPassengersDependFromSurvivedSexAndPassengerClass(Sex.FEMALE, null, PassengerClass.FIRST);
        System.out.println("Liczba kobiet  z 1 klasy: " + firstClassWomen);
        double firstClassPercentWomen  = firstClassSurvivedWoman/firstClassWomen * 100;
        System.out.println(Math.round(firstClassPercentWomen)+ "%");

        double ThirdClassSurvivedMen = countPassengersDependFromSurvivedSexAndPassengerClass(Sex.MALE, true, PassengerClass.THIRD);
        System.out.println("Liczba osób które przeżyły z 1 klasy: " + ThirdClassSurvivedMen);
        double ThirdClassMen = countPassengersDependFromSurvivedSexAndPassengerClass(Sex.MALE, null, PassengerClass.THIRD);
        System.out.println("Liczba osób  z 1 klasy: " + ThirdClassMen);
        double firstClassPercentMen  = ThirdClassSurvivedMen/ThirdClassMen * 100;
        System.out.println(Math.round(firstClassPercentMen)+ "%");

        System.out.println(Math.round(firstClassPercentWomen-firstClassPercentMen) + " punktów procentowych więcej");
    }

    private long countPassengersDependFromSurvivedSexAndPassengerClass(Sex sex, Boolean isSurvived, PassengerClass passengerClass){
        if(isSurvived == null)
            return passengerList.stream().filter(passenger -> passenger.getPassengerClass().equals(passengerClass) && passenger.getSex().equals(sex)).count();
        else
            return passengerList.stream().filter(passenger -> passenger.isSurvived() && passenger.getPassengerClass().equals(passengerClass) && passenger.getSex().equals(sex)).count();
    }

    private void getInformationAboutPassengernewTask() {
        System.out.println("Kobiety alfabetycznie z pierwszej klasy: ");
        passengerList
                .stream()
                .filter(passenger -> passenger.getSex().equals(Sex.FEMALE))
                .map(Passenger::getName)
                .sorted()
                .forEach(System.out::println);

        //kobiety
        long countWomenclassFirstPortC = passengerList.stream().filter(passenger -> passenger.getSex().equals(Sex.FEMALE) && passenger.getPassengerClass()
                .equals(PassengerClass.FIRST) && passenger.getEmbarked().equals(Embarked.C)).count();
        System.out.println("Suma kobiet, które wypłynęły z portu z klasy pierwszej z portu C: " + countWomenclassFirstPortC);

        long countWomenclassFirstPortQ = passengerList.stream().filter(passenger -> passenger.getSex().equals(Sex.FEMALE) && passenger.getPassengerClass()
                .equals(PassengerClass.FIRST) && passenger.getEmbarked().equals(Embarked.Q)).count();
        System.out.println("Suma kobiet, które wypłynęły z portu z klasy pierwszej z portu Q: " + countWomenclassFirstPortQ);

        long countWomenclassFirstPortS = passengerList.stream().filter(passenger -> passenger.getSex().equals(Sex.FEMALE) && passenger.getPassengerClass()
                .equals(PassengerClass.FIRST) && passenger.getEmbarked().equals(Embarked.S)).count();
        System.out.println("Suma kobiet, które wypłynęły z portu z klasy pierwszej z portu S: " + countWomenclassFirstPortS);

        //mężczyźni
        long countMenclassFirstPortC = passengerList.stream().filter(passenger -> passenger.getSex().equals(Sex.MALE) && passenger.getPassengerClass()
                .equals(PassengerClass.FIRST) && passenger.getEmbarked().equals(Embarked.C)).count();
        System.out.println("Suma kobiet, które wypłynęły z portu z klasy pierwszej z portu C: " + countMenclassFirstPortC);

        long countMenclassFirstPortQ = passengerList.stream().filter(passenger -> passenger.getSex().equals(Sex.MALE) && passenger.getPassengerClass()
                .equals(PassengerClass.FIRST) && passenger.getEmbarked().equals(Embarked.Q)).count();
        System.out.println("Suma kobiet, które wypłynęły z portu z klasy pierwszej z portu Q: " + countMenclassFirstPortQ);

        long countMenclassFirstPortS = passengerList.stream().filter(passenger -> passenger.getSex().equals(Sex.MALE) && passenger.getPassengerClass()
                .equals(PassengerClass.FIRST) && passenger.getEmbarked().equals(Embarked.S)).count();
        System.out.println("Suma kobiet, które wypłynęły z portu z klasy pierwszej z portu S: " + countMenclassFirstPortS);
    }
}