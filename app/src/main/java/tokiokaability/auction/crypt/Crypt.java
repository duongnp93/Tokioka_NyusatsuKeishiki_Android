package tokiokaability.auction.crypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

public class Crypt {

        private String iv = "fedcba9876543210";//Dummy iv (CHANGE IT!)
        private IvParameterSpec ivspec;
        private SecretKeySpec keyspec;
        private Cipher cipher;
        
        //private String SecretKey = "01234567";//Dummy secretKey (CHANGE IT!)
        private String SecretKey = md5("realcarauc").substring(0, 8);
        public Crypt()
        {
                ivspec = new IvParameterSpec(iv.getBytes());

                keyspec = new SecretKeySpec(SecretKey.getBytes(), "DES");
                
                try {
                        cipher = Cipher.getInstance("DES/ECB/PKCS7Padding");
                } catch (NoSuchAlgorithmException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
        }
        
        public byte[] encrypt(String text) throws Exception
        {
                if(text == null || text.length() == 0)
                        throw new Exception("Empty string");
                
                byte[] encrypted = null;

                try {
                        //cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
                	cipher.init(Cipher.ENCRYPT_MODE, keyspec);

                        //encrypted = cipher.doFinal(padString(text).getBytes());
                	encrypted = cipher.doFinal(text.getBytes());
                } catch (Exception e)
                {                       
                        throw new Exception("[encrypt] " + e.getMessage());
                }
                
                return encrypted;
        }
        
        public byte[] decrypt(String code) throws Exception
        {
                if(code == null || code.length() == 0)
                        throw new Exception("Empty string");
                
                byte[] decrypted = null;

                try {
                        //cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
                	cipher.init(Cipher.DECRYPT_MODE, keyspec);
                        
                        //decrypted = cipher.doFinal(hexToBytes(code));
                	decrypted = cipher.doFinal(Base64.decode(code,  Base64.DEFAULT));
                } catch (Exception e)
                {
                        throw new Exception("[decrypt] " + e.getMessage());
                }
                return decrypted;
        }
        

        
        public static String bytesToHex(byte[] data)
        {
                if (data==null)
                {
                        return null;
                }
                
                int len = data.length;
                String str = "";
                for (int i=0; i<len; i++) {
                        if ((data[i]&0xFF)<16)
                                str = str + "0" + java.lang.Integer.toHexString(data[i]&0xFF);
                        else
                                str = str + java.lang.Integer.toHexString(data[i]&0xFF);
                }
                return str;
        }
        
                
        public static byte[] hexToBytes(String str) {
                if (str==null) {
                        return null;
                } else if (str.length() < 2) {
                        return null;
                } else {
                        int len = str.length() / 2;
                        byte[] buffer = new byte[len];
                        for (int i=0; i<len; i++) {
                                buffer[i] = (byte) Integer.parseInt(str.substring(i*2,i*2+2),16);
                        }
                        return buffer;
                }
        }
        
        

        private static String padString(String source)
        {
          char paddingChar = ' ';
          int size = 16;
          int x = source.length() % size;
          int padLength = size - x;

          for (int i = 0; i < padLength; i++)
          {
                  source += paddingChar;
          }

          return source;
        }
        
        private static final String md5(final String s) {
            final String MD5 = "MD5";
            try {
                // Create MD5 Hash
                MessageDigest digest = java.security.MessageDigest
                        .getInstance(MD5);
                digest.update(s.getBytes());
                byte messageDigest[] = digest.digest();

                // Create Hex String
                StringBuilder hexString = new StringBuilder();
                for (byte aMessageDigest : messageDigest) {
                    String h = Integer.toHexString(0xFF & aMessageDigest);
                    while (h.length() < 2)
                        h = "0" + h;
                    hexString.append(h);
                }
                return hexString.toString();

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return "";
        }
}