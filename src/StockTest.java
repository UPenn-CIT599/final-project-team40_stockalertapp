import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;

class StockTest {

	@Test
	void testStock() throws FileNotFoundException, InterruptedException {
		System.gc(); 
		Stock t = new Stock("TLT");
		assertEquals(t.getTicker()+".csv", t.getCsv());
		
	}



	@Test
	void testGetDatahistory() throws FileNotFoundException, InterruptedException {
		System.gc(); 
		Stock t = new Stock("XLK");
		TreeMap g= t.getDatahistory();
		
		assert (g) != null;
	}

}
