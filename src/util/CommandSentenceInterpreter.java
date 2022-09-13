package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandSentenceInterpreter
{
	
	private static Pattern dateArgumentsPattern = 
		Pattern.compile(
			"(\\bin\\b|\\bafter\\b)* *([1-9a]+) *"
		+	"(\\bday|\\bweek|\\bmonth|\\byear)s*\\b *"
		+	"(\\bago\\b|\\bbefore\\b|\\bprior\\b)*"
		);
	
	public static Date interpretArgumentsForPrintDate(String...args)
	{
		String sentence = Converter.stringArrayToConcatenatedString(' ', args).toLowerCase();
		Matcher argumentsMatcher = dateArgumentsPattern.matcher(sentence);
		if(argumentsMatcher.find())
		{
			String 
				wordRepresentingFuture = argumentsMatcher.group(1),
				wordRepresentingNumberOfTimeUnits = argumentsMatcher.group(2),
				wordRepresentingTypeOfATimeUnit = argumentsMatcher.group(3),
				wordRepresentingPast = argumentsMatcher.group(4);
			
//			Assistant.write
//			(
//				String.format
//				(
//					"1: %s\n" +
//					"2: %s\n" +
//					"3: %s\n" +
//					"4: %s\n",
//					wordRepresentingFuture,
//					wordRepresentingNumberOfTimeUnits,
//					wordRepresentingTypeOfATimeUnit,
//					wordRepresentingPast
//				)
//			);
			
			if(wordRepresentingFuture == null && wordRepresentingPast == null) 
				return null;
			
			boolean
				requestedTimeIsFuture = (wordRepresentingFuture != null);
			
			DateUnit 
				unitUsed = DateUnit.valueOf(wordRepresentingTypeOfATimeUnit.toUpperCase());
			
			int
				numberOfUnits = wordRepresentingNumberOfTimeUnits.equals("a") ? 1:Integer.parseInt(wordRepresentingNumberOfTimeUnits);
			
			return new Date()
					.changeByNumberOfTimeUnits(
						unitUsed, 
						numberOfUnits * (requestedTimeIsFuture? 1:-1)
					);
		}
		
		return null;
	}
}
