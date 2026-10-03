package inventory;

import java.util.LinkedHashMap;
import java.util.Map;

public class InventoryService {
	private static final int LOW_STOCK_LIMIT = 5;
	private Map<Integer, Product> products = new LinkedHashMap<>();

	public void addProduct(Product product) {
		if (products.containsKey(product.getProductId())) {
			throw new IllegalArgumentException("A product with ID " + product.getProductId() + " already exists.");
		}
		products.put(product.getProductId(), product);
	}

	public Product searchById(int productId) {
		Product product = products.get(productId);
		if (product == null) {
			throw new IllegalArgumentException("No product found with ID " + productId + ".");
		}
		return product;
	}

	public void updatePrice(int productId, double newPrice) {
		searchById(productId).setPrice(newPrice);
	}

	public void addStock(int productId, int quantity) {
		searchById(productId).addStock(quantity);
	}
	public void sellProduct(int productId, int quantity, Bill bill) {
		Product product = searchById(productId);
		product.sellProduct(quantity);
		bill.addItem(product.getProductName(), quantity, product.getPrice());
	}

	public void deleteProduct(int productId) {
		searchById(productId);
		products.remove(productId);
	}

	public void showAllProducts() {
		if (products.isEmpty()) {
			System.out.println("Inventory is empty.");
			return;
		}
		Product.printHeader();
		for (Product product : products.values()) {
			product.printRow();
		}
	}

	public void showLowStock() {
		boolean found = false;
		for (Product product : products.values()) {
			if (product.getStock() < LOW_STOCK_LIMIT) {
				if (!found) {
					Product.printHeader();
					found = true;
				}
				product.printRow();
			}
		}
		if (!found) {
			System.out.println("No products below " + LOW_STOCK_LIMIT + " in stock.");
		}
	}
}
