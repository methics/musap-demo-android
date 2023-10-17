# MUSAP Library

## Enabling an SSCD

Call `MusapClient.init()` and MusapClient.enableSscd

```java
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MusapClient.init(this);
        MusapClient.enableSscd(new AndroidKeystoreSscd(this));
        MusapClient.enableSscd(new YubiKeySscd(this));
    }
}

```

## Generating a key

```java
KeyGenReq req = new KeyGenReqBuilder()
        .setActivity(this.getActivity())
        .setView(this.getView())
        .setAlias("my key")
        .setKeyAlgorithm(MusapKeyAlgorithm.RSA_2K)
        .createKeyGenReq();

MusapClient.generateKey(sscd, req, new MusapCallback<MusapKey>() {
    @Override
    public void onSuccess(MusapKey result) {
        MLog.d("Successfully generated key " + alias);
    }

    @Override
    public void onException(MusapException e) {
        MLog.e("Failed to generate key " + alias, e);
    }
});

```

## Signing

```java

MusapKey       key = MusapClient.getKeyByUri(keyuri);
SignatureReq   req = new SignatureReqBuilder().setKey(key).setData(data).setActivity(this.getActivity()).createSignatureReq();
MusapSigner signer = new MusapSigner(key, this.getActivity());

try {
    signer.sign(data, new MusapCallback<MusapSignature>() {
        @Override
        public void onSuccess(MusapSignature mSig) {
            String signatureStr = mSig.getB64Signature();
            MLog.d("Signature successful: " + signatureStr);
        }

        @Override
        public void onException(MusapException e) {
            MLog.e("Failed to sign", e.getCause());
        }
    });
} catch (MusapException e) {
    MLog.e("Failed to sign", e.getCause());
}

```

