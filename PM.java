import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.sql.Date;
import java.util.Scanner;

//Kayla Kraft
//CSE 241 
//Creates object of Property Manager

public class PM {

    //creates one scanner for entire file
    Scanner scan = new Scanner(System.in);
    
    String userName;
    String passWord;
    String fN;
    String lN;
    int SSN;
    int phoneNum;
    int creditScore;
    Date day;
    String visitDataS;

    int leaseLength;
    int id_num;
    int serviceCost;
    int dry_clean;
    int parking;
    int dog_walking;
    String prev_add;
    int lease_num;
    int transaction_num;
    int serv_costPK;

    int monthRent;
    Date moveIn;
    Date moveOutDate;


    ResultSet rSet;

    Calendar cal;

    


    PreparedStatement visitData;
    PreparedStatement recordLease;
    PreparedStatement serviceCosts;
    PreparedStatement moveOut;
    //default constructor
    public PM(){

    }
    
    //constructor with two parameters
    public PM(String userN, String passW){
        userName = userN;
        passWord = passW;
    }

    //asks user what he/she wants to as a property manager
    //returns int
    public int setUp(Scanner scan){
        System.out.println("What would you like to do?");
        System.out.println("1. Record visit data");
        System.out.println("2. Record lease data");
        System.out.println("3. Record move-out");
        System.out.println("4. Exit");
        int userinput = scan.nextInt();
        while((userinput<1) || (userinput>4)){
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

                int user;

                do{
                switch(user = setUp(scan)){

                    
                    case 1:

                    //creates values for potential tenant
                    visitData(scan);
                    visitData = conn.prepareStatement("insert into PERSON (firstn,lastn,SSN,phone_num) values ((?),(?),(?),(?))");
                    visitData.setString(1, fN);
                    visitData.setString(2, lN);
                    visitData.setInt(3, SSN);
                    visitData.setInt(4, phoneNum);
                    rSet = visitData.executeQuery();
                    
                    visitData = conn.prepareStatement("insert into POTENTIAL_TENT (credit_score,date_visit,firstN,lastN) values ((?),(?),(?),(?))");
                    visitData.setInt(1, creditScore);
                    visitData.setDate(2, day);
                    visitData.setString(3, fN);
                    visitData.setString(4, lN);

                    rSet = visitData.executeQuery();


                    
                    break;


                //allows potential tenant to be tenant
                case 2:
                recordLease(scan);

                
                serviceCosts = conn.prepareStatement("select cost from dry_cleaning where id = (?)");
                serviceCosts.setInt(1, id_num);
                rSet = serviceCosts.executeQuery();
                if (rSet.next() == false) {
                    dry_clean = 0;
                } else {
                    dry_clean = rSet.getInt(1);
                }

                serviceCosts = conn.prepareStatement("select cost from parking where id = (?)");
                serviceCosts.setInt(1, id_num);
                rSet = serviceCosts.executeQuery();
                if (rSet.next() == false) {
                    parking = 0;
                } else {
                    parking = rSet.getInt(1);
                }

                serviceCosts = conn.prepareStatement("select cost from dog_walking where id = (?)");
                serviceCosts.setInt(1, id_num);
                rSet = serviceCosts.executeQuery();
                if (rSet.next() == false) {
                    dog_walking = 0;
                } else {
                    dog_walking = rSet.getInt(1);
                }

                serviceCost = dog_walking + parking + dry_clean;

                //updates service cost from ALL charges
                serviceCosts = conn.prepareStatement("UPDATE chargable_services SET serv_cost = (?) WHERE ID = (?)");
                    serviceCosts.setInt(1, serviceCost);
                    serviceCosts.setInt(2, id_num);
                rSet = serviceCosts.executeQuery();



                serviceCosts = conn.prepareStatement("insert into tenant (firstN,lastN,prev_address) values ((?),(?),(?))");
                    serviceCosts.setString(1, fN);
                    serviceCosts.setString(2, lN);
                    serviceCosts.setString(3, prev_add);
                rSet = serviceCosts.executeQuery();

                serviceCosts = conn.prepareStatement("delete from potential_tent where firstN = (?) AND lastN = (?)");
                serviceCosts.setString(1, fN);
                serviceCosts.setString(2, lN);
                rSet = serviceCosts.executeQuery();

                System.out.println(serviceCost);
                serviceCosts = conn.prepareStatement("select lease_num from tenant where firstN = (?) AND lastN = (?)");
                serviceCosts.setString(1, fN);
                serviceCosts.setString(2, lN);
                rSet = serviceCosts.executeQuery();
                if (rSet.next() == false) {
                    System.out.print("Tenant is not recorded.");
                } else {
                    lease_num = rSet.getInt(1);
                }
                
                //creating transaction num
                serviceCosts = conn.prepareStatement("insert into payment (payments) values ((?))");
                monthRent *=12;
                serviceCosts.setInt(1, monthRent);
                    
                rSet = serviceCosts.executeQuery();

                //finding transaction number
                serviceCosts = conn.prepareStatement("select transaction_num from payment where payments = (?)");
                serviceCosts.setInt(1, monthRent);
                rSet = serviceCosts.executeQuery();
                if (rSet.next() == false) {
                    System.out.print("No transaction number.");
                } else {
                    transaction_num = rSet.getInt(1);
                }


                serviceCosts = conn.prepareStatement("select serv_cost from chargable_services where id = (?)");
                serviceCosts.setInt(1, id_num);
                rSet = serviceCosts.executeQuery();
                if (rSet.next() == false) {
                    serv_costPK = 0;
                } else {
                    serv_costPK = rSet.getInt(1);
                }
                
                //WORKS WHEN BELOW IS COMMENTED OUT. 

                serviceCosts = conn.prepareStatement("insert into lease (lease_num,transaction_num,lease_length,serv_cost,month_rent,date_movein) values ((?),(?),(?),(?),(?),(?))");
                    serviceCosts.setInt(1, lease_num);
                    serviceCosts.setInt(2, transaction_num);
                    serviceCosts.setInt(3, leaseLength);
                    serviceCosts.setInt(4, serv_costPK);
                    serviceCosts.setInt(5, (monthRent/12));
                    serviceCosts.setDate(6, moveIn);
                rSet = serviceCosts.executeQuery();

                
                break;

                //fills values for moveOut
                //also output date for moveout using ID
                case 3:
                moveoutDate(scan);
                moveOut = conn.prepareStatement("select lease_length from lease where lease_num = (?)");
                moveOut.setInt(1, lease_num);
                rSet = moveOut.executeQuery();
                if (rSet.next() == false) {
                    leaseLength = 0;
                } else {
                    leaseLength = rSet.getInt(1);
                }

                moveOut = conn.prepareStatement("select date_movein from lease where lease_num = (?)");
                moveOut.setInt(1, lease_num);
                rSet = moveOut.executeQuery();
                if (rSet.next() == false) {
                    System.out.println("Error.");
                } else {
                    moveOutDate = rSet.getDate(1);
                }
                
                
                

                break;

                //breaks
                case 4: 
                    cancel();
                }
            }while(user !=4);
                
                conn.close();
            } catch (SQLException sqle) {
                System.out.println("SQLException : " + sqle);
            }
}



