package front;

import javax.swing.JTextArea;

public class AssistantTextArea extends JTextArea
{
	private static final long serialVersionUID = 1L;
	private final int LINE_LENGTH;

	public AssistantTextArea(int lineLength)
	{
		super();
		this.LINE_LENGTH = lineLength;
	}
	
	public void append(String senderOfMessage, Object messageToAppend)
	{
		super.append(prepareStringForAppending(senderOfMessage, stringify(messageToAppend))+"\n");
	}
	
	public void append(Object messageToAppend)
	{
		this.append( "ASSISTANT", messageToAppend );
	}
	
	public String prepareStringForAppending(String senderOfMessage, String precuratedString)
	{
		String outputString = "";
		int paragraphOffset = senderOfMessage.length(); 
		if( paragraphOffset > 0 ) 
		{
			paragraphOffset += 3;
			outputString = "[" + senderOfMessage +"]:";
		}
		
		String[] precuratedStringLines = precuratedString.split("\n");
		
		for(int i = 0; i < precuratedStringLines.length; i++)
		{
			outputString += 
				curateLineToLineLength(precuratedStringLines[i], paragraphOffset) + "\n"
					+ (
						(i + 1) < precuratedStringLines.length
							? getAStringContainingANumberOfSpaces(paragraphOffset)
							: ""
					);
		}
		
		return outputString;
	}
	
	private String curateLineToLineLength(String uncuratedLine, int paragraphOffset)
	{
		String curatedLine = "";
		
		String[] words = uncuratedLine.split(" ");
		
		int currentLineLength = 0;
		
		for(String word: words)
		{
			if(currentLineLength + word.length() > LINE_LENGTH)
			{
				curatedLine += "\n " + getAStringContainingANumberOfSpaces(paragraphOffset);
				currentLineLength = paragraphOffset + 1 + word.length();
			}
			else
			{
				curatedLine += " " + word;
				currentLineLength += 1 + word.length();
			}
		}
				
		return curatedLine;
	}
	
	private String getAStringContainingANumberOfSpaces(int number)
	{
		String output = "";
		
		for(int i = 0; i < number; i++)
			output += " ";
		
		return output;
	}
	
	private String stringify(Object message)
	{
		return (message instanceof String) ? (String) message : message.toString();
	}
	
}
