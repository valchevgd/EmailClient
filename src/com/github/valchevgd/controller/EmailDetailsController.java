package com.github.valchevgd.controller;

import com.github.valchevgd.EmailManager;
import com.github.valchevgd.model.EmailMessage;
import com.github.valchevgd.services.MessageRendererService;
import com.github.valchevgd.view.ViewFactory;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EmailDetailsController extends BaseController implements Initializable {

    private final String DOWNLOADS_LOCATION = System.getProperty("user.home") + "/Downloads/";

    @FXML
    private WebView webView;

    @FXML
    private Label subjectLabel;

    @FXML
    private Label senderLabel;

    @FXML
    private Label attachmentsLabel;

    @FXML
    private HBox hBoxDownloads;

    public EmailDetailsController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        EmailMessage emailMessage = emailManager.getSelectedMessage();
        subjectLabel.setText(emailMessage.getSubject());
        senderLabel.setText(emailMessage.getSender());
        loadAttachments(emailMessage);

        MessageRendererService messageRendererService = new MessageRendererService(webView.getEngine());
        messageRendererService.setEmailMessage(emailMessage);
        messageRendererService.restart();
    }

    private void loadAttachments(EmailMessage emailMessage){

        if (emailMessage.isHasAttachment()) {

            for (MimeBodyPart bodyPart : emailMessage.getAttachmentList()) {
                try {
                    AttachmentButton button = new AttachmentButton(bodyPart);
                    hBoxDownloads.getChildren().add(button);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        } else {
            attachmentsLabel.setText("");
        }

    }

    private class AttachmentButton extends Button {

        private MimeBodyPart mimeBodyPart;
        private String downloadedFilePath;


        public AttachmentButton(MimeBodyPart bodyPart) throws MessagingException {
            this.mimeBodyPart = bodyPart;
            this.setText(bodyPart.getFileName());
            this.downloadedFilePath = DOWNLOADS_LOCATION + bodyPart.getFileName();

            this.setOnAction(e -> downloadAttachment());
        }

        private void downloadAttachment() {
            Service service = new Service() {
                @Override
                protected Task createTask() {
                    return new Task() {
                        @Override
                        protected Object call() throws Exception {
                            mimeBodyPart.saveFile(downloadedFilePath);
                            return null;
                        }
                    };
                }
            };

            service.restart();
            service.setOnSucceeded(e -> {
                File file = new File(downloadedFilePath);
                Desktop desktop = Desktop.getDesktop();

                if (file.exists()) {
                    try {
                        desktop.open(file);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            });
        }
    }
}
