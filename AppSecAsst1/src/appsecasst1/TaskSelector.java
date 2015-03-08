/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appsecasst1;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author Rajat-HP
 */
public class TaskSelector {

    private MyAES myAES;
    private File keyFile, cipherFile;

    public TaskSelector(String keyPath, String cipherTextPath) throws NoSuchAlgorithmException, NoSuchPaddingException, IOException, FileNotFoundException, ClassNotFoundException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

        boolean continueForward = true;
        int userInput;
        keyFile = new File(cipherTextPath + File.separator + "ciphers");
        cipherFile = new File(cipherTextPath);

        myAES = new MyAES(keyPath, cipherTextPath);

        while (continueForward) {

            Scanner taskreader = new Scanner(System.in);
            System.out.println("Enter your choice: \n 1: Add Username and password \n 2: Validate a Username and Password \n 3: Quit");
            userInput = taskreader.nextInt();
            if (userInput == 1) {

                Scanner myScanner = new Scanner(System.in);

                System.out.println("Enter username:");
                String inputUsername = myScanner.nextLine();

                System.out.println("Enter password:");
                String inputPassword = myScanner.nextLine();

                // encrypt and store the password and username in the file
                encryptAndStore(inputUsername, inputPassword);

            } else if (userInput == 2) {

                Scanner myScanner = new Scanner(System.in);

                System.out.println("Enter username:");
                String inputUsername = myScanner.nextLine();

                System.out.println("Enter password:");
                String inputPassword = myScanner.nextLine();

                checkCredentials(inputUsername, inputPassword);

            } else {
                System.exit(1);
            }

        }

//        System.out.println("Encrypting ATTACK");
//
//        byte[] cipherText = myAES.encrypt("ATTACK");
//        System.out.println("Encrypted text is " + Integer.toBinaryString(cipherText[0]));
//
//        String decryptedPlainText = myAES.decrypt(cipherText);
//
//        System.out.println("Decrypted text is " + decryptedPlainText.toString());
        // TODO code application logic here
    }

    private void encryptAndStore(String inputUsername, String inputPassword) throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, FileNotFoundException, UnsupportedEncodingException {
        // encrypt the  username and password

        byte[] encryptedUsername = myAES.encrypt(inputUsername);
        
        
        byte[] encryptedPassword = myAES.encrypt(inputPassword);
        

        // store the username and password line by line
        ArrayList inputByteList = new ArrayList();
        if (keyFile.exists()) {
            inputByteList = getFileList(keyFile);
        }
        inputByteList.add((Object) encryptedUsername);
        inputByteList.add((Object) encryptedPassword);

        writeToKeyfile(inputByteList);

        // create new keyfile and append whole inputByteList
        // append current username and password
        // forget
    }

    private ArrayList getFileList(File keyFile) throws FileNotFoundException {

        ArrayList tempArrayList = new ArrayList();

        FileInputStream myFIS = new FileInputStream(keyFile);
        ObjectInputStream myOIS;
        try {
            myOIS = new ObjectInputStream(myFIS);
            while (true) {
                try {
                    tempArrayList.add(myOIS.readObject());
                   // myOIS.close();
                   // myFIS.close();
                } catch (EOFException ex) {
                    break;
                } catch (IOException ex) {
                    Logger.getLogger(TaskSelector.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(TaskSelector.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(TaskSelector.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tempArrayList;
    }

    private void writeToKeyfile(ArrayList inputByteList) {
        
        try {
            
            if(!keyFile.exists())
                keyFile.createNewFile();
            
            FileOutputStream keyfos = null;
            try {
                keyfos = new FileOutputStream(keyFile);
                ObjectOutputStream keyoos = new ObjectOutputStream(keyfos);
                Iterator inputByteListIt = inputByteList.iterator();
                while (inputByteListIt.hasNext()) {
                    keyoos.writeObject(inputByteListIt.next());
                }
                keyoos.close();
                keyfos.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(TaskSelector.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(TaskSelector.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    keyfos.close();
                } catch (IOException ex) {
                    Logger.getLogger(TaskSelector.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(TaskSelector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * check if the input pair exists in the files
     *
     * @param inputUsername
     * @param inputPassword
     * @throws UnsupportedEncodingException 
     */
    private void checkCredentials(String inputUsername, String inputPassword) throws UnsupportedEncodingException {
    	
        try {
            ArrayList currentList = getFileList(keyFile);
            if (currentList.size() % 2 != 0) {
                System.out.println("Keyfile externally modified!!");
                System.exit(1);
            } else {
                boolean found = false;
                Iterator currentListIt = currentList.iterator();

                while (currentListIt.hasNext()) {
                    try {
                        
                    	
                    	byte[] currentCheckUsername = (byte[]) currentListIt.next();
                        byte[] currentCheckPassword = (byte[]) currentListIt.next();
                        
                      
                        

                        // decrypt both and check if true
                        String currentUName = myAES.decrypt(currentCheckUsername);
                        String currentPwd = myAES.decrypt(currentCheckPassword);

                        if (currentUName.equals(inputUsername) && currentPwd.equals(inputPassword)) {
                            System.out.println("This pair exists!");
                            found = true;
                        }
                    } catch (InvalidKeyException ex) {
                        Logger.getLogger(TaskSelector.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InvalidAlgorithmParameterException ex) {
                        Logger.getLogger(TaskSelector.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalBlockSizeException ex) {
                        Logger.getLogger(TaskSelector.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (BadPaddingException ex) {
                        Logger.getLogger(TaskSelector.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (!found) {
                    System.out.println("This pair does not exist!");
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TaskSelector.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}