package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import core.Assistant;
import front.AssistantFrame;

public class ProcessHandler implements Runnable 
{
	private ProcessBuilder builder;
	private AssistantFrame assistantFrame;

	public ProcessHandler(AssistantFrame assistantFrame, String... input) 
	{
		this.assistantFrame = assistantFrame;
		this.builder = new ProcessBuilder(input);
		this.builder.redirectErrorStream(true);
	}

	@Override
	public void run() 
	{
		Assistant.setInputEnabled(false);
		Process process;
		
		try  
		{
			process = builder.start();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			ProcessActivityChecker activityChecker = new ProcessActivityChecker(100, 5);
			while (activityChecker.shouldAllowAnotherProcessRun()) 
			{
				String line;
				
				if ((line = bufferedReader.readLine()) != null) 
				{
					//System.out.printf("'%s'\n", line);
					assistantFrame.addToArea("", line);
					assistantFrame.scrollDown();
					activityChecker.clearIdleRunsCounter();
				}
				
				activityChecker.sleepThreadUntilNextActivityCheck();
			}
		}
		
		catch (IOException e) 
		{
			Assistant.error(e.toString());
		}
		
		finally
		{
			assistantFrame.scrollDown();
			Assistant.setInputEnabled(true);
		}
	}
}

class ProcessActivityChecker
{
	private final int milisOfSleepBetweenActivityChecks;
	private final int numberOfAllowedIdleRuns;
	private int currentNumberOfIdleRuns;
	
	public ProcessActivityChecker(int milisOfSleepBetweenActivityChecks, int numberOfAllowedIdleRuns)
	{
		this.milisOfSleepBetweenActivityChecks = milisOfSleepBetweenActivityChecks;
		this.numberOfAllowedIdleRuns = numberOfAllowedIdleRuns;
		this.currentNumberOfIdleRuns = 0;
	}
	
	public boolean shouldAllowAnotherProcessRun()
	{
		return this.currentNumberOfIdleRuns++ < this.numberOfAllowedIdleRuns;
	}
	
	public void clearIdleRunsCounter()
	{
		this.currentNumberOfIdleRuns = 0;
	}
	
	public void sleepThreadUntilNextActivityCheck()
	{
		try
		{
			Thread.sleep(milisOfSleepBetweenActivityChecks);
		} 
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}
