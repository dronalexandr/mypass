package com.vegasoft.mypasswords.bussiness;

import java.security.KeyPair;

import ru.bullyboo.encoder.Encoder;
import ru.bullyboo.encoder.methods.AES;
import ru.bullyboo.encoder.methods.RSA;

public class SecureManager {

    public static String encryptAES(String data) {
        return Encoder.BuilderAES()
                .method(AES.Method.AES_CBC_PKCS5PADDING)
                .message(data)
                .key("test key") // not necessary
                .keySize(AES.Key.SIZE_128) // not necessary
                .iVector("test vector") // not necessary
                .encrypt();
    }

    public static String decryptAES(String data) {
        return Encoder.BuilderAES()
                .method(AES.Method.AES_CBC_PKCS5PADDING)
                .message(data)
                .key("test key") // not necessary
                .keySize(AES.Key.SIZE_128) // not necessary
                .iVector("test vector") // not necessary
                .decrypt();
    }

    public static String encryptRSA(String data) {
        // TODO: 02.01.2018 implement rsa
        return Encoder.BuilderAES()
                .method(AES.Method.AES_CBC_PKCS5PADDING)
                .message(data)
                .key("test key") // not necessary
                .keySize(AES.Key.SIZE_128) // not necessary
                .iVector("test vector") // not necessary
                .encrypt();
    }
    public static String decryptRSA(String data) {
        // TODO: 02.01.2018 implement rsa
        return Encoder.BuilderAES()
                .method(AES.Method.AES_CBC_PKCS5PADDING)
                .message(data)
                .key("test key") // not necessary
                .keySize(AES.Key.SIZE_128) // not necessary
                .iVector("test vector") // not necessary
                .decrypt();
    }

    public static String getHash256(String data) {
        return Encoder.Hashes().sha256(data);
    }

    public static void generateKey(){
        KeyPair key = Encoder.BuilderRSA().keySize(RSA.setKeySize(2048)).generateKey();
        System.out.println("generateKey = " + key);
        System.out.println("generateKey public key = " + key.getPublic());
        System.out.println("generateKey private key = " + key.getPrivate());
    }
}
