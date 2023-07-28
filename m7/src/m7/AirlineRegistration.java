/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m7;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 *
 * @author Hoang Nguyen
 */
public class AirlineRegistration extends Application {
    //Start Variables
    Image logo;
    Label lblTitle, lblSubtitle;
    Button btnRegister, btnView, btnEdit;
    Stage stageStart;
    
    //Login variables
    Label lblError;
    TextField txtUsername, txtPassword;
    Stage stageLogin;
    
    //Registration Variables
    VBox passengersFields;
    ScrollPane scrollPanePassengers;
    Passenger passenger;
    Image imgCustomerInfo;
    ImageView ivCustomerInfo;
    Label lblPassenger;
    Button btnAdd, btnRegisterSubmit, btnBackRegister;
    Scene sceneRegister;
    Stage stageRegister;
    int countPassenger = 0;
    ArrayList<Passenger> passengerList;
    
    //View Variables
    //TableView seatTable;
    ScrollPane scrollView;
    VBox vboxSeatView;
    Label lblViewSeats;
    Button btnViewSeats;
    Scene sceneView;
    Stage stageView;
    final String[][][] seatsList = {
        {{"A1", "B1", "C1"}, {"D1", "E1", "F1"}},
        {{"A2", "B2", "C2"}, {"D2", "E2", "F2"}}
    };
    final int rows = 2, scts = 2, amnt = 3;
    
    //Edit Variables
    VBox vboxPassenger, vboxSeat;
    BorderPane editRoot;
    Scene sceneEdit;
    Stage stageEdit;
    
    //Database stuff
    final String DB_URL = "jdbc:ucanaccess://C:\\Users\\Hoang Nguyen\\Documents\\CSAirlines.accdb";
    
    /*
    //Menus
    Menu menu;
    //MenuItem mnuFile;
    
    //Payment Variables
    Scene scenePayment;
    Stage stagePayment;
    */
    
