import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

/**
 * Junit test for DataPull Class
 * 
 * @author Joseph Kravets, Tiffany Choi
 *
 */
class DataPullTest {

	@Test
	void testGetCSV() throws InterruptedException, FileNotFoundException {

		DataPull.getCsv("SPY");
		File data = new File("SPY.csv");
		Scanner fileReader = new Scanner(data);
		assert (fileReader.hasNextLine());
	}

	@Test
	void testGetCurrentQuote() throws InterruptedException {

		Double price = DataPull.getCurrentQuote("SPY");
		assert (price) != null;

	}

	@Test
	void testGetSMA() throws InterruptedException {

		Double sma = DataPull.getIndicator("SMA", "SPY");
		assert (sma) != null;
		
	}
	
	@Test
	void testGetEMA() throws InterruptedException {

		Double ema = DataPull.getIndicator("EMA", "SPY");
		assert (ema) != null;
		
	}
	
	@Test
	void testGetRSI() throws InterruptedException {

		Double rsi = DataPull.getIndicator("RSI", "SPY");
		assert (rsi) != null;
		
	}
	
	@Test
	void testGetMACD() throws InterruptedException {

		Double macd = DataPull.getIndicator("MACD", "SPY");
		assert (macd) != null;
		
	}
	
	@Test
	void testGetOBV() throws InterruptedException {

		Double obv = DataPull.getIndicator("OBV", "SPY");
		assert (obv) != null;
		
	}
	
	@Test
	void testWrongIndicator() throws InterruptedException {

		Double wrongindicator = DataPull.getIndicator("XXX", "SPY");
		assert (wrongindicator) == 0.0;
		
	}
	
}
