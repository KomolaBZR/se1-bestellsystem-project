package system;

import datamodel.Order;
import datamodel.TAX;

/**
 * Public interface of calculation of values with/or without VAT
 * 
 * @since "0.1.0"
 * @version {@value package_info#Version}
 * @author Komola Benzinger
 *
 */

public interface Calculator {
	
	/**
	 * Calculates the compound value of all orders
	 * 
	 * @param orders list of orders
	 * @return compound value of all orders
	 */
	public long calculateValue( Iterable<Order> orders );
	
	/**
	 * Calculates the value of one order
	 * 
	 * @param order order for which the value is calculated
	 * @return value of the order
	 */
	public long calculateValue( Order order );
	
	/**
	 * Calculates the compound VAT of all orders
	 * 
	 * @param orders list of orders
	 * @return compound VAT of all orders
	 */
	public long calculateIncludedVAT( Iterable<Order> orders );
	
	/**
	 * Calculates the VAT of one order. VAT is based on the rate that
	 * applies to the articles of each line item.
	 * 
	 * @param order order for which the included VAT is calculated
	 * @return compound VAT of all ordered items 
	 */
	public long calculateIncludedVAT( Order order ); 
	
	/**
	 * public helper to calculate included VAT for a price based on
	 * a TAX rate (as defined in Enum TAX).
	 * 
	 * @param order order for which the value's VAT is calculated
	 * @return VAT
	 */
	public long calculateIncludedVAT( long price, TAX taxRate ); 

}
