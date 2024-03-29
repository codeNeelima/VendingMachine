package handlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import exceptions.NotFullPaidException;
import exceptions.NotSufficientChangeException;
import exceptions.SoldOutException;
import general.Bucket;
import general.Coin;
import general.Inventory;
import general.Item;
import interfaces.VendingMachine;
import logging.LogHandler;

public class VendingMachineHandler implements VendingMachine {

	private Inventory<Coin> cashInventory = new Inventory<Coin>();
	private Inventory<Item> itemInventory = new Inventory<Item>();
	private double totalSales;
	private Item currentItem;
	private double currentBalance;

	public VendingMachineHandler() {
		initialize();
	}

	private void initialize() {
		// initialize machine with 5 coins of each denomination
		// and 5 cans of each Item
		for (Coin c : Coin.values()) {
			cashInventory.put(c, 5);
		}

		for (Item i : Item.values()) {
			itemInventory.put(i, 5);
		}

	}

	@Override
	public double selectItemAndGetPrice(Item item) {
		if (itemInventory.hasItem(item)) {
			currentItem = item;
			return currentItem.getPrice();
		}
		throw new SoldOutException("Sold Out, Please buy another item");
	}

	@Override
	public void insertCoin(Coin coin) {
		currentBalance = currentBalance + coin.getDenomination();
		cashInventory.add(coin);
	}

	@Override
	public Bucket<Item, List<Coin>> collectItemAndChange() {
		Item item = collectItem();
		totalSales = totalSales + currentItem.getPrice();
		List<Coin> change = collectChange();
		return new Bucket<Item, List<Coin>>(item, change);
	}

	/**
	 * 
	 * @return
	 * @throws NotSufficientChangeException
	 * @throws NotFullPaidException
	 */
	private Item collectItem() throws NotSufficientChangeException, NotFullPaidException {
		if (isFullPaid()) {
			if (hasSufficientChange()) {
				itemInventory.deduct(currentItem);
				return currentItem;
			}
			throw new NotSufficientChangeException("Not Sufficient change in Inventory");

		}
		double remainingBalance = currentItem.getPrice() - currentBalance;
		throw new NotFullPaidException("Price not full paid, remaining : ", remainingBalance);
	}

	/**
	 * 
	 * @return
	 */
	private List<Coin> collectChange() {
		double changeAmount = currentBalance - currentItem.getPrice();
		List<Coin> change = getChange(changeAmount);
		updateCashInventory(change);
		currentBalance = 0;
		currentItem = null;
		return change;
	}

	/**
	 * 
	 */
	public List<Coin> collectPayment(double amount) {
		double changeAmount = currentBalance - amount;
		List<Coin> change = getChange(changeAmount);
		updateCashInventory(change);
		currentBalance = 0;
		currentItem = null;
		return change;
	}

	@Override
	public List<Coin> refund() {
		List<Coin> refund = getChange(currentBalance);
		updateCashInventory(refund);
		currentBalance = 0;
		currentItem = null;
		return refund;
	}

