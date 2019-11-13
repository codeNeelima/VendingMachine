package tests;

import java.util.ArrayList;
import java.util.List;

import general.Coin;
import general.Item;
import handlers.VendingMachineImpl;

public class VendingFlowTest {
	
	public static void main(String[] args) {
		
		VendingMachineImpl impl = new VendingMachineImpl();
		
		List<Item> myOrderList = new ArrayList<>();
		impl.showInventory();
		
		myOrderList.add(Item.COKE);
		myOrderList.add(Item.SODA);
		
		double billAmount = impl.placeOrder(myOrderList);
		
		List<Coin> coinsList = new ArrayList<>();
		
		impl.insertCoin(coinsList,billAmount,myOrderList);
		
		impl.printStats();
	}

}
