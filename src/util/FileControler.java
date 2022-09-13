package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import core.Assistant;

public class FileControler {

	private static JFileChooser fileChooser = new JFileChooser(".");

	public static String fileToByteArray(String path) {
		File file = new File(path);
		FileInputStream inStream;
		try {
			inStream = new FileInputStream(file);
			int content;
			ArrayList<Byte> temp = new ArrayList<Byte>();
			while ((content = inStream.read()) != -1) {
				temp.add((byte) (content/*-44*/));
			}
			inStream.close();
			String boop = new String();
			for (Byte b : temp)
				boop += (char) Byte.toUnsignedInt(b);
			// Program.log(boop);
			return boop;
		} catch (Exception e) {
			e.printStackTrace();
			Assistant.error("FileControler.saveToFile: " + e.toString());
			return null;
		}
	}

	public static String fileToByteArray(JFrame parent) {
		int choice = fileChooser.showOpenDialog(parent);
		File file;
		if (choice == JFileChooser.APPROVE_OPTION) {
			file = fileChooser.getSelectedFile();
			FileInputStream inStream;
			try {
				inStream = new FileInputStream(file);
				int content;
				ArrayList<Byte> temp = new ArrayList<Byte>();
				while ((content = inStream.read()) != -1) {
					temp.add((byte) (content/*-44*/));
				}
				inStream.close();
				String boop = new String();
				for (Byte b : temp)
					boop += (char) Byte.toUnsignedInt(b);
				// Program.log(boop);
				return boop;
			} catch (Exception e) {
				e.printStackTrace();
				Assistant.error("FileControler.saveToFile: " + e.toString());
				return null;
			}
		} else {
			// Program.error("File Controler.fileToByteArray: choice is NULL");
			return null;
		}
	}
	
	public static boolean saveToFile(String path, byte[] input) {
		File file = new File(path);

		FileOutputStream inStream;
		try {
			inStream = new FileOutputStream(file);
			int content;
			for (int i = 0; i < input.length; i++) {
				content = (int) input[i];
				inStream.write(content/* +44 */);
			}
			inStream.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			Assistant.error("FileControler.saveToFile: " + e.toString());
			return false;
		}
	}

	public static boolean saveToFile(JFrame parent, byte[] input) {
		int choice = fileChooser.showSaveDialog(parent);
		File file;
		if (choice == JFileChooser.APPROVE_OPTION)
			file = fileChooser.getSelectedFile();
		else
			file = new File("C:\\Users\\" + System.getProperty("user.name") + "\\Desktop\\NCFile.bin");

		FileOutputStream inStream;
		try {
			inStream = new FileOutputStream(file);
			int content;
			for (int i = 0; i < input.length; i++) {
				content = (int) input[i];
				inStream.write(content/* +44 */);
			}
			inStream.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			Assistant.error("FileControler.saveToFile: " + e.toString());
			return false;
		}
	}
}
