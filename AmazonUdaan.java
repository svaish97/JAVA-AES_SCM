
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;


public class AmazonUdaan {
	
	private static final String ENCRYPT_ALGO = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH_BIT = 128;
    private static final int IV_LENGTH_BYTE = 12;
    private static final Charset UTF_8 = StandardCharsets.UTF_8;
    private static final Base64.Encoder encoder = Base64.getEncoder();
    private static final Base64.Decoder decoder = Base64.getDecoder();
	private static final String MOBILE_DECRYPTION_ALGO = "AES";
	private static final byte[] aad={
			52, 121, 99, 30, -73, -92, -25, 112, -11, 18, 20, 31, 106, -103, -111, 84, 69, 59, -43, 71, 105, -116, -99,
			-123, -49, -92, 1, -80, -95, 81, -91, 53, 27, 83, 104, -83, 22, 111, -71, -113, 74, -74, 65, -4, 41, 59, 77,
			-44, 13, 104, 76, -101, -49, 58, 89, 71, -81, -25, -91, -81, 98, -79, 22, 87, 86, 66, -34, 68, 17, 63, -83,
			87, 33, 87, -1, -72, 46, 53, 107, -61, 84, 51, 59, 62, 44, 61, -1, -59, 23, 7, 57, 94, 27, 2, 10, -114, 81,
			92, 90, 121, 18, 78, 92, -105, 75, -89, 120, -120, 7, 113, -60, 95, -14, -44, -108, 102, 61, -19, 73, -5,
			77, 50, 87, 107, 59, -78, 91, 109 };
	private static final byte[] ivVector= {-119, -30, 121, 25, 41, -11, -16, -80, 89, -3, -29, 118};
    
	public static void main(String[] args) throws Exception {
		 String hexKey="376EE46751E451986CDB5028F2F5F9F9667C4C886735A643BF771BFB62B03C28";
		 byte[] key = ByteUtil.hex2byte(hexKey);
         SecretKeySpec secretKeySpec= secretKeySpec= new SecretKeySpec(key, MOBILE_DECRYPTION_ALGO);
         String encrypted=encrypt("9561446368", secretKeySpec);
         System.out.println(encrypted);
         String decrypted=decrypt(encrypted, secretKeySpec);
         System.out.println(decrypted);
	}
	


    public static String encrypt(String original, SecretKey secretKey) throws Exception {
        byte[] iv = new byte[] {-119, -30, 121, 25, 41, -11, -16, -80, 89, -3, -29, 118};
        System.out.println("Random Generated IV->" + Arrays.toString(iv));
        Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(TAG_LENGTH_BIT, iv));
        cipher.updateAAD(iv);
        byte[] cipherText = cipher.doFinal(original.getBytes(UTF_8));
        return encoder.encodeToString(ByteBuffer
            .allocate(iv.length + cipherText.length)
            .put(iv)
            .put(cipherText)
            .array());
    }

    public static String decrypt(String encrypted, SecretKey secretKey) throws Exception {
        ByteBuffer byteBuffer = ByteBuffer.wrap(decoder.decode(encrypted));
        byte[] iv = new byte[IV_LENGTH_BYTE];
        byteBuffer.get(iv);
        System.out.println("Extracted IV during decryption->" + Arrays.toString(iv));
        byte[] cipherText = new byte[byteBuffer.remaining()];
        byteBuffer.get(cipherText);
        Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(TAG_LENGTH_BIT, iv));
        cipher.updateAAD(iv);
        return new String(cipher.doFinal(cipherText));
    }
}
