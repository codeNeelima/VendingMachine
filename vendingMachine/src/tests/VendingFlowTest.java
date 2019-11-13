package tests;

import java.util.ArrayList;
import java.util.List;

import general.Coin;
import general.Item;
import handlers.VendingMachineHandler;
import logging.LogHandler;

public class VendingFlowTest {
	
	public static void main(String[] args) {
		
		VendingMachineHandler impl = new VendingMachineHandler();
		
		List<Item> myOrderList = new ArrayList<>();
		impl.showInventory();
		
		myOrderList.add(Item.COKE);
		myOrderList.add(Item.SODA);
		
		double billAmount = impl.placeOrder(myOrderList);
		
		List<Coin> coinsList = new ArrayList<>();
		coinsList.add(Coin.HALF_DOLLAR);
		coinsList.add(Coin.QUARTER);
		impl.collectPayment(coinsList,billAmount,myOrderList);
		
		LogHandler.log("Your Transaction is Complete");
		
		LogHandler.log("Cart Status Now");
		
		impl.printStats();
		
	}

}
