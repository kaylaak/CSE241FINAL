import java.util.Scanner;
import java.util.*;
import java.sql.*;
import java.sql.Date;



public class Tenant {

    Scanner scan = new Scanner(System.in);
    String userN;
    String passW;

    int userinput;

    String fN;
    String lN;

    public Tenant(){
        userN =  " ";
        passW = "";

    }

    public Tenant(String userN, String passW){
        userN = this.userN;
        passW = this.passW;
    }

    public int setUp(Scanner scan){
        System.out.println("What would you like to do?");
        System.out.println("1. Check Payment Status");
        System.out.println("2. Make rental payment");
        System.out.println("4. Set move-out date");
        System.out.println("5. Update personal-data");
        System.out.println("6. Exit");
        int userinput = scan.nextInt();
        while((userinput<1) || (userinput>6)){
            System.out.println("Number is not an option. Try again.");
            userinput = scan.nextInt();
        }

        if(userinput == 6){
            cancel();
        }


        return userinput;

    }

    public void connect(String userN, String passW){
        try (Connection conn = DriverManager.getConnection(
            "jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241", userN, passW);
            Statement stmt = conn.createStatement();) {
                name(scan);
                userinput = setUp(scan);

                System.out.println("What is the potential tenant's date of visit?(yyyy-mm-dd)");
        String dayy = scan.next();

        if(!(dayy.matches("\\d{4}-\\d{2}-\\d{2}"))){
            System.out.println("Date input is not expected. Try again (yyyy-mm-dd)\n\n");
            System.out.println("What is the potential tenant's date of visit?(yyyy-mm-dd)");
            dayy = scan.next();
            }

        Date day = Date.valueOf(dayy);
     
                switch(userinput){

                    case 1: 


                }

                conn.close();
            } catch (SQLException sqle) {
                System.out.println("SQLException : " + sqle);
            }
    }
    


    public void name(Scanner scan){
        System.out.println("What is your first name?");
        fN = scan.nextLine();
        System.out.println("What is your last name?");
        lN = scan.nextLine();


    }

    public void cancel(){
        System.out.println("Goodbye.");
        System.exit(0);
    }
    
}
