package inventory;

public class Product {
	private int productId;
	private String productName;
	private Category category;
	private double price;
	private int stock;

	public Product(int productId, String productName, Category category, double price, int stock) {
		if (price <= 0 || stock < 0) {
			throw new IllegalArgumentException("Price must be positive and stock cannot be negative.");
		}
		this.productId = productId;
		this.productName = productName;
		this.category = category;
		this.price = price;
		this.stock = stock;
	}

	public int getProductId() {
		return productId;
	}

	public String getProductName() {
		return productName;
	}

	public Category getCategory() {
		return category;
	}

	public double getPrice() {
		return price;
	}

	public int getStock() {
		return stock;
	}

	public void setPrice(double price) {
		if (price <= 0) {
			throw new IllegalArgumentException("Price must be greater than zero.");
		}
		this.price = price;
	}

	public void addStock(int quantity) {
		if (quantity <= 0) {
			throw new IllegalArgumentException("Quantity must be greater than zero.");
		}
		stock += quantity;
	}

	public void sellProduct(int quantity) {
		if (quantity <= 0) {
			throw new IllegalArgumentException("Quantity must be greater than zero.");
		}
		if (stock == 0) {
			throw new IllegalStateException(productName + " is out of stock.");
		}
		if (quantity > stock) {
			throw new IllegalStateException("Only " + stock + " unit(s) of " + productName + " left.");
		}
		stock -= quantity;
	}

	public static void printHeader() {
		System.out.printf("%-6s %-22s %-16s %12s %7s%n", "ID", "Name", "Category", "Price (Rs.)", "Stock");
		System.out.println("-".repeat(68));
	}

	public void printRow() {
		System.out.printf("%-6d %-22s %-16s %12.2f %7d%n", productId, productName, category, price, stock);
	}
}
