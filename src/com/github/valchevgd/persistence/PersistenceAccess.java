package com.github.valchevgd.persistence;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PersistenceAccess {

    private final String VALID_ACCOUNTS_LOCATION =
            System.getProperty("user.home") + File.separator + "validAccounts.ser";
    private Encoder encoder = new Encoder();

    public List<ValidAccount> loadFromPersistence() {
        List<ValidAccount> resultList = new ArrayList<>();

        try {
            FileInputStream fileInputStream = new FileInputStream(VALID_ACCOUNTS_LOCATION);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            List<ValidAccount> persistentList = (List<ValidAccount>) objectInputStream.readObject();
            decodePasswords(persistentList);
            resultList.addAll(persistentList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultList;
    }

    private void decodePasswords(List<ValidAccount> persistentList) {
        for (ValidAccount validAccount : persistentList) {
            String originalPassword = validAccount.getPassword();
            validAccount.setPassword(encoder.decode(originalPassword));
        }
    }

    private void encodePasswords(List<ValidAccount> persistentList) {
        for (ValidAccount validAccount : persistentList) {
            String originalPassword = validAccount.getPassword();
            validAccount.setPassword(encoder.encode(originalPassword));
        }
    }

    public void saveToPersistence(List<ValidAccount> validAccounts) {
        try {
            File file = new File(VALID_ACCOUNTS_LOCATION);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            encodePasswords(validAccounts);
            objectOutputStream.writeObject(validAccounts);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
