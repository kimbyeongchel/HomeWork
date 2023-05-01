package javaSearch;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class TestClient {
	public static void main(String[] args) {
		//ST<String, Integer> st = new ST<String, Integer>();
		File file;
		final JFileChooser fc = new JFileChooser();
		
		if(fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) file = fc.getSelectedFile();
		else {
			JOptionPane.showMessageDialog(null, "파일을 선택하세요", "오류", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		
	}
}
