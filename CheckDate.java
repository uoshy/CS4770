import java.util.Date;
import java.util.Calendar;

public class CheckDate
{
	public static final long MILLISECONDS_PER_DAY = 86400000;
	public static final long MILLISECONDS_PER_HOUR = 3600000;
	public static final long MILLISECONDS_PER_MINUTE = 60000;

	public static void main(String[] args)
	{
		Calendar cal = Calendar.getInstance();
		cal.set(2016, 3, 4, 9, 30);

		Calendar now = Calendar.getInstance();
		long nowTime = now.getTime().getTime();
		long goalTime = cal.getTime().getTime();
		long diff = goalTime-nowTime;
		long days = diff/MILLISECONDS_PER_DAY;
		long hours = (diff - days*MILLISECONDS_PER_DAY) / MILLISECONDS_PER_HOUR;
		long minutes = (diff - (days*MILLISECONDS_PER_DAY) - hours*MILLISECONDS_PER_HOUR) / MILLISECONDS_PER_MINUTE; 
		System.out.printf("\n%d days, %d hours, %d minutes\n", days, hours, minutes);
	}
}