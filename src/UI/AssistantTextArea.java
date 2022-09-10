package UI;

import javax.swing.JTextArea;

public class AssistantTextArea extends JTextArea
{
	private static final long serialVersionUID = 1L;
	private final int LINE_LENGTH;

	public AssistantTextArea(int lineLength)
	{
		this.LINE_LENGTH = lineLength;
	}
	
	public void append(String senderOfMessage, Object messageToAppend)
	{
		super.append(prepareStringForAppending(senderOfMessage, stringify(messageToAppend)));
	}
	
	public void append(Object messageToAppend)
	{
		this.append( "ASSISTANT", messageToAppend );
	}
	
	public String prepareStringForAppending(String senderOfMessage, String precuratedString)
	{
		
		return null;
	}
	
	private String stringify(Object message)
	{
		return (message instanceof String) ? (String) message : message.toString();
	}
}
