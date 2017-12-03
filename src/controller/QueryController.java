package controller;

import application.DbConnector;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
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
import javafx.util.Callback;
import model.WorksOn;

import java.sql.*;
import java.util.List;

public class QueryController {
    @FXML
    private TextArea sqlInput, sqlOutput, sqlErrors;
    @FXML
    private TableView<ObservableList<String>> outPutTable;

    private Stage stage;
    private AnchorPane root;
    private Scene scene;

//    private Connection conn = DbConnector.getConnection();

    public void sqlInAndOut() throws Exception{

//        ObservableList<ObservableList> data = FXCollections.observableArrayList();
//        List<String> columns;
//        List<List<String>> data;

//        Statement stmt = null;
//        DbConnector conn;
        String SQLQuery = sqlInput.getText();
//        ObservableList<String> row = FXCollections.observableArrayList();


        String sqlOut = "";
        //TODO add button to choose either update or query
        try(
                 Connection conn = DbConnector.getConnection();
//                PreparedStatement displayprofile = conn.prepareStatement(SQLQuery);
//                ResultSet resultSet = displayprofile.executeQuery()
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(SQLQuery);
        ){
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
//            for(int i = 1; i <= columnsNumber; i++) {
//                final int j = i;
//                System.out.println(String.valueOf(i));
//                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i));
////                col.setCellValueFactory(new PropertyValueFactory<String, String>(rs.getString(i+1)));
//
////                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){
////                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
////                        return new SimpleStringProperty(param.getValue().get(j).toString());
////                    }
////                });
//                outPutTable.getColumns().addAll(col);
//                System.out.println("Column[" + i + "] ");
//            }
                while (rs.next()){
//                for(int i = 1; i <= columnsNumber; i++) {
//                    outPutTable.getItems().add(FXCollections.observableArrayList(data.get(i)));
//                }
//                System.out.println("Row [1] added "+data);

//                data.add(new SimpleStringProperty(String row));

                for(int i = 1; i <= columnsNumber; i++){
                    if(i > 1) System.out.print(", ");
                    String columnValue = rs.getString(i);
                    System.out.print(rsmd.getColumnName(i) + "\n " + columnValue);
                    sqlOut = sqlOut /*+ rsmd.getColumnName(i) + "\n "*/ + columnValue +", ";


//                    outPutTable.setItems();

                    }
                }
//                outPutTable.getItems().set(data.get());
//                outPutTable.setItems(row); //was the original
//                System.out.println();
//                worksOns.add(new WorksOn(resultSet.getInt("Essn"), resultSet.getInt("Pno"),
//                        resultSet.getInt("Hours")) );


        }catch(SQLException ex){
            DbConnector.displayException(ex);
            return;
        }
        sqlOutput.setText(sqlOut);
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
