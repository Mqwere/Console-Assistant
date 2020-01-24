package Support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JScrollBar;

import Core.Assistant;

public class ProcessHandler implements Runnable {
		private ProcessBuilder builder;

	public ProcessHandler(String... input) {
		builder = new ProcessBuilder(input);
		builder.redirectErrorStream(true);
	}

	@Override
	public void run() {
		Assistant.UI.inputField.setEnabled(false);
		Process p;
		try {
			p = builder.start();
	        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
	        int in = 0;
			while (in<100) {
	            in++;
	            String line = r.readLine();
	            if (line == null) { break; }
	            Assistant.write("",line);
	            Assistant.UI.scrollDown();
	        }
			Assistant.UI.inputField.setEnabled(true);
			Assistant.UI.inputField.requestFocus();
		} catch (IOException e) {
			Assistant.error(e.toString());
			Assistant.UI.inputField.setEnabled(true);
			Assistant.UI.inputField.requestFocus();
		}
	}
}
