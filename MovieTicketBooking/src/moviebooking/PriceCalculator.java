package moviebooking;

public class PriceCalculator {
	private static final double WEEKEND_SURCHARGE = 0.20;

	public static boolean isWeekend(int day) {
		return day == 6 || day == 7;
	}

	public static double ticketPrice(double basePrice, int day) {
		if (isWeekend(day)) {
			return basePrice + basePrice * WEEKEND_SURCHARGE;
		}
		return basePrice;
	}

	public static double discount(String code, double amount) {
		if (code == null || code.isBlank()) {
			return 0;
		}
		switch (code.trim().toUpperCase()) {
		case "SAVE10":
			return amount * 0.10;
		case "FLAT50":
			return Math.min(50, amount);
		default:
			throw new IllegalArgumentException("Invalid coupon code.");
		}
	}
}
