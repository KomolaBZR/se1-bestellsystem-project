package system;

import java.io.IOException;
import java.util.List;

import datamodel.Order;

/**
 * Public interface of print Orders with specified format 
 * 
 * @since "0.1.0"
 * @version {@value package_info#Version}
 * @author Komola Benzinger
 *
 */

public interface Printer {
	/**
	 * Print list of orders using OrderTableFormatter.
	 * 
	 * @param orders list of orders to print
	 * @return String Buffer with representation of all orders 
	 */
	public StringBuffer printOrders( Iterable<Order> orders ); 
	
	
	/**
	 * Print one order
	 * 
	 * @param order to print
	 * @return String Buffer with representation of one order
	 */
	public StringBuffer printOrder( Order order ); 
	
	/**
	 * Print orders as table to a file.
	 * 
	 * Conditions:
	 *  - creates new file or overwrites an existing file.
	 *  - not existing parts of the path are creates, throws IOException
	 *    if this is not possible.
	 *    
	 *    @param orders list of orders to print.
	 *    @param filepath path and name of the output file.
	 *    @throws IOException for errors.
	 */
	void printOrdersToFile( Iterable<Order> orders, String filepath ) throws IOException;
	
	
	/**
	 * Create new format
	 * 
	 * @return interpreter for printf-style format strings
	 */
	public Formatter createFormatter(); 
	
	
}
