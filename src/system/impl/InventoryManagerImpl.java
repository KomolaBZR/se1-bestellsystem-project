package system.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import datamodel.Article;
import datamodel.Currency;
import datamodel.Order;
import system.DataRepository;
import system.Formatter;
import system.InventoryManager;
import system.RTE;
import system.Formatter.TableFormatter;

class InventoryManagerImpl implements InventoryManager {
	/**
	 * private static singleton instance.
	 */
	private static InventoryManager iM_instance = null;

	private final DataRepository.ArticleRepository articleRepository;

	/**
	 * internal data structure to manage inventory (unitsInStore) by Article-id's.
	 */
	private final Map<String, Integer> inventory;

	/**
	 * Static access method to InventoryManagerImpl singleton instance (singleton
	 * pattern).
	 * 
	 * @return reference to InventoryManagerImpl singleton instance.
	 */

	public static InventoryManager getInstance(DataRepository.ArticleRepository articleRepository) {
		/*
		 * lazy instance creation (only when getInstance() is called)
		 */
		if (iM_instance == null) {
			iM_instance = new InventoryManagerImpl(articleRepository);
		}
		return iM_instance;
	}

	/*
	 * Private constructor (singleton pattern).
	 */

	private InventoryManagerImpl(DataRepository.ArticleRepository articleRepository) {
		this.articleRepository = articleRepository;
		this.inventory = new HashMap<>();
	}

	@Override
	public Optional<Article> findById(String id) {
		// TODO Auto-generated method stub
		return this.articleRepository.findById(id);
	}

	@Override
	public Iterable<Article> findAll() {
		// TODO Auto-generated method stub
		return this.articleRepository.findAll();
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return this.articleRepository.count();
	}

	/**
	 * Create new article in InventoryManager (add to internal ArticleRepository).
	 * 
	 * @param article article to create.
	 * @throws IllegalArgumentException if article is null, has no valid id or
	 *                                  already exists.
	 */
	@Override
	public Article save(Article article) {
		if (article == null)
			throw new IllegalArgumentException("illegal article: null");
		//
		String id = article.getId();
		if (id == null)
			throw new IllegalArgumentException("illegal article.id: null");
		//
		articleRepository.save(article); // save, make sure to avoid duplicates
		//
		if (!inventory.containsKey(id)) {
			this.inventory.put(id, Integer.valueOf(0));
		}
		return article;
	}

	@Override
	public int getUnitsInStock(String id) {
		// TODO Auto-generated method stub
		return this.inventory.get(id);
	}

	@Override
	public void update(String id, int updatedUnitsInStock) {
		// TODO Auto-generated method stub
		this.inventory.put(id, updatedUnitsInStock);
	}

	@Override
	public boolean isFillable(Order order) {
		// TODO Auto-generated method stub
	    // array of boolean for understanding the availability of orders with a missing number of units of goods
	    ArrayList<Boolean> listBool = new ArrayList<>();  
	    // check all ordersItems of the order
	    for(int i = 0; i < order.getItemsAsArray().length; i++){
	      int orderItemNum = order.getItemsAsArray()[i].getUnitsOrdered();
	      String orderId = order.getItemsAsArray()[i].getArticle().getId();
	      // if there's enough number of the units
	      if(orderItemNum <= this.inventory.get(orderId)){
	        // then we add "true" in the list
	        listBool.add(true);
	      }else{
	        // else "false"
	        listBool.add(false);
	      }
	    }
	    // check if in the list there are "false"-values, if yes,
	    if(listBool.contains(false)){
	      // then we return "false" for whole orderItem. 
	      return false;
	      // else true. Order can be placed
	    } else{
	      return true;
	    }
	}

	@Override
	public boolean fill(Order order) {
		// TODO Auto-generated method stub
		// if all orderItems are available, 
	      if(isFillable(order)){
	      // then we have to subtract the available number of units from the quantity in stock 
	      for(int i = 0; i < order.getItemsAsArray().length; i++){
	        String articleId = order.getItemsAsArray()[i].getArticle().getId();
	        int units = order.getItemsAsArray()[i].getUnitsOrdered();
	        int availUnits = this.inventory.get(articleId);
	        update(articleId, availUnits - units);
	      }
	      // and return true for the whole order
	      return true;
	    } else{
	      return false;
	    }
	}

