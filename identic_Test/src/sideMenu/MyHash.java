package sideMenu;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

public class MyHash {
	private static int bufSize = 1024;

	public static void main(String[] args) {
		long c1 = Calendar.getInstance().getTimeInMillis();
		String filePath = "C:\\Temp\\testfile1.dat";
		// String filePath = "C:\\Temp\\Click.WAV";
		String fileHash = null;
		try {
			fileHash = hashFile(filePath, "SHA-256");
		} catch (HashGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // try

		long c2 = Calendar.getInstance().getTimeInMillis();

		System.out.printf("bufSize = %d , TimeInMillis = %d%n", bufSize, c2 - c1);

		System.out.printf("fileHash = %s%n", fileHash);
		System.out.printf("filePath = %s%n", filePath);

	}// main

	private static String hashFile1(String file, String algorithm) throws HashGenerationException {
		try {
			MessageDigest digest = MessageDigest.getInstance(algorithm);
			byte[] bigByte = Files.readAllBytes(Paths.get(file));
			byte[] hashedBytes = digest.digest(bigByte);
			return convertByteArrayToHexString(hashedBytes);
		} catch (IOException | NoSuchAlgorithmException e) {
			throw new HashGenerationException("Could not generate hash from file", e);
		} // try
	}// hashFile1

	private static String hashFile(String file, String algorithm) throws HashGenerationException {
		try (FileInputStream inputStream = new FileInputStream(file)) {
			MessageDigest digest = MessageDigest.getInstance(algorithm);

			byte[] bytesBuffer = new byte[bufSize];
			int bytesRead = -1;

			while ((bytesRead = inputStream.read(bytesBuffer)) != -1) {
				digest.update(bytesBuffer, 0, bytesRead);
			} // while

			byte[] hashedBytes = digest.digest();

			return convertByteArrayToHexString(hashedBytes);
		} catch (NoSuchAlgorithmException | IOException ex) {
			throw new HashGenerationException("Could not generate hash from file", ex);
		} // try
	}// hashFile

	private static String convertByteArrayToHexString(byte[] arrayBytes) {
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < arrayBytes.length; i++) {
			stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16).substring(1));
		} // for
		return stringBuffer.toString();
	}// convertByteArrayToHexString

}// class MyHash
