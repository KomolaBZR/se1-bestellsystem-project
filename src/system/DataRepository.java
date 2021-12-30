package system;

import java.util.Optional;

import datamodel.Article;
import datamodel.Customer;
import datamodel.Order;



/**
 * Public interface of data repositories that store objects (entities)
 * of data model classes.
 * 
 * @since "0.1.0"
 * @version {@value package_info#Version}
 * @author Komola Benzinger
 *
 */

public interface DataRepository {

	/**
	 * Public interface of the Customer repository.
	 *
	 */
	interface CustomerRepository {
		Optional<Customer> findById( long id );
		Iterable<Customer> findAll();
		long count();
		Customer save( Customer entity );
	}

	/**
	 * Public interface of the Article repository.
	 *
	 */

	interface ArticleRepository {
		Optional<Article> findById( String id );
		Iterable<Article> findAll();
		long count();
		Article save( Article entity );
	}

	/**
	 * Public interface of the Order repository.
	 *
	 */
	interface OrderRepository {
		Optional<Order> findById( String id );
		Iterable<Order> findAll();
		long count();
		Order save( Order entity );
	}

}
