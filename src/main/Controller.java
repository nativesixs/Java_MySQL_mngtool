package main;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import sql.Connected;
import sql.Tables;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;

import org.controlsfx.control.CheckComboBox;
public class Controller implements Initializable {
    public Controller() throws SQLException {
        getDblst();
    }
    private static String selected;
    public static ArrayList dblst;
    private static String seldb;

    @FXML private CheckComboBox<?> insertComboBox;
    @FXML private ChoiceBox<String> updateChoiceBox;
    @FXML private Button updateBtn;
    @FXML private Button insertBtn;
    @FXML private TextField inupField;
    @FXML private Button deleteTableBtn;
    @FXML private ChoiceBox<String> dbChangeBox;
    @FXML private Button changedbBtn;
    @FXML private TextField customCommandField;
    @FXML private Button customBtn;
    @FXML private ChoiceBox<?> idChoiceBox;
    @FXML private Button searchBtn;
    @FXML private TextField searchField;
    @FXML private Button editBtn;

    @FXML private TableColumn<Tables, String> tables;

    @FXML private TableView tableview;

    @FXML private Button showBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dbChangeBox.setItems(FXCollections.observableArrayList(dblst));
    }
    @FXML
    public void deleteTable(ActionEvent event) throws IOException, SQLException {
        selected= String.valueOf(tableview.getFocusModel().getFocusedItem());
        selected=selected.substring(1,selected.length()-1);

        boolean confirmation = prompt("Are you sure you want to delete this table ?");
        if(confirmation==false){
            return;
        } else if (confirmation==true) {
            //TODO
            //uncomment when ready, dont wanna drop tables while testing
            //Connected.queryExecute(selected,"","",6);
            System.out.println("deleting: "+selected);

        }
    }

    public boolean prompt(String text){
        //TODO
        //set custom titles/headers, can use to confirm actions then
        Alert yesNoAlert = new Alert(Alert.AlertType.CONFIRMATION);
        yesNoAlert.setTitle("Warning");
        yesNoAlert.setContentText(text);
        yesNoAlert.setHeaderText("Warning");
        ButtonType buttonYes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType buttonNo = new ButtonType("No" , ButtonBar.ButtonData.NO);
        yesNoAlert.getButtonTypes().setAll(buttonYes,buttonNo);
        Optional<ButtonType> result = yesNoAlert.showAndWait();
        if (result.get() == buttonYes){
            return true;
        } else  {
            return false;
        }
    }


    public void getDblst() throws SQLException {
        //gets list of databases + currently used db to put into dbchoicebox
        ResultSet rs = Connected.queryExecute("", "", "", 5);
        ArrayList dblist = new ArrayList();
        while(rs.next()){
            dblist.add(rs.getString(1));
        }
        dblst=dblist;

        rs = Connected.queryExecute("", "", "select database()", 4);
        rs.next();
        String s = rs.getString("database()");
        seldb=s;
    }


    @FXML
    public void changedb(ActionEvent event) throws IOException, SQLException {
        //changes current db
        if(dbChangeBox.getValue()==null){
            dbChangeBox.setValue("Select database first");
            return;
        }
        String value = dbChangeBox.getValue();
        Connected.pickdatabase(value);
        seldb=value;
        dbChangeBox.setValue(seldb);
    }


    @FXML
    public void showTables(ActionEvent event) throws SQLException {
        //on click calls table getter getTables -> calls Connected.java to handle SQL con ->returns
        //ResultSet rs = Connected.showTables();
        ResultSet rs = Connected.queryExecute(selected,(String) idChoiceBox.getValue(),customCommandField.getText(),2);
        tableSetter(rs);
        }

    @FXML
    public void getSelTable(ActionEvent event) throws SQLException {
        //TODO
        //solve the getSelectedElement to work -> implement substring solution written below
        /*String*/ selected= String.valueOf(tableview.getFocusModel().getFocusedItem());
        selected=selected.substring(1,selected.length()-1);

        ResultSet rs = Connected.queryExecute(selected,(String) idChoiceBox.getValue(),customCommandField.getText(),1);
        //ResultSet rs = Connected.grabdata(selected);
        tableSetter(rs);

        initBoxes();
    }

    private void initBoxes() throws SQLException {
        idChoiceBox.getItems().clear();
        idChoiceBox.setItems(Connected.getColumnNames(selected));
        idChoiceBox.getSelectionModel().selectFirst();

        updateChoiceBox.getItems().clear();
        updateChoiceBox.setItems(Connected.getColumnNames(selected));
        updateChoiceBox.getSelectionModel().selectFirst();

        insertComboBox.getItems().clear();
        insertComboBox.getItems().addAll(Connected.getColumnNames(selected));
    }
    @FXML
    private void searchCustom(ActionEvent event) throws SQLException {
        //String id= (String) idChoiceBox.getValue();
        ResultSet rs = Connected.queryExecute(selected,(String) idChoiceBox.getValue(),searchField.getText(),3);
        tableSetter(rs);
    }
    @FXML
    void doCustomQuery(ActionEvent event) throws SQLException {
        ResultSet rs = Connected.queryExecute(selected,(String) idChoiceBox.getValue(),customCommandField.getText(),4);
        tableSetter(rs);
    }

    @FXML
    public void createTable(){
        //TODO
    }

    @FXML
    public void insertInto() throws SQLException {
        ObservableList colnames = insertComboBox.getCheckModel().getCheckedItems();
        ObservableList fullColnames=Connected.getColumnNames(selected);
        ObservableList coltypes=FXCollections.observableArrayList();
        //getting text from textfield and cleaning it
        String s = inupField.getText().replaceAll("\\s+","");
        String[] userinputstr=s.split(",");
        System.out.println(userinputstr);
        ObservableList userinput=FXCollections.observableArrayList();
        for(int i=0;i<userinputstr.length;i++) {
            userinput.add(userinputstr[i]);
        }
        String query="insert into "+selected+" (";
        String query2="";
        //only get column types of selected columns
        int containedIndex=0;
        for(int i=0;i<colnames.size();i++){
            containedIndex=fullColnames.indexOf(colnames.get(i));
            coltypes.add(Connected.getColumnType(selected).get(containedIndex));
            query2+=colnames.get(i)+", ";
        }
        query2=query2.substring(0,query2.length()-2);
        query2+=") values (";
        for(int i=0;i<colnames.size();i++){
            if(coltypes.get(i)=="java.lang.Integer"){
                query2+=userinput.get(i)+", ";
            } else if (coltypes.get(i)=="java.lang.String") {
                query2+='\u0022'+userinput.get(i).toString()+'\u0022'+" ,";
            }
        }
        //sql command is done
        query2=query2.substring(0,query2.length()-2)+")";
        Connected.queryExecute("","",query+query2,7);
        tableSetter(Connected.queryExecute(selected, "", "", 1));

    }

    @FXML
    public void updateTable() throws SQLException {
        if(updateChoiceBox.getValue()==null){updateChoiceBox.setValue("Select column first");return;}
        ObservableList row = (ObservableList) tableview.getFocusModel().getFocusedItem();
        ObservableList colnames = Connected.getColumnNames(selected);
        ObservableList coltypes =Connected.getColumnType(selected);
        String query="update "+selected+" set ";
        String query2=updateChoiceBox.getValue()+"=";
        int i=updateChoiceBox.getSelectionModel().getSelectedIndex();
        if(coltypes.get(i)=="java.lang.Integer"){
            query2+=inupField.getText()+" where ";
        } else if (coltypes.get(i)=="java.lang.String") {
            query2+='\u0022'+inupField.getText()+'\u0022'+" where ";
        }

        for(int j = 0;j<coltypes.size();j++){
            query2+=colnames.get(j)+" = ";
            if(coltypes.get(j)=="java.lang.Integer"){
                query2+=row.get(j)+" and ";
            } else if (coltypes.get(j)=="java.lang.String") {
                query2+='\u0022'+row.get(j).toString()+'\u0022'+" and ";
            }
        }
        query+=query2.substring(0,query2.length()-5);
        Connected.queryExecute(selected,"",query,7);
        int selrowindex=tableview.getFocusModel().getFocusedIndex();
        tableSetter(Connected.queryExecute(selected, "", "", 1));
        tableview.getSelectionModel().select(selrowindex);



    }

    @FXML
    public void deleteRow() throws SQLException {
        ObservableList row = (ObservableList) tableview.getFocusModel().getFocusedItem();
        ObservableList colnames = Connected.getColumnNames(selected);
        ObservableList coltypes =Connected.getColumnType(selected);
        String query="delete from "+selected+" where ";
        String query2 ="";

        for(int i = 0;i<coltypes.size();i++){
            query2+=colnames.get(i).toString()+"=";
            if(coltypes.get(i)=="java.lang.Integer"){
                query2+=row.get(i)+" and ";
            } else if (coltypes.get(i)=="java.lang.String") {
                query2+='\u0022'+row.get(i).toString()+'\u0022'+" and ";
            }
        }
        query+=query2.substring(0,query2.length()-5);
        boolean confirmation = prompt("Are you sure you want to delete this row ?");
        if(confirmation==false){
            return;
        } else if (confirmation==true) {
            Connected.queryExecute("", "", query, 7);
            tableSetter(Connected.queryExecute(selected, "", "", 1));
        }
    }


    public void tableSetter(ResultSet rs) throws SQLException {
        //takes resultset and inserts elements into tableview row by row
        tableview.getColumns().clear();
        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
            final int j = i;
            TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
            col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(j).toString());
                }
            });
            tableview.getColumns().addAll(col);
        }
        while (rs.next()) {
            //iterate row
            ObservableList<String> row = FXCollections.observableArrayList();
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                //iter col
                row.add(rs.getString(i));
            }
            data.add(row);
        }
        tableview.setItems(data);
    }

    public String getSelectedElement(){
        //returns content of selected tableview cell as string
        Tables tp = (Tables) tableview.getFocusModel().getFocusedItem();
        return tp.getTable();
        //return null;
    }
    /*
    public void showOkay(){
        lable.setText("data inserted");
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> lable.setText(null));
        pause.play();
    }

     */

    /*
    public void getCurrentDb(Dbdata dbobj) {
        seldb=dbobj.getSeldb();
        dblst=dbobj.getDblst();
    }
    */
}




