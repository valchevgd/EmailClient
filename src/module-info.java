module EmailClient {

    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.web;
    requires activation;
    requires java.mail;

    opens com.github.valchevgd;
    opens com.github.valchevgd.view;
    opens com.github.valchevgd.controller;
}