    //method that asks for all information needed for case one
    public void visitData(Scanner scan){

        System.out.println("What is the potential tenant's first name?");
        if(scan.hasNext()){
            fN = scan.nextLine();
        } else {
            System.out.println("Not a string. Please try again.");
            fN= scan.nextLine();
        }

        System.out.println("What is the potential tenant's last name?");

        if(scan.hasNext()){
            lN = scan.nextLine();
        } else {
            System.out.println("Not a string. Please try again.");
            lN= scan.nextLine();
        }
    
        System.out.println("What is the potential tenant's phone number(xxxxxxxxxx)?");
        phoneNum = scan.nextInt();
        String phoneN = String.valueOf(phoneNum);
        if (!(phoneN.matches("\\d{10}"))) {
            System.out.println("You need 10 digits for the phone number. Try again.\n\n");
            System.out.println("What is the potential tenant's phone number(xxxxxxxxxx)?");
        phoneNum = scan.nextInt();
        }

        System.out.println("What is the potential tenant's SSN?(xxxxxxxxxx");
        SSN = scan.nextInt();
        String ssn = String.valueOf(SSN);
        if (!(ssn.matches("\\d{9}"))) {
            System.out.println("You need 9 digits for the Social Security Number. Try again.\n\n");
            System.out.println("What is the potential tenant's SSN?(xxxxxxxxxx");
        SSN = scan.nextInt();
        }
        System.out.println("What is the potential tenant's credit score? ");
        creditScore = scan.nextInt();
        String cs = String.valueOf(creditScore);
        if (!(cs.matches("\\d{3}"))) {
            System.out.println("You need 3 digits for the phone number. Try again.");
        } else if (creditScore < 300 || creditScore > 850){
            System.out.println("Invalid credit score. Please try again\n\n");
            System.out.println("What is the potential tenant's credit score? ");
        creditScore = scan.nextInt();

        }
        System.out.println("What is the potential tenant's date of visit?(yyyy-mm-dd)");
        String dayy = scan.next();

        if(!(dayy.matches("\\d{4}-\\d{2}-\\d{2}"))){
            System.out.println("Date input is not expected. Try again (yyyy-mm-dd)\n\n");
            System.out.println("What is the potential tenant's date of visit?(yyyy-mm-dd)");
            dayy = scan.next();
            }

        day = Date.valueOf(dayy);
        cal = Calendar.getInstance();
        cal.setTime(day);
        cal.add(Calendar.YEAR, 2);

        
    }