    @Override
    public void start(Stage primaryStage) {
        stageStart = primaryStage;
        buildStart();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    //builds login page
    public void buildLogin()
    {
        //Displays error message
        lblError = new Label("");
        
        //Displays username and password fields
        Label lblUsername = new Label("Username:");
        lblUsername.setStyle("-fx-font:16px 'Serif Regular'");
        txtUsername = new TextField();
        txtUsername.setStyle("-fx-font:16px 'Serif Regular'");
        
        Label lblPassword = new Label("Password:");
        lblPassword.setStyle("-fx-font:16px 'Serif Regular'");
        txtPassword = new PasswordField();
        txtPassword.setStyle("-fx-font:16px 'Serif Regular'");
        
        //Displays buttons
        Button btnLogin = new Button("Login");
        btnLogin.setStyle("-fx-font:14px 'Verdana'");
        
        Button btnCancel = new Button("Cancel");
        btnCancel.setStyle("-fx-font:14px 'Verdana'");
        
        //adds event actions for the buttons
        btnLogin.setOnAction(event -> login());
        
        btnCancel.setOnAction(event -> {
            stageLogin.close();
            buildStart();
        });
        
        //displays buttons in a horizontal row
        HBox buttons = new HBox(50, btnLogin, btnCancel);
        buttons.setAlignment(Pos.CENTER_LEFT);
        
        //displays controls in a vertical column
        VBox root = new VBox(20, lblError, lblUsername, txtUsername, lblPassword, txtPassword,
        buttons);
        root.setBackground(Background.EMPTY);
        
        //creates scene and sets stage
        Scene scene = new Scene(root, 300, 300);
        scene.setFill(Color.BEIGE);
        stageLogin = new Stage();
        stageLogin.setScene(scene);
        stageLogin.setTitle("Log in");
        stageLogin.show();
    }
    
    //attempt login with the username and password
    public void login()
    {
        lblError.setText("");
        
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        System.out.println(username);
        System.out.println(password);
        try
        {
            //connectint to database
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            Connection connection = DriverManager.getConnection(DB_URL);
            System.out.println("Database Connected");
            
            String queryString = "SELECT * FROM Login";
            //String queryString = "INSERT into PASSENGERS (name, gender, height,"
                    //+ "seatid)values('tony', 'm', '66', 'A1')";

            Statement stmt = connection.createStatement();
            
            //extracts all login information from the LOGIN table
            ResultSet rset = stmt.executeQuery(queryString);
            
            //loops through all usernames and passwords
            while(rset.next())
            {
                String u = rset.getString("Username");
                String p = rset.getString("Password");
                
                //lets the user in if the username and password is found
                //in the database
                if (u.equals(username) && p.equals(password))
                {
                    JOptionPane.showMessageDialog(null, "Welcome " + u);
                    buildEdit();
                    stageLogin.close();
                }
            }
            
            //shows the error message if no match is found
            lblError.setText("Login Failed. Try again");
            lblError.setTextFill(Color.RED);
            
            //Display records on table
            //seatTable.setSelectionModel(DbUtils.resultSetToTableModel(rset));
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, ex.toString());
        }
    }
    
    
    //builds start menu
    public void buildStart()
    {
        //makes the logo
        logo = new Image("file:CSAirlines Logo.png");
        ImageView iv = new ImageView();
        iv.setImage(logo);
        
        //Makes the title and directions
        lblTitle = new Label("Welcome to Computer Science Airlines!");
        lblTitle.setStyle("-fx-font: 28px \"Courier New\";\n"
        + "-fx-font-weight:bold;");
        lblSubtitle = new Label("What would you like to do?");
        lblSubtitle.setStyle("-fx-font: 18px \"Courier New\";");
        
        //adds buttons for the user
        btnRegister = new Button("Register");
        btnRegister.setStyle("-fx-font: 16px \"Verdana\";");
        btnView = new Button("View Seats");
        btnView.setStyle("-fx-font: 16px \"Verdana\";");
        btnEdit = new Button("Edit Data");
        btnEdit.setStyle("-fx-font: 16px \"Verdana\";");
        
        //adds event actions for the buttons
        btnRegister.setOnAction(event -> {
           stageStart.close();
           buildRegistration();
        });
        
        btnView.setOnAction(event -> {
            stageStart.close();
            buildView();
        });
        
        btnEdit.setOnAction(event -> {
            stageStart.close();
            buildLogin();
        });
        
        //removes padding between the title and subtitle
        VBox vboxText = new VBox(5, lblTitle, lblSubtitle);
        vboxText.setAlignment(Pos.CENTER);
        
        //displays controls in a vertical column
        VBox root = new VBox(20, iv, vboxText, btnRegister, btnView, btnEdit);
        root.setAlignment(Pos.CENTER);
        root.setBackground(Background.EMPTY);
        
        //makes scene and sets stage
        Scene scene = new Scene(root, 700, 700);
        scene.setFill(Color.CYAN);
        stageStart.setScene(scene);
        stageStart.setTitle("Start");
        stageStart.show();
    }
    
    
    
    //builds the registration menu
    public void buildRegistration()
    {
        //keeps track of how many passengers are being registered
        countPassenger = 0;
        //stores the Passenger fields in an array
        passengerList = new ArrayList<>();
        
        //loads in customer info icon
        imgCustomerInfo = new Image("file:customerinfo.jpg");
        ivCustomerInfo = new ImageView(imgCustomerInfo);
        ivCustomerInfo.setPreserveRatio(true);
        ivCustomerInfo.setFitWidth(75);
        
        //creates title for the registration form
        lblPassenger = new Label("Passenger(s):");
        lblPassenger.setStyle("-fx-font: 18px \"Arial\";");
        
        //displays the icon and Title horizontally
        HBox hboxheader = new HBox(10, ivCustomerInfo, lblPassenger);
        hboxheader.setAlignment(Pos.CENTER);
        
        //creates a VBox for all the passenger fields
        passengersFields = new VBox(10);
        passengersFields.setAlignment(Pos.CENTER);
        passengersFields.getChildren().addAll(hboxheader);
        passengersFields.setBackground(new Background(new BackgroundFill(Color.LIME, CornerRadii.EMPTY, Insets.EMPTY)));
        
        //creates a scroll pane and adds the passenger fields to it
        scrollPanePassengers = new ScrollPane();
        scrollPanePassengers.setFitToWidth(true);
        scrollPanePassengers.setContent(passengersFields);
        scrollPanePassengers.setBackground(new Background(new BackgroundFill(Color.LIME, CornerRadii.EMPTY, Insets.EMPTY)));
        addPassenger();
        
        //Button adds passengers to the form
        btnAdd = new Button("Add Passenger");
        btnAdd.setStyle("-fx-font: 16px \"Verdana\";");
        btnAdd.setOnAction(event -> addPassenger());
        
        //Button goes back to the start menu
        Button btnBack = new Button("Back");
        btnBack.setStyle("-fx-font: 16px \"Verdana\";");
        btnBack.setOnAction(event -> {
            stageRegister.close();
            buildStart();
        });
        
        //Button submits info
        btnRegisterSubmit = new Button("Register");
        btnRegisterSubmit.setStyle("-fx-font: 16px \"Verdana\";");
        btnRegisterSubmit.setOnAction(event -> registerPassengers());
        
        //displays butons horizontally
        HBox hboxButtons = new HBox(20, btnRegisterSubmit, btnAdd, btnBack);
        hboxButtons.setAlignment(Pos.CENTER);
        hboxButtons.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        
        //displays the controls in a border pane
        //passenger fields are in the center, buttons are on the bottom
        BorderPane root = new BorderPane();
        root.setCenter(scrollPanePassengers);
        root.setBottom(hboxButtons);
        root.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
        
        //creates scene and sets stage
        sceneRegister = new Scene(root, 500, 800);
        stageRegister = new Stage();
        stageRegister.setTitle("Airplane Registration");
        stageRegister.setScene(sceneRegister);
        stageRegister.show();
        
    }
    
