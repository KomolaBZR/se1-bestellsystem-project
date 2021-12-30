package system.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import datamodel.Article;
import datamodel.Customer;
import datamodel.Order;
import system.DataRepository;

public class DataRepositoryImpl {
	private static CustomerRepository cusRep;
	private static ArticleRepository artRep;
	private static OrderRepository ordRep;
	
	CustomerRepository getCustomerRepository() {
		if(cusRep == null) {
			cusRep = new CustomerRepository();
		}
		return cusRep;
	}
	ArticleRepository getArticleRepository() {
		if(artRep == null) {
			artRep = new ArticleRepository();
		}
		return artRep;
	}
	OrderRepository getOrderRepository(){
		if(ordRep == null) {
			ordRep = new OrderRepository();
		}
		return ordRep;
	}
	
	
	static class CustomerRepository implements DataRepository.CustomerRepository{
		Map<Long,Customer> entities = new HashMap<>();
		@Override
		public Optional<Customer> findById(long id) {
			return Optional.of(entities.get(id));
		}
		@Override
		public Iterable<Customer> findAll() {
			return entities.values();
		}
		@Override
		public long count() {
			return entities.size();
		}
		@Override
		public Customer save(Customer entity) {
			entities.put(Long.valueOf(entity.getId()), entity);
			return entity;
		}
	}
	
	static class ArticleRepository implements DataRepository.ArticleRepository{
		Map<String,Article> entities = new HashMap<>();

		@Override
		public Optional<Article> findById(String id) {
			return Optional.of(entities.get(id));
		}
		@Override
		public Iterable<Article> findAll() {
			return entities.values();
		}
		@Override
		public long count() {
			return entities.size();
		}
		@Override
		public Article save(Article entity) {
			entities.put(String.valueOf(entity.getId()), entity);
			return entity;
		}
	}
	
	static class OrderRepository implements DataRepository.OrderRepository{
		Map<String,Order> entities = new HashMap<>();

		@Override
		public Optional<Order> findById(String id) {
			return Optional.of(entities.get(id));
		}
		@Override
		public Iterable<Order> findAll() {
			return entities.values();
		}
		@Override
		public long count() {
			return entities.size();
		}
		@Override
		public Order save(Order entity) {
			entities.put(String.valueOf(entity.getId()), entity);
			return entity;
		}
	}
}
