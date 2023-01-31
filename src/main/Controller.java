package main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sql.Connected;
import sql.SQLHandler;
import sql.Tables;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private Button editBtn;

    //@FXML
    //private TableColumn<Tables, String> tables;

    @FXML
    private TableView<Tables> tableview;

    @FXML
    private Button showBtn;

    @FXML
    private Button confirmBtn;

    @FXML
    private ComboBox<String> dbChoice;

    @FXML
    private TextField hostField;

    @FXML
    private Button loginBtn;

    @FXML
    private TextField loginField;

    @FXML
    private TextField portField;

    @FXML
    private PasswordField pwField;

    @FXML
    private Font x11;

    @FXML
    private Font x111;

    @FXML
    private Font x1111;

    @FXML
    private Font x11111;

    @FXML
    private Color x21;

    @FXML
    private Color x211;

    @FXML
    private Color x2111;

    @FXML
    private Color x21111;

    private Parent root;
    private Stage stage;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }
    /*
    * INIT login + switch to main
    *
     */

    @FXML
    private void handleLogin(ActionEvent event) throws SQLException, IOException {
        if(event.getSource()==loginBtn){
            SQLHandler connection = new SQLHandler();
            ArrayList dblst = SQLHandler.verifyLogin(hostField.getText(), portField.getText(), loginField.getText(), pwField.getText());
            dbChoice.setItems(FXCollections.observableArrayList(dblst));

        }
    }

    @FXML
    public void switchToMainScene(ActionEvent event) throws IOException, SQLException {
        root= FXMLLoader.load(getClass().getResource("MainScene.fxml"));
        stage=(Stage) confirmBtn.getScene().getWindow();
        stage.setScene(new Scene(root,600, 500));
        stage.show();
        String seldb=dbChoice.getValue();
        Connected.pickdatabase(seldb);
    }
    /*
    * GET TABLES
    *
    *
     */

    public ObservableList<Tables> getTables() throws SQLException {
        ObservableList<Tables> prod= FXCollections.observableArrayList();
        Tables tables = null;
        prod=Connected.showtables(tables,prod);
        return prod;
    }
    @FXML
    public void showtables(ActionEvent event) throws SQLException {
        //on click calls table getter getTables -> calls Connected.java to handle SQL con ->returns
        tableview.getColumns().clear();
        TableColumn<Tables, String> tables = new TableColumn<Tables,String>("Table");
        tables.setCellValueFactory(new PropertyValueFactory<Tables,String>("Table"));
        tableview.getColumns().add(tables);
        tableview.setItems(getTables());
        }

    @FXML
    public void getSelTable(ActionEvent event) throws SQLException {
        tableview.getColumns().clear();
        ResultSet content=Connected.getSelTable(getSelectedElement());




    }


    public String getSelectedElement(){
        //returns content of selected tableview cell as string
        Tables tp = tableview.getFocusModel().getFocusedItem();
        String selected=tp.getTable();
        return selected;
    }

    public void getSelectedPos(){
        //TODO
        //Tables tp = tableview.getFocusModel().getFocusedItem();
        //String selected=tp.getTable();
        //return selected;
    }


    }




