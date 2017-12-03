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
import model.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class EmployeeController {
    @FXML
    private TableView<Employee> employeeTableView;
    @FXML
    private TableColumn<Employee, String> fname, lname;
    @FXML
    private TableColumn<Employee, Integer> supper_ssn, dno, ssn;
    @FXML
    private Button createDatabase;

    @FXML
    private TextField fnameInput, lnameInput, super_ssnInput, ssnInput;
    @FXML
    private TextArea MetaDataAreaEmployee;
    private Stage stage;
    private AnchorPane root;
    private Scene scene;
    @FXML

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
//    public void insertDepartment(){
//        String fname = fnameInput.getText();
//        String lanme = lnameInput.getText();
//        String supper_ssn = super_ssnInput.getText();
//
//
//        System.out.println(
//                "dName: " + dName +"\n"+
//                        "dNumber: "+ dNumber+"\n"+
//                        "mgr_ssn: "+ mgr_ssn+"\n"+
//                        "dlocation: "+ dlocation+"\n"
//        );
//        if(validateDname(dName) == false || validateDnumber(dNumber) == false
//                || validateMgr_ssn(mgr_ssn) == false || validateDlocation(dlocation) == false){
//            return;
//        }
//
//        //table 'department', columns: 'Dname' String, 'Dnumber' int, 'Mgr_ssn' int, 'Dlocation' String
//        /*
//        INSERT INTO department(Dname, Dnumber, Mgr_ssn, Dlocation)
//        VALUES('testDepartment', 77, 777333888, 'testLocation');
//        */
//
//        String sqlQuery = "INSERT INTO department(Dname, Dnumber, Mgr_ssn, Dlocation) " +
//                "VALUES("+"'"+dName+"', "+dNumber+", "+mgr_ssn+", "+"'"+dlocation+"');";
//
//        try {
//            Connection conn = DbConnector.getConnection();
//            PreparedStatement displayprofile = conn.prepareStatement(sqlQuery);
//            displayprofile.executeUpdate();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        initialize();
//    }
//    private boolean validateDname(String dName){
//        if(dName.length() > 15) {
//            System.out.println("Dname Length");
//            return false;
//        }
//        ArrayList<Character> chars = new ArrayList<>();
//        for(int i = 0; i <dName.length(); i++){
//            chars.add(dName.charAt(i));
//        } //TODO change all of the && to seperate properly
//        for(int i = 0; i < chars.size(); i++){
//            int ascii = (int)chars.get(i);
//            //A: 65, Z: 90, a:97, z:122
//            if((ascii < 65 || ascii > 90) && (ascii < 97 || ascii > 122)){
//                System.out.println("invalid Danme character");
//                return false;
//            }
//
//        }
//        return true;
//    }
//    private boolean validateDlocation(String dLocation){
//        if(dLocation.length() > 9) {
//            System.out.println("invalid Dlocation Length");
//            return false;
//        }
//        ArrayList<Character> chars = new ArrayList<>();
//        for(int i = 0; i <dLocation.length(); i++){
//            chars.add(dLocation.charAt(i));
//        }
//        for(int i = 0; i < chars.size(); i++){
//            int ascii = (int)chars.get(i);
//            //A: 65, Z: 90, a:97, z:122
//            if((ascii < 65 || ascii > 90) && (ascii < 97 || ascii > 122)){
//                System.out.println("invalid Dlocation character");
//                return false;
//            }
//
//        }
//        return true;
//    }
    public void metdataMethod(ActionEvent event) throws SQLException {
        String SQLQuery = "show create table employee;";
        Connection conn = DbConnector.getConnection();
        PreparedStatement displayprofile = conn.prepareStatement(SQLQuery);
        ResultSet resultSet = displayprofile.executeQuery();
    	if(resultSet.next()) {
    		MetaDataAreaEmployee.setText(resultSet.getString(2));
    	}
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
