package rsa;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.Cipher;
public class encNew {
	
protected static final String ALGORITHM = "RSA";
static PrivateKey privKey;
static PublicKey pubKey;
static String srcFileName,destFileName,srcFileName1,destFileName1,keylocation;
/* public static KeyPair generateKey() throws NoSuchAlgorithmException
{
KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
keyGen.initialize(1024);
KeyPair key = keyGen.generateKeyPair();
return key;
}*/
public static void encryptFile(String srcFileName, String destFileName, PublicKey key) throws Exception
{
encryptDecryptFile(srcFileName,destFileName, key, Cipher.ENCRYPT_MODE);
}
/**
* Decrypt file using 1024 RSA encryption
*
* @param srcFileName Source file name
* @param destFileName Destination file name
* @param key The key. For encryption this is the Private Key and for decryption this is the public key
* @param cipherMode Cipher Mode
* @throws Exception
*/
public static void decryptFile(String srcFileName, String destFileName, PrivateKey key) throws Exception
{
encryptDecryptFile(srcFileName,destFileName, key, Cipher.DECRYPT_MODE);
}
/**
* Encrypt and Decrypt files using 1024 RSA encryption
*
* @param srcFileName Source file name
* @param destFileName Destination file name
* @param key The key. For encryption this is the Private Key and for decryption this is the public key
* @param cipherMode Cipher Mode
* @throws Exception
*/

public static void encryptDecryptFile(String srcFileName, String destFileName, Key key, int cipherMode) throws Exception {
OutputStream outputWriter = null;
InputStream inputReader = null;
System.out.println("srcFileName ----- "+srcFileName);
System.out.println("destFileName ----- "+destFileName);
try
{
Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
String textLine = null;
byte[] buf = cipherMode == Cipher.ENCRYPT_MODE? new byte[100] : new byte[128];
int bufl;
// init the Cipher object for Encryption...
cipher.init(cipherMode, key);
// start FileIO
outputWriter = new FileOutputStream(destFileName);
inputReader = new FileInputStream(srcFileName);
while ( (bufl = inputReader.read(buf)) != -1)
{
System.out.println("hi sale");
byte[] encText = null;
if (cipherMode == Cipher.ENCRYPT_MODE)
encText = encrypt(copyBytes(buf,bufl),(PublicKey)key);
else
encText = decrypt(copyBytes(buf,bufl),(PrivateKey)key);
System.out.println("encText ----------- "+encText);
outputWriter.write(encText);
}
outputWriter.flush();
}
catch (Exception e)
{
throw e;
}
finally
{
try
{
if (outputWriter != null)
outputWriter.close();
if (inputReader != null)
inputReader.close();
}
catch (Exception e)
{}
}
}
public static byte[] copyBytes(byte[] arr, int length)
{
byte[] newArr = null;
if (arr.length == length)
newArr = arr;
else
{
newArr = new byte[length];
for (int i = 0; i < length; i++)
{
newArr[i] = (byte) arr[i];
}
}
System.out.println("newArr -- "+new String(newArr));
return newArr;
}
public static byte[] encrypt(byte[] text, PublicKey key) throws Exception
{
byte[] cipherText = null;
try
{
// get an RSA cipher object and print the provider
Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
// encrypt the plaintext using the public key
cipher.init(Cipher.ENCRYPT_MODE, key );
cipherText = cipher.doFinal(text);
}
catch (Exception e)
{
throw e;
}
return cipherText;
}
public static byte[] decrypt(byte[] text, PrivateKey key) throws Exception
{
byte[] dectyptedText = null;
try
{
// decrypt the text using the private key
Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
cipher.init(Cipher.DECRYPT_MODE, key);
try {
dectyptedText = cipher.doFinal(text);
} catch (Exception e) {
System.out.println("Exception ======== "+e);
e.printStackTrace();
}
System.out.println("dectyptedText ----------- "+dectyptedText);
}
catch (Exception e)
{
throw e;
}
return dectyptedText;
}

public void encryptor() throws Exception{
	//keylocation=Choose("Key Store");
	
	try {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
		keyGen.initialize(1024, random);
		//keyGen.initialize(1024);
		KeyPair key = keyGen.generateKeyPair();
		privKey = key.getPrivate();
		pubKey = key.getPublic();
		}
		catch (NoSuchAlgorithmException nsae){}
		byte[] key = privKey.getEncoded();
		FileOutputStream keyfos;
		try {
			keyfos = new FileOutputStream(keylocation);

			keyfos.write(key);
			keyfos.close();

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		encryptFile(encNew.srcFileName,encNew.destFileName,pubKey);
	
}

//decrypt file

public void decryptor() throws Exception{
	//keylocation=ChooseFile("Priv Key");
	
	FileInputStream keyfis = new FileInputStream(keylocation);
	byte[] encKey = new byte[keyfis.available()];
	keyfis.read(encKey);
	keyfis.close();
	PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(encKey);
	KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
	PrivateKey privKeyNew = keyFactory.generatePrivate(privKeySpec);
	//decryptFile(srcFileName,destFileName,privKeyNew);
decryptFile(encNew.srcFileName1,encNew.destFileName1,privKeyNew);



	
}

//File chooser functions

public static String Choose(String g){
	String tex="";
	Frame j=new Frame();
	j.setSize(400,400);
	FileDialog f=new FileDialog(j,g,FileDialog.SAVE);
	f.show();
	if(f.getFile()!=null){
		tex=f.getDirectory()+f.getFile();
	}
	return tex;
}
public static  String ChooseFile(String g){
	String text="";
	Frame j=new Frame();
	j.setTitle("Encryptor");
	j.setSize(400,400);
	FileDialog fd=new FileDialog(j,g,FileDialog.LOAD);
	fd.show();
	if(fd.getFile()!=null){
	text=fd.getDirectory()+fd.getFile();
    }
	return text;
}

public static void main(String args[]) throws NoSuchProviderException{
	encNew ee=new encNew();
//encNew. srcFileName = "D:/hosts.txt";
//encNew. destFileName = "D:/Transcriptviewcipher.txt";
//encNew. srcFileName1 = "D:/Transcriptviewcipher.txt";
//encNew.destFileName1 = "D:/Transcriptviewclr.txt";

//encNew.srcFileName=ee.ChooseFile("Choose Encrypted file!");
//String priv=ChooseFile("Choose Key file location");
//encNew.destFileName=ee.Choose("Decrypted output File Location");
/*try {
KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
keyGen.initialize(1024, random);
//keyGen.initialize(1024);
KeyPair key = keyGen.generateKeyPair();
privKey = key.getPrivate();
pubKey = key.getPublic();
}
catch (NoSuchAlgorithmException nsae){}
byte[] key = privKey.getEncoded();
FileOutputStream keyfos;
try {
	keyfos = new FileOutputStream("D:/privkey1");

	keyfos.write(key);
	keyfos.close();

} catch (IOException e1) {
	// TODO Auto-generated catch block
	e1.printStackTrace();
}
//keyfos.close();*/



try{
System.out.println("in try block of main");
int i=0;
System.out.println("Enter 1 or 2 for decrytp");
BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
i=Integer.parseInt(br.readLine());
if(i==1){
//encryptFile(encNew.srcFileName,encNew.destFileName,pubKey);
	encNew.srcFileName=ee.ChooseFile("Choose Encrypted file!");
	//String priv=ChooseFile("Choose Key file location");
	encNew.destFileName=ee.Choose("Decrypted output File Location");
	ee.encryptor();
}
else{
	/*FileInputStream keyfis = new FileInputStream("D:/privkey1");
	byte[] encKey = new byte[keyfis.available()];
	keyfis.read(encKey);
	keyfis.close();
	PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(encKey);
	KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
	PrivateKey privKeyNew = keyFactory.generatePrivate(privKeySpec);
	//decryptFile(srcFileName,destFileName,privKeyNew);
decryptFile(encNew.srcFileName1,encNew.destFileName1,privKeyNew);*/
	encNew. srcFileName1 = ee.ChooseFile("Cipher location");
	encNew.destFileName1 =ee.Choose("Clear Text");
	ee.decryptor();



}
}catch (Exception e){
}
}
}