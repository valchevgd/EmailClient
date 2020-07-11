package com.github.valchevgd;

import com.github.valchevgd.model.EmailAccount;
import com.github.valchevgd.model.EmailTreeItem;
import com.github.valchevgd.services.FetchFolderService;
import com.github.valchevgd.services.FolderUpdaterService;

import javax.mail.Folder;
import java.util.ArrayList;
import java.util.List;

public class EmailManager {

    private final EmailTreeItem<String> foldersRoot = new EmailTreeItem<>("");
    private List<Folder> foldersList = new ArrayList<>();
    private FolderUpdaterService folderUpdaterService;

    public EmailManager() {
        folderUpdaterService = new FolderUpdaterService(foldersList);
        folderUpdaterService.start();
    }

    public EmailTreeItem<String> getFoldersRoot() {
        return foldersRoot;
    }

    public List<Folder> getFoldersList() {
        return foldersList;
    }

    public void addEmailAccount(EmailAccount emailAccount) {
        EmailTreeItem<String> treeItem = new EmailTreeItem<>(emailAccount.getAddress());
        FetchFolderService fetchFolderService = new FetchFolderService(emailAccount.getStore(), treeItem, foldersList);
        fetchFolderService.start();
        foldersRoot.getChildren().add(treeItem);
    }
}
