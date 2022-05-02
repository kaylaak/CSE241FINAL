import java.util.Scanner;
import java.util.*;
import java.sql.*;
import java.sql.Date;

public class NUMAManager {

    Scanner scan = new Scanner(System.in);
    String userN;
    String passW;

    int userinput;

    String city;
    String state;
    String address;
    PreparedStatement prop; 
    ResultSet rSet;
    int key; 
    int room;
    int dumbA;

    int bedR;
    int bathR;
    int sqft; 
    int room_num;
    int avabile;
    PreparedStatement randApart; 

    int gym;
    int doorman;
    int pool;
    PreparedStatement amenities; 




    public NUMAManager(){
        userN =  " ";
        passW = "";

    }

    public NUMAManager(String userN, String passW){
        userN = this.userN;
        passW = this.passW;
    }

    public int setUp(Scanner scan){
        System.out.println("1. To add a new property");
        System.out.println("2. Exit");
        int userinput = scan.nextInt();
        while((userinput<1) || (userinput>2)){
            System.out.println("Number is not an option. Try again.");
            userinput = scan.nextInt();
        }

        if(userinput == 2){
            cancel();
        }


        return userinput;

    }

    public void connect(String userN, String passW){
        try (Connection conn = DriverManager.getConnection(
            "jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241", userN, passW);
            Statement stmt = conn.createStatement();) {
                userinput = setUp(scan);

                //asks Manager where their building is located
                state(scan);
                city(scan);
                address();
               

                //inserts address into oracle
                prop = conn.prepareStatement("insert into PROPERTIES (city,state,address) values ((?),(?),(?))");
                    prop.setString(1, city);
                    prop.setString(2, state);
                    prop.setString(3, address);
            
                    rSet = prop.executeQuery();
                    
                    insertProp();

                    //grabs key # for future SQL entries
                    prop = conn.prepareStatement("Select key from PROPERTIES where address = (?)");
                    prop.setString(1, address);
        
                    rSet = prop.executeQuery();

                    if(rSet.next() == false){
                        System.out.println("No key found.");
                    } else {
                        key = rSet.getInt(1);
            

                    }

                    //asks manager how many rooms does he/she wants
                    howManyRooms();

                    
                    //uses that number (above) and generates that number of room using randoming generated numbers
                    //uses avaiable to indicate to other classes if it is open for renting
                    for(int i = 0; i < room; i++){

                        bedR = randR();
                        bathR = randR();
                        room_num = randRoomNum();
                        sqft = randSqft();
                        avabile = Available();


                        randApart = conn.prepareStatement("insert into apartment (apt_num, key, num_bed, num_bath, sqft,avabile) values ((?),(?),(?),(?),(?),(?))");
                    randApart.setInt(1, room_num);
                    randApart.setInt(2, key);
                    randApart.setInt(3, bedR);
                    randApart.setInt(4, bathR);
                    randApart.setInt(5, sqft);
                    randApart.setInt(6, avabile);
            
                    rSet = randApart.executeQuery();

                    }


                    //asks manager what amenities does the building have
                    gym(scan);
                    pool(scan);
                    doorman(scan);



                    amenities = conn.prepareStatement("insert into amenities(key, pool, gym, doorman) values ((?),(?),(?),(?))");
                    amenities.setInt(1, key);
                    amenities.setInt(2, pool);
                    amenities.setInt(3, gym);
                    amenities.setInt(4, doorman);
            
                    rSet = amenities.executeQuery();



                conn.close();
            } catch (SQLException sqle) {
                System.out.println("SQLException : " + sqle);
            }
    }
    


    public void city(Scanner scan){
        System.out.println("What city?");
            city = scan.next();
    }

    public void state(Scanner scan){
        System.out.println("What state (NY for New York)?");
            state = scan.next();
    }

    public void address(){
        Scanner mom = new Scanner(System.in);
        System.out.println("What is the address?");
            address = mom.nextLine();
            
    }

    public void gym (Scanner scan){
        System.out.println("Enter 1 for yes and 0 for no\n\n");
        System.out.println("Does this property have a gym?");
            gym = scan.nextInt();
        while((gym > 1) || (gym < 0)){
            System.out.println("User did not input 1 or 0. Try again.");
            gym = scan.nextInt();
        } 

    }

    public void pool (Scanner scan){
        System.out.println("Enter 1 for yes and 0 for no\n\n");
        System.out.println("Does this property have a pool?");
    
            pool = scan.nextInt();
        while((pool > 1) ||(pool <0)){
            System.out.println("User did not input 1 or 0. Try again.");
            pool = scan.nextInt();
        } 

    }

    public void doorman (Scanner scan){
        System.out.println("Enter 1 for yes and 0 for no\n\n");
        System.out.println("Does this property have a doorman?");
    
            doorman = scan.nextInt();
        while((doorman > 1) || (doorman < 0)){
            System.out.println("User did not input 1 or 0. Try again.");
            doorman = scan.nextInt();
        }


    }


    public void insertProp(){
        System.out.println("You have now inserted a building located in " + city + ", " + state + " at " +address);
    }


    public void howManyRooms(){
        Scanner dad = new Scanner(System.in);
        System.out.println("How many apartments are in this property?");
            room = dad.nextInt();
        

    }

    public int randR(){
        int rooms; 

        Random rnd = new Random();
        rooms =rnd.nextInt(5);

        return rooms;
    }

    public int randRoomNum(){
        int rooms; 

        Random rnd = new Random();
        rooms = 1 +rnd.nextInt(20000);

        return rooms;
    }

    public int randSqft(){
        int SQft; 

        Random rnd = new Random();
        SQft = 100 +rnd.nextInt(2000);

        return SQft;
    }


    //returns 2 or 1
    //2 indicates apartment is full
    //1 indicates apartment is empty 
    public int Available(){
        int ocupied; 

        Random rnd = new Random();
        ocupied = 1 + rnd.nextInt(2);

        return ocupied;
    }




    public void cancel(){
        System.out.println("Goodbye.");
        System.exit(0);
    }
    

    
}
