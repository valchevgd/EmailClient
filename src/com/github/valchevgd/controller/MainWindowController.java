package com.github.valchevgd.controller;

import com.github.valchevgd.EmailManager;
import com.github.valchevgd.model.EmailMessage;
import com.github.valchevgd.model.EmailTreeItem;
import com.github.valchevgd.model.SizeInteger;
import com.github.valchevgd.services.MessageRendererService;
import com.github.valchevgd.view.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebView;
import javafx.util.Callback;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class MainWindowController extends BaseController implements Initializable {

    private MenuItem markUnread = new MenuItem("Mark As Unread");
    private MenuItem deleteMessage = new MenuItem("Delete Message");

    @FXML
    public TreeView<String> emailsTreeView;

    @FXML
    public TableView<EmailMessage> emailsTableView;

    @FXML
    private TableColumn<EmailMessage, String> senderColumn;

    @FXML
    private TableColumn<EmailMessage, String> subjectColumn;

    @FXML
    private TableColumn<EmailMessage, String> recipientColumn;

    @FXML
    private TableColumn<EmailMessage, SizeInteger> sizeColumn;

    @FXML
    private TableColumn<EmailMessage, Date> dateColumn;

    @FXML
    public WebView emailWebView;

    private MessageRendererService messageRendererService;

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

    @FXML
    public void composeMessageAction() { viewFactory.showComposeMessageWindow();}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpEmailsTreeView();
        setUpEmailsTableView();
        setUpFolderSelection();
        setUpBoldRows();
        setUpMessageRenderService();
        setUpMessageSelection();
        setUpContextMenus();
    }

    private void setUpContextMenus() {
        markUnread.setOnAction(e -> {
            emailManager.setMessageToUnread();
        });

        deleteMessage.setOnAction(e -> {
            emailManager.deleteSelectedMessage();
            emailWebView.getEngine().loadContent("");
        });
    }

    private void setUpMessageSelection() {
        emailsTableView.setOnMouseClicked(e -> {
            EmailMessage emailMessage = emailsTableView.getSelectionModel().getSelectedItem();
            if (emailMessage != null) {

                emailManager.setSelectedMessage(emailMessage);
                if (!emailMessage.isRead()) {
                    emailManager.setMessageToRead();
                }
                messageRendererService.setEmailMessage(emailMessage);
                messageRendererService.restart();

            }
        });
    }

    private void setUpMessageRenderService() {
        messageRendererService = new MessageRendererService(emailWebView.getEngine());
    }

    private void setUpBoldRows() {
        emailsTableView.setRowFactory(new Callback<TableView<EmailMessage>, TableRow<EmailMessage>>() {
            @Override
            public TableRow<EmailMessage> call(TableView<EmailMessage> emailMessageTableView) {
                return new TableRow<>(){
                    @Override
                    protected void updateItem(EmailMessage item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            if (item.isRead()) {
                                setStyle("");
                            } else {
                                setStyle("-fx-font-weight: bold");
                            }
                        }
                    }
                };
            }
        });
    }

    private void setUpFolderSelection() {
        emailsTreeView.setOnMouseClicked(e -> {
            EmailTreeItem<String> item = (EmailTreeItem<String>)emailsTreeView.getSelectionModel().getSelectedItem();
            if (item != null) {
                emailsTableView.setItems(item.getEmailMessages());
                emailManager.setSelectedFolder(item);
            }
        });
    }

    private void setUpEmailsTableView() {
        senderColumn.setCellValueFactory((new PropertyValueFactory<EmailMessage, String>("sender")));
        subjectColumn.setCellValueFactory((new PropertyValueFactory<EmailMessage, String>("subject")));
        recipientColumn.setCellValueFactory((new PropertyValueFactory<EmailMessage, String>("recipient")));
        sizeColumn.setCellValueFactory((new PropertyValueFactory<EmailMessage, SizeInteger>("size")));
        dateColumn.setCellValueFactory((new PropertyValueFactory<EmailMessage, Date>("date")));

        emailsTableView.setContextMenu(new ContextMenu(markUnread, deleteMessage));
    }

    private void setUpEmailsTreeView() {
        emailsTreeView.setRoot(emailManager.getFoldersRoot());
        emailsTreeView.setShowRoot(false);
    }
}
