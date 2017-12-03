package controller;

import application.DbConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Department;
import model.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    private Stage stage;
    private AnchorPane root;
    private Scene scene;

    public void initialize() {
        employeeTableView.setItems(getEmployeeList());

        fname.setCellValueFactory(new PropertyValueFactory<Employee, String>("Fname"));
        lname.setCellValueFactory(new PropertyValueFactory<Employee, String>("Lname"));
        supper_ssn.setCellValueFactory(new PropertyValueFactory<Employee, Integer>("Supper_ssn"));
        dno.setCellValueFactory(new PropertyValueFactory<Employee, Integer>("Dno"));
        ssn.setCellValueFactory(new PropertyValueFactory<Employee, Integer>("Ssn"));
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
            return;
        }

        //Fname: varchar(15), Lname: varchar(15), Supper_ssn: varchar(9), Dno: int(11), Ssn: varchar(9)
        /*
        INSERT INTO employee(Fname, Lname, Super_ssn, Dno, Ssn)
        VALUES('Testing', 'Tezting', '987654321', 81, '555333222');
        */

        String sqlQuery = "INSERT INTO employee(Fname, Lname, Super_ssn, Dno, Ssn) " +
                "VALUES("+"'"+fname+"', '"+lname+"', '"+supper_ssn+"', "+dno+", '"+ssn+"');";

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
                return false;
            }

        }
        return true;
    }
    private boolean validateLname(String lname){
        if(lname.length() > 15) {
            System.out.println("Lname Length");
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
                return false;
            }

        }
        return true;
    }
    private boolean validateSuper_ssn(String supper_ssn){
        if(supper_ssn.length() > 9) {
            System.out.println("supper_ssn Length");
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
                return false;
            }

        }
        return true;
    }
    private boolean validateDno(String dno){
        if(dno.length() > 11) {
            System.out.println("invalidDnumber Length");
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

                return false;
            }

        }
        return true;
    }
    private boolean validateSsn(String ssn){
        if(ssn.length() > 9) {
            System.out.println("invalid dnum Length");
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
                return false;
            }

        }
        return true;
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
}
