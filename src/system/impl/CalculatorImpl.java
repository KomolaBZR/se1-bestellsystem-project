package system.impl;

import java.util.ArrayList;

import datamodel.Order;
import datamodel.OrderItem;
import datamodel.TAX;
import system.Calculator;

/**
 * Local implementation of Calculator interface.
 *
 * @author Komola Benzinger
 */


class CalculatorImpl implements Calculator{
	
	/**
	 * Calculates the compound value of all orders
	 * 
	 * @param orders list of orders
	 * @return compound value of all orders
	 */
	@Override
	public long calculateValue(Iterable<Order> orders) {
		ArrayList<Order> ordersNew = new ArrayList<>();
		orders.forEach(ordersNew::add);
		long value = 0;
		for(int i = 0; i < ordersNew.size(); i++) {
			value += calculateValue(ordersNew.get(i));
		}
		return value;
	}

	/**
	 * Calculates the value of one order
	 * 
	 * @param order order for which the value is calculated
	 * @return value of the order
	 */
	@Override
	public long calculateValue(Order order) {
		long value = 0;
		ArrayList<OrderItem> items = new ArrayList<>();
		order.getItems().forEach(items::add);
		for(int i = 0; i < items.size(); i++) {
			value += items.get(i).getUnitsOrdered() * items.get(i).getArticle().getUnitPrice();
		}
		return value;
	}


	/**
	 * Calculates the compound VAT of all orders
	 * 
	 * @param orders list of orders
	 * @return compound VAT of all orders
	 */
	@Override
	public long calculateIncludedVAT(Iterable<Order> orders) {
		ArrayList<Order> ordersNew = new ArrayList<>();
		orders.forEach(ordersNew::add);
		long vat = 0;
		for(int i = 0; i < ordersNew.size(); i++) {
			vat += calculateIncludedVAT(ordersNew.get(i));
		}
		return vat;
	}

	/**
	 * Calculates the VAT of one order. VAT is based on the rate that
	 * applies to the articles of each line item.
	 * 
	 * @param order order for which the included VAT is calculated
	 * @return compound VAT of all ordered items 
	 */
	@Override
	public long calculateIncludedVAT(Order order) {
		long vat = 0;
		ArrayList<OrderItem> items = new ArrayList<>();
		order.getItems().forEach(items::add);
		for(int i = 0; i < items.size(); i++) {
			double calculPrTax = calculateIncludedVAT(items.get(i).getArticle().getUnitPrice(), 
					items.get(i).getArticle().getTax())/100.00;
			vat += (long)(Math.round(calculPrTax * items.get(i).getUnitsOrdered()));
		}
		return vat;
	}
	
	/**
	 * public helper to calculate included VAT for a price based on
	 * a TAX rate (as defined in Enum TAX).
	 * 
	 * @param order order for which the value's VAT is calculated
	 * @return VAT
	 */
	@Override
	public long calculateIncludedVAT(long price, TAX taxRate) {
		long vat = (long) ((price -(price / ((taxRate.rate()/100.00) + 1.0)))*100.00);
		return vat;
	}
}
