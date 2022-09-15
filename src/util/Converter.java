package util;

public class Converter
{
	public static String makeSureThisIsASingleLine(String allegedLine)
	{
		return allegedLine.split("\n")[0];
	}
	
	public static String stringArrayToConcatenatedString(char splitter, String...strings)
	{
		String output = "";
		
		for(String string: strings)
		{
			output += string + splitter;
		}
		
		return getStringWithoutTheLastCharacter(output);
	}
	
	public static String getStringWithoutTheLastCharacter(String input)
	{
		return input.length() <= 1 
				? ""
				: input.substring( 0, input.length() - 1 );
	}
	
	public static String[] getStringSubarrayWithoutFirstElement(String[] input)
	{
		if(input.length < 2) return new String[]{};
		
		String[] output = new String[ input.length - 1 ];
		
		for(int i = 0; i < output.length; i++)
		{
			output[ i ] = input[ i + 1 ];
		}
		
		return output;
	}
}
