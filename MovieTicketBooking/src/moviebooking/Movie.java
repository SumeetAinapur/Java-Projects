package moviebooking;

public class Movie {
	private String title;
	private double basePrice;
	private int ratingTotal;
	private int ratingCount;
	private Theatre theatre;

	public Movie(String title, double basePrice, int rows, int seatsPerRow) {
		this.title = title;
		this.basePrice = basePrice;
		this.theatre = new Theatre(rows, seatsPerRow);
	}

	public String getTitle() {
		return title;
	}

	public double getBasePrice() {
		return basePrice;
	}

	public Theatre getTheatre() {
		return theatre;
	}

	public int getRatingCount() {
		return ratingCount;
	}

	public void rate(int stars) {
		if (stars < 1 || stars > 5) {
			throw new IllegalArgumentException("Rating must be between 1 and 5.");
		}
		ratingTotal += stars;
		ratingCount++;
	}

	// average is calculated from the total, so we never store a rounded value
	public double getAverageRating() {
		if (ratingCount == 0) {
			return 0;
		}
		return (double) ratingTotal / ratingCount;
	}
}