package main;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sql.Connected;
import sql.SQLHandler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class loginController {
    private static ArrayList dblst;
    private static String seldb;
    private Parent root;
    private Stage stage;


    @FXML
    private Button confirmBtn;

    @FXML
    private ComboBox<?> dbChoice;

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

    @FXML
    private void handleLogin(ActionEvent event) throws SQLException, IOException {
        if (event.getSource() == loginBtn) {
            SQLHandler connection = new SQLHandler();
            dblst = SQLHandler.verifyLogin(hostField.getText(), portField.getText(), loginField.getText(), pwField.getText());
            dbChoice.setItems(FXCollections.observableArrayList(dblst));
            dbChoice.getSelectionModel().selectFirst();

        }
    }

    @FXML
    public void switchToMainScene(ActionEvent event) throws IOException, SQLException {
        root = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
        stage = (Stage) confirmBtn.getScene().getWindow();
        stage.setScene(new Scene(root, 600, 500));
        stage.show();
        seldb = (String) dbChoice.getValue();
        Connected.pickdatabase(seldb);
    }


}