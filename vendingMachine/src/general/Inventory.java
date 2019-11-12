package general;

import java.util.HashMap;
import java.util.Map;

public class Inventory<T> {
	private Map<T, Integer> inventory = new HashMap<T, Integer>();

	public int getQuantity(T item) {
		Integer value = inventory.get(item);
		return value == null ? 0 : value;
	}

	public void add(T item) {
		int count = inventory.get(item);
		inventory.put(item, count + 1);
	}

	public void deduct(T item) {
		if (hasItem(item)) {
			int count = inventory.get(item);
			inventory.put(item, count - 1);
		}
	}

	public boolean hasItem(T item) {
		return getQuantity(item) > 0;
	}

	public void clear() {
		inventory.clear();
	}

	public void put(T item, int quantity) {
		inventory.put(item, quantity);
	}
	
	public void listInventoryItems(){
		System.out.println("Available List of Items");
		for(Map.Entry<T, Integer> item : inventory.entrySet()){
			System.out.println("======================================");
			System.out.println(item.getKey()+" == "+item.getValue());
		}
	}

	
}
