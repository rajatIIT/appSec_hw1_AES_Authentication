/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appsecasst1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/**
 *
 * @author Rajat-HP
 */
public class MyAES {

    public KeyGenerator keygenerator;
    public SecretKey myDesKey;
    Cipher c;
    public IvParameterSpec iv;
    public String encryptionMode;

    public MyAES(String keyLocation, String cipherTextLocation) throws NoSuchAlgorithmException, NoSuchPaddingException, IOException, FileNotFoundException, ClassNotFoundException {

        // key generation
        //keygenerator = KeyGenerator.getInstance("AES");
        validateKey(keyLocation);


    }

    public byte[] encrypt(String s) throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        
    	if (encryptionMode.equals("AES/CBC/PKCS5Padding"))
    	c.init(Cipher.ENCRYPT_MODE, myDesKey, iv);
    	else if (encryptionMode.equals("AES/ECB/PKCS5Padding"))
    		c.init(Cipher.ENCRYPT_MODE, myDesKey);
    	else if (encryptionMode.equals("AES/CTR/PKCS5Padding"))
    		c.init(Cipher.ENCRYPT_MODE, myDesKey, iv);
    	
   // 	encryptionMode="AES/ECB/PKCS5Padding";
     //   else if (modeDecision==2)
       // 	encryptionMode="AES/CTR/NoPadding";
      //  else if (modeDecision==3)
        //	encryptionMode="AES/CBC/PKCS5Padding";

        byte[] text = s.getBytes();

        byte[] textEncrypted = c.doFinal(text);

        return textEncrypted;
    }

    public String decrypt(byte[] s) throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        //c.init(Cipher.DECRYPT_MODE, myDesKey, iv);
    	
    	if (encryptionMode.equals("AES/CBC/PKCS5Padding"))
        	c.init(Cipher.DECRYPT_MODE, myDesKey, iv);
        	else if (encryptionMode.equals("AES/ECB/PKCS5Padding"))
        		c.init(Cipher.DECRYPT_MODE, myDesKey);
        	else if (encryptionMode.equals("AES/CTR/PKCS5Padding"))
        		c.init(Cipher.DECRYPT_MODE, myDesKey, iv);
    	

        byte[] textDecrypted = c.doFinal(s);

        return (new String(textDecrypted));
    }

    /**
     * Checks if a key already exists : if not, create a new Key
     * @throws NoSuchPaddingException 
     */
    private void validateKey(String keyLocation) throws FileNotFoundException, IOException, ClassNotFoundException, NoSuchPaddingException {

        try {
            File keyFile = new File(keyLocation + File.separator + "todolist");
            File IVFile = new File(keyLocation + File.separator + "email");
            File modeFile = new File(keyLocation + File.separator + "assignment");
            
            keygenerator = KeyGenerator.getInstance("AES");
            
            if (keyFile.exists() && IVFile.exists() && modeFile.exists()) {
                
                // read the secretKey Object and use it
                FileInputStream keyFIS = new FileInputStream(keyFile);
                FileInputStream IVFIS = new FileInputStream(IVFile);
                FileInputStream modeFIS = new FileInputStream(modeFile);
                
                ObjectInputStream keyOIS = new ObjectInputStream(keyFIS);
                ObjectInputStream ivOIS = new ObjectInputStream(IVFIS);
                ObjectInputStream modeOIS = new ObjectInputStream(modeFIS);
                
                Object inputKeyObject = keyOIS.readObject();
                Object inputIVObject = ivOIS.readObject();
                Object inputModeObject = modeOIS.readObject();
                
                if ((inputKeyObject instanceof SecretKey) && (inputIVObject instanceof byte[]) && (inputModeObject instanceof String)) {
                    
                    myDesKey = (SecretKey) inputKeyObject;
                    
                    iv = new IvParameterSpec((byte[])inputIVObject);
                    
                    encryptionMode = (String)inputModeObject;
                    
                } else {
                    System.out.println("Key, IV or mode file is corrupt!!");
                    System.exit(1);
                }
                
               // AES/CBC/PKCS5Padding"
                
                c = Cipher.getInstance(encryptionMode);
                
            } else {
                
                // initialize a new SecretKey Object
                keygenerator.init(new SecureRandom());
                myDesKey = keygenerator.generateKey();
                iv = new IvParameterSpec(new byte[16]);
                
                
                // input the encryption mode from the user and put it into the file. 
                System.out.println("You are creating keys for the first time! Enter the mode you want to choose: ");
                System.out.println("Press 1 for ECB");
                System.out.println("Press 2 for CTR");
                System.out.println("Press 3 for CBC");
                Scanner myModeScanner = new Scanner(System.in);
                int modeDecision = myModeScanner.nextInt();
                if (modeDecision==1)
                	encryptionMode="AES/ECB/PKCS5Padding";
                else if (modeDecision==2)
                	encryptionMode="AES/CTR/PKCS5Padding";
                else if (modeDecision==3)
                	encryptionMode="AES/CBC/PKCS5Padding";

                
                // put it into the file
                FileOutputStream fos = new FileOutputStream(keyFile);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(myDesKey);
                oos.close();
                
                // put IV into the file
                FileOutputStream ivFOS = new FileOutputStream(IVFile);
                ObjectOutputStream ivOOS = new ObjectOutputStream(ivFOS);
                ivOOS.writeObject((byte[])iv.getIV());
                ivOOS.close();
                
                // put Mode Information into the file
               
                FileOutputStream modeFOS = new FileOutputStream(modeFile);
                ObjectOutputStream modeOOS = new ObjectOutputStream(modeFOS);
                modeOOS.writeObject(encryptionMode);
                modeOOS.close();
                
                c = Cipher.getInstance(encryptionMode);
            }
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(MyAES.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}