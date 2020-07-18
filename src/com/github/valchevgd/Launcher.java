package com.github.valchevgd;

import com.github.valchevgd.model.EmailAccount;
import com.github.valchevgd.persistence.PersistenceAccess;
import com.github.valchevgd.persistence.ValidAccount;
import com.github.valchevgd.services.LoginService;
import com.github.valchevgd.view.ViewFactory;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Launcher extends Application {

    private PersistenceAccess persistenceAccess = new PersistenceAccess();
    private EmailManager emailManager = new EmailManager();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        ViewFactory viewFactory = new ViewFactory(emailManager);
        List<ValidAccount> validAccounts = persistenceAccess.loadFromPersistence();

        if (validAccounts.size() > 0) {
            viewFactory.showMainWindow();
            for (ValidAccount validAccount : validAccounts) {
                EmailAccount emailAccount = new EmailAccount(validAccount.getAddress(), validAccount.getPassword());
                LoginService loginService = new LoginService(emailAccount, emailManager);
                loginService.start();
            }
        } else {
            viewFactory.showLoginWindow();
        }
    }

    @Override
    public void stop() throws Exception {
        List<ValidAccount> validAccounts = new ArrayList<>();
        for (EmailAccount emailAccount : emailManager.getEmailAccounts()) {
            validAccounts.add(new ValidAccount(emailAccount.getAddress(), emailAccount.getPassword()));
        }

        persistenceAccess.saveToPersistence(validAccounts);
    }
}
