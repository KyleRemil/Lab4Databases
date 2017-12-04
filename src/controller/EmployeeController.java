package controller;

import application.DbConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import jdk.nashorn.api.tree.WhileLoopTree;
import model.Department;
import model.Employee;

import java.sql.*;
import java.util.ArrayList;

public class EmployeeController {
    @FXML
    private TableView<Employee> employeeTableView;
    @FXML
    private TableColumn<Employee, String> fname, lname;
    @FXML
    private TableColumn<Employee, Integer> supper_ssn, dno, ssn;

    @FXML
    private TextField fnameInput, lnameInput, super_ssnInput, ssnInput, dnoInput;
    @FXML
    private TextArea MetaDataAreaEmployee, javaErrors;
    private Stage stage;
    private AnchorPane root;
    private Scene scene;
    @FXML

    String errors = "";

    public void initialize() {
        employeeTableView.setItems(getEmployeeList());

        fname.setCellValueFactory(new PropertyValueFactory<Employee, String>("Fname"));
        lname.setCellValueFactory(new PropertyValueFactory<Employee, String>("Lname"));
        supper_ssn.setCellValueFactory(new PropertyValueFactory<Employee, Integer>("Supper_ssn"));
        dno.setCellValueFactory(new PropertyValueFactory<Employee, Integer>("Dno"));
        ssn.setCellValueFactory(new PropertyValueFactory<Employee, Integer>("Ssn"));
    }
    @SuppressWarnings("resource")
    public void createDatabase(ActionEvent event) throws Exception{
        String employeeCreator = "CREATE TABLE `employee` (  `Fname` varchar(15) NOT NULL,  `Lname` varchar(15) NOT NULL,  `Super_ssn` varchar(9) DEFAULT NULL,  `Dno` int(11) NOT NULL DEFAULT '1',  `Ssn` varchar(9) DEFAULT NULL,  PRIMARY KEY (`Fname`),  UNIQUE KEY `Ssn` (`Ssn`),  KEY `Super_ssn` (`Super_ssn`),  KEY `Dno` (`Dno`),  CONSTRAINT `employee_ibfk_1` FOREIGN KEY (`Super_ssn`) REFERENCES `employee` (`Ssn`)) ENGINE=InnoDB DEFAULT CHARSET=latin1";
        Statement stmt = null;


        try {
            Connection conn = DbConnector.getConnection();
            PreparedStatement displayprofile = conn.prepareStatement("Show create table employee;");
            ResultSet resultSet = displayprofile.executeQuery();
            while (resultSet.next()) {

                String database = resultSet.getString("Create Table");

                // if the table is empty
                if (database != "Table 'company.employee' doesn't exist") {
                    MetaDataAreaEmployee.setText("The employee table already exists");
                    System.out.println("The employee table already exists");
                    // System.out.println(database);
                } else {

                    System.out.println("Database is missing the employee table");
                    String query = employeeCreator;
                    stmt = conn.createStatement();
                    resultSet = stmt.executeQuery(query);
                    System.out.println("employee table added");

                }
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    public ObservableList<Employee>/*<String>*/  getEmployeeList()
    {
        ObservableList<Employee>/*<String>*/ employees = FXCollections.observableArrayList();

        String SQLQuery = "select Fname, Lname, Super_ssn, Dno, Ssn FROM employee";

        try(
                Connection conn = DbConnector.getConnection();
                PreparedStatement displayprofile = conn.prepareStatement(SQLQuery);
                ResultSet resultSet = displayprofile.executeQuery()
        ){
            while (resultSet.next()){
                employees.add(new Employee(resultSet.getString("Fname"), resultSet.getString("Lname"),
                        resultSet.getInt("Super_ssn"), resultSet.getInt("Dno"),
                        resultSet.getInt("Ssn")) );

            }
        }catch(SQLException ex){
            DbConnector.displayException(ex);
            return null;
        }
        return employees;
    }
    public void insertEmployee(){
        String fname = fnameInput.getText();
        String lname = lnameInput.getText();
        String supper_ssn = super_ssnInput.getText();
        String dno = dnoInput.getText();
        String ssn = ssnInput.getText();
        //TODO to input employee super_ssn must match an existing employee SSN
        //TODO check to make sure employee SSN is not already in the table

        System.out.println(
                "fname: " + fname +"\n"+
                        "lname: "+ lname+"\n"+
                        "supper_ssn: "+ supper_ssn+"\n"+
                        "dno: "+ dno+"\n"+
                        "ssn: "+ ssn+"\n"
        );
        if(validateFname(fname) == false || validateLname(lname) == false
                || validateSuper_ssn(supper_ssn) == false || validateDno(dno) == false || validateSsn(ssn) == false){
            validationErrors();
            errors = "";
            return;
        }

        //Fname: varchar(15), Lname: varchar(15), Supper_ssn: varchar(9), Dno: int(11), Ssn: varchar(9)
        /*
        INSERT INTO employee(Fname, Lname, Super_ssn, Dno, Ssn)
        VALUES('Testing', 'Tezting', '987654321', 81, '555333222');
        */

        String sqlQuery = "INSERT INTO employee(Fname, Lname, Super_ssn, Dno, Ssn) " +
                "VALUES("+"'"+fname+"', '"+lname+"', '"+supper_ssn+"', "+dno+", '"+ssn+"');";
        String checkFnameQuery = "SELECT Fname FROM employee WHERE Fname ='"+fname+"'";
        String checkSuper_ssn = "SELECT Mgr_ssn FROM department WHERE Mgr_ssn ='"+supper_ssn+"'";
        String checkDnoQuery = "SELECT Dnumber From department WHERE Dnumber ="+dno;

        try {
            Connection conn = DbConnector.getConnection();
            PreparedStatement checkValue = conn.prepareStatement(checkFnameQuery);
            ResultSet resultSet = checkValue.executeQuery();
            while(resultSet.next()) {
                System.out.println(resultSet.getString("Fname"));
                if (resultSet.getString("Fname").equals(fname)) {
                    errors = errors + "Primary Key " + fname + " Already Exists!";
                    javaErrors.setText(errors);
                    errors = "";
                    return;
                }
            }
            PreparedStatement checkSuper = conn.prepareStatement(checkSuper_ssn);
            ResultSet superResults = checkSuper.executeQuery();
            if(superResults.next() == false){
                errors = errors + "Referential Constraint " + supper_ssn + " DNE \n";
                javaErrors.setText(errors);
                errors = "";
                return;
            }
            PreparedStatement checkDno = conn.prepareStatement(checkDnoQuery);
            ResultSet dnoResults = checkDno.executeQuery();
                if (dnoResults.next() == false) {
                    errors = errors + "Referential Constraint " + dno + " DNE";
                    javaErrors.setText(errors);
                    errors = "";
                    return;
                }
            PreparedStatement displayprofile = conn.prepareStatement(sqlQuery);
            displayprofile.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        initialize();
    }
    public void delete(){
        String fname = fnameInput.getText();
        String lname = lnameInput.getText();
        String supper_ssn = super_ssnInput.getText();
        String dno = dnoInput.getText();
        String ssn = ssnInput.getText();

        /*
        DELETE FROM testing
        WHERE testInt = 3
        AND
        testVarchar = 'test3';

        INSERT INTO department(Dname, Dnumber, Mgr_ssn, Dlocation)
        VALUES('testing1', 66, 111222333, 'testLocation1'),
        ('testing2', 67, 222333444, 'testLocation2')
         */
        String sqlQuery = "DELETE FROM employee WHERE Fname ='"+fname+"' AND Lname ='"+lname+"' AND Super_ssn ='"+supper_ssn+
                "' AND Dno ="+dno+" AND Ssn ='"+ssn+"'";
        try {
            Connection conn = DbConnector.getConnection();
            PreparedStatement displayprofile = conn.prepareStatement(sqlQuery);
            displayprofile.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        initialize();

    }
    private boolean validateFname(String fname){
        if(fname.length() > 15) {
            System.out.println("fname Length");
            errors = errors + "invalid fname length \n";
            return false;
        }
        ArrayList<Character> chars = new ArrayList<>();
        for(int i = 0; i <fname.length(); i++){
            chars.add(fname.charAt(i));
        }
        for(int i = 0; i < chars.size(); i++){
            int ascii = (int)chars.get(i);
            //A: 65, Z: 90, a:97, z:122
            if((ascii < 65 || ascii > 90) && (ascii < 97 || ascii > 122)){
                System.out.println("invalid fname character");
                errors = errors + "invalid fname character \n";
                return false;
            }

        }
        return true;
    }
    private boolean validateLname(String lname){
        if(lname.length() > 15) {
            System.out.println("Lname Length");
            errors = errors + "lname length \n";
            return false;
        }
        ArrayList<Character> chars = new ArrayList<>();
        for(int i = 0; i <lname.length(); i++){
            chars.add(lname.charAt(i));
        }
        for(int i = 0; i < chars.size(); i++){
            int ascii = (int)chars.get(i);
            //A: 65, Z: 90, a:97, z:122
            if((ascii < 65 || ascii > 90) && (ascii < 97 || ascii > 122)){
                System.out.println("invalid Lname character");
                errors = errors + "Lname character \n";
                return false;
            }

        }
        return true;
    }
    private boolean validateSuper_ssn(String supper_ssn){
        if(supper_ssn.length() > 9) {
            System.out.println("supper_ssn Length");
            errors = errors + "supper_ssn length \n";
            return false;
        }
        ArrayList<Character> chars = new ArrayList<>();
        for(int i = 0; i <supper_ssn.length(); i++){
            chars.add(supper_ssn.charAt(i));
        }
        for(int i = 0; i < chars.size(); i++){
            int ascii = (int)chars.get(i);
            //A: 65, Z: 90, a:97, z:122
            if((ascii < 48 || ascii > 57)){
                System.out.println("invalid supper_ssn character");
                errors = errors + "supper_ssn character \n";
                return false;
            }

        }
        return true;
    }
    private boolean validateDno(String dno){
        if(dno.length() > 11) {
            System.out.println("invalidDnumber Length");
            errors = errors + "Dnumber length \n";
            return false;
        }
        //0: 48, 9: 57
        ArrayList<Character> characters = new ArrayList<>();
        String dNumString = String.valueOf(dno);
        for(int i = 0; i < dNumString.length(); i++){
            characters.add(dno.charAt(i));
        }
        for(int i =0; i < dNumString.length(); i ++){
            int ascii = (int)characters.get(i);
            if(ascii < 48 || ascii > 57){
                System.out.println("invalidDnumber character");
                errors = errors + "Dnumber character \n";
                return false;
            }

        }
        return true;
    }
    private boolean validateSsn(String ssn){
        if(ssn.length() > 9) {
            System.out.println("invalid dnum Length");
            errors = errors + "dnum length \n";
            return false;
        }
        ArrayList<Character> chars = new ArrayList<>();
        for(int i = 0; i <ssn.length(); i++){
            chars.add(ssn.charAt(i));
        }
        for(int i = 0; i < chars.size(); i++){
            int ascii = (int)chars.get(i);
            //A: 65, Z: 90, a:97, z:122
            if(ascii < 48 || ascii > 57){
                System.out.println("invalid dnum character");
                errors = errors + "dnum character \n";
                return false;
            }

        }
        return true;
    }
    public void metdataMethod(ActionEvent event) throws SQLException {
        String SQLQuery = "show create table employee;";
        Connection conn = DbConnector.getConnection();
        PreparedStatement displayprofile = conn.prepareStatement(SQLQuery);
        ResultSet resultSet = displayprofile.executeQuery();
    	if(resultSet.next()) {
    		MetaDataAreaEmployee.setText(resultSet.getString(2));
    	}
    }
    private void validationErrors(){
        javaErrors.setText(errors);
        System.out.println(errors);
    }
    public void toProjectView(ActionEvent event) throws Exception {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("../view/projectView.fxml"));
        scene = new Scene(root);
        stage.setScene(scene);
    }
    public void toEmployeeView(ActionEvent event) throws Exception {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("../view/employeeView.fxml"));
        scene = new Scene(root);
        stage.setScene(scene);
    }
    public void toDepartmentView(ActionEvent event) throws Exception {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("../view/departmentView.fxml"));
        scene = new Scene(root);
        stage.setScene(scene);
    }
    public void toWorksonView(ActionEvent event) throws Exception {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("../view/worksonView.fxml"));
        scene = new Scene(root);
        stage.setScene(scene);
    }
    public void toCustomQuery(ActionEvent event) throws Exception {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("../view/queryView.fxml"));
        scene = new Scene(root);
        stage.setScene(scene);//kjh
    }
}
