package sg.edu.np.mad.mad2023_team2.ui.LoginSignup;
import android.util.Base64;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {
    private static final String secretKey = "123MADTeam2BEST"; // Hard-coded secret key for encryption
    private static final String salt = "TravelWISE123"; // Hard-coded salt for encryption

    // Method to encrypt a given string using AES encryption
    public static String encrypt(String strToEncrypt) {
        try {
            // Initialization Vector (IV) for encryption, typically random, but all zeros here
            byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

            // Creating a SecretKeyFactory for key generation
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

            // Create the PBEKeySpec object, using the secretKey and salt
            PBEKeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt.getBytes(), 65536, 256);

            // Generate the SecretKey
            SecretKey tmp = factory.generateSecret(spec);

            // Convert the SecretKey into a SecretKeySpec
            SecretKeySpec keySpec = new SecretKeySpec(tmp.getEncoded(), "AES");

            // Get a Cipher object for the AES encryption algorithm
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");

            // Initialize the Cipher in encrypt mode, using the generated key and IV
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv));

            // Perform the encryption and return the encrypted data encoded as a Base64 string
            return Base64.encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")), Base64.DEFAULT);

        } catch (Exception e) {
            // Print any exceptions that occur during encryption
            e.printStackTrace();
        }

        // Return null if encryption fails
        return null;
    }
}
