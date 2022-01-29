package system;

import datamodel.Order;
import system.RTE.Runtime;

public interface OrderBuilder {
	/**
	* Validate and save order to OrderRepository, if order is accepted.
	*
	* @param order order to accept.
	* @return true if order was validated and saved to OrderRepository.
	*/
	public boolean accept( Order order );
	/**
	* Build orders in OrderRepository.
	*
	* @return chainable selfâ€�reference.
	*/
	public OrderBuilder build();
}
