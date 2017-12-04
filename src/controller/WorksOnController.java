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
import model.Employee;
import model.WorksOn;

import java.sql.*;
import java.util.ArrayList;

public class WorksOnController {
    @FXML
    private TableView<WorksOn> worksonTableView;
    @FXML
    private TableColumn<WorksOn, Integer> essn, pno, hours;
    @FXML
    private TextArea MetaDataWorksOn, javaErrors;
    @FXML
    private TextField essnInput, pnoInput, hoursInput;

    private Stage stage;
    private AnchorPane root;
    private Scene scene;

    String errors = "";

    public void initialize() {
        worksonTableView.setItems(getWorksonList());

        essn.setCellValueFactory(new PropertyValueFactory<WorksOn, Integer>("Essn"));
        pno.setCellValueFactory(new PropertyValueFactory<WorksOn, Integer>("Pno"));
        hours.setCellValueFactory(new PropertyValueFactory<WorksOn, Integer>("Hours"));
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
    public void metdataMethod(ActionEvent event) throws SQLException {
        String SQLQuery = "show create table works_on;";
        Connection conn = DbConnector.getConnection();
        PreparedStatement displayprofile = conn.prepareStatement(SQLQuery);
        ResultSet resultSet = displayprofile.executeQuery();
    	if(resultSet.next()) {
    		MetaDataWorksOn.setText(resultSet.getString(2));
    	}
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
            validationErrors();
            errors = "";
            return;
        }
        //TODO validate wether the Pno exists. CHeck for duplicates
        //Essn: char(9), Pno: int(11), Hours: int(11)
        /*
        INSERT INTO works_on(Essn, Pno, Hours)
        VALUES('333777999', 20, 33);

        SELECT Essn, Pno From works_on
        WHERE Essn = '123456789'
        AND
        Pno = 1
        */

        String sqlQuery = "INSERT INTO works_on(Essn, Pno, Hours) " +
                "VALUES("+"'"+essn+"', "+pno+", "+hours+");";
        String checkEssnQuery = "SELECT Ssn FROM employee WHERE Ssn ='"+essn+"'";
        String checkPnoFKQuery = "SELECT Pnumber FROM project WHERE Pnumber ="+pno;
        String checkDupeQuery = "SELECT Pno, Essn FROM works_on WHERE Essn ='"+essn+"' AND Pno ="+pno;

        try {
            Connection conn = DbConnector.getConnection();
            PreparedStatement checkPnoFK = conn.prepareStatement(checkPnoFKQuery);
            ResultSet pnoFKResults = checkPnoFK.executeQuery();
            if(pnoFKResults.next() == false){
                errors = errors + "Project number "+ pno;
                javaErrors.setText(errors);
                errors = "";
                return;
            }
            PreparedStatement checkEssnPno = conn.prepareStatement(checkEssnQuery);
            ResultSet essnResults = checkEssnPno.executeQuery();
            if(essnResults.next() == false){
                    errors = errors + "Social "+ essn;
                    javaErrors.setText(errors);
                    errors = "";
                    return;
            }
            PreparedStatement checkDupe = conn.prepareStatement(checkDupeQuery);
            ResultSet dupeResults = checkDupe.executeQuery();
            if(dupeResults.next() == true){
                errors = errors + "Duplicate Information "+essn+" "+pno;
                javaErrors.setText(errors);
                errors="";
                return;
            }

            PreparedStatement displayprofile = conn.prepareStatement(sqlQuery);
            displayprofile.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        initialize();
    }
    public void delete(){
        String essn = essnInput.getText();
        String pno = pnoInput.getText();
        String hours = hoursInput.getText();

        /*
        DELETE FROM testing
        WHERE testInt = 3
        AND
        testVarchar = 'test3';

        INSERT INTO department(Dname, Dnumber, Mgr_ssn, Dlocation)
        VALUES('testing1', 66, 111222333, 'testLocation1'),
        ('testing2', 67, 222333444, 'testLocation2')
         */
        String sqlQuery = "DELETE FROM works_on WHERE Essn ='"+essn+"' AND Pno ="+pno+" AND Hours ="+hours;
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
            errors = errors + "essn length \n";
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
                errors = errors + "essn character \n";
                return false;
            }

        }
        return true;
    }
    private boolean validatePnumber(String pnumber){
        if(pnumber.length() > 10) {
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
    private boolean validateHours(String hours){
        if(hours.length() > 10) {
            System.out.println("invalid hours Length");
            errors = errors + "hours length \n";
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
                errors = errors + "hours character \n";

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
