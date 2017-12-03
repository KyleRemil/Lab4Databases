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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProjectController {
    @FXML
    private TableView<Project> projectTableView;
    @FXML
    private TableColumn<Project, String> pName, pLocation;
    @FXML
    private TableColumn<Project, Integer> pNumber, dNum;
    @FXML
    private TextArea MetaDataProject;
    private Button metadatabutton;

    private Stage stage;
    private AnchorPane root;
    private Scene scene;
    @FXML
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
    public void metdataMethod(ActionEvent event) throws SQLException {
        String SQLQuery = "show create table project;";
        Connection conn = DbConnector.getConnection();
        PreparedStatement displayprofile = conn.prepareStatement(SQLQuery);
        ResultSet resultSet = displayprofile.executeQuery();
    	if(resultSet.next()) {
    		MetaDataProject.setText(resultSet.getString(2));
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
