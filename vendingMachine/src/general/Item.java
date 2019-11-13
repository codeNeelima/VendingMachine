package general;

public enum Item {

	COKE("Coke", 25), PEPSI("Pepsi", 35), SODA("Soda", 45);
	private String name;
	private double price;

	private Item(String name, int price) {
		this.name = name;
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public double getPrice() {
		return price;
	}
}
