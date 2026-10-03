package moviebooking;

public class Theatre {
	// true = booked, false = available
	private boolean[][] seats;

	public Theatre(int rows, int seatsPerRow) {
		seats = new boolean[rows][seatsPerRow];
	}

	public static String seatLabel(int row, int col) {
		return "" + (char) ('A' + row) + (col + 1);
	}

	public boolean isValidSeat(int row, int col) {
		return row >= 0 && row < seats.length && col >= 0 && col < seats[0].length;
	}

	public void bookSeat(int row, int col) {
		if (!isValidSeat(row, col)) {
			throw new IllegalArgumentException("That seat does not exist.");
		}
		if (seats[row][col]) {
			throw new IllegalStateException("Seat " + seatLabel(row, col) + " is already booked.");
		}
		seats[row][col] = true;
	}

	public void cancelSeat(int row, int col) {
		if (!isValidSeat(row, col)) {
			throw new IllegalArgumentException("That seat does not exist.");
		}
		if (!seats[row][col]) {
			throw new IllegalStateException("Seat " + seatLabel(row, col) + " is not booked, nothing to cancel.");
		}
		seats[row][col] = false;
	}

	public int availableSeats() {
		int count = 0;
		for (int r = 0; r < seats.length; r++) {
			for (int c = 0; c < seats[r].length; c++) {
				if (!seats[r][c]) {
					count++;
				}
			}
		}
		return count;
	}

	public void showSeats() {
		System.out.println();
		System.out.printf("%-4s %s%n", "", "-------- SCREEN --------");
		// seat numbers on top
		System.out.printf("%-4s", "");
		for (int c = 0; c < seats[0].length; c++) {
			System.out.printf("%-4d", c + 1);
		}
		System.out.println();
		// one line per row, row letter on the left
		for (int r = 0; r < seats.length; r++) {
			System.out.printf("%-4c", (char) ('A' + r));
			for (int c = 0; c < seats[r].length; c++) {
				System.out.printf("%-4s", seats[r][c] ? "[X]" : "[ ]");
			}
			System.out.println();
		}
		System.out.println();
		System.out.println("[ ] available    [X] booked");
		System.out.printf("Available seats: %d%n", availableSeats());
	}
}