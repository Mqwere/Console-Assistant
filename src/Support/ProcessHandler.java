package Support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import Core.Assistant;

public class ProcessHandler implements Runnable {
	private ProcessBuilder builder;

	public ProcessHandler(String... input) 
	{
		builder = new ProcessBuilder(input);
		builder.redirectErrorStream(true);
	}

	@Override
	public void run() 
	{
		Assistant.setInputEnabled(false);
		Process p;
		try 
		{
			p = builder.start();
			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			int in = 0;
			while (in<100) 
			{
				in++;
				String line = r.readLine();
				
				if (line == null) 
				{ 
					break; 
				}
				
				Assistant.write("",line);
				Assistant.assistantFrame.scrollDown();
			}
			Assistant.setInputEnabled(true);
		} 
		catch (IOException e) 
		{
			Assistant.error(e.toString());
			Assistant.setInputEnabled(true);
		}
	}
}
