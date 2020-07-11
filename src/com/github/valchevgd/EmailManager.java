package com.github.valchevgd;

import com.github.valchevgd.model.EmailAccount;
import com.github.valchevgd.model.EmailMessage;
import com.github.valchevgd.model.EmailTreeItem;
import com.github.valchevgd.services.FetchFolderService;
import com.github.valchevgd.services.FolderUpdaterService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.mail.Flags;
import javax.mail.Folder;
import java.util.ArrayList;
import java.util.List;

public class EmailManager {

    private final EmailTreeItem<String> foldersRoot = new EmailTreeItem<>("");
    private List<Folder> foldersList = new ArrayList<>();
    private FolderUpdaterService folderUpdaterService;
    private EmailMessage selectedMessage;
    private EmailTreeItem<String> selectedFolder;
    private ObservableList<EmailAccount> emailAccounts = FXCollections.observableArrayList();

    public EmailManager() {
        folderUpdaterService = new FolderUpdaterService(foldersList);
        folderUpdaterService.start();
    }

    public ObservableList<EmailAccount> getEmailAccounts() {
        return emailAccounts;
    }

    public EmailMessage getSelectedMessage() {
        return selectedMessage;
    }

    public void setSelectedMessage(EmailMessage selectedMessage) {
        this.selectedMessage = selectedMessage;
    }

    public EmailTreeItem<String> getSelectedFolder() {
        return selectedFolder;
    }

    public void setSelectedFolder(EmailTreeItem<String> selectedFolder) {
        this.selectedFolder = selectedFolder;
    }

    public EmailTreeItem<String> getFoldersRoot() {
        return foldersRoot;
    }

    public List<Folder> getFoldersList() {
        return foldersList;
    }

    public void addEmailAccount(EmailAccount emailAccount) {
        emailAccounts.add(emailAccount);
        EmailTreeItem<String> treeItem = new EmailTreeItem<>(emailAccount.getAddress());
        FetchFolderService fetchFolderService = new FetchFolderService(emailAccount.getStore(), treeItem, foldersList);
        fetchFolderService.start();
        foldersRoot.getChildren().add(treeItem);
    }

    public void setMessageToRead() {
        try {
            selectedMessage.setRead(true);
            selectedMessage.getMessage().setFlag(Flags.Flag.SEEN, true);
            selectedFolder.decrementMessagesCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMessageToUnread() {
        try {
            selectedMessage.setRead(false);
            selectedMessage.getMessage().setFlag(Flags.Flag.SEEN, false);
            selectedFolder.incrementMessagesCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteSelectedMessage() {
        try {
            selectedMessage.getMessage().setFlag(Flags.Flag.DELETED, true);
            selectedFolder.getEmailMessages().remove(selectedMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
