package system.impl;

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
		return null;
	}
}
