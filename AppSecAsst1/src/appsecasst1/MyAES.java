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

    public MyAES(String keyLocation, String cipherTextLocation) throws NoSuchAlgorithmException, NoSuchPaddingException, IOException, FileNotFoundException, ClassNotFoundException {

        // key generation
        //keygenerator = KeyGenerator.getInstance("AES");
        validateKey(keyLocation);

        c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        

    }

    public byte[] encrypt(String s) throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        c.init(Cipher.ENCRYPT_MODE, myDesKey, iv);

        byte[] text = s.getBytes();

        byte[] textEncrypted = c.doFinal(text);

        return textEncrypted;
    }

    public String decrypt(byte[] s) throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        c.init(Cipher.DECRYPT_MODE, myDesKey, iv);

        byte[] textDecrypted = c.doFinal(s);

        return (new String(textDecrypted));
    }

    /**
     * Checks if a key already exists : if not, create a new Key
     */
    private void validateKey(String keyLocation) throws FileNotFoundException, IOException, ClassNotFoundException {

        try {
            File keyFile = new File(keyLocation + File.separator + "todolist");
            File IVFile = new File(keyLocation + File.separator + "email");
            keygenerator = KeyGenerator.getInstance("AES");
            
            if (keyFile.exists() && IVFile.exists()) {
                
                // read the secretKey Object and use it
                FileInputStream keyFIS = new FileInputStream(keyFile);
                FileInputStream IVFIS = new FileInputStream(IVFile);
                
                ObjectInputStream keyOIS = new ObjectInputStream(keyFIS);
                ObjectInputStream ivOIS = new ObjectInputStream(IVFIS);
                
                Object inputKeyObject = keyOIS.readObject();
                Object inputIVObject = ivOIS.readObject();
                
                if ((inputKeyObject instanceof SecretKey) && (inputIVObject instanceof byte[])) {
                    
                    myDesKey = (SecretKey) inputKeyObject;
                    
                    iv = new IvParameterSpec((byte[])inputIVObject);
                    
                    
                } else {
                    System.out.println("Key or IV file is corrupt!!");
                    System.exit(1);
                }
            } else {
                
                // initialize a new SecretKey Object
                keygenerator.init(new SecureRandom());
                myDesKey = keygenerator.generateKey();
                iv = new IvParameterSpec(new byte[16]);
                System.out.println("write iv" +iv.getIV()[1]);
                // put it into the file
                FileOutputStream fos = new FileOutputStream(keyFile);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(myDesKey);
                
                FileOutputStream ivFOS = new FileOutputStream(IVFile);
                ObjectOutputStream ivOOS = new ObjectOutputStream(ivFOS);
                ivOOS.writeObject((byte[])iv.getIV());
                
            }
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(MyAES.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
