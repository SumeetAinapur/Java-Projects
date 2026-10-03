package moviebooking;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

	private static final String[] DAYS = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday",
			"Sunday" };
	private static Scanner sc = new Scanner(System.in);
	private static Movie[] movies = new Movie[3];

	public static void main(String[] args) {
		loadSampleData();
		int choice;
		do {
			printMenu();
			choice = readInt("Enter choice: ");
			try {
				switch (choice) {
				case 1 -> showMovies();
				case 2 -> showSeats();
				case 3 -> bookTicket();
				case 4 -> cancelTicket();
				case 5 -> rateMovie();
				case 0 -> System.out.println("Thank you, visit again!");
				default -> System.out.println("Invalid choice, try again.");
				}
			} catch (IllegalArgumentException | IllegalStateException e) {
				System.out.println("Error: " + e.getMessage());
			}
		} while (choice != 0);
	}

	private static void printMenu() {
		System.out.println();
		System.out.println("===== MOVIE TICKET BOOKING =====");
		System.out.printf("%d. %s%n", 1, "Show movies and ratings");
		System.out.printf("%d. %s%n", 2, "Show available seats");
		System.out.printf("%d. %s%n", 3, "Book ticket");
		System.out.printf("%d. %s%n", 4, "Cancel ticket");
		System.out.printf("%d. %s%n", 5, "Rate a movie");
		System.out.printf("%d. %s%n", 0, "Exit");
	}

	private static void showMovies() {
		System.out.println();
		System.out.printf("%-4s %-22s %12s   %s%n", "No.", "Movie", "Price (Rs.)", "Rating");
		System.out.println("-".repeat(62));
		for (int i = 0; i < movies.length; i++) {
			Movie m = movies[i];
			String rating = m.getRatingCount() == 0 ? "No ratings yet"
					: String.format("%.1f / 5 (%d)", m.getAverageRating(), m.getRatingCount());
			System.out.printf("%-4d %-22s %12.2f   %s%n", i + 1, m.getTitle(), m.getBasePrice(), rating);
		}
		System.out.println("Weekend (Sat/Sun) tickets cost 20% extra.");
	}

	private static void showSeats() {
		Movie movie = chooseMovie();
		System.out.println("Seats for " + movie.getTitle());
		movie.getTheatre().showSeats();
	}

	private static void bookTicket() {
		Movie movie = chooseMovie();
		Theatre theatre = movie.getTheatre();
		int day = readDay();
		theatre.showSeats();
		int count = readInt("How many seats do you want? ");
		if (count <= 0) {
			throw new IllegalArgumentException("Number of seats must be at least 1.");
		}
		if (count > theatre.availableSeats()) {
			throw new IllegalArgumentException("Only " + theatre.availableSeats() + " seats are available.");
		}
		List<String> bookedSeats = new ArrayList<>();
		for (int i = 1; i <= count; i++) {
			while (true) {
				System.out.print("Enter seat " + i + " (example B4): ");
				try {
					int[] seat = parseSeat(sc.nextLine(), theatre);
					theatre.bookSeat(seat[0], seat[1]);
					bookedSeats.add(Theatre.seatLabel(seat[0], seat[1]));
					break;
				} catch (IllegalArgumentException | IllegalStateException e) {
					System.out.println(e.getMessage() + " Try another seat.");
				}
			}
		}
		double ticketPrice = PriceCalculator.ticketPrice(movie.getBasePrice(), day);
		double subtotal = ticketPrice * count;
		double discount = 0;
		while (true) {
			System.out.print("Coupon code (SAVE10 / FLAT50, press Enter to skip): ");
			try {
				discount = PriceCalculator.discount(sc.nextLine(), subtotal);
				break;
			} catch (IllegalArgumentException e) {
				System.out.println(e.getMessage() + " Try again or press Enter to skip.");
			}
		}
		double total = subtotal - discount;
		System.out.println();
		System.out.println("============ TICKET ============");
		System.out.printf("%-16s: %s%n", "Movie", movie.getTitle());
		System.out.printf("%-16s: %s%s%n", "Day", DAYS[day - 1], PriceCalculator.isWeekend(day) ? " (weekend)" : "");
		System.out.printf("%-16s: %s%n", "Seats", String.join(", ", bookedSeats));
		System.out.printf("%-16s: Rs. %.2f%n", "Price per ticket", ticketPrice);
		System.out.printf("%-16s: Rs. %.2f%n", "Subtotal", subtotal);
		System.out.printf("%-16s: Rs. %.2f%n", "Discount", discount);
		System.out.println("--------------------------------");
		System.out.printf("%-16s: Rs. %.2f%n", "TOTAL", total);
		System.out.println("================================");
	}

	private static void cancelTicket() {
		Movie movie = chooseMovie();
		Theatre theatre = movie.getTheatre();

		System.out.print("Enter seat to cancel (example B4): ");
		int[] seat = parseSeat(sc.nextLine(), theatre);
		theatre.cancelSeat(seat[0], seat[1]);
		System.out.println("Seat " + Theatre.seatLabel(seat[0], seat[1]) + " cancelled for " + movie.getTitle() + ".");
	}

	private static void rateMovie() {
		Movie movie = chooseMovie();
		int stars = readInt("Your rating for " + movie.getTitle() + " (1 to 5): ");
		movie.rate(stars);
		System.out.printf("Thanks! New average rating: %.1f / 5%n", movie.getAverageRating());
	}

	private static Movie chooseMovie() {
		showMovies();
		while (true) {
			int pick = readInt("Choose movie number: ");
			if (pick >= 1 && pick <= movies.length) {
				return movies[pick - 1];
			}
			System.out.println("Please choose between 1 and " + movies.length + ".");
		}
	}

	private static int readDay() {
		System.out.println("Show day:");
		for (int i = 0; i < DAYS.length; i++) {
			System.out.printf("  %d. %s%n", i + 1, DAYS[i]);
		}
		while (true) {
			int day = readInt("Choose day (1-7): ");
			if (day >= 1 && day <= 7) {
				return day;
			}
			System.out.println("Please choose between 1 and 7.");
		}
	}

	private static int[] parseSeat(String text, Theatre theatre) {
		text = text.trim().toUpperCase();
		if (text.length() < 2) {
			throw new IllegalArgumentException("Enter the seat like A1 or C5.");
		}
		int row = text.charAt(0) - 'A';
		int col;
		try {
			col = Integer.parseInt(text.substring(1)) - 1;
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Enter the seat like A1 or C5.");
		}
		if (!theatre.isValidSeat(row, col)) {
			throw new IllegalArgumentException("Seat " + text + " does not exist.");
		}
		return new int[] { row, col };
	}

	private static int readInt(String message) {
		while (true) {
			System.out.print(message);
			try {
				return Integer.parseInt(sc.nextLine().trim());
			} catch (NumberFormatException e) {
				System.out.println("Please enter a whole number.");
			}
		}
	}

	private static void loadSampleData() {
		movies[0] = new Movie("Kantara", 200, 5, 8);
		movies[1] = new Movie("Jawan", 250, 5, 8);
		movies[2] = new Movie("Interstellar", 300, 5, 8);
		movies[0].getTheatre().bookSeat(0, 0);
		movies[0].getTheatre().bookSeat(0, 1);
		movies[0].getTheatre().bookSeat(2, 3);
		movies[1].getTheatre().bookSeat(1, 4);
		movies[1].getTheatre().bookSeat(1, 5);
		movies[2].getTheatre().bookSeat(4, 7);
		int[] kantaraRatings = { 5, 4, 5, 5 };
		int[] jawanRatings = { 4, 3, 4 };
		int[] interstellarRatings = { 5, 5, 4, 5, 4 };
		for (int r : kantaraRatings) {
			movies[0].rate(r);
		}
		for (int r : jawanRatings) {
			movies[1].rate(r);
		}
		for (int r : interstellarRatings) {
			movies[2].rate(r);
		}
	}
}
