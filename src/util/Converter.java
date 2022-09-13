package util;

public class Converter
{
	public static String stringArrayToConcatenatedString(char splitter, String...strings)
	{
		String output = "";
		
		for(String string: strings)
		{
			output += string + splitter;
		}
		
		return output.substring(0, output.length( ) > 0 ? output.length( ) - 1 : 0);
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
