package com.github.valchevgd.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginWindowController {

    @FXML
    public TextField emailAddressField;

    @FXML
    public PasswordField passwordField;

    @FXML
    public Label errorLabel;

    public void loginButtonAction(ActionEvent actionEvent) {
        System.out.println("Login press");
    }
}
