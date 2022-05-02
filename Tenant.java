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

    ResultSet rSet;
    PreparedStatement leaseN;

    int lease_num;

    public Tenant() {
        userN = " ";
        passW = "";

    }

    public Tenant(String userN, String passW) {
        userN = this.userN;
        passW = this.passW;
    }

    public int setUp(Scanner scan) {
        System.out.println("What would you like to do?");
        // check balence
        System.out.println("1. Check Payment Status");
        // subtract from monthly rent and chargable service
        System.out.println("2. Make rental payment");
        // UPDATE chargable service
        System.out.println("4. Add chargable service");
        // UPDATE
        System.out.println("5. Update personal-data");
        System.out.println("6. Exit");
        int userinput = scan.nextInt();
        while ((userinput < 1) || (userinput > 6)) {
            System.out.println("Number is not an option. Try again.");
            userinput = scan.nextInt();
        }

        if (userinput == 6) {
            cancel();
        }

        return userinput;

    }

    public void connect(String userN, String passW) {
        try (Connection conn = DriverManager.getConnection(
                "jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241", userN, passW);
                Statement stmt = conn.createStatement();) {
            Boolean t = true;
            while (t == true) {
                name(scan);
                leaseN = conn.prepareStatement(
                        "select lease_num from tenant where firstN = (?) and lastN = (?)");
                leaseN.setString(1, fN);
                leaseN.setString(2, lN);
                rSet = leaseN.executeQuery();
                if (rSet.next() == false) {
                    System.out.println("Error. Try again");
                } else {
                    lease_num = rSet.getInt(1);
                    t = false;
                }
            }

            System.out.println("Hello " + fN +", welcome to NUMA Apartments!\n\n");
            userinput = setUp(scan);

            while (userinput != 6) {
                switch (userinput) {

                    case 1:
                    

                }
            }
            conn.close();
        } catch (SQLException sqle) {
            System.out.println("SQLException : " + sqle);
        }
    }

    public void name(Scanner scan) {
        System.out.println("What is your first name?");
        fN = scan.nextLine();
        System.out.println("What is your last name?");
        lN = scan.nextLine();

    }

    public void cancel() {
        System.out.println("Goodbye.");
        System.exit(0);
    }

}
