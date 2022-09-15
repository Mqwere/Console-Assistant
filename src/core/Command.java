package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import util.CommandRunnable;
import util.CommandSentenceInterpreter;
import util.Converter;
import util.Date;
import util.ProcessHandler;

public class Command implements CommandRunnable
{
	protected String[] triggerKeywords;
	protected CommandRunnable runnable;
	
	public static ArrayList<Command> commands;
	
	private static final Command closeApp =
		new CommandBuilder()
		.setTriggerKeywords("close", "exit")
		.setRunnable(
			(args) -> 
			{
				System.exit(0);
			}
		)
		.build();
	
	private static final Command printInfoAboutApp =
		new CommandBuilder()
		.setTriggerKeywords("about", "info")
		.setRunnable(
			(args) -> 
			{
				Assistant.write(Assistant.APP_INFO);
			}
		)
		.build();
	
	//"(\bin\b|\bafter\b)* *([1-9a]+) *(\bdays*\b|\bweeks*\b|\byears*\b) *(\bago\b)*"gm
	
	private static final Command printDate =
		new CommandBuilder()
		.setTriggerKeywords("date")
		.setRunnable(
			(args) -> 
			{
				Date result = args.length > 0 
						? CommandSentenceInterpreter.interpretArgumentsForPrintDate(args)
						: new Date();
				
				if(result == null)
				{
					Assistant.write("Incorrect structure of given command.");
					return;
				}
				
				Assistant.write(result);
			}
		)
		.build();
	
	private static final Command runCmdCommandThroughAssistant = 
		new CommandBuilder()
		.setTriggerKeywords("cmd")
		.setRunnable(
				(args) -> 
				{
					new Thread(new ProcessHandler(Assistant.assistantFrame, args)).start();
				}
			)
		.build();
	
	private static final Command separateAllLettersWithinStringWithSpaces =
			new CommandBuilder()
			.setTriggerKeywords("hiperbolize", "separate", "sprt")
			.setRunnable(
					(args) -> 
					{
						
					}
				)
			.build();
	
	public static void initialize()
	{
		commands = new ArrayList<>(
			Arrays.asList(
				printInfoAboutApp,
				closeApp,
				printDate,
				runCmdCommandThroughAssistant
			)
		);
	}
	
	public static void respondToInput(String input)
	{
		try
		{
			String[] inputWordsArray = input.toLowerCase().split(" ");
			
			Optional<Command> responder = getRespondingCommand(inputWordsArray[0]);
			
			final String[] argumentsArray = Converter.getStringSubarrayWithoutFirstElement(inputWordsArray);
			responder.ifPresentOrElse(
				(cmd) ->
				{
					cmd.run(argumentsArray);
				}, 
				
				() ->
				{
					Assistant.write("Incorrect command");
				}
			);
		} 
		catch (Exception e)
		{ 
			e.printStackTrace();			
		}
	}
	
	public static ArrayList<String> getAllTriggers()
	{
		ArrayList<String> triggers = new ArrayList<>();
		
		commands
		.stream()
		.forEach(
			(cmd)->
			{ 
				triggers.addAll(Arrays.asList(cmd.triggerKeywords)); 
			}
		);
		
		return triggers;
	}
	
	private static Optional<Command> getRespondingCommand(String potentialTrigger)
	{
		return commands
				.stream()
				.filter(
					(cmd) -> 
					{
						return cmd.reactsTo(potentialTrigger);
					}
				)
				.findAny();
	}
	
	public Command(CommandBuilder builder)
	{
		this.triggerKeywords = builder.triggerKeywords.orElseThrow( );
		this.runnable = builder.runnable.orElseThrow( );
	}
	
	public boolean reactsTo(String possibleTrigger)
	{
		return 
			Arrays
				.asList(this.triggerKeywords)
				.stream()
				.anyMatch(
					(actualTrigger)->
					{
						return actualTrigger.equals(possibleTrigger);
					}
				);
	}

	@Override
	public void run(String... arguments)
	{
		this.runnable.run(arguments);
	}
}

class CommandBuilder
{
	Optional<String[]> triggerKeywords;
	Optional<CommandRunnable> runnable;
	
	public CommandBuilder()
	{
		
	}
	
	public CommandBuilder(String...triggerKeywords)
	{
		this.triggerKeywords = Optional.of(triggerKeywords);
	}
	
	public CommandBuilder setTriggerKeywords(String...triggerKeywords)
	{
		this.triggerKeywords = Optional.of(triggerKeywords);
		return this;
	}
	
	public CommandBuilder setRunnable(CommandRunnable runnable)
	{
		this.runnable = Optional.of(runnable);
		return this;
	}
	
	public Command build()
	{
		return new Command(this);
	}
}
