package core;

import front.AssistantFrame;
import util.Scribe;

public class Assistant {
	public static AssistantFrame assistantFrame;

	public static Scribe scribe = new Scribe(20);
	public static final int AREA_WIDTH = 424;
	public static String APP_INFO = "        INFO:\n" + "Console Assistant App\n"
			+ "A smol console assistant\n" + "Project started 27 may 2019\n"
			+ "Author: Marcin Chrąchol\n";

	public static void main(String[] args) 
	{
		Command.initialize();
		assistantFrame = new AssistantFrame(AREA_WIDTH);
	}
	
	public static void setInputEnabled(boolean enabled)
	{
		assistantFrame.setInputEnabled(enabled);
	}
	
	public static void error(Object message) 
	{
		write("ERROR", message);
	}

	public static void print(Object message) 
	{
		assistantFrame.addToArea("", message);
	}

	public static void sys(Object input) 
	{
		write("SYSTEM", input);
	}

	public static void log(Object input)
	{
		write("LOG", input);
	}

	public static void write(Object input)
	{
		write("ASSISTANT", input);
	}

	public static void write(String from, Object input) 
	{
		assistantFrame.addToArea(from, input);
	}
}
