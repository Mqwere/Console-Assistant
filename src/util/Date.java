package util;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class Date
{
	public static int timeZoneOffset = 2;
	
	private final ZonedDateTime timeAtCreation;
	
	public Date()
	{
		this.timeAtCreation = Instant.now().atZone(ZoneOffset.ofHours(timeZoneOffset));
	}
	
	public Date(ZonedDateTime zdt)
	{
		this.timeAtCreation = zdt;
	}
	
	public Date changeByNumberOfTimeUnits(DateUnit unit, int numberOfUnits)
	{
		ZonedDateTime wantedZoneDateTime;
		switch(unit)
		{
			default:
			case DAY:
				wantedZoneDateTime = numberOfUnits >= 0 
					? timeAtCreation.plusDays(numberOfUnits)
					: timeAtCreation.minusDays(-numberOfUnits);
				break;
				
			case MONTH:
				wantedZoneDateTime = numberOfUnits >= 0 
					? timeAtCreation.plusMonths(numberOfUnits)
					: timeAtCreation.minusMonths(-numberOfUnits);
				break;
				
			case WEEK:
				wantedZoneDateTime = numberOfUnits >= 0 
					? timeAtCreation.plusDays(7 * numberOfUnits)
					: timeAtCreation.minusDays(-7 * numberOfUnits);
				break;
				
			case YEAR:
				wantedZoneDateTime = numberOfUnits >= 0 
					? timeAtCreation.plusYears(numberOfUnits)
					: timeAtCreation.minusYears(-numberOfUnits);
				break;
		}
		
		return new Date(wantedZoneDateTime);
	}
	
	public String toTimeString()
	{
		return
		String.format(
			"%02d:%02d:%02d", 
			timeAtCreation.getHour(),
			timeAtCreation.getMinute(),
			timeAtCreation.getSecond()
		);
	}
	
	@Override
	public String toString()
	{
		return
		String.format(
			"%02d.%02d.%d, %s", 
			timeAtCreation.getDayOfMonth(),
			timeAtCreation.getMonthValue(),
			timeAtCreation.getYear(),
			timeAtCreation.getDayOfWeek().toString().toLowerCase()
		);
	}

}
