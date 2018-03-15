package Utils;

public class TimeUtil {
	public static boolean checkValid(String enterTime, String exitTime) {
		// TODO test time valid
		int enHour = Integer.valueOf(enterTime.split(":")[0]);
		int enMinute = Integer.valueOf(enterTime.split(":")[1]);
		int exHour = Integer.valueOf(exitTime.split(":")[0]);
		int exMinute = Integer.valueOf(exitTime.split(":")[1]);
		if (enHour > exHour)
			return false;
		else if(enHour == exHour && exMinute < enMinute)
			return false;
		return true;
	}
	public static int getTime(String enterTime, String exitTime) {
		// TODO getPriceByTime
		int enHour = Integer.valueOf(enterTime.split(":")[0]);
		int enMinute = Integer.valueOf(enterTime.split(":")[1]);
		int exHour = Integer.valueOf(exitTime.split(":")[0]);
		int exMinute = Integer.valueOf(exitTime.split(":")[0]);
		int time = (exHour - enHour)*60 + exMinute - enMinute;
		return time;
	}
}
