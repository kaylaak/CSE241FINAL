package kak524;
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
    PreparedStatement payment;

    int lease_num;
    int trans_num;

    int amount;

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

            leaseN = conn.prepareStatement(
                        "select transaction_num from lease where lease_num = (?)");
                leaseN.setInt(1, lease_num);
                rSet = leaseN.executeQuery();
            if (rSet.next() == false) {
                    System.out.println("Error.");
                } else {
                    trans_num = rSet.getInt(1);
                }




            System.out.println("Hello " + fN +", welcome to NUMA Apartments!\n\n");
            userinput = setUp(scan);

            while (userinput != 6) {
                switch (userinput) {

                    case 1:
                    PreparedStatement rent;
                    rent = conn.prepareStatement("select month_rent from lease where lease_num = (?)");
                        rent.setInt(1, lease_num);
                        rSet = rent.executeQuery();

                        if(rSet.next()==false){
                            System.out.println("Nothing. I guess rent is free XD");
                        } else {
                            System.out.println("Your outstanding balance is: " + rSet.getInt(1));
                            System.out.println("**Remember to pay your balance every month, or you might get evicted!**");
                        }


                    break;

                    case 2:
                    System.out.println("How would you like to pay \n 1. Bank\n2. Check\n3. Vemno");
                    int num = payMent(scan);
                    if(num == 1){
                        int bnum = bank(scan);
                        payment = conn.prepareStatement("INSERT INTO bank_acct (amount, transaction_num, bank_num) VALUES ((?), (?), (?))");
                        payment.setInt(1, amount);
                        payment.setInt(2, trans_num);
                        payment.setInt(3, bnum);
                        rSet = payment.executeQuery();
                    } else if (num ==2){
                    }
                    


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


    public int payMent(Scanner scan){
        System.out.println("How would you like to pay \n 1. Bank\n2. Check\n3. Vemno");
        int num = scan.nextInt();
        int t = 1;
        while(t ==1){
            if(num>3 || num < 1){
                System.out.println("Try again.");
                num = scan.nextInt();
            } else {
                t =0;
                break;
            }
        }

        return num;
    }

    public int bank(Scanner scan){
        System.out.println("You have chosen bank. Please enter the following information.");
        System.out.println("Enter your bank number(10 digits)");
        int bank = scan.nextInt();

        System.out.println("How much you would like to pay?");
        amount = scan.nextInt();

        return bank;
    }

    public int check(Scanner scan){
        System.out.println("You have chosen check. Please enter the following information.");
        System.out.println("Enter the check number(10 digits)");
        int check = scan.nextInt();

        System.out.println("How much you would like to pay?");
        amount = scan.nextInt();

        return check;
    }

    public int vemno(Scanner scan){
        System.out.println("You have chosen vemno. Please enter the following information.");
        System.out.println("Enter your userName");
        int vemno = scan.nextInt();

        System.out.println("How much you would like to pay?");
        amount = scan.nextInt();

        return vemno;
    }
    public void cancel() {
        System.out.println("Goodbye.");
        System.exit(0);
    }

}
