package inventory;

import java.util.ArrayList;
import java.util.List;

public class Bill {
	// price is copied into the bill at the time of sale,
	// so changing the product price later does not change an old bill
	private static class Item {
		String name;
		int quantity;
		double price;

		Item(String name, int quantity, double price) {
			this.name = name;
			this.quantity = quantity;
			this.price = price;
		}

		double lineTotal() {
			return quantity * price;
		}
	}

	private List<Item> items = new ArrayList<>();

	public void addItem(String name, int quantity, double price) {
		items.add(new Item(name, quantity, price));
	}

	public boolean isEmpty() {
		return items.isEmpty();
	}

	public double getTotal() {
		double total = 0;
		for (Item item : items) {
			total += item.lineTotal();
		}
		return total;
	}

	public void print() {
		System.out.println();
		System.out.println("============== BILL ==============");
		System.out.printf("%-18s %4s %10s %10s%n", "Item", "Qty", "Price", "Total");
		System.out.println("-".repeat(36));
		for (Item item : items) {
			System.out.printf("%-18s %4d %10.2f %10.2f%n", item.name, item.quantity, item.price, item.lineTotal());
		}
		System.out.println("-".repeat(36));
		System.out.printf("%-18s %26.2f%n", "GRAND TOTAL (Rs.)", getTotal());
		System.out.println("==================================");
	}
}