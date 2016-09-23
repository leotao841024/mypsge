package tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageEncodeUtil {
	    public static final String MD5 = "MD5";
	    public static final String SHA_1 = "SHA-1";
	    public static final String SHA_256 = "SHA-256";
	    private static final char[] CH_HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8','9', 'A', 'B', 'C', 'D', 'E', 'F'};
	    private static final char[] CH_HEX_LOWWER= {'0', '1', '2', '3', '4', '5', '6', '7', '8','9', 'a', 'b', 'c', 'd', 'e', 'f'};
	    public static String passAlgorithmsCiphering(String sourceStr,String algorithmsName,String encodingTyp,Boolean islower){
	        String password = "";
	        MessageDigest md;
	        try {
	            md = MessageDigest.getInstance(algorithmsName);
	            md.update(sourceStr.getBytes());
	            byte[] b = md.digest();
	            password = byteArrayToHex(b,"");
	        } catch (NoSuchAlgorithmException e) {
	            e.printStackTrace();
	        }
	        return password;
	    }
	    
	    private byte[] getBytes(String pmsg,String encodingTyp){
	    	try{
	    		return pmsg.getBytes(encodingTyp);
	    	}catch(Exception ex){
	    		return null;
	    	}
	    }
	    
	    
	    
	    
	    private static String byteArrayToHex(byte[] bytes,String encodeTyp) {
	        char[] chars = new char[bytes.length * 2];
	        int index = 0;
	        for (byte b : bytes) {
	            chars[index++] = CH_HEX[b >>> 4 & 0xf];
	            chars[index++] = CH_HEX[b & 0xf];
	        }
	        return new String(chars);
	    }
}
