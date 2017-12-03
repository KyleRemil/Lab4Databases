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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Department;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MainMenuController {

    @FXML
    private TableView<Department> departmentTable;
    @FXML
    private TableColumn<Department, String> Dname;
    @FXML
    private TableColumn<Department, Integer> Dnumber;
    @FXML
    private TableColumn<Department, Integer> Mgr_ssn;
    @FXML
    private TableColumn<Department, String> Dlocation;
    @FXML
    private Button employeeBtn, addDepartmentBtn, createDatabase;
    @FXML
    private TextField dnameInput, dnumberInput, mgr_ssnInput, dlocationInput;
    @FXML
    private TextArea MetaDataArea;

    private Stage stage;
    private AnchorPane root;
    private Scene scene;

    @FXML
    public void initialize() {
        departmentTable.setItems(getDepartmentList());



        Dname.setCellValueFactory(new PropertyValueFactory<Department, String>("Dname"));
        Dnumber.setCellValueFactory(new PropertyValueFactory<Department, Integer>("Dnumber"));
        Mgr_ssn.setCellValueFactory(new PropertyValueFactory<Department, Integer>("Mgr_ssn"));
        Dlocation.setCellValueFactory(new PropertyValueFactory<Department, String>("Dlocation"));

    }
    public void createDatabase(ActionEvent event) throws Exception{
    	String departmentCreator = "CREATE TABLE `department` (  `Dname` varchar(15) NOT NULL,  `Dnumber` int(11) NOT NULL,  `Mgr_ssn` char(9) DEFAULT NULL,  `Dlocation` varchar(9) DEFAULT NULL,  PRIMARY KEY (`Dnumber`),  UNIQUE KEY `Dname` (`Dname`),  KEY `Mgr_ssn` (`Mgr_ssn`)) ENGINE=InnoDB DEFAULT CHARSET=latin1";
		Statement stmt = null;


		try {
	        Connection conn = DbConnector.getConnection();
	        PreparedStatement displayprofile = conn.prepareStatement("Show create table department;");
	        ResultSet resultSet = displayprofile.executeQuery();
			while (resultSet.next()) {

				String database = resultSet.getString("Create Table");
				String databasetester = "";
				// if the table is empty
				if (database != databasetester) {
					MetaDataArea.setText("The department table already exists");
					System.out.println("The department table already exists");
					// System.out.println(database);
				} else {

					System.out.println("Database is missing the department");
					String query = departmentCreator;
					stmt = conn.createStatement();
					resultSet = stmt.executeQuery(query);
					System.out.println("department table added");

				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
    //TODO SQLException. Remeber to make some thing to show the user SQL errors.
    public ObservableList<Department>/*<String>*/  getDepartmentList()
    {
        ObservableList<Department>/*<String>*/ departments = FXCollections.observableArrayList();

        String SQLQuery = "select Dname, Dnumber, Mgr_ssn, Dlocation FROM department";

        try(
                Connection conn = DbConnector.getConnection();
                PreparedStatement displayprofile = conn.prepareStatement(SQLQuery);
                ResultSet resultSet = displayprofile.executeQuery()
        ){
            while (resultSet.next()){
                departments.add(new Department(resultSet.getString("Dname"), resultSet.getString("Dlocation"),
                        resultSet.getInt("Dnumber"), resultSet.getInt("Mgr_ssn") ));

            }
        }catch(SQLException ex){
            DbConnector.displayException(ex);
            return null;
        }
        return departments;
    }
    public void metdataMethod(ActionEvent event) throws SQLException {
        String SQLQuery = "show create table department;";
        Connection conn = DbConnector.getConnection();
        PreparedStatement displayprofile = conn.prepareStatement(SQLQuery);
        ResultSet resultSet = displayprofile.executeQuery();
    	if(resultSet.next()) {
    		MetaDataArea.setText(resultSet.getString(2));
    	}

    }
    public void insertDepartment(){
        String dName = dnameInput.getText();
        String dNumber = dnumberInput.getText();
        String mgr_ssn = mgr_ssnInput.getText();
        String dlocation = dlocationInput.getText();

        System.out.println(
                "dName: " + dName +"\n"+
                        "dNumber: "+ dNumber+"\n"+
                        "mgr_ssn: "+ mgr_ssn+"\n"+
                        "dlocation: "+ dlocation+"\n"
        );
        if(validateDname(dName) == false || validateDnumber(dNumber) == false
                || validateMgr_ssn(mgr_ssn) == false || validateDlocation(dlocation) == false){
            return;
        }

            //table 'department', columns: 'Dname' String, 'Dnumber' int, 'Mgr_ssn' int, 'Dlocation' String
        /*
        INSERT INTO department(Dname, Dnumber, Mgr_ssn, Dlocation)
        VALUES('testDepartment', 77, 777333888, 'testLocation');
        */

        String sqlQuery = "INSERT INTO department(Dname, Dnumber, Mgr_ssn, Dlocation) " +
                "VALUES("+"'"+dName+"', "+dNumber+", "+mgr_ssn+", "+"'"+dlocation+"');";

        try {
            Connection conn = DbConnector.getConnection();
            PreparedStatement displayprofile = conn.prepareStatement(sqlQuery);
            displayprofile.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        initialize();
    }
    private boolean validateDname(String dName){
        if(dName.length() > 15) {
            System.out.println("Dname Length");
            return false;
        }
            ArrayList<Character> chars = new ArrayList<>();
            for(int i = 0; i <dName.length(); i++){
                chars.add(dName.charAt(i));
            } //TODO change all of the && to seperate properly
            for(int i = 0; i < chars.size(); i++){
                int ascii = (int)chars.get(i);
                //A: 65, Z: 90, a:97, z:122
                if((ascii < 65 || ascii > 90) && (ascii < 97 || ascii > 122)){
                    System.out.println("invalid Danme character");
                    return false;
                }

        }
        return true;
    }
    private boolean validateMgr_ssn(String mgr_ssn){
        if(mgr_ssn.length() != 9) {
            System.out.println("invalid mgr_ssn Length");
            return false;
        }
            //0: 48, 9: 57
            ArrayList<Character> characters = new ArrayList<>();
            for(int i = 0; i < mgr_ssn.length(); i++){
                characters.add(mgr_ssn.charAt(i));
            }
            for(int i =0; i < mgr_ssn.length(); i ++){
                int ascii = (int)characters.get(i);
                if(ascii < 48 || ascii > 57){
                    System.out.println("invalid mgr_ssn char");
                    return false;
                }

        }
        return true;
    }
    private boolean validateDlocation(String dLocation){
        if(dLocation.length() > 9) {
            System.out.println("invalid Dlocation Length");
            return false;
        }
            ArrayList<Character> chars = new ArrayList<>();
            for(int i = 0; i <dLocation.length(); i++){
                chars.add(dLocation.charAt(i));
            }
            for(int i = 0; i < chars.size(); i++){
                int ascii = (int)chars.get(i);
                //A: 65, Z: 90, a:97, z:122
                if((ascii < 65 || ascii > 90) && (ascii < 97 || ascii > 122)){
                    System.out.println("invalid Dlocation character");
                    return false;
                }

        }
        return true;
    }
    private boolean validateDnumber(String dNum){
        if(dNum.length() > 11) {
            System.out.println("invalidDnumber Length");
            return false;
        }
            //0: 48, 9: 57
            ArrayList<Character> characters = new ArrayList<>();
            String dNumString = String.valueOf(dNum);
            for(int i = 0; i < dNumString.length(); i++){
                characters.add(dNum.charAt(i));
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
