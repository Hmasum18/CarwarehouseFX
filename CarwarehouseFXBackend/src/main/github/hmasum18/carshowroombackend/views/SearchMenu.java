package github.hmasum18.carshowroombackend.views;


public class SearchMenu {
    /*public static void searchMenu(Scanner scanner, Controller controller){
        while(true){
            //take user input
            //prompt message
            System.out.println(MyColor.ANSI_BRIGHT_BLUE+"|||        Search Options         |||");
            System.out.println("||| (1) By Registration Number    |||");
            System.out.println("||| (2) By Car Make and Car Model |||");
            System.out.println("||| (3) Back to Main Menu         |||"+MyColor.ANSI_RESET);
            System.out.print("Input a number from 1 to 3:");
            boolean isBackToMainMenu = false;

            try {
                //handle usr input
                switch (scanner.nextInt()){
                    case 1:
                        //take user input
                        System.out.print("Input car registration number:");
                        String carReg = scanner.next();

                        //get data
                        Car car = controller.searchByCarReg(carReg);
                        //show data
                        //showCarData(car);
                        break;
                    case 2:
                        //take user input
                        System.out.print("Input car make:");
                        String carMake = scanner.next();
                        System.out.print("Input car model:");
                        String carModel = scanner.next();

                        //get data
                        List<Car> cars = controller.searchByCarMakeAndCarModel(carMake,carModel);

                        //show all data
                        if(cars.size()>0)
                            System.out.println(MyColor.ANSI_GREEN+"Success!! Total "+cars.size()
                                    +" car data found in the database. Details given below:"+MyColor.ANSI_RESET);
                        else
                            System.out.println(MyColor.ANSI_RED+"Error:No such car with this car make or car model."+MyColor.ANSI_RESET);

                        for (Car c : cars) {
                           // showCarData(c);
                        }
                        break;
                    case 3:
                        isBackToMainMenu = true;
                        break;
                    default:
                        System.out.println(MyColor.ANSI_RED+"Error!!! Option out of range! Try again....Only integer 1-3 is a valid input"+MyColor.ANSI_RESET);
                }
            }catch (Exception e){
                System.out.println(MyColor.ANSI_RED+"Error!!! You have given invalid input! Try again....Only integer 1-3 is a valid input "+MyColor.ANSI_RESET);
                scanner.nextLine();
            }

            if(isBackToMainMenu)
                break;
        }
    }*/

   /* private static void showCarData(Car car){
        System.out.println(MyColor.ANSI_BRIGHT_YELLOW+"Car info:"+MyColor.ANSI_RESET);
        System.out.println(MyColor.ANSI_GREEN+"    Registration number: "+car.getCarReg()+"     Year made: "+car.getYearMade());
        System.out.println("    Color: "+car.getColor1()+", "+car.getColor2()+", "+car.getColor3());
        System.out.println("    Car make: "+car.getCarMake()+"     Car model: "+car.getCarModel());
        System.out.println("    Car price: $"+car.getPrice()+MyColor.ANSI_RESET);
    }*/
}
