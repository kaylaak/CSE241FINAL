/*
Kayla Kraft 
CSE 241 Final Project
Main File 
//no oop in here

CONNECTED TO GIT
*/

import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class NUMA {

    public static void main(String [] args){
        Scanner scan = new Scanner(System.in);

        System.out.println("Welcome to NUMA apartments.\n\n\nPlease sign in with your username and password.");
     
        int check = 0;
        String userN = "";
        String passW = "";
    

        while (check == 0){

            userN = userName(scan);
            passW = passWord(scan);

            check = checkDB(userN,passW);
        }

        int userinput = menu(scan);
        

        
        switch(userinput){
            case 1:
            System.out.println("Welcome Property Manager.");
            PM property = new PM(userN, passW);
            property.connect(userN, passW);
            break;

            case 2:
            System.out.println("Welcome Tenant");
            Tenant user = new Tenant(userN,passW);
            user.connect(userN,passW);
            break;

            case 3:
            System.out.println("Welcome Business Managaer");
            BusinessManager manager = new BusinessManager(userN, passW);
            manager.connect(userN, passW);
            break;
        }

        
    }

    public static int menu(Scanner scan){
        System.out.println("Please enter the type of interface you are trying to access.");
        System.out.println("1. Property Manager");
        System.out.println("2. Tenant");
        System.out.println("3. Buisness Manager");

        int userinput = scan.nextInt();
        while(!(userinput>0) || !(userinput<4)){
            System.out.println("Please try again.");
            userinput = scan.nextInt();
        } 
        
            return userinput;
    }


    public static int checkDB (String userN, String passW){

        //1 means DB is connected-- 0 means DB is not
        int check = 0;
    

             try (Connection conn = DriverManager.getConnection(
                "jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241", userN, passW);
                Statement stmt = conn.createStatement();) {
                    check = 1;
                    conn.close();
                } catch (SQLException sqle) {
                    System.out.println("Username/Password not found in our database");
                }

            return check;
    }

    //
    public static String userName (Scanner scan){
        System.out.println("What is your username");
        String userN = scan.nextLine();
        
        return userN;
    }

    public static String passWord (Scanner scan){

        System.out.println("What is your password");
        String password = scan.nextLine();

        return password;
    }
}