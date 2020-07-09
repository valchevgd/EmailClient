package com.github.valchevgd.controller;

import com.github.valchevgd.EmailManager;
import com.github.valchevgd.model.EmailAccount;
import com.github.valchevgd.services.EmailLoginResult;
import com.github.valchevgd.services.LoginService;
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

        if (fieldsAreValid()) {
            EmailAccount emailAccount = new EmailAccount(emailAddressField.getText(), passwordField.getText());
            LoginService loginService = new LoginService(emailAccount, emailManager);

            loginService.start();
            loginService.setOnSucceeded(e -> {
                EmailLoginResult loginResult = loginService.getValue();

                switch (loginResult) {
                    case SUCCESS:
                        if (!viewFactory.isMainViewInitialized()) {

                            viewFactory.showMainWindow();
                        }

                        Stage stage = (Stage) errorLabel.getScene().getWindow();
                        viewFactory.closeStage(stage);
                        return;
                    case FAILED_BY_CREDENTIALS:
                        errorLabel.setText("Invalid credentials");
                        return;
                    case FAILED_BY_UNEXPECTED_ERROR:
                        errorLabel.setText("Unexpected error");
                        return;
                    default:
                        return;
                }
            });
        }
    }

    private boolean fieldsAreValid() {
        boolean fieldsAreValid = true;

        if (emailAddressField.getText().isEmpty()) {
            errorLabel.setText(String.format("Please fill email%n"));
            fieldsAreValid = false;
        }

        if (passwordField.getText().isBlank()) {
            errorLabel.setText(errorLabel.getText() + "Please fill password");
            fieldsAreValid = false;
        }

        return fieldsAreValid;
    }
}
