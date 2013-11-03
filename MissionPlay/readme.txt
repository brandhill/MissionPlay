The SDK tools create the debug keystore/key with predetermined names/passwords:

Keystore name: "debug.keystore"
Keystore password: "android"
Key alias: "androiddebugkey"
Key password: "android"
CN: "CN=Android Debug,O=Android,C=US"


key.store=missionplay.ks
key.alias=missionplay
key.store.password=missionplay.2013
key.alias.password=missionplay.2013


facebook api

-release
keytool -exportcert -alias missionplay -keystore missionplay.ks | openssl sha1 -binary | openssl base64
YMQ08cGJ2L9p64IgWrqaplaASqY=
UyUXm/wzHYj9/hLZQIHQ6Gyv83A=


-debug
keytool -exportcert -alias androiddebugkey -keystore C:\Users\Shih-Wei\.android\debug.keystore | openssl sha1 -binary | openssl base64
6llbpaBM2g==


keytool -exportcert -alias androiddebugkey -keystore %HOMEPATH%\.android\debug.keystore | openssl sha1 -binary | openssl base64
SIjkItq7J+/0NBk5GxCyCcFMd18=



 --  GCM ---
 
https://code.google.com/apis/console/b/0/#project:718695621323:access

keytool -list -v -keystore %HOMEPATH%\.android\debug.keystore

Certificate fingerprints:
         MD5:  F0:E8:74:21:63:A6:F3:A3:20:14:40:A9:D1:FE:D3:1E
         SHA1: 48:88:E4:22:DA:BB:27:EF:F4:34:19:39:1B:10:B2:09:C1:4C:77:5F
         SHA256: 45:1A:6A:81:03:C0:9B:1B:1A:8A:8B:84:6C:C2:EE:11:09:77:B4:0C:63:
A0:61:B5:17:01:04:52:1E:1F:F2:34

API key:    
AIzaSyCi8hSrNIwwRMAThvunNlKcdOFV59N1BMs



