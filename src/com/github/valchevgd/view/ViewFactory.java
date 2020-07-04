package com.github.valchevgd.view;

import com.github.valchevgd.EmailManager;

public class ViewFactory {

    private EmailManager emailManager;

    public ViewFactory(EmailManager emailManager) {
        this.emailManager = emailManager;
    }

    public void showLoginWindow(){
        System.out.println("Show login window");
    }
}
