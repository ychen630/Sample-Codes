package util;
/*
 * data util
 * 
 */
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DataUtil {
	//byte to hex
	public static String bytesToHex(byte[] hash) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < hash.length; i++) {
			String hex = Integer.toHexString(0xff & hash[i]);
			if(hex.length() == 1) hexString.append('0');
			hexString.append(hex);
		}
		return hexString.toString();
	}

	//get hashed password
	public static String returnHash(String originalString) {
		String hashedString = new String();
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] encodedhash = digest.digest(
					originalString.getBytes(StandardCharsets.UTF_8));
			hashedString = bytesToHex(encodedhash);
			System.out.println("hashed String: " + hashedString);
		}
		catch (NoSuchAlgorithmException ex) {
			System.err.println("Error! Algorithm for hash undefined!");
		}
		return hashedString;
	}
	
	//bool to int
	public static int boolToInt(boolean b) {
		return b ? 1 : 0;
	}
	
	//email validation
    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
 }
}
