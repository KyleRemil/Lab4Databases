package controller;

import application.DbConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Project;

import java.sql.*;
import java.util.ArrayList;

public class ProjectController {
    @FXML
    private TableView<Project> projectTableView;
    @FXML
    private TableColumn<Project, String> pName, pLocation;
    @FXML
    private TableColumn<Project, Integer> pNumber, dNum;
    @FXML
    private TextArea MetaDataProject, javaErrors;
    private Button metadatabutton;

    @FXML
    private TextField pnameInput, pnumberInput, plocationInput, dnumberInput;

    private Stage stage;
    private AnchorPane root;
    private Scene scene;

    String errors = "";

    @FXML
    public void initialize() {
        projectTableView.setItems(getProjectList());

        pName.setCellValueFactory(new PropertyValueFactory<Project, String>("Pname"));
        pNumber.setCellValueFactory(new PropertyValueFactory<Project, Integer>("Pnumber"));
        pLocation.setCellValueFactory(new PropertyValueFactory<Project, String>("Plocation"));
        dNum.setCellValueFactory(new PropertyValueFactory<Project, Integer>("Dnum"));
    }
    @SuppressWarnings("resource")
    public void createDatabase(ActionEvent event) throws Exception{
        String projectCreator = "CREATE TABLE `project` (  `Pname` varchar(15) NOT NULL,  `Pnumber` int(11) NOT NULL,  `Plocation` varchar(15) DEFAULT NULL,  `Dnum` int(11) NOT NULL,  PRIMARY KEY (`Pnumber`),  UNIQUE KEY `Pname` (`Pname`),  KEY `Dnum` (`Dnum`),  CONSTRAINT `project_ibfk_1` FOREIGN KEY (`Dnum`) REFERENCES `department` (`Dnumber`)) ENGINE=InnoDB DEFAULT CHARSET=latin1";
        Statement stmt = null;


        try {
            Connection conn = DbConnector.getConnection();
            PreparedStatement displayprofile = conn.prepareStatement("Show create table employee;");
            ResultSet resultSet = displayprofile.executeQuery();
            while (resultSet.next()) {

                String database = resultSet.getString("Create Table");

                // if the table is empty
                if (database != "Table 'company.project' doesn't exist") {
                    MetaDataProject.setText("The project table already exists");
                    System.out.println("The project table already exists");
                    // System.out.println(database);
                } else {

                    System.out.println("Database is missing the project table");
                    String query = projectCreator;
                    stmt = conn.createStatement();
                    resultSet = stmt.executeQuery(query);
                    System.out.println("project table added");

                }
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

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
    public void metdataMethod(ActionEvent event) throws SQLException {
        String SQLQuery = "show create table project;";
        Connection conn = DbConnector.getConnection();
        PreparedStatement displayprofile = conn.prepareStatement(SQLQuery);
        ResultSet resultSet = displayprofile.executeQuery();
    	if(resultSet.next()) {
    		MetaDataProject.setText(resultSet.getString(2));
    	}
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
            validationErrors();
            errors = "";
            return;
        }

        //Pname: varchar(15), Pnumber: int(11), Plocation: varchar(15), Dnum: int(11)
        /*
        INSERT INTO project(Pname, Pnumber, Plocation, Dnum)
        VALUES('Testing', 66, 'CandyLand', 4);


        */

        String sqlQuery = "INSERT INTO project(Pname, Pnumber, Plocation, Dnum) " +
                "VALUES("+"'"+pname+"', "+pnumber+", '"+plocation+"', "+dnum+");";
        String checkPnumberQuery = "SELECT Pnumber FROM project WHERE Pnumber ="+pnumber;

        String checkDnumQuery = "SELECT Dnumber FROM department WHERE Dnumber ="+dnum;
        String checkPnameQuery = "SELECT Pname FROM project WHERE Pname ='"+pname+"'";

        try {
            Connection conn = DbConnector.getConnection();
            PreparedStatement checkPnumber = conn.prepareStatement(checkPnumberQuery);
            ResultSet pnumberResults = checkPnumber.executeQuery();
            while (pnumberResults.next()){
                if(pnumberResults.getInt("Pnumber") == Integer.valueOf(pnumber)){
                    errors = errors + "Primary Key "+pnumber+" Already Exists! \n";
                    javaErrors.setText(errors);
                    errors = "";
                    return;
                }
            }
            PreparedStatement checkDnum = conn.prepareStatement(checkDnumQuery);
            ResultSet dnumResults = checkDnum.executeQuery();
            if(dnumResults.next() == false){
                errors = errors + "Referential key constraint "+ dnum +" DNE \n";
                javaErrors.setText(errors);
                return;
            }
            PreparedStatement checkPname = conn.prepareStatement(checkPnameQuery);
            ResultSet pnameResults = checkPname.executeQuery();
            while (pnameResults.next()){
                if(pnameResults.getString("Pname").equals(pname)){
                    errors = errors + "Project Name "+pname+" already exists \n";
                    javaErrors.setText(errors);
                    errors = "";
                    return;
                }
            }

            PreparedStatement displayprofile = conn.prepareStatement(sqlQuery);
            displayprofile.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        initialize();
    }
    public void delete(){
        String pname = pnameInput.getText();
        String pnumber = pnumberInput.getText();
        String plocation = plocationInput.getText();
        String dnum = dnumberInput.getText();

        /*
        DELETE FROM testing
        WHERE testInt = 3
        AND
        testVarchar = 'test3';

        INSERT INTO department(Dname, Dnumber, Mgr_ssn, Dlocation)
        VALUES('testing1', 66, 111222333, 'testLocation1'),
        ('testing2', 67, 222333444, 'testLocation2')
         */
        String sqlQuery = "DELETE FROM project WHERE Pname ='"+pname+"' AND Pnumber ="+pnumber+" AND Plocation ='"+plocation+
                "' AND Dnum ="+dnum;
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
            errors = errors + "pname length \n";
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
                errors = errors + "pname character \n";
                return false;
            }

        }
        return true;
    }
    private boolean validatePnumber(String pnumber){
        if(pnumber.length() > 11) {
            System.out.println("invalid pnumber Length");
            errors = errors + "pnumber length \n";
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
                errors = errors + "pnumber character \n";

                return false;
            }

        }
        return true;
    }
    private boolean validatePlocation(String plocation){
        if(plocation.length() > 15) {
            System.out.println("plocation Length");
            errors = errors + "plocation length \n";
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
                errors = errors + "plocation character \n";
                return false;
            }

        }
        return true;
    }
    private boolean validateDnumber(String dnum){
        if(dnum.length() > 11) {
            System.out.println("invalid Dnumber Length");
            errors = errors + "dnumber length \n";
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
                errors = errors + "dnumber character \n";

                return false;
            }

        }
        return true;
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