	/**
	 * Print inventory as table.
	 * 
	 * @return printed inventory (as table).
	 */
	@Override
	public StringBuffer printInventory() {
		return printInventory(StreamSupport.stream(articleRepository.findAll().spliterator(), false));
	}

	private StringBuffer printInventory(Stream<Article> articleStream) {
		//
		Formatter formatter = new FormatterImpl();
		TableFormatter tfmt = new TableFormatterImpl(formatter, new Object[][] {
				// five column table with column specs: width and alignment ('[' left, ']'
				// right)
				{ 12, '[' }, { 32, '[' }, { 12, ']' }, { 10, ']' }, { 14, ']' } }).liner("+-+-+-+-+-+") // print table
																										// header
						.hdr("||", "Inv.-Id", "Article / Unit", "Unit", "Units", "Value")
						.hdr("||", "", "", "Price", "in-Stock", "(in €)").liner("+-+-+-+-+-+");
		//
		long totalValue = articleStream.map(a -> {
			long unitsInStock = this.inventory.get(a.getId()).intValue();
			long value = a.getUnitPrice() * unitsInStock;
			tfmt.hdr("||", a.getId(), a.getDescription(),
					formatter.fmtPrice(a.getUnitPrice(), a.getCurrency()).toString(), Long.toString(unitsInStock),
					formatter.fmtPrice(value, a.getCurrency()).toString());
			return value;
		}).reduce(0L, (a, b) -> a + b);
		//
		String inventoryValue = formatter.fmtPrice(totalValue, Currency.EUR).toString();
		tfmt.liner("+-+-+-+-+-+").hdr("", "", "Invent", "ory Value:", inventoryValue);
		//
		return tfmt.getFormatter().getBuffer();
	}

	@Override
	public StringBuffer printInventory(int sortedBy, boolean decending, Integer... limit) {
		// TODO Auto-generated method stub
		Stream<Article> streamArticle = StreamSupport.stream(articleRepository.findAll().spliterator(), false);
		Stream<Article> sortedSA;
		
		if(sortedBy == 1){
			sortedSA = streamArticle.sorted(Comparator.comparing(p->p.getUnitPrice()));
		}else if(sortedBy == 2){
			sortedSA = streamArticle.sorted(Comparator.comparing(v->v.getUnitPrice()*getUnitsInStock(v.getId())));
		}else if(sortedBy == 3){
			sortedSA = streamArticle.sorted(Comparator.comparing(u->getUnitsInStock(u.getId())));
		}else if(sortedBy == 4){
			sortedSA = streamArticle.sorted(Comparator.comparing(d->d.getDescription()));
		}else if(sortedBy == 5){
			sortedSA = streamArticle.sorted(Comparator.comparing(i->i.getId()));
		}else {
			sortedSA = StreamSupport.stream(articleRepository.findAll().spliterator(), false);
		}
		Formatter formatter = new FormatterImpl();
		TableFormatter tfmt = new TableFormatterImpl(formatter, new Object[][] {
				// five column table with column specs: width and alignment ('[' left, ']'
				// right)
				{ 12, '[' }, { 32, '[' }, { 12, ']' }, { 10, ']' }, { 14, ']' } }).liner("+-+-+-+-+-+") // print table
																										// header
						.hdr("||", "Inv.-Id", "Article / Unit", "Unit", "Units", "Value")
						.hdr("||", "", "", "Price", "in-Stock", "(in €)").liner("+-+-+-+-+-+");
		//
		long totalValue = sortedSA.map(a -> {
			long unitsInStock = this.inventory.get(a.getId()).intValue();
			long value = a.getUnitPrice() * unitsInStock;
			tfmt.hdr("||", a.getId(), a.getDescription(),
					formatter.fmtPrice(a.getUnitPrice(), a.getCurrency()).toString(), Long.toString(unitsInStock),
					formatter.fmtPrice(value, a.getCurrency()).toString());
			return value;
		}).reduce(0L, (a, b) -> a + b);
		//
		String inventoryValue = formatter.fmtPrice(totalValue, Currency.EUR).toString();
		tfmt.liner("+-+-+-+-+-+").hdr("", "", "Invent", "ory Value:", inventoryValue);
		//
		return tfmt.getFormatter().getBuffer();
	}
}
