import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;

/**
 * Junit test for Stock Class
 * 
 * @author Joseph Kravets
 *
 */
class StockTest {

    @Test
    void testStock() throws FileNotFoundException, InterruptedException {
	System.gc();
	Stock tlt = new Stock("TLT");
	assertEquals(tlt.getTicker() + ".csv", tlt.getCsv());

    }

    @Test
    void testGetDatahistory() throws FileNotFoundException, InterruptedException {
	System.gc();
	Stock xlk = new Stock("XLK");
	System.gc();
	TreeMap data = xlk.getdataHistory();

	assert (data) != null;
    }

}
