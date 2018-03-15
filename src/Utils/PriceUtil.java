package Utils;

public class PriceUtil {
	public static int getPriceByDistence(int distence) {
		if (distence == 0)
			return -1;
		else if (distence <= 3000)
			return 2;
		else if (distence < 5000)
			return 3;
		else if (distence <= 10000)
			return 4;
		else
			return 5;
	}

	public static int getPriceByMinute(int minute) {
		if (minute <= 30)
			return 0;
		else
			return 3;
	}

	public static boolean isDiscountTime(String enterTime) {
		int enHour = Integer.valueOf(enterTime.split(":")[0]);
		int enMinute = Integer.valueOf(enterTime.split(":")[1]);
		if (enHour >= 10 && enHour < 15)
			return true;
		else if (enHour == 15 && enMinute == 0)
			return true;
		return false;
	}
}
