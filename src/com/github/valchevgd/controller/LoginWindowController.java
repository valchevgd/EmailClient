package com.github.valchevgd.controller;

import com.github.valchevgd.EmailManager;
import com.github.valchevgd.view.ViewFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginWindowController extends BaseController {

    @FXML
    public TextField emailAddressField;

    @FXML
    public PasswordField passwordField;

    @FXML
    public Label errorLabel;

    public LoginWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    @FXML
    public void loginButtonAction() {
        viewFactory.showMainWindow();

        Stage stage = (Stage) errorLabel.getScene().getWindow();
        viewFactory.closeStage(stage);
    }
}
