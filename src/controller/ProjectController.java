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
import model.Project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProjectController {
    @FXML
    private TableView<Project> projectTableView;
    @FXML
    private TableColumn<Project, String> pName, pLocation;
    @FXML
    private TableColumn<Project, Integer> pNumber, dNum;

    @FXML
    private TextField pnameInput, pnumberInput, plocationInput, dnumberInput;

    private Stage stage;
    private AnchorPane root;
    private Scene scene;

    public void initialize() {
        projectTableView.setItems(getProjectList());

        pName.setCellValueFactory(new PropertyValueFactory<Project, String>("Pname"));
        pNumber.setCellValueFactory(new PropertyValueFactory<Project, Integer>("Pnumber"));
        pLocation.setCellValueFactory(new PropertyValueFactory<Project, String>("Plocation"));
        dNum.setCellValueFactory(new PropertyValueFactory<Project, Integer>("Dnum"));
    }
    public ObservableList<Project>/*<String>*/  getProjectList()
    {
        ObservableList<Project>/*<String>*/ projects = FXCollections.observableArrayList();

        String SQLQuery = "select Pname, Pnumber, Plocation, Dnum FROM project";

        try(
                Connection conn = DbConnector.getConnection();
                PreparedStatement displayprofile = conn.prepareStatement(SQLQuery);
                ResultSet resultSet = displayprofile.executeQuery()
        ){
            while (resultSet.next()){
                projects.add(new Project(resultSet.getString("Pname"), resultSet.getInt("Pnumber"),
                        resultSet.getString("Plocation"), resultSet.getInt("Dnum")) );

            }
        }catch(SQLException ex){
            DbConnector.displayException(ex);
            return null;
        }
        return projects;
    }
    public void insertProject(){
       String pname = pnameInput.getText();
       String pnumber = pnumberInput.getText();
       String plocation = plocationInput.getText();
       String dnum = dnumberInput.getText();
        //TODO to input employee super_ssn must match an existing employee SSN
        //TODO check to make sure employee SSN is not already in the table

//        System.out.println(
//                "fname: " + fname +"\n"+
//                        "lname: "+ lname+"\n"+
//                        "supper_ssn: "+ supper_ssn+"\n"+
//                        "dno: "+ dno+"\n"+
//                        "ssn: "+ ssn+"\n"
//        );
        if(validatePname(pname) == false || validatePnumber(pnumber) == false
                || validatePlocation(plocation) == false || validateDnumber(dnum) == false){
            return;
        }

        //Pname: varchar(15), Pnumber: int(11), Plocation: varchar(15), Dnum: int(11)
        /*
        INSERT INTO project(Pname, Pnumber, Plocation, Dnum)
        VALUES('Testing', 66, 'CandyLand', 4);
        */

        String sqlQuery = "INSERT INTO project(Pname, Pnumber, Plocation, Dnum) " +
                "VALUES("+"'"+pname+"', "+pnumber+", '"+plocation+"', "+dnum+");";

        try {
            Connection conn = DbConnector.getConnection();
            PreparedStatement displayprofile = conn.prepareStatement(sqlQuery);
            displayprofile.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        initialize();
    }
    private boolean validatePname(String pname){
        if(pname.length() > 15) {
            System.out.println("pname Length");
            return false;
        }
        ArrayList<Character> chars = new ArrayList<>();
        for(int i = 0; i <pname.length(); i++){
            chars.add(pname.charAt(i));
        }
        for(int i = 0; i < chars.size(); i++){
            int ascii = (int)chars.get(i);
            //A: 65, Z: 90, a:97, z:122
            if((ascii < 65 || ascii > 90) && (ascii < 97 || ascii > 122)){
                System.out.println("invalid pname character");
                return false;
            }

        }
        return true;
    }
    private boolean validatePnumber(String pnumber){
        if(pnumber.length() > 11) {
            System.out.println("invalid pnumber Length");
            return false;
        }
        //0: 48, 9: 57
        ArrayList<Character> characters = new ArrayList<>();
        String dNumString = String.valueOf(pnumber);
        for(int i = 0; i < dNumString.length(); i++){
            characters.add(pnumber.charAt(i));
        }
        for(int i =0; i < dNumString.length(); i ++){
            int ascii = (int)characters.get(i);
            if(ascii < 48 || ascii > 57){
                System.out.println("invalid pnumber character");

                return false;
            }

        }
        return true;
    }
    private boolean validatePlocation(String plocation){
        if(plocation.length() > 15) {
            System.out.println("plocation Length");
            return false;
        }
        ArrayList<Character> chars = new ArrayList<>();
        for(int i = 0; i <plocation.length(); i++){
            chars.add(plocation.charAt(i));
        }
        for(int i = 0; i < chars.size(); i++){
            int ascii = (int)chars.get(i);
            //A: 65, Z: 90, a:97, z:122
            if((ascii < 65 || ascii > 90) && (ascii < 97 || ascii > 122)){
                System.out.println("invalid plocation character");
                return false;
            }

        }
        return true;
    }
    private boolean validateDnumber(String dnum){
        if(dnum.length() > 11) {
            System.out.println("invalid Dnumber Length");
            return false;
        }
        //0: 48, 9: 57
        ArrayList<Character> characters = new ArrayList<>();
        String dNumString = String.valueOf(dnum);
        for(int i = 0; i < dNumString.length(); i++){
            characters.add(dnum.charAt(i));
        }
        for(int i =0; i < dNumString.length(); i ++){
            int ascii = (int)characters.get(i);
            if(ascii < 48 || ascii > 57){
                System.out.println("invalid Dnumber character");

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
