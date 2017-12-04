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
import model.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

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
    private Button employeeBtn, addDepartmentBtn, toQuery;
    @FXML
    private TextField dnameInput, dnumberInput, mgr_ssnInput, dlocationInput;
    @FXML
    private TextArea MetaDataArea, javaErrors, sqlErrors;

    private Stage stage;
    private AnchorPane root;
    private Scene scene;

    private String errors = "";

    @FXML
    public void initialize() {
        departmentTable.setItems(getDepartmentList());


        Dname.setCellValueFactory(new PropertyValueFactory<Department, String>("Dname"));
        Dnumber.setCellValueFactory(new PropertyValueFactory<Department, Integer>("Dnumber"));
        Mgr_ssn.setCellValueFactory(new PropertyValueFactory<Department, Integer>("Mgr_ssn"));
        Dlocation.setCellValueFactory(new PropertyValueFactory<Department, String>("Dlocation"));

    }
    @SuppressWarnings("resource")
    public void createDatabase(ActionEvent event) throws Exception{
        String departmentCreator = "CREATE TABLE `department` (  `Dname` varchar(15) NOT NULL,  `Dnumber` int(11) NOT NULL,  `Mgr_ssn` char(9) DEFAULT NULL,  `Dlocation` varchar(9) DEFAULT NULL,  PRIMARY KEY (`Dnumber`),  UNIQUE KEY `Dname` (`Dname`),  KEY `Mgr_ssn` (`Mgr_ssn`)) ENGINE=InnoDB DEFAULT CHARSET=latin1";
        Statement stmt = null;


        try {
            Connection conn = DbConnector.getConnection();
            PreparedStatement displayprofile = conn.prepareStatement("Show create table department;");
            ResultSet resultSet = displayprofile.executeQuery();
            while (resultSet.next()) {

                String database = resultSet.getString("Create Table");

                // if the table is empty
                if (database != "Table 'company.department' doesn't exist") {
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
            sqlErrors.setText(e.toString());
        }

    }
    public void deleteCascade(){
        String dnum = dnumberInput.getText();
//        System.out.println(dnum);
//        String checkDepartmentMgr_ssnQuery = "SELECT Ssn FROM employee WHERE Super_ssn ='"+ssn+"'";
        ArrayList<String> queryList1 = new ArrayList<>();

        try{
            Connection conn = DbConnector.getConnection();
//            PreparedStatement checkDepartmentMgr_ssn = conn.prepareStatement(checkDepartmentMgr_ssnQuery);
//            ResultSet departmentMgr_ssnResults = checkDepartmentMgr_ssn.executeQuery();
//            if(departmentMgr_ssnResults.next() == true){
//                errors = errors + "FK Mgr_ssn "+ssn+" in department";
            Alert alert = new Alert(Alert.AlertType.WARNING, "FK Dnumber "+dnum+" in other tables. DO you want to override?", ButtonType.YES, ButtonType.NO);

            Optional<ButtonType> deleteAlert = alert.showAndWait();
            if(deleteAlert.get() == ButtonType.YES){
                /*
                SELECT Pnumber FROM project where Dnum = 5
                 */
                Statement statement = conn.createStatement();
                ResultSet resultSet1 = statement.executeQuery("SELECT Pnumber FROM project WHERE Dnum ="+dnum);
                while (resultSet1.next()){
                    queryList1.add(resultSet1.getString("Pnumber"));
                }
                for(int i = 0; i <queryList1.size(); i++){
                    statement.executeUpdate("DELETE FROM works_on WHERE Pno ="+queryList1.get(i));
//                    System.out.println(queryList1.get(i));
                }
                statement.executeUpdate("DELETE FROM project WHERE Dnum ="+dnum);
                statement.executeUpdate("DELETE FROM department WHERE Dnumber ="+dnum);

            }

//            }
        } catch (SQLException e){
            e.printStackTrace();
            sqlErrors.setText(e.toString());
            return;

        }
    }
    //TODO SQLException. Remeber to make some thing to show the user SQL errors.
    public ObservableList<Department>/*<String>*/  getDepartmentList() {
        ObservableList<Department>/*<String>*/ departments = FXCollections.observableArrayList();

        String SQLQuery = "select Dname, Dnumber, Mgr_ssn, Dlocation FROM department";

        try (
                Connection conn = DbConnector.getConnection();
                PreparedStatement displayprofile = conn.prepareStatement(SQLQuery);
                ResultSet resultSet = displayprofile.executeQuery()
        ) {
            while (resultSet.next()) {
                departments.add(new Department(resultSet.getString("Dname"), resultSet.getString("Dlocation"),
                        resultSet.getInt("Dnumber"), resultSet.getInt("Mgr_ssn")));

            }
        } catch (SQLException ex) {
            DbConnector.displayException(ex);
            sqlErrors.setText(ex.toString());
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

    public void insertDepartment() {
        String dName = dnameInput.getText();
        String dNumber = dnumberInput.getText();
        String mgr_ssn = mgr_ssnInput.getText();
        String dlocation = dlocationInput.getText();

        System.out.println(
                "dName: " + dName + "\n" +
                        "dNumber: " + dNumber + "\n" +
                        "mgr_ssn: " + mgr_ssn + "\n" +
                        "dlocation: " + dlocation + "\n"
        );
        if (validateDname(dName) == false || validateDnumber(dNumber) == false
                || validateMgr_ssn(mgr_ssn) == false || validateDlocation(dlocation) == false) {
            validationErrors();
            errors = "";
            return;
        }

        //table 'department', columns: 'Dname' String, 'Dnumber' int, 'Mgr_ssn' int, 'Dlocation' String
        /*
        INSERT INTO department(Dname, Dnumber, Mgr_ssn, Dlocation)
        VALUES('testDepartment', 77, 777333888, 'testLocation');
        */

        String sqlQuery = "INSERT INTO department(Dname, Dnumber, Mgr_ssn, Dlocation) " +
                "VALUES(" + "'" + dName + "', " + dNumber + ", " + mgr_ssn + ", " + "'" + dlocation + "');";

        String checkKeys = "SELECT Dlocation FROM department WHERE Dlocation = '"+dlocation+"'";
        String checkDnameQuery = "SELECT Dname From department WhERE Dname ='"+dName+"'";
        String checkMgr_ssnQuery = "SELECT Ssn FROM employee WHERE Ssn ='"+mgr_ssn+"'";
        String checkDnumQuery = "SELECT Dnumber FROM department WHERE Dnumber ="+dNumber;
        try {
            Connection conn = DbConnector.getConnection();
            PreparedStatement check = conn.prepareStatement(checkKeys);
            ResultSet resultSet = check.executeQuery();
            while(resultSet.next()){
                System.out.println(resultSet.getString("Dlocation"));
                if(resultSet.getString("Dlocation").equals(dlocation)){
                    errors = errors + "Primary Key "+dlocation+" Already Exists! \n";
                    javaErrors.setText(errors);
                    errors = "";
                    return;
                }
            }
            PreparedStatement checkDname = conn.prepareStatement(checkDnameQuery);
            ResultSet dnameResults = checkDname.executeQuery();
            if(dnameResults.next() == true){
                errors = errors +"Dname "+dName+" Already exist \n";
                javaErrors.setText(errors);
                errors = "";
                return;
            }
            PreparedStatement checkMgr_ssn = conn.prepareStatement(checkMgr_ssnQuery);
            ResultSet mgr_ssnResults = checkMgr_ssn.executeQuery();
            if(mgr_ssnResults.next() == false){
                errors = errors +"Mgr_ssn "+mgr_ssn+" does not exist \n";
                javaErrors.setText(errors);
                errors = "";
                return;
            }
            PreparedStatement checkDnum = conn.prepareStatement(checkDnumQuery);
            ResultSet dnumResults = checkDnum.executeQuery();
            if(dnumResults.next() == true){
                errors = errors + "Dnumber "+dNumber+" already exists";
                javaErrors.setText(errors);
                errors = "";
                return;
            }

            PreparedStatement displayprofile = conn.prepareStatement(sqlQuery);
            displayprofile.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            sqlErrors.setText(e.toString());
        }
        initialize();
    }
    public void delete(){
        String dName = dnameInput.getText();
        String dNumber = dnumberInput.getText();
        String mgr_ssn = mgr_ssnInput.getText();
        String dlocation = dlocationInput.getText();

        /*
        DELETE FROM testing
        WHERE testInt = 3
        AND
        testVarchar = 'test3';

        INSERT INTO department(Dname, Dnumber, Mgr_ssn, Dlocation)
        VALUES('testing1', 66, 111222333, 'testLocation1'),
        ('testing2', 67, 222333444, 'testLocation2')
         */
        String sqlQuery = "DELETE FROM department WHERE Dname ='"+dName+"' AND Dnumber ="+dNumber+" AND Mgr_ssn ="+mgr_ssn+
                " AND Dlocation ='"+dlocation+"'";
        try {
            Connection conn = DbConnector.getConnection();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT Pnumber FROM project WHERE Dnum ="+dNumber);
            if(resultSet.next() == true){
                deleteCascade();
                return;
            }

            PreparedStatement displayprofile = conn.prepareStatement(sqlQuery);
            displayprofile.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            sqlErrors.setText(e.toString());
        }
        initialize();

    }

    private boolean validateDname(String dName) {
        if (dName.length() > 15) {
            System.out.println("Invalid Dname Length");
            errors = errors + "Invalid Dname Length \n";
            return false;
        }
        ArrayList<Character> chars = new ArrayList<>();
        for (int i = 0; i < dName.length(); i++) {
            chars.add(dName.charAt(i));
        } //TODO change all of the && to seperate properly
        for (int i = 0; i < chars.size(); i++) {
            int ascii = (int) chars.get(i);
            //A: 65, Z: 90, a:97, z:122
            if ((ascii < 65 || ascii > 90) && (ascii < 97 || ascii > 122)) {
                System.out.println("invalid Danme character");
                errors = errors + "invalid Danme character \n";
                return false;
            }

        }
        return true;
    }

    private boolean validateMgr_ssn(String mgr_ssn) {
        if (mgr_ssn.length() != 9) {
            System.out.println("invalid mgr_ssn Length");
            errors = errors + "invalid mgr_ssn Length \n";
            return false;
        }
        //0: 48, 9: 57
        ArrayList<Character> characters = new ArrayList<>();
        for (int i = 0; i < mgr_ssn.length(); i++) {
            characters.add(mgr_ssn.charAt(i));
        }
        for (int i = 0; i < mgr_ssn.length(); i++) {
            int ascii = (int) characters.get(i);
            if (ascii < 48 || ascii > 57) {
                System.out.println("invalid mgr_ssn char");
                errors = errors + "invalid mgr_ssn character \n";
                return false;
            }

        }
        return true;
    }

    private boolean validateDlocation(String dLocation) {
        if (dLocation.length() > 9) {
            System.out.println("invalid Dlocation Length");
            errors = errors + "invalid Dlocation length \n";
            return false;
        }
        ArrayList<Character> chars = new ArrayList<>();
        for (int i = 0; i < dLocation.length(); i++) {
            chars.add(dLocation.charAt(i));
        }
        for (int i = 0; i < chars.size(); i++) {
            int ascii = (int) chars.get(i);
            //A: 65, Z: 90, a:97, z:122
            if ((ascii < 65 || ascii > 90) && (ascii < 97 || ascii > 122)) {
                System.out.println("invalid Dlocation character");
                errors = errors + "invalid Dlocation character \n";
                return false;
            }

        }
        return true;
    }

    private boolean validateDnumber(String dNum) {
        if (dNum.length() > 11) {
            System.out.println("invalid Dnumber Length");
            errors = errors + "invalid Dnumber Length \n";
            return false;
        }
        //0: 48, 9: 57
        ArrayList<Character> characters = new ArrayList<>();
        String dNumString = String.valueOf(dNum);
        for (int i = 0; i < dNumString.length(); i++) {
            characters.add(dNum.charAt(i));
        }
        for (int i = 0; i < dNumString.length(); i++) {
            int ascii = (int) characters.get(i);
            if (ascii < 48 || ascii > 57) {
                System.out.println("invalid Dnumber character");
                errors = errors + "invalid Dnumber character \n";

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
