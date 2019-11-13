package interfaces; 

import java.util.List;
import general.Bucket;
import general.Coin;
import general.Item; 

public interface VendingMachine { 
	public double selectItemAndGetPrice(Item item);
	public void insertCoin(Coin coin); 
	public List<Coin> refund(); 
	public Bucket<Item, List<Coin>> collectItemAndChange(); 
	public void reset();
	public double placeOrder(List<Item> item);
	public void showInventory();
	}

