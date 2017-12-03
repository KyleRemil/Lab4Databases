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
import model.Employee;
import model.WorksOn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class WorksOnController {
    @FXML
    private TableView<WorksOn> worksonTableView;
    @FXML
    private TableColumn<WorksOn, Integer> essn, pno, hours;

    @FXML
    private TextField essnInput, pnoInput, hoursInput;

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
    public void insertWorks_on(){
        String essn = essnInput.getText();
        String pno = pnoInput.getText();
        String hours = hoursInput.getText();

//        System.out.println(
//                "fname: " + fname +"\n"+
//                        "lname: "+ lname+"\n"+
//                        "supper_ssn: "+ supper_ssn+"\n"+
//                        "dno: "+ dno+"\n"+
//                        "ssn: "+ ssn+"\n"
//        );
        if(validateEssn(essn) == false || validatePnumber(pno) == false
                || validateHours(hours) == false){
            return;
        }
        //TODO validate wether the Pno exists. CHeck for duplicates
        //Essn: char(9), Pno: int(11), Hours: int(11)
        /*
        INSERT INTO works_on(Essn, Pno, Hours)
        VALUES('333777999', 20, 33);
        */

        String sqlQuery = "INSERT INTO works_on(Essn, Pno, Hours) " +
                "VALUES("+"'"+essn+"', "+pno+", "+hours+");";

        try {
            Connection conn = DbConnector.getConnection();
            PreparedStatement displayprofile = conn.prepareStatement(sqlQuery);
            displayprofile.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        initialize();
    }
    private boolean validateEssn(String essn){
        if(essn.length() > 9) {
            System.out.println("essn Length");
            return false;
        }
        ArrayList<Character> chars = new ArrayList<>();
        for(int i = 0; i <essn.length(); i++){
            chars.add(essn.charAt(i));
        }
        for(int i = 0; i < chars.size(); i++){
            int ascii = (int)chars.get(i);
            //A: 65, Z: 90, a:97, z:122
            if(ascii < 48 || ascii > 57){
                System.out.println("invalid essn character");
                return false;
            }

        }
        return true;
    }
    private boolean validatePnumber(String pnumber){
        if(pnumber.length() > 10) {
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
    private boolean validateHours(String hours){
        if(hours.length() > 10) {
            System.out.println("invalid hours Length");
            return false;
        }
        //0: 48, 9: 57
        ArrayList<Character> characters = new ArrayList<>();
        String dNumString = String.valueOf(hours);
        for(int i = 0; i < dNumString.length(); i++){
            characters.add(hours.charAt(i));
        }
        for(int i =0; i < dNumString.length(); i ++){
            int ascii = (int)characters.get(i);
            if(ascii < 48 || ascii > 57){
                System.out.println("invalid hours character");

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
