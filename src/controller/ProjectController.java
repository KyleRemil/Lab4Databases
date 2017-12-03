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
import java.sql.Statement;

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
