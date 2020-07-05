package com.github.valchevgd.controller;

import com.github.valchevgd.EmailManager;
import com.github.valchevgd.view.ViewFactory;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.scene.web.WebView;

public class MainWindowController extends BaseController {

    @FXML
    public TreeView emailsTreeView;

    @FXML
    public TableView emailsTableView;

    @FXML
    public WebView emailWebView;

    public MainWindowController(EmailManager emailManager,
                                ViewFactory viewFactory,
                                String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    @FXML
    public void optionsAction() {
        viewFactory.showOptionsWindow();
    }

    @FXML
    public void addAccountAction() {
        viewFactory.showLoginWindow();
    }
}
