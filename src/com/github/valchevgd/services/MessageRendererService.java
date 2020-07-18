package com.github.valchevgd.services;

import com.github.valchevgd.model.EmailMessage;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.web.WebEngine;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import java.io.IOException;

public class MessageRendererService extends Service {

    private EmailMessage emailMessage;
    private WebEngine webEngine;
    private StringBuffer stringBuffer;

    public MessageRendererService(WebEngine webEngine) {
        this.webEngine = webEngine;
        stringBuffer = new StringBuffer();
        this.setOnSucceeded(e -> {
            displayMessage();
        });
    }

    public void setEmailMessage(EmailMessage emailMessage) {
        this.emailMessage = emailMessage;
    }

    private void displayMessage() {
        webEngine.loadContent(stringBuffer.toString());
    }

    @Override
    protected Task createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {

                try {
                    loadMessage();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }
        };
    }

    private void loadMessage() throws MessagingException, IOException {
        stringBuffer.setLength(0);
        Message message = emailMessage.getMessage();
        String contentType = message.getContentType();
        if (isSimpleType(contentType)) {
            stringBuffer.append(message.getContent().toString());
        } else if (isMultipartType(contentType)) {
            Multipart multipart = (Multipart) message.getContent();
            loadMultipart(multipart, stringBuffer);
        }
    }

    private void loadMultipart(Multipart multipart, StringBuffer stringBuffer) throws MessagingException, IOException {

        for (int i = multipart.getCount() - 1; i >= 0; i--) {
            BodyPart bodyPart = multipart.getBodyPart(i);
            String bodyPartContentType = bodyPart.getContentType();
            if (isSimpleType(bodyPartContentType)) {
                stringBuffer.append(bodyPart.getContent().toString());
            } else if(isMultipartType(bodyPartContentType)){
                Multipart multipart1 = (Multipart)bodyPart.getContent();
                loadMultipart(multipart1, stringBuffer);
            } else if (!isTextPlain(bodyPartContentType)) {
                MimeBodyPart mimeBodyPart = (MimeBodyPart) bodyPart;
                emailMessage.addAttachment(mimeBodyPart);
            }
        }
    }

    private boolean isSimpleType(String contentType) {
        return contentType.contains("TEXT/HTML") ||
                contentType.contains("mixed") ||
                contentType.contains("text");
    }

    private boolean isTextPlain(String contentType) {
        return contentType.contains("TEXT/PLAIN");
    }

    private boolean isMultipartType(String contentType) {
        return contentType.contains("multipart");
    }
}
