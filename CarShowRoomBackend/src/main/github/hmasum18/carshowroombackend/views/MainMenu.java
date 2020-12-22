package github.hmasum18.carshowroombackend.views;



import github.hmasum18.carshowroombackend.controller.CarController;
import github.hmasum18.carshowroombackend.controller.UserController;
import github.hmasum18.carshowroombackend.database.SqliteConnectionBuilder;
import github.hmasum18.carshowroombackend.model.UserInfo;
import res.R;

import java.util.Arrays;
import java.util.Scanner;


public class MainMenu{
    public static final String TAG = "MainMenu-Debug->";
    private static CarController carController;
    private static UserController userController;
    private static Scanner scanner;

    public static void main(String[] args) {
        //carController = new CarController();
       // userController = new UserController();
        scanner = new Scanner(System.in);
        try {
            //load all the car information from file
            //controller.loadDataBase();
           // startMenu();
            /*System.out.print("give reg number: ");
            String reg = scanner.next();
            carController.searchByCarReg(reg);*/
           /* System.out.print("Enter username: ");
            String username = scanner.next();
            System.out.print("Enter password: ");
            String password = scanner.next();
*/
            //UserInfo userInfo = userController.login(username,password);
            //System.out.println(userInfo);

            //byte[] bytes = R.image.getByteArrayByName("me.jpg");
            //System.out.println(Arrays.toString(bytes));

            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("end of main");
        SqliteConnectionBuilder.executorService.shutdown();
    }

    /*private static void startMenu(){
        while(true){
            //prompt user input
            System.out.println(MyColor.ANSI_BRIGHT_MAGENTA+"|||     Main Menu   |||");
            System.out.println("||| (1) Search Cars |||");
            System.out.println("||| (2) Add Car     |||" );
            System.out.println("||| (3) Delete Car  |||");
            System.out.println("||| (4) Exit System |||"+ MyColor.ANSI_RESET);
            boolean isExit = false;
            System.out.print("Input a number from 1 to 4:");
            //get user input
            try{
                switch (scanner.nextInt()){
                    case 1: //search cars
                        SearchMenu.searchMenu(scanner,controller);
                        break;
                    case 2:
                        scanner.nextLine();
                        addCarMenu();
                        break;
                    case 3:
                        deleteCarMenu();
                        break;
                    case 4:
                        isExit = exitSystem();
                        break;
                    default:
                        System.out.println(MyColor.ANSI_RED+"Error!!! Option out of range! Try again....Only integer 1-4 is a valid input"+MyColor.ANSI_RESET);
                }
            }catch (Exception e){
                System.out.println(MyColor.ANSI_RED+"Error!!! You have given invalid input! Try again....Only integer 1-4 is a valid input "+MyColor.ANSI_RESET);
                scanner.nextLine();
            }
            if(isExit) {
                scanner.close();
                break;
            }
        }
    }*/

    /*private static void addCarMenu(){
        //take user input
        System.out.println(MyColor.ANSI_YELLOW+"Car data structure:CarReg,YearMade,Colour1,Colour2,Colour3,CarMake,CarModel,Price"+MyColor.ANSI_RESET);
        System.out.print("Input car data in above form:");
        String carData = scanner.nextLine();
        //insert data
        if(isValid(carData)){
            try {
               */
    /* int idx = controller.addCar(new Car(carData));
                //System.out.println(TAG+"addCar: idx:"+idx);
                if(idx>=0){
                    System.out.println(MyColor.ANSI_BRIGHT_GREEN+"Inserted successfully."+MyColor.ANSI_RESET);
                }*/
    /*
            }catch (Exception e){
                System.out.println(MyColor.ANSI_RED+e.getLocalizedMessage()+MyColor.ANSI_RESET);
                //e.printStackTrace();
            }

        }else{
            System.out.println(MyColor.ANSI_RED+"Error!!! Car data is not valid."+MyColor.ANSI_RESET);
        }
    }

    *//**
     * chk if a carData is valid or not.(front end validation)
     * it simply check the length of the array. We can add other restriction and validation logic
     * @param carData input from the user as string.
     * @return true if valid and false otherwise.
     */
    /*
    private static boolean isValid(String carData){
        return carData.split(",").length == 8;
    }

    private static void deleteCarMenu(){
        //take user input
        System.out.print("Input car registration number to delete the Car:");
        String carReg = scanner.next();
        //delete car data if found
        int idx = controller.deleteByCarReg(carReg);
        if(idx!=-1){
            System.out.println(MyColor.ANSI_BRIGHT_GREEN+"Success!!! Car with registration number "
                    +carReg+" deleted successfully from idx "+idx+MyColor.ANSI_RESET);
        }else{
            System.out.println(MyColor.ANSI_RED+"ERROR!!! Car with registration number "+carReg+" was not found."+MyColor.ANSI_RESET);
        }
    }

    private static boolean exitSystem() {
        // write all the changed data to outfile before exiting the program
        //controller.saveAndCloseDatabase();
        return true;
    }*/
}
