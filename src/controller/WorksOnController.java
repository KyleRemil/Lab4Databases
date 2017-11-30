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
import model.Employee;
import model.WorksOn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WorksOnController {
    @FXML
    private TableView<WorksOn> worksonTableView;
    @FXML
    private TableColumn<WorksOn, Integer> essn, pno, hours;

    private Stage stage;
    private AnchorPane root;
    private Scene scene;

    public void initialize() {
        worksonTableView.setItems(getWorksonList());

        essn.setCellValueFactory(new PropertyValueFactory<WorksOn, Integer>("Essn"));
        pno.setCellValueFactory(new PropertyValueFactory<WorksOn, Integer>("Pno"));
        hours.setCellValueFactory(new PropertyValueFactory<WorksOn, Integer>("Hours"));
    }
    public ObservableList<WorksOn>/*<String>*/  getWorksonList()
    {
        ObservableList<WorksOn>/*<String>*/ worksOns = FXCollections.observableArrayList();

        String SQLQuery = "select Essn, Pno, Hours FROM works_on";

        try(
                Connection conn = DbConnector.getConnection();
                PreparedStatement displayprofile = conn.prepareStatement(SQLQuery);
                ResultSet resultSet = displayprofile.executeQuery()
        ){
            while (resultSet.next()){
                worksOns.add(new WorksOn(resultSet.getInt("Essn"), resultSet.getInt("Pno"),
                        resultSet.getInt("Hours")) );

            }
        }catch(SQLException ex){
            DbConnector.displayException(ex);
            return null;
        }
        return worksOns;
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
