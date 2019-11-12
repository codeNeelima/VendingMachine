package handlers;

import interfaces.VendingMachine;

public class VendingMachineFactory {

	public static VendingMachine createVendingMachine() {
		return new VendingMachineImpl();
	}

}
