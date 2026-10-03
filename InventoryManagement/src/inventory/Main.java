package inventory;

import java.util.Scanner;

public class Main {
	private static Scanner sc = new Scanner(System.in);
	private static InventoryService service = new InventoryService();

	public static void main(String[] args) {
		loadSampleData();
		int choice;
		do {
			printMenu();
			choice = readInt("Enter choice: ");
			System.out.println();
			try {
				switch (choice) {
				case 1 -> addProduct();
				case 2 -> searchProduct();
				case 3 -> updatePrice();
				case 4 -> addStock();
				case 5 -> sellProducts();
				case 6 -> service.showAllProducts();
				case 7 -> service.showLowStock();
				case 8 -> deleteProduct();
				case 0 -> System.out.println("Goodbye!");
				default -> System.out.println("Invalid choice, try again.");
				}
			} catch (IllegalArgumentException | IllegalStateException e) {
				System.out.println("Error: " + e.getMessage());
			}
		} while (choice != 0);
	}

	private static void printMenu() {
		System.out.println();
		System.out.println("===== INVENTORY MENU =====");
		System.out.printf("%d. %s%n", 1, "Add product");
		System.out.printf("%d. %s%n", 2, "Search product by ID");
		System.out.printf("%d. %s%n", 3, "Update product price");
		System.out.printf("%d. %s%n", 4, "Add stock");
		System.out.printf("%d. %s%n", 5, "Sell products (generate bill)");
		System.out.printf("%d. %s%n", 6, "Show all products");
		System.out.printf("%d. %s%n", 7, "Show low stock products");
		System.out.printf("%d. %s%n", 8, "Delete a product");
		System.out.printf("%d. %s%n", 0, "Exit");
	}

	private static void addProduct() {
		int id = readInt("Product ID: ");
		System.out.print("Product name: ");
		String name = sc.nextLine().trim();
		System.out.println("Categories:");
		Category[] categories = Category.values();
		for (int i = 0; i < categories.length; i++) {
			System.out.printf("  %d. %s%n", i + 1, categories[i]);
		}
		int pick = readInt("Choose category number: ");
		if (pick < 1 || pick > categories.length) {
			throw new IllegalArgumentException("Invalid category number.");
		}
		double price = readDouble("Price: ");
		int stock = readInt("Opening stock: ");
		service.addProduct(new Product(id, name, categories[pick - 1], price, stock));
		System.out.println("Product added.");
	}

	private static void searchProduct() {
		int id = readInt("Product ID to search: ");
		Product.printHeader();
		service.searchById(id).printRow();
	}

	private static void updatePrice() {
		int id = readInt("Product ID: ");
		double price = readDouble("New price: ");
		service.updatePrice(id, price);
		System.out.println("Price updated.");
	}

	private static void addStock() {
		int id = readInt("Product ID: ");
		int qty = readInt("Quantity to add: ");
		service.addStock(id, qty);
		System.out.println("Stock updated.");
	}

	private static void sellProducts() {
		Bill bill = new Bill();
		System.out.println("Enter product IDs to sell. Enter 0 to finish the bill.");
		while (true) {
			int id = readInt("Product ID (0 to finish): ");
			if (id == 0) {
				break;
			}
			int qty = readInt("Quantity: ");
			try {
				service.sellProduct(id, qty, bill);
				System.out.println("Added to bill.");
			} catch (IllegalArgumentException | IllegalStateException e) {
				System.out.println("Could not sell: " + e.getMessage());
			}
		}
		if (bill.isEmpty()) {
			System.out.println("No items sold.");
		} else {
			bill.print();
		}
	}

	private static void deleteProduct() {
		int id = readInt("Product ID to delete: ");
		service.deleteProduct(id);
		System.out.println("Product deleted.");
	}

	// keeps asking until the user types a valid number, so the program never
	// crashes on bad input
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

	private static double readDouble(String message) {
		while (true) {
			System.out.print(message);
			try {
				return Double.parseDouble(sc.nextLine().trim());
			} catch (NumberFormatException e) {
				System.out.println("Please enter a valid number.");
			}
		}
	}

	private static void loadSampleData() {
		service.addProduct(new Product(101, "Dell Inspiron 15", Category.LAPTOP, 55000, 8));
		service.addProduct(new Product(102, "HP Pavilion 14", Category.LAPTOP, 62000, 4));
		service.addProduct(new Product(103, "Lenovo IdeaPad", Category.LAPTOP, 48000, 6));
		service.addProduct(new Product(104, "Redmi Note 13", Category.MOBILE, 16000, 3));
		service.addProduct(new Product(105, "Samsung Galaxy M35", Category.MOBILE, 19500, 10));
		service.addProduct(new Product(106, "Realme Narzo 70", Category.MOBILE, 14000, 7));
		service.addProduct(new Product(107, "Logitech Keyboard", Category.ACCESSORY, 1200, 15));
		service.addProduct(new Product(108, "HP Wireless Mouse", Category.ACCESSORY, 650, 25));
		service.addProduct(new Product(109, "boAt Earphones", Category.ACCESSORY, 999, 2));
		service.addProduct(new Product(110, "Sandisk 64GB Pendrive", Category.ACCESSORY, 550, 30));
		service.addProduct(new Product(111, "Philips Mixer", Category.HOME_APPLIANCE, 3500, 0));
		service.addProduct(new Product(112, "Prestige Induction", Category.HOME_APPLIANCE, 2800, 5));
		service.addProduct(new Product(113, "Havells Table Fan", Category.HOME_APPLIANCE, 2200, 9));
		service.addProduct(new Product(114, "Bajaj Iron Box", Category.HOME_APPLIANCE, 1100, 1));
		service.addProduct(new Product(115, "Zebronics Speaker", Category.ACCESSORY, 1800, 12));
	}
}