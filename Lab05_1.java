import java.io.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.util.Arrays;

public class Lab05_1 {
  private static String[] DesModes = {
      "DES/ECB/PKCS5Padding",
      "DES/ECB/NoPadding",
      "DES/CBC/PKCS5Padding",
      "DES/CBC/NoPadding", };

  public static void doEncryptDES(String keyFile, String plaintextFile, Integer DesMode) {

    try {
      // Create new encrypt file
      File desFile = new File("output.enc");
      FileInputStream fis;
      FileOutputStream fos;
      CipherInputStream cis;

      // Create a Secret key
      File f = new File(keyFile);
      FileReader fr = new FileReader(f);
      BufferedReader br = new BufferedReader(fr);
      String line = br.readLine();
      byte key[] = line.getBytes();
      SecretKeySpec secretKey = new SecretKeySpec(key, "DES");
      fr.close();
      br.close();

      // Create Cipher object
      Cipher encrypt = Cipher.getInstance(DesModes[DesMode]);
      encrypt.init(Cipher.ENCRYPT_MODE, secretKey);
      long start = System.currentTimeMillis();

      // Open the Plaintext file
      try {
        fis = new FileInputStream(plaintextFile);
        cis = new CipherInputStream(fis, encrypt);

        // Write to the Encrypted file
        fos = new FileOutputStream(desFile);
        byte[] b = new byte[8];
        int i = cis.read(b);
        while (i != -1) {
          fos.write(b, 0, i);
          i = cis.read(b);
        }
        long end = System.currentTimeMillis();
        long elapsedTime = end - start;
        System.out.printf("Time encrypt = %d miliseconds\n", elapsedTime);
        System.out.print("File Encrypt in output.enc\n");
        fos.flush();
        fos.close();
        cis.close();
        fis.close();
      } catch (IOException err) {
        System.out.println("Cannot open file!");
        System.exit(-1);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void doDecryptDES(String args[], Integer desMode) {
    if (args.length < 2) {
      System.out.println("Usage: java Lab05_1 dec <file name encrypt> <file name key>\n");
      System.exit(-1);
    }
    try {
      File desFile = new File("output.enc");
      File desFileBis = new File("output.dec");
      FileInputStream fis;
      FileOutputStream fos;
      CipherInputStream cis;

      // Create a Secret key
      File f = new File(args[1]);
      FileReader fr = new FileReader(f);
      BufferedReader br = new BufferedReader(fr);
      String line = br.readLine();
      byte key[] = line.getBytes();
      SecretKeySpec secretKey = new SecretKeySpec(key, "DES");
      fr.close();
      br.close();

      // Create Cipher object
      Cipher decrypt = Cipher.getInstance(DesModes[desMode]);
      decrypt.init(Cipher.DECRYPT_MODE, secretKey);

      // Open the Encrypted file
      fis = new FileInputStream(desFile);
      cis = new CipherInputStream(fis, decrypt);
      long start = System.currentTimeMillis();
      // Write to the Decrypted file
      fos = new FileOutputStream(desFileBis);
      byte[] b = new byte[8];
      int i = cis.read(b);
      while (i != -1) {
        fos.write(b, 0, i);
        i = cis.read(b);
      }
      long end = System.currentTimeMillis();
      long elapsedTime = end - start;
      System.out.printf("Time decrypt = %d miliseconds\n", elapsedTime);
      System.out.print("File Decrypt in output.dec, You can check this file with plaintext File\n");
      fos.flush();
      fos.close();
      cis.close();
      fis.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  static public void main(String[] args) throws Exception {
    System.out.println("Lab05_1");
    System.out.print("Enter mode: ");
    /*
     * private static String[] DesModes = {
     * "DES/ECB/PKCS5Padding",
     * "DES/ECB/NoPadding",
     * "DES/CBC/PKCS5Padding",
     * "DES/CBC/NoPadding", };
     */
    System.out.println("1. DES/ECB/PKCS5Padding");
    System.out.println("2. DES/ECB/NoPadding");
    System.out.println("3. DES/CBC/PKCS5Padding");
    System.out.println("4. DES/CBC/NoPadding");
    System.out.print("Choose: ");
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    String choose = br.readLine();
    Integer DesMode = Integer.parseInt(choose) - 1;

    if (DesMode < 0 || DesMode > 3) {
      System.out.println("Invalid choice. Please choose 1 or 2.");
      System.exit(-1);
    }

    System.out.println("Choose encrypt or decrypt");
    System.out.println("1. Encrypt");
    System.out.println("2. Decrypt");
    System.out.print("Choose: ");
    choose = br.readLine();

    if (choose.equals("1")) {
      System.out.print("Enter filename of plaintext: ");
      String plaintextFile = br.readLine();

      System.out.print("Enter filename of key: ");
      String key = br.readLine();

      doEncryptDES(key, plaintextFile, DesMode);

    } else if (choose.equals("2")) {
      System.out.print("Enter filename of encrypt: ");
      String encryptFile = br.readLine();

      System.out.print("Enter filename of key: ");
      String key = br.readLine();
      doDecryptDES(new String[] { encryptFile, key }, DesMode);
    } else {
      System.out.println("Invalid choice. Please choose 1 or 2.");
    }
  }

}
