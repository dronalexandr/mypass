package com.vegasoft.mypasswords.bussiness;

import ru.bullyboo.encoder.Encoder;
import ru.bullyboo.encoder.methods.AES;

public class SecureManager {

    public String encriptAES(String data) {
        return Encoder.BuilderAES()
                .method(AES.Method.AES_CBC_PKCS5PADDING)
                .message(data)
                .key("test key") // not necessary
                .keySize(AES.Key.SIZE_128) // not necessary
                .iVector("test vector") // not necessary
                .encrypt();
    }

    public String decryptAES(String data) {
        return Encoder.BuilderAES()
                .method(AES.Method.AES_CBC_PKCS5PADDING)
                .message(data)
                .key("test key") // not necessary
                .keySize(AES.Key.SIZE_128) // not necessary
                .iVector("test vector") // not necessary
                .encrypt();
    }

    public String encriptRSA(String data) {
        return "rsa";
    }
    public String decryptRSA(String data) {
        return "rsa";
    }

    public String getHash256(String data) {
        return Encoder.Hashes().sha256(data);
    }
}
