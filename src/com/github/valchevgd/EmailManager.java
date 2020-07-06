package com.github.valchevgd;

import com.github.valchevgd.model.EmailAccount;
import javafx.scene.control.TreeItem;

public class EmailManager {

    private final TreeItem<String> foldersRoot = new TreeItem<>("");

    public TreeItem<String> getFoldersRoot() {
        return foldersRoot;
    }

    public void addEmailAccount(EmailAccount emailAccount) {
        TreeItem<String> treeItem = new TreeItem<>(emailAccount.getAddress());
        treeItem.setExpanded(true);
        treeItem.getChildren().add(new TreeItem<>("Inbox"));
        treeItem.getChildren().add(new TreeItem<>("Send"));
        treeItem.getChildren().add(new TreeItem<>("Spam"));
        foldersRoot.getChildren().add(treeItem);
    }
}
