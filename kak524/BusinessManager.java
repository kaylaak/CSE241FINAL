import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class BusinessManager {

    int userinput;
    String userN;
    String passW;
    Scanner scan = new Scanner(System.in);

    ResultSet rSet;
    int choice;
    int b1Pool;
    int b1Door;
    int b1Gym;

    PreparedStatement b1Amenities;

    String fN;
    String lN;
    int phoneNum;
    PreparedStatement peopleFinder;

    int numOfB;
    int numOfB2;
    int arr[];
    int total;

    // default constructor
    public BusinessManager() {

    }

    // constructor w 2 parameters
    public BusinessManager(String userN, String passW) {
        userN = this.userN;
        passW = this.passW;
    }

    // asking BM what he wants to do
    // 1 shows amenities to user selected building
    // 2 option to combine data from the two to find empty rooms
    // 3 Offers to locate phone number of tenant
    // 4 leaves program
    public int setUp(Scanner scan) {
        System.out.println("What data would you like to collect?");
        System.out.println("1. Property");
        System.out.println("2. Set of Properties");
        System.out.println("3. Entire NUMA enterprise");
        System.out.println("4. Exit");
        userinput = scan.nextInt();

        while ((userinput < 1) || (userinput > 4)) {
            System.out.println("Number is not an option. Try again.");
            userinput = scan.nextInt();
        }

        if (userinput == 4) {
            cancel();
        }

        return userinput;

    }

    // connects DB
    public void connect(String userN, String passW) {
        try (Connection conn = DriverManager.getConnection(
                "jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241", userN, passW);
                Statement stmt = conn.createStatement();) {

            do {

                userinput = setUp(scan);

                // choice 1,2 or 3 uses switch statements
                switch (userinput) {

                    // offers to show manager amenities in each building
                    case 1:
                        choice = choiceProp(scan);
                        while (choice == 4) {
                            choice = choiceProp(scan);
                        }

                        b1Amenities = conn.prepareStatement("select doorman from amenities where key = (?)");
                        b1Amenities.setInt(1, choice);
                        rSet = b1Amenities.executeQuery();
                        if (rSet.next() == false) {
                            System.out.println("Error.");
                        } else {
                            b1Door = rSet.getInt(1);
                        }

                        b1Amenities = conn.prepareStatement("select gym from amenities where key = (?)");
                        b1Amenities.setInt(1, choice);
                        rSet = b1Amenities.executeQuery();
                        if (rSet.next() == false) {
                            System.out.println("Error.");
                        } else {
                            b1Gym = rSet.getInt(1);
                        }

                        b1Amenities = conn.prepareStatement("select pool from amenities where key = (?)");
                        b1Amenities.setInt(1, choice);
                        rSet = b1Amenities.executeQuery();
                        if (rSet.next() == false) {
                            System.out.println("Error.");
                        } else {
                            b1Pool = rSet.getInt(1);
                        }

                        propertyAmenities(choice);

                        break;

                    // offers to show the combined empty units
                    case 2:

                        numOfBuilding(scan);
                        numOfB2 = numOfB;
                        int j = 0;
                        arr = new int[100];
                        System.out.println("Enter key number of Property you would like to search.");
                        for (int i = 0; i < numOfB2; i++) {
                            int a = scan.nextInt();
                            PreparedStatement prop = conn
                                    .prepareStatement("select key from properties where key = (?)");
                            prop.setInt(1, a);
                            rSet = prop.executeQuery();
                            if (rSet.next() == false) {
                                System.out.println("Error. Try again");
                                numOfB2++;
                            } else {
                                a = rSet.getInt(1);
                                arr[j++] = a;
                            }
                        }

                        for (int i = 0; i < numOfB; i++) {
                            int a = 0;
                            PreparedStatement count = conn.prepareStatement(
                                    "select COUNT(apt_num)from apartment where key = (?) and avabile = 1");
                            count.setInt(1, arr[i]);
                            rSet = count.executeQuery();
                            if (rSet.next() == false) {
                                System.out.println("Error. Try again");
                            } else {
                                a = rSet.getInt(1);
                            }

                            total += a;
                        }

                        System.out.println("The total rooms that are empty in your selected buildings are " + total);
                        break;

                    // allows manager to find the tenants phone number
                    case 3:

                        findPeople(scan);
                        peopleFinder = conn.prepareStatement("select phone_num from person where firstN = (?) AND lastN = (?)");
                        peopleFinder.setString(1, fN);
                        peopleFinder.setString(2, lN);
                        rSet = peopleFinder.executeQuery();

                        while (rSet.next() == false) {
                            System.out.println("No name found");
                            findPeople(scan);
                            peopleFinder.setString(1, fN);
                            peopleFinder.setString(2, lN);
                            rSet = peopleFinder.executeQuery();
                        }

                        phoneNum = rSet.getInt(1);
                        phoneNumSOP();

                        break;
                }

            } while (userinput != 4);
            conn.close();
        } catch (SQLException sqle) {
            System.out.println("SQLException : " + sqle);
        }
    }

    // allows user to choice property number
    public int choiceProp(Scanner scan) {
        System.out.println("What is the key code of the property you want to search");
        int choice = scan.nextInt();

        return choice;
    }

    // prints out the features
    public void propertyAmenities(int prop) {
        System.out.println("Features in property " + prop + ":\n");

        if (b1Pool == 1) {
            System.out.println("Pool is in building " + prop);
        }
        if (b1Gym == 1) {
            System.out.println("Gym is in building " + prop);
        }
        if (b1Door == 1) {
            System.out.println("Building " + prop + " has a doorman");
        }

        if (b1Door == 0 && b1Gym == 0 && b1Pool == 0) {
            System.out.println("No amenities in this building");
        }

    }

    // asks user who they are trying to locate
    public void findPeople(Scanner scan) {
        System.out.println("What is the first name of the tenant you are trying to locate their phone number?");
        fN = scan.next();
        System.out.println("What is the last name of the tenant you are trying to locate their phone number??");
        lN = scan.next();
    }

    // prints out phone number of user
    public void phoneNumSOP() {
        System.out.println("Your tenant " + fN + " " + lN + " phone number is " + phoneNum);
    }

    public void numOfBuilding(Scanner scan) {
        System.out.println("How many buildings would you like to search");
        numOfB = scan.nextInt();
    }

    // cancels program
    public void cancel() {
        System.out.println("Goodbye.");
        System.exit(0);
    }

}
