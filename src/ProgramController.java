import java.awt.GraphicsEnvironment;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

/***
 * PROGRAMCONTROLLER CLASS: 
 * 
 * This class carries out the various use cases of the program.
 * 
 * @author Tiffany Choi
 *
 */

public class ProgramController {

	/**
	 * RUNPROGRAM METHOD:
	 * This method runs the various use cases of the program.
	 * 
	 */
	public void runProgram() {
		if(GraphicsEnvironment.isHeadless()) {
		    System.out.println("running from server ... ");
		    
		} else {
		    System.out.println("fetching data and loading gui ... ");
		    
		    SwingUtilities.invokeLater(new Runnable() {
		        public void run() {
		            try {
		                
		                Stock spy = new Stock("SPY");
		                ArrayList<Stock> stocks = new ArrayList<>();
	                    stocks.add(spy);
	                    BaseGUI userInterface = new BaseGUI(stocks);
	                    
		            } catch(FileNotFoundException | InterruptedException e) { 
		                e.printStackTrace();
		            }
		        }
		    });
		}
	}
	
	public static void main(String[] args) {
        ProgramController cont = new ProgramController();
        cont.runProgram();
    }
}
