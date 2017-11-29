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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Department;
import model.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeController {
    @FXML
    private TableView<Employee> employeeTableView;
    @FXML
    private TableColumn<Employee, String> fname, lname;
    @FXML
    private TableColumn<Employee, Integer> supper_ssn, dno, ssn;

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
                ResultSet resultSet = displayprofile.executeQuery();
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
