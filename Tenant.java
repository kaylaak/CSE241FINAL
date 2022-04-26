import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Tenant {

    Scanner scan = new Scanner(System.in);
    String userN;
    String passW;

    public Tenant(){

    }

    public Tenant(String userN, String passW){
        userN = this.userN;
        passW = this.passW;
    }

    public int setUp(Scanner scan){
        System.out.println("What would you like to do?");
        System.out.println("1. Check Payment Status");
        System.out.println("2. Make rental payment");
        System.out.println("3. Add person/pet");
        System.out.println("4. Set move-out date");
        System.out.println("5. Update personal-data");
        System.out.println("6. Exit");
        int userinput = scan.nextInt();
        while((userinput<1) || (userinput>6)){
            System.out.println("Number is not an option. Try again.");
            userinput = scan.nextInt();
        }

        if(userinput == 4){
            cancel();
        }


        return userinput;

    }

    public void connect(String userN, String passW){
        try (Connection conn = DriverManager.getConnection(
            "jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241", userN, passW);
            Statement stmt = conn.createStatement();) {
                setUp(scan);

                conn.close();
            } catch (SQLException sqle) {
                System.out.println("SQLException : " + sqle);
            }
    }
    


    public void cancel(){
        System.out.println("Goodbye.");
        System.exit(0);
    }
    
}
