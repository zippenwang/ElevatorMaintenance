package wzp.project.android.elvtmtn.util;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;

public class DESUtil {
    //�㷨���� 
    public static final String KEY_ALGORITHM = "DES";
    //�㷨����/����ģʽ/��䷽ʽ 
    //DES�������ֹ���ģʽ-->>ECB���������뱾ģʽ��CBC�����ܷ�������ģʽ��CFB�����ܷ���ģʽ��OFB���������ģʽ
    public static final String CIPHER_ALGORITHM = "DES/ECB/NoPadding";

    /**
     *   
     * ������Կkey����
     * @param KeyStr ��Կ�ַ�����������16���ַ���ɣ�
     * @return ��Կ���� 
     * @throws InvalidKeyException   
     * @throws NoSuchAlgorithmException   
     * @throws InvalidKeySpecException   
     * @throws Exception 
     */
    private static SecretKey keyGenerator(String keyStr) throws Exception {
        byte input[] = HexString2Bytes(keyStr);
        DESKeySpec desKey = new DESKeySpec(input);
        //����һ���ܳ׹�����Ȼ��������DESKeySpecת����
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(desKey);
        return securekey;
    }

    private static int parse(char c) {
        if (c >= 'a') return (c - 'a' + 10) & 0x0f;
        if (c >= 'A') return (c - 'A' + 10) & 0x0f;
        return (c - '0') & 0x0f;
    }

    // ��ʮ�������ַ������ֽ�����ת�� 
    public static byte[] HexString2Bytes(String hexstr) {
        byte[] b = new byte[hexstr.length() / 2];
        int j = 0;
        for (int i = 0; i < b.length; i++) {
            char c0 = hexstr.charAt(j++);
            char c1 = hexstr.charAt(j++);
            b[i] = (byte) ((parse(c0) << 4) | parse(c1));
        }
        return b;
    }

    /** 
     * ��������
     * @param data ����������
     * @param key ��Կ
     * @return ���ܺ������ 
     */
    public static String encrypt(String data, String key) throws Exception {
        Key deskey = keyGenerator(key);
        // ʵ����Cipher�������������ʵ�ʵļ��ܲ���
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        SecureRandom random = new SecureRandom();
        // ��ʼ��Cipher��������Ϊ����ģʽ
        cipher.init(Cipher.ENCRYPT_MODE, deskey, random);
        byte[] results = cipher.doFinal(data.getBytes());
        // �ò�����Ϊ����ӽ������߲�����վ��http://tripledes.online-domain-tools.com/����ʮ�����ƽ�����к˶�
        for (int i = 0; i < results.length; i++) {
            System.out.print(results[i] + " ");
        }
        System.out.println();
        // ִ�м��ܲ��������ܺ�Ľ��ͨ��������Base64������д��� 
        return Base64.encodeBase64String(results);
    }

    /** 
     * �������� 
     * @param data ���������� 
     * @param key ��Կ 
     * @return ���ܺ������ 
     */
    public static String decrypt(String data, String key) throws Exception {
        Key deskey = keyGenerator(key);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        //��ʼ��Cipher��������Ϊ����ģʽ
        cipher.init(Cipher.DECRYPT_MODE, deskey);
        // ִ�н��ܲ���
        return new String(cipher.doFinal(Base64.decodeBase64(data)));
    }

    public static void main(String[] args) throws Exception {
        String source = "amigoxie";
        System.out.println("ԭ��: " + source);
        
//        String key = "A1B2C3D4E5F60708";
        String key = "wzp@dfa21&fdbnm!";
        String encryptData = encrypt(source, key);
        System.out.println("���ܺ�: " + encryptData);
        String decryptData = decrypt(encryptData, key);
        System.out.println("���ܺ�: " + decryptData);
    }
}