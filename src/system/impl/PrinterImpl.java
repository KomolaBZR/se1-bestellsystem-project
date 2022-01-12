package system.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import datamodel.Order;
import datamodel.OrderItem;
import datamodel.Currency;
import datamodel.Customer;
import system.Calculator;
import system.Formatter;
import system.Printer;


/**
 * Local implementation of Printer interface.
 *
 * @author Komola Benzinger
 */

class PrinterImpl implements Printer{
	Calculator calc;
	Map<Customer, Integer> hashMap = new HashMap<>();
	Formatter formatter = createFormatter();
	OrderTableFormatterImpl otfmt = new OrderTableFormatterImpl ( formatter, new Object[][] {
		// five column table with column specs: width and alignment ('[' left, ']' right)
		{ 12, '[' }, { 20, '[' }, { 36, '[' }, { 10, ']' }, { 10, ']' }
	});
	
	
	PrinterImpl (Calculator calculator){// im Konstruktor injizierte Abhängigkeit
		this.calc=calculator;           // zur Calculator Komponente
	}

	
	/**
	 * Print list of orders using OrderTableFormatter.
	 * 
	 * @param orders list of orders to print
	 * @return String Buffer with representation of all orders 
	 */
	@Override
	public StringBuffer printOrders(Iterable<Order> orders) {
		List<Order> listOfOrders = new ArrayList<>();
		orders.forEach(listOfOrders::add);
		
		OrderTableFormatterImpl otfmt = new OrderTableFormatterImpl( formatter, new Object[][] {
			// five column table with column specs: width and alignment ('[' left, ']' right)
			{ 12, '[' }, { 20, '[' }, { 36, '[' }, { 10, ']' }, { 10, ']' }
		});
			otfmt.liner( "+-+-+-+-+-+" )		// print table header
			.hdr( "||", "Order-Id", "Customer", "Ordered Items", "Order", "MwSt." )
			.hdr( "||", "", "", "", "Value", "incl." )
			.liner( "+-+-+-+-+-+" )
			.liner( "||" );
		
		for(int i = 0; i < listOfOrders.size(); i++) {
			printOrder( listOfOrders.get(i) );	// print first order in table
		}

		long totalAllOrders = calc.calculateValue( orders );		// calculate value of all orders
		long totalVAT = calc.calculateIncludedVAT( orders );		// calculate compound VAT (MwSt.) for all orders

		otfmt			// finalize table with compound value and VAT (MwSt.) of all orders
			.lineTotal( totalAllOrders, totalVAT, Currency.EUR ); // output table
		return formatter.getBuffer();
	}
	
	/**
	 * Print one order
	 * 
	 * @param order to print
	 * @return String Buffer with representation of one order
	 */
	@Override
	public StringBuffer printOrder(Order order) {
		String orderId = order.getId();
		
		//here the number of orders from one customer will be counted 
		String customerOrder;
		if(!hashMap.containsKey(order.getCustomer())) {
			hashMap.put(order.getCustomer(), 1);
		}else {
			int numOfOrder = hashMap.get(order.getCustomer())+1;
			hashMap.replace(order.getCustomer(), numOfOrder);
		}
		
		if(hashMap.get(order.getCustomer()) == 1) {
			customerOrder = order.getCustomer().getFirstName() + "'s order";
		} else if(hashMap.get(order.getCustomer()) == 2) {
			customerOrder = order.getCustomer().getFirstName() + "'s "+hashMap.get(order.getCustomer())+"nd order";
		} else if(hashMap.get(order.getCustomer()) == 3) {
			customerOrder = order.getCustomer().getFirstName() + "'s "+hashMap.get(order.getCustomer())+"rd order";
		} else {
			customerOrder = order.getCustomer().getFirstName() + "'s "+hashMap.get(order.getCustomer())+"th order";
		}
		
		//here info about Article will be get and taxes&sums will be calculated 
		
		String infoItem = "";
		long sumTotal = 0;
		long taxTotal = 0;
		OrderItem[] items = order.getItemsAsArray();
		for(int i = 0; i < items.length; i++) {
			StringBuffer priceForItem = formatter.fmtPrice(items[i].getArticle().getUnitPrice(), items[i].getArticle()
					.getCurrency());
			sumTotal = items[i].getArticle().getUnitPrice() * items[i].getUnitsOrdered();
			double taxItem = calc.calculateIncludedVAT(items[i].getArticle().getUnitPrice(),items[i].getArticle()
					.getTax())/100.00;
			taxTotal = (long)(Math.round(taxItem * items[i].getUnitsOrdered()));
			if(items[i].getUnitsOrdered() == 1) {
				infoItem = items[i].getUnitsOrdered() + " " + items[i].getArticle().getDescription()
						+ " (" + items[i].getArticle().getId() + "), " + priceForItem;
			} else {
				infoItem = items[i].getUnitsOrdered() + " " + items[i].getArticle().getDescription()
						+ " (" + items[i].getArticle().getId() + "), " 
						+ items[i].getUnitsOrdered() + "x "+ priceForItem;
			}
			if(i == 0) {
				otfmt.line(orderId, customerOrder, infoItem, sumTotal, taxTotal);
			}
			if (i > 0){
				otfmt.line("", "", infoItem, sumTotal, taxTotal);
			}
		}
		if (order.itemsCount() > 1) {
			otfmt.liner("| | |-|-|-|").line( "", "", "total:", calc.calculateValue(order), calc.calculateIncludedVAT(order));
		}
		otfmt.liner( "| | | |=|=|" ).liner( "| | | | | |" );
		return formatter.getBuffer();
	}

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
	@Override
	public void printOrdersToFile( Iterable<Order> orders, String filepath ) throws IOException {
		File file = new File(filepath);
		file.getParentFile().mkdirs();
		FileWriter fileWriter = new FileWriter(file);
		fileWriter.write(printOrders(orders).toString());
		fileWriter.close();
		//throw new IOException( "not implemented." );
	}
	/**
	 * Create new format
	 * 
	 * @return interpreter for printf-style format strings
	 */
	@Override
	public Formatter createFormatter() {
		return new FormatterImpl();  //create new Formatter instance
	}

}