	/**
	 * 
	 * @return
	 */
	private boolean isFullPaid() {
		if (currentBalance >= currentItem.getPrice()) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param amount
	 * @return
	 * @throws NotSufficientChangeException
	 */
	private List<Coin> getChange(double amount) throws NotSufficientChangeException {
		List<Coin> changes = Collections.EMPTY_LIST;

		if (amount > 0) {
			changes = new ArrayList<Coin>();
			double balance = amount;
			while (balance > 0) {
				if (balance >= Coin.GOLDEN_DOLLAR.getDenomination() && cashInventory.hasItem(Coin.GOLDEN_DOLLAR)) {
					changes.add(Coin.GOLDEN_DOLLAR);
					balance = balance - Coin.GOLDEN_DOLLAR.getDenomination();
					continue;

				} else if (balance >= Coin.HALF_DOLLAR.getDenomination() && cashInventory.hasItem(Coin.HALF_DOLLAR)) {
					changes.add(Coin.HALF_DOLLAR);
					balance = balance - Coin.HALF_DOLLAR.getDenomination();
					continue;

				} else if (balance >= Coin.QUARTER.getDenomination() && cashInventory.hasItem(Coin.QUARTER)) {
					changes.add(Coin.QUARTER);
					balance = balance - Coin.QUARTER.getDenomination();
					continue;

				} else if (balance >= Coin.DIME.getDenomination() && cashInventory.hasItem(Coin.DIME)) {
					changes.add(Coin.DIME);
					balance = balance - Coin.DIME.getDenomination();
					continue;

				} else if (balance >= Coin.NICKLE.getDenomination() && cashInventory.hasItem(Coin.NICKLE)) {
					changes.add(Coin.NICKLE);
					balance = balance - Coin.NICKLE.getDenomination();
					continue;

				} else if (balance >= Coin.PENNY.getDenomination() && cashInventory.hasItem(Coin.PENNY)) {
					changes.add(Coin.PENNY);
					balance = balance - Coin.PENNY.getDenomination();
					continue;

				} else {
					throw new NotSufficientChangeException("NotSufficientChange,Please try another product");
				}
			}
		}

		return changes;
	}

	@Override
	public void reset() {
		cashInventory.clear();
		itemInventory.clear();
		totalSales = 0;
		currentItem = null;
		currentBalance = 0;
	}

	public void printStats() {
		LogHandler.log("Total Sales : " + totalSales);
		LogHandler.log("Total Balance : " + currentBalance);
		LogHandler.log("Current Item Inventory : ");
		itemInventory.listInventoryItems();
		LogHandler.log("Current Cash Inventory : ");
		cashInventory.listInventoryItems();
	}

	/**
	 * 
	 * @return
	 */
	private boolean hasSufficientChange() {
		return hasSufficientChangeForAmount(currentBalance - currentItem.getPrice());
	}

	/**
	 * 
	 * @param amount
	 * @return
	 */
	private boolean hasSufficientChangeForAmount(double amount) {
		boolean hasChange = true;
		try {
			getChange(amount);
		} catch (NotSufficientChangeException nsce) {
			return hasChange = false;
		}

		return hasChange;
	}

	private void updateCashInventory(List<Coin> change) {
		for (Coin c : change) {
			cashInventory.deduct(c);
		}
	}

	private void addCashInventory(List<Coin> change) {
		for (Coin c : change) {
			cashInventory.add(c);
		}
	}

	private void removeItemInventory(List<Item> items) {
		for (Item c : items) {
			itemInventory.deduct(c);
		}
	}

	public double getTotalSales() {
		return totalSales;
	}

	public double placeOrder(List<Item> item) {
		double amount = 0.0;
		for (Item i : item) {
			try {
				amount += selectItemAndGetPrice(i);

			} catch (RuntimeException e) {

				LogHandler.log("Please consider reordering " + i + " item is not available");
			}
		}

		return amount;

	}

	public void showInventory() {
		itemInventory.listInventoryItems();

	}

	public void acceptPayment(double placeOrder) {
		this.getChange(placeOrder);

	}

	/**
	 * 
	 * @param coinsList
	 * @param billAmount
	 * @param myOrderList
	 * @return
	 */
	public List<Coin> collectPayment(List<Coin> coinsList, double billAmount, List<Item> myOrderList)
			throws NotSufficientChangeException {
		double paidAmount = 0;
		boolean transaction = false;
		for (Coin coin : coinsList) {
			paidAmount += coin.getDenomination();
		}
		if (paidAmount == billAmount) {
			LogHandler.log("Thank you for the Transaction");
			addCashInventory(coinsList);
			transaction = true;
			totalSales = paidAmount;

		} else if (paidAmount > billAmount) {
			LogHandler.log("Please wait while change is processing ...");
			addCashInventory(coinsList);
			transaction = true;
			totalSales = paidAmount;

		} else if (paidAmount < billAmount) {
			throw new NotFullPaidException("Please complete the payment : ", billAmount - paidAmount);
		}

		if (transaction) {
			removeItemInventory(myOrderList);
		}
		List<Coin> refundCoins = getChange(paidAmount - billAmount);
		for (Coin coin : refundCoins) {
			cashInventory.deduct(coin);
		}
		return refundCoins;
	}

}
