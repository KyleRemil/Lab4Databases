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
import model.Employee;
import model.WorksOn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class WorksOnController {
    @FXML
    private TableView<WorksOn> worksonTableView;
    @FXML
    private TableColumn<WorksOn, Integer> essn, pno, hours;
    @FXML
    private TextArea MetaDataWorksOn;
    private Button metadatabutton;

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
    @SuppressWarnings("resource")
 	public void createDatabase(ActionEvent event) throws Exception{
    	String worksonCreator = "CREATE TABLE `works_on` (  `Essn` char(9) NOT NULL,  `Pno` int(11) NOT NULL,  `Hours` int(11) DEFAULT NULL,  PRIMARY KEY (`Essn`,`Pno`),  KEY `Pno` (`Pno`),  CONSTRAINT `works_on_ibfk_2` FOREIGN KEY (`Pno`) REFERENCES `project` (`Pnumber`)) ENGINE=InnoDB DEFAULT CHARSET=latin1";
    	Statement stmt = null;


 		try {
 	        Connection conn = DbConnector.getConnection();
 	        PreparedStatement displayprofile = conn.prepareStatement("Show create table employee;");
 	        ResultSet resultSet = displayprofile.executeQuery();
 			while (resultSet.next()) {

 				String database = resultSet.getString("Create Table");

 				// if the table is empty
 				if (database != "Table 'company.works_on' doesn't exist") {
 					MetaDataWorksOn.setText("The works_on table already exists");
 					System.out.println("The works_on table already exists");
 					// System.out.println(database);
 				} else {

 					System.out.println("Database is missing the works_on table");
 					String query = worksonCreator;
 					stmt = conn.createStatement();
 					resultSet = stmt.executeQuery(query);
 					System.out.println("works_on table added");

 				}
 			}
 		} catch (SQLException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}

     }
    public void metdataMethod(ActionEvent event) throws SQLException {
        String SQLQuery = "show create table works_on;";
        Connection conn = DbConnector.getConnection();
        PreparedStatement displayprofile = conn.prepareStatement(SQLQuery);
        ResultSet resultSet = displayprofile.executeQuery();
    	if(resultSet.next()) {
    		MetaDataWorksOn.setText(resultSet.getString(2));
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