    //adds passenger to registration view and the passenger list array
    public void addPassenger()
    {
        Passenger p = new Passenger(true);
        passengersFields.getChildren().add(p);
        passengerList.add(p);
    }
    
    //Submits passenger info to Database
    public void registerPassengers()
    {
        try
        {
            //gets the count of the amount of passengers being registered
            int size = passengerList.size();
            String[] seats = new String[size];
            //loops through each seat from each passenger for input validation
            for (int i = 0; i < size; i ++)
            {
                Passenger passenger = passengerList.get(i);
                String seat = passenger.getSeatID();
                //checks if a seat is empty
                if (seat == null)
                {
                    throw(new Exception("Add seat for passenger " + (i + 1)));
                }
                else
                {
                    //checks if two passengers have the same seat
                    if (Arrays.asList(seats).contains(seat))
                    {
                        throw(new Exception("Two passengers are in seat " + seat
                        + "\nPlease choose different seats"));
                    }
                    else
                    {
                        seats[i] = seat;
                    }
                }
                
                //checks if any fields are left empty
                if (passenger.getName().equals(""))
                {
                    throw(new Exception("Add name for passenger " + (i + 1)));
                }
                System.out.println("name is not null");
                
                if (passenger.getAge().equals(""))
                {
                    throw(new Exception("Add age for passenger " + (i + 1)));
                }
                System.out.println("age is not null");
                
                if (passenger.getFlight() == 0)
                {
                    throw(new Exception("Add flight for passenger " + (i + 1)));
                }
                System.out.println("flight is not null");
            }
            
            //connects to the database
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            Connection connection = DriverManager.getConnection(DB_URL);
            System.out.println("Database Connected");
            
            String queryString;
            Statement stmt;
            PreparedStatement pstmt;
            
            //loops through all the passenger fields
            for (int i = 0; i < size; i ++)
            {
                System.out.println("Passenger: " + (i + 1));
                Passenger passenger = passengerList.get(i);
                System.out.println("Got Passenger: " + (i + 1));
                
                //gets all the values from the fields
                String name = passenger.getName();
                String age = passenger.getAge();
                String seat = passenger.getSeatID();
                int flight = passenger.getFlight();
                boolean carryon = passenger.getCarryOn();
                
                //displays all the values from the passengers to the console
                System.out.println(name);
                System.out.println(age);
                System.out.println(seat);
                System.out.println(flight);
                System.out.println(carryon);
                
                //inserts the values into the Passengers database
                queryString = "INSERT into PASSENGERS (name, Age, SeatID, flight, carryon)"
                    + "values(?, ?, ?, ?, ?)";
                pstmt = connection.prepareStatement(queryString);
                pstmt.setString(1, name);
                pstmt.setString(2, age);
                pstmt.setString(3, seat);
                pstmt.setInt(4, flight);
                pstmt.setBoolean(5, carryon);
                
                pstmt.executeUpdate();
                
                //confirmation that the passenger was recorded
                System.out.println("Passenger " + (i + 1) + " recorded");
                
                //Gets the id of the recently added passenger
                queryString = "SELECT id from Passengers";
                stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);         
                ResultSet rset = stmt.executeQuery(queryString);
                rset.last();
                int id = rset.getInt("id");
                
                //updates the passenger id to the occupied seat 
                //in the seats table
                queryString = "UPDATE Seats SET PassengerID = '" + id + "'"
                        + "where id='" + seat + "'";
                stmt.executeUpdate(queryString);
            }
            JOptionPane.showMessageDialog(null, "Passengers Registered");
            stageRegister.close();
            buildRegistration();
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, ex.toString());
        }
        
    }
    
    
    
    //build edit menu
    public void buildEdit()
    {
        //creates the title and loads the image
        Label lblTitle = new Label("Database Editor");
        lblTitle.setFont(new Font("typewriter", 50));
        
        Image imgPencil = new Image("file:pencilicon.png");
        ImageView ivPencil = new ImageView(imgPencil);
        ivPencil.setFitWidth(70);
        ivPencil.setPreserveRatio(true);
        
        //displays the title and image horizontally
        HBox header = new HBox(5, lblTitle, ivPencil);
        
        //creates all the buttons
        Button btnEditPassenger = new Button("Edit Passenger");
        btnEditPassenger.setStyle("-fx-font:14px 'Verdana'");
        
        Button btnEditSeat = new Button("Edit Seat");
        btnEditSeat.setStyle("-fx-font:14px 'Verdana'");
        
        Button btnBack = new Button("Back");
        btnBack.setStyle("-fx-font:14px 'Verdana'");
        
        //sets action events for the buttons
        btnEditPassenger.setOnAction(event -> {
            buildVBoxPassenger();
        });
        btnEditSeat.setOnAction(event -> {
            buildVBoxSeat();
        });
        btnBack.setOnAction(event -> {
            stageView.close();
            buildStart();
        });
        
        //sets up the passenger vbox
        
        //displays the buttons horizontally and centered
        HBox hboxButtons = new HBox(10, btnEditPassenger, btnEditSeat,
            btnBack);
        hboxButtons.setAlignment(Pos.CENTER);
        
        //sets the header to the top
        //sets the buttons to the bottom
        //will set either the edit passenger vbox or the edit seat vbox
            //to the center
        editRoot = new BorderPane();
        editRoot.setTop(header);
        editRoot.setBottom(hboxButtons);
        editRoot.setBackground(Background.EMPTY);
        
        //makes the scene and sets the stage
        sceneView = new Scene(editRoot, 450, 650);
        sceneView.setFill(Color.LIMEGREEN);
        
        stageView = new Stage();
        stageView.setTitle("Edit Database");
        stageView.setScene(sceneView);
        stageView.show();
    }
    
    //build vbox passenger control
    public void buildVBoxPassenger()
    {
        //Creates an ID label and text field
        Label lblID = new Label("ID: ");
        lblID.setStyle("-fx-font:18px 'Trebuchet MS'");
        TextField txtID = new TextField();
        txtID.setStyle("-fx-font:18px 'Trebuchet MS'");
        
        //displays them horizontally
        HBox hboxid = new HBox(20, lblID, txtID);
        hboxid.setAlignment(Pos.CENTER);
        
        //false means that the passenger will not have a label that records
        //the passenger count
        Passenger p = new Passenger(false);
        
        //creates the update button
        Button btnUpdate = new Button("Update");
        btnUpdate.setStyle("-fx-font: 16px \"Verdana\";");
        
        //sets the update button to the updatePassengers event function
        btnUpdate.setOnAction(event -> {
            updatePassengers(p, txtID.getText());
        });
        
        //displays the controls in a vertical column
        vboxPassenger = new VBox(20, hboxid, p, btnUpdate);
        vboxPassenger.setAlignment(Pos.CENTER);
        
        //sets the vbox to the center of the stage
        editRoot.setCenter(vboxPassenger);
    }
    
    //Update records in the passengers table
    public void updatePassengers(Passenger p, String id)
    {
        //initial variable declaration
        ResultSet rsetInitial;
        String name, age, seatID;
        int flight;
        boolean carryon;
        try
        {
            //connects to the database
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            Connection connection = DriverManager.getConnection(DB_URL);
            System.out.println("Database Connected");
            
            //gets the records of the passenger
            String queryString = "Select * from Passengers where id = '" + id + "'";
            Statement stmt = connection.createStatement();
            
            //Get the initial variables from the record
            rsetInitial = stmt.executeQuery(queryString);
            rsetInitial.next();
            String nameI = rsetInitial.getString("Name");
            String ageI = rsetInitial.getString("Age");
            String seatIDI = rsetInitial.getString("SeatId");
            int flightI = rsetInitial.getInt("Flight");
            System.out.println(seatIDI);
            
            //if any field is left blank, the variable is set to the 
            //initial value
            name = p.getName();
            if (name.equals(""))
            {
                name = nameI;
            }
            System.out.println("name obtained");
            
            age = p.getAge();
            if (age.equals(""))
            {
                age = ageI;
            }
            System.out.println("age obtained");
            
            seatID = p.getSeatID();
            if (seatID.equals(""))
            {
                seatID = seatIDI;
            }
            System.out.println("seat obtained");
            
            flight = p.getFlight();
            if (flight == 0)
            {
                flight = flightI;
            }
            System.out.println("flight obtained");
            
            //must be selected manually, even if the user doesn't want
            //to edit it
            System.out.println(p.getCarryOn());
            carryon = p.getCarryOn();
            System.out.println("carryon obtained");
            
            //displays all the values to the console
            System.out.println(name);
            System.out.println(age);
            System.out.println(seatID);
            System.out.println(flight);
            System.out.println(carryon);
            
            //Updates all the new information to the passenger table
            queryString = "UPDATE PASSENGERS SET name = ?, age = ?,"
                    + "seatID = ?, Flight = ?, CarryOn = ?"
                    + "where id = '" + id + "'";
            
            PreparedStatement pstmt = connection.prepareStatement(queryString);
            pstmt.setString(1, name);
            pstmt.setString(2, age);
            pstmt.setString(3, seatID);
            pstmt.setInt(4, flight);
            pstmt.setBoolean(5, carryon);
            
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Passenger " + id + " Updated");
            
            //frees the previous seat
            stmt = connection.createStatement();
            queryString = "UPDATE Seats set PassengerID = NULL where ID = '" 
                    + rsetInitial.getString("SeatID") + "'";
            stmt.executeUpdate(queryString);
            System.out.println("seat set to null");
            
            //records passenger id to current seast
            queryString = "UPDATE Seats set PassengerID = '" + id + "' where "
                    + "id = '" + seatID + "'";
            stmt.executeUpdate(queryString);
            JOptionPane.showMessageDialog(null, "Seat Recorded");
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, ex.toString());
        }
    }
    
    //build vbox seat control
    public void buildVBoxSeat()
    {
        //Creates the seat label and combo box
        Label lblSeat = new Label("Seat:");
        lblSeat.setStyle("-fx-font:18px 'Trebuchet MS'");
        ComboBox comboSeat = new ComboBox();
        comboSeat.setStyle("-fx-font:18px 'Trebuchet MS'");
        
        //aligns the label and combo box horizontally and centered
        HBox hbox = new HBox(20, lblSeat, comboSeat);
        hbox.setAlignment(Pos.CENTER);
        
        //adds only occupied seats to the combobox
        String[] seats = getSeats(false);
        for(String seat : seats)
        {
            System.out.println(seat);
            comboSeat.getItems().add(seat);
        }
        
        //creates the drop button and sets the action to the dropSeat function
        Button btnDrop = new Button("Drop");
        btnDrop.setStyle("-fx-font:18px 'Trebuchet MS'");
        btnDrop.setOnAction(event -> dropSeat((String)comboSeat.getValue()));
    
        //aligns the controls vertically and centered
        vboxSeat = new VBox(20, hbox, btnDrop);
        vboxSeat.setAlignment(Pos.CENTER);
        
        //sets the vbox to the center of the stage
        editRoot.setCenter(vboxSeat);
    }
    
    //removes the passenger from the seat
    public void dropSeat(String s)
    {
        try
        {
            //connects to the database
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            Connection connection = DriverManager.getConnection(DB_URL);
            System.out.println("Database Connected");
            
            //Gets the records for the seat
            String queryString = "SELECT * FROM SEATS where ID='" + s + "'";
            Statement stmt = connection.createStatement();
            ResultSet rset = stmt.executeQuery(queryString);
            //moves the cursor to the next record
            rset.next();
            
            //saves the id of the passenger
            String idPassenger = rset.getString("PassengerID");
            System.out.println(idPassenger);
            
            //changes the passenger id to null for the corresponding seat
            queryString = "UPDATE Seats set PassengerID=null where "
                    + "id='" + s + "'";
            stmt.executeUpdate(queryString);
            
            //deletes the passenger from the passengers table
            queryString = "DELETE FROM Passengers where id='"
                   + idPassenger + "'";
            stmt.executeUpdate(queryString);
            JOptionPane.showMessageDialog(null, "Seat " + s + " dropped\n"
            + "Passenger " + idPassenger + " removed.");
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, ex.toString());
        }
    }
    
    
    //build view menu
    public void buildView()
    {
        //creates the seats label and loads the seat icon
        lblViewSeats = new Label("Seats: ");
        lblViewSeats.setStyle("-fx-font:24px 'Georgia';");
        
        Image imgSeats = new Image("file:seaticon.png");
        ImageView ivSeats = new ImageView(imgSeats);
        ivSeats.setFitWidth(75);
        ivSeats.setPreserveRatio(true);
        
        //displays the seat icon and label horizontally and centered
        HBox hboxseats = new HBox(ivSeats, lblViewSeats);
        hboxseats.setAlignment(Pos.CENTER);
        
        //seatTable = new TableView();
        scrollView = new ScrollPane();
        scrollView.setPrefWidth(600);
        scrollView.setPrefHeight(300);
        
        //creates the buttons, styles them, and sets action events
        btnViewSeats = new Button("View Seats");
        btnViewSeats.setStyle("-fx-font: 16px \"Verdana\";");
        btnViewSeats.setOnAction(event -> displaySeats());
        
        Button btnBack = new Button("Back");
        btnBack.setStyle("-fx-font: 16px \"Verdana\";");
        btnBack.setOnAction(event -> {
            stageEdit.close();
            buildStart();
        });
        
        //displays the buttons horizontally and centered
        HBox hboxButtons = new HBox(100, btnViewSeats, btnBack);
        hboxButtons.setAlignment(Pos.CENTER);
        
        //displays the content vertically
        VBox root = new VBox(hboxseats, scrollView, hboxButtons);
        root.setBackground(Background.EMPTY);
        
        //creates the scene and sets the stage
        sceneEdit = new Scene(root, 450, 400);
        sceneEdit.setFill(Color.LIME);
        
        stageEdit = new Stage();
        stageEdit.setTitle("Airplane Registration");
        stageEdit.setScene(sceneEdit);
        stageEdit.show();
    }
    
    //displays the seat layout to the view window
    public void displaySeats()
    {
        try
        {
            //connects to the database
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            Connection connection = DriverManager.getConnection(DB_URL);
            System.out.println("Database Connected");
            
            String queryString = "SELECT * FROM Seats";
            Statement stmt = connection.createStatement();
            ResultSet rset;
            
            //creates the vbox for the seat view
            vboxSeatView = new VBox(20);
            
            //loops throw all the predetermined rows
            for(int i = 0; i < rows; i ++)
            {
                //displays the seats in separate groups
                HBox hboxsection = new HBox(50);
                //loops through all the sections
                for(int j = 0; j < scts; j ++)
                {
                    //displays the seats next to each other
                    HBox hboxgroup = new HBox(20);
                    //loops through each seat
                    for (int k = 0; k < amnt; k ++)
                    {
                        //creates a lable and image for the seat
                        Label seatLabel = new Label();
                        Image imgFree = new Image("file:seatfree.png");
                        Image imgOccupied = new Image("file:seatoccupied.png");
                        ImageView iv = new ImageView();
                        iv.setFitWidth(50);
                        iv.setPreserveRatio(true);
                        
                        //gets the seat id from the seats array
                        String seatID = seatsList[i][j][k];
                        
                        //gets the records of the seat
                        queryString = "SELECT * FROM seats where id='" + seatID + "'";
                        rset = stmt.executeQuery(queryString);
                        rset.next();
                        
                        //styles the seat label
                        seatLabel.setFont(new Font("Roboto", 30));
                        
                        //if a seat is unoccupied, the seat will be white
                        if (rset.getString("passengerID") == null)
                        {
                            iv.setImage(imgFree);
                            //label is opposite color
                            seatLabel.setTextFill(Color.color(0, 0, 0));
                            System.out.println("free");
                        }
                        //if a seat is occupied, the seat will be black
                        else
                        {
                            iv.setImage(imgOccupied);
                            //label is opposite color
                            seatLabel.setTextFill(Color.color(1, 1, 1));
                            System.out.println("occupied");
                        }
                        
                        //displays the seat id on the seat label
                        seatLabel.setText(seatID);
                        
                        //stacks the seat label onto the image of the seat
                        StackPane seatView = new StackPane(iv, seatLabel);
                        
                        //adds the seat to the group
                        hboxgroup.getChildren().add(seatView);
                    }
                    //adds the group to the section
                    hboxsection.getChildren().add(hboxgroup);
                }
                //adds the section to the column
                vboxSeatView.getChildren().addAll(hboxsection);
            }
            //sets the seat view to the scroll view
            scrollView.setContent(vboxSeatView);
            
            //confirmation
            JOptionPane.showMessageDialog(null, "Seats added to view");
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, ex.toString());
        }
    }
    
    //gets all occupied or unoccupied seats (depending on the argument)
    public String[] getSeats(boolean free)
    {
        try
        {
            //connection
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            Connection connection = DriverManager.getConnection(DB_URL);
            String queryString;
            
            //query string changes based on argument
            if (free)
            {
                queryString = "Select * from SEATS where passengerID IS NULL";
            }
            else
            {
                queryString = "Select * from SEATS where passengerID IS NOT NULL";
            }
             
            Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            //gets either all occupied or unoccupied seats
            ResultSet rset = stmt.executeQuery(queryString);
            
            //records the size of the rset
            int size = 0;
            rset.last();
            size = rset.getRow();
            //moves the cursor to the start
            rset.beforeFirst();
            
            //creates a seats array, holding all the seat ids
            String[] seats = new String[size];
            
            //adds the seat ids to the seats array
            while(rset.next())
            {
                //rset is 1 index, arrays are 0 index
                int i = rset.getRow() - 1;
                seats[i] = rset.getString("id");
            }
            //returns the array of occupied/unoccupied seats
            return seats;
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, ex.toString());
        }
        //returns an array of nothing if an error is thrown
        return new String[0];
    }
    
    /*
    public void buildPayment()
    {
        Label lblMethod = new Label("Select a payment method");
        ComboBox cmbMethod = new ComboBox();
        //check if numbers line up?
        cmbMethod.getItems().addAll("Visa", "MasterCard");
        
        Label lblCardNumber = new Label("Enter the card number");
        
    }
    
    public void buildMenu()
    {
    
    }
    */
    
    class Passenger extends GridPane
    {
        //declares all controls
        Image imgPassenger, imgFlight, imgSeat, imgCarryOn;
        ImageView ivPassenger, ivFlight, ivSeat, ivCarryOn;
        Label lblCount, lblName, lblTicket, lblSeat, lblFlight, lblCarryOn;
        TextField txtName, txtSeat, txtFlight;
        RadioButton rdoAdult, rdoChild;
        CheckBox chkCarryOn;
        ComboBox cmbSeat;
        public Passenger(boolean count)
        {
            //adds one to the passenger count
            countPassenger ++;
            
            //displays the count if count = true, otherwise skips the label
            if (count)
            {
                lblCount = new Label("Passenger " + countPassenger + ":");
                lblCount.setStyle("-fx-font:16px \"Lucida Fax\";");
            }
            else
            {
                lblCount = new Label(""); //skips the label
            }
            
            //creates the name label and icon
            lblName = new Label("Name:");
            lblName.setStyle("-fx-font:14px \"Lucida Fax\";");
            imgPassenger = new Image("file:customer.png");
            ivPassenger = new ImageView(imgPassenger);
            ivPassenger.setPreserveRatio(true);
            ivPassenger.setFitWidth(50);
            VBox vboxpersonView = new VBox(5, lblName, ivPassenger);
            
            //creates the flight label and icon
            lblFlight = new Label("Flight:");
            lblFlight.setStyle("-fx-font:14px \"Lucida Fax\";");
            imgFlight = new Image("file:flighticon.png");
            ivFlight = new ImageView(imgFlight);
            ivFlight.setPreserveRatio(true);
            ivFlight.setFitWidth(50);
            VBox vboxFlight = new VBox(lblFlight, ivFlight);
            
            //creates the seat label and icon
            lblSeat = new Label("Seat:");
            lblSeat.setStyle("-fx-font:14px \"Lucida Fax\";");
            imgSeat = new Image("file:seaticon.png");
            ivSeat = new ImageView(imgSeat);
            ivSeat.setPreserveRatio(true);
            ivSeat.setFitWidth(50);
            VBox vboxSeat = new VBox(lblSeat, ivSeat);
            
            //creates the carry on label and icon
            lblCarryOn = new Label("Carry On:");
            lblCarryOn.setStyle("-fx-font:14px \"Lucida Fax\";");
            imgCarryOn = new Image("file:luggageicon.png");
            ivCarryOn = new ImageView(imgCarryOn);
            ivCarryOn.setPreserveRatio(true);
            ivCarryOn.setFitWidth(50);
            VBox vboxCarryOn = new VBox(lblCarryOn, ivCarryOn);
            
            //creates the text fields, radio buttons, combo boxe, and check box
            txtName = new TextField();
            txtName.setStyle("-fx-font:14px \"Lucida Fax\";");
            
            txtFlight = new TextField();
            txtFlight.setStyle("-fx-font:14px \"Lucida Fax\";");
            
            rdoAdult = new RadioButton("Adult (18 and up)");
            rdoAdult.setStyle("-fx-font:14px \"Lucida Fax\";");
            rdoChild = new RadioButton("Child");
            rdoChild.setStyle("-fx-font:14px \"Lucida Fax\";");
            ToggleGroup tg = new ToggleGroup();
            rdoAdult.setToggleGroup(tg);
            rdoChild.setToggleGroup(tg);
            
            cmbSeat = new ComboBox();
            cmbSeat.setStyle("-fx-font:14px \"Lucida Fax\";");
            String[] seatsAvaliable = getSeats(true);
            for (String seat : seatsAvaliable)
            {
                cmbSeat.getItems().add(seat);
            }
            
            chkCarryOn = new CheckBox("Carry On");
            chkCarryOn.setStyle("-fx-font:14px \"Lucida Fax\";");
            
            //aligns the vbox to the center
            setAlignment(Pos.CENTER);
            setPadding(new Insets(20));
            
            //makes name and age choices apepar in a column
            VBox vboxperson = new VBox(5, txtName, rdoAdult, rdoChild);
            
            //styles and formats the vbox
            setStyle("-fx-border-color: rgb(0, 150, 0);\n-fx-border-insets:5;\n"
            + "-fx-border-width: 5;\n-fx-border-style:dotted inside;\n");
            setBackground(new Background(new BackgroundFill(Color.LAVENDER, CornerRadii.EMPTY, new Insets(10))));
            //setPadding(new Insets(20));
            setHgap(100);
            setVgap(10);
            
            //formatting, makes text appear left to right
            addColumn(0, lblCount, vboxpersonView, vboxFlight, vboxSeat, vboxCarryOn);
            GridPane.setValignment(vboxpersonView, VPos.TOP);//moves name label to top
            
            //new label skips a row
            addColumn(1, new Label(), vboxperson, txtFlight, cmbSeat, chkCarryOn);
            if (!count)
                addRow(5, new Label());
        }
        
        //returns the name
        public String getName()
        {
            return txtName.getText();
        }
        
        //returns the age group, nothing if not selected
        public String getAge()
        {
            if (rdoAdult.isSelected())
            {
                return "Adult";
            }
            else if (rdoChild.isSelected()) 
            {
                return "Child";
            }
            else
            {
                return "";
            }
        }
        
        //returns the seat id
        public String getSeatID()
        {
            return (String)cmbSeat.getValue();
        }
        
        //returns the flight number, 0 on fail
        public int getFlight()
        {
            if (txtFlight.getText().equals(""))
            {
                return 0;
            }
            return Integer.parseInt(txtFlight.getText());
        }
        
        //returns the state of the carry on check box
        public boolean getCarryOn()
        {
            return chkCarryOn.isSelected();
        }
    }
}
