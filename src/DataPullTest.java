import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

/**
 * Junit test for DataPull Class
 * 
 * @author Joseph Kravets
 *
 */
class DataPullTest {

    @Test
    void testGetcsv() throws InterruptedException, FileNotFoundException {

	DataPull.getCsv("SPY");
	File data = new File("SPY.csv");
	Scanner fileReader = new Scanner(data);
	assert (fileReader.hasNextLine());
    }

    @Test
    void testGetcurrentquote() throws InterruptedException {

	Double price = DataPull.getCurrentQuote("SPY");

	assert (price) != null;
    }

}
