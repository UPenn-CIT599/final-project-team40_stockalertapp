import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

class DataPullTest {

	

	@Test
	void testGetcsv() throws InterruptedException, FileNotFoundException {
		DataPull t1= new DataPull();
		t1.getcsv("SPY");
		File data= new File("SPY.csv");
		Scanner fileReader = new Scanner(data);
		assert (fileReader.hasNextLine());
	}

	@Test
	void testGetcurrentquote() throws InterruptedException {
		DataPull t1= new DataPull();
		Double g= t1.getcurrentquote("SPY");
		
		assert (g) != null;
	}

}