    //method that asks for all information for case two
   public void recordLease (Scanner scan){
    System.out.println("What is the tenant's first name?");
    if(scan.hasNext()){
        fN = scan.nextLine();
    } else {
        System.out.println("Not a string. Please try again.");
        fN= scan.nextLine();
    }
    System.out.println("What is the tenant's last name?");
    if(scan.hasNext()){
        lN = scan.nextLine();
    } else {
        System.out.println("Not a string. Please try again.");
        lN= scan.nextLine();
    }
    System.out.println("How long is the lease(months)");
    leaseLength = scan.nextInt();
    if(!(String.valueOf(leaseLength).matches("\\d{2}"))){
        System.out.println("Try again\n\n");
        System.out.println("How long is the lease(months)");
        leaseLength = scan.nextInt();

    }
    //SQL to find out how much they owe for services
    System.out.println("What ID number was assigned to tenant for chargable services");
    id_num = scan.nextInt();
    System.out.println("How much is each month's rent");
    monthRent = scan.nextInt();
    System.out.println("What was the tenant previous address?(Street name)");
    prev_add = scan.next();
    System.out.println("When does tenant expect to move in?");
    String dayy = scan.next();

    if(!(dayy.matches("\\d{4}-\\d{2}-\\d{2}"))){
        System.out.println("Date input is not expected. Try again (yyyy-mm-dd)\n\n");
        System.out.println("What is the potential tenant's date of visit?(yyyy-mm-dd)");
        dayy = scan.next();
        }

    moveIn = Date.valueOf(dayy);
   }


   //uses id number to grab information from tenant
   public void moveoutDate (Scanner scan){
    System.out.println("What is the tenant's lease number?");
    lease_num = scan.nextInt();
    
    //grab lease length from .LEASE
    //grab moveIn from .LEASE

   }


    public void cancel(){
        System.out.println("Goodbye.");
        System.exit(0);
    }
    

    
}
