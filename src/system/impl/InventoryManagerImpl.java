package system.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import datamodel.Article;
import datamodel.Order;
import system.DataRepository;
import system.InventoryManager;
import system.RTE;

class InventoryManagerImpl implements InventoryManager {
	/**
	 * private static singleton instance.
	 */
	private static InventoryManager iM_instance = null;
	
	private final DataRepository.ArticleRepository articleRepository;

	/**
	 * internal data structure to manage inventory (unitsInStore) by Article-id's.
	 */
	private final Map<String,Integer> inventory;

	/**
	 * Static access method to InventoryManagerImpl singleton instance (singleton pattern).
	 * 
	 * @return reference to InventoryManagerImpl singleton instance.
	 */

	public static InventoryManager getInstance(DataRepository.ArticleRepository articleRepository) {
		/*
		 * lazy instance creation (only when getInstance() is called)
		 */
		if( iM_instance == null ) {
			iM_instance = new InventoryManagerImpl(articleRepository);
		}
		return iM_instance;
	}


	/*
	 * Private constructor (singleton pattern).
	 */

	private InventoryManagerImpl(DataRepository.ArticleRepository articleRepository) { 
		this.articleRepository=articleRepository;
		this.inventory = new HashMap<>(); 
	}


	@Override
	public Optional<Article> findById(String id) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Iterable<Article> findAll() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public Article save(Article entity) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public int getUnitsInStock(String id) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void update(String id, int updatedUnitsInStock) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean isFillable(Order order) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean fill(Order order) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public StringBuffer printInventory() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public StringBuffer printInventory(int sortedBy, boolean decending, Integer... limit) {
		// TODO Auto-generated method stub
		return null;
	}
}
