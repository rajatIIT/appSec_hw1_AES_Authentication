/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appsecasst1;

import java.io.File;
import java.io.IOException;
import java.security.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.*;

/**
 *
 * @author Rajat-HP
 */
public class AppSecAsst1 {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        
    	
    	// pass args[0] and args[1] two seperate directories. In 0, we store ciphers and in 1 we store keys   
    	String cipherTextPath = args[0];
        String keyPath = args[1];
   
        try {
            TaskSelector myTaskSelector = new TaskSelector(keyPath,cipherTextPath);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AppSecAsst1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(AppSecAsst1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AppSecAsst1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AppSecAsst1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(AppSecAsst1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidAlgorithmParameterException ex) {
            Logger.getLogger(AppSecAsst1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(AppSecAsst1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(AppSecAsst1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}