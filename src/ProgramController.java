import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
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
	    
	    File stockListFile = new File("tickerList.ser");
	    FileInputStream file;
	    ArrayList<String> tickerList = new ArrayList<>();
	    ArrayList<Stock> stocks = new ArrayList<>();
	    
	    
        try {
            file = new FileInputStream(stockListFile);
            if(stockListFile.exists()) {
                ObjectInputStream in = new ObjectInputStream(file);
                tickerList = (ArrayList<String>) in.readObject();
                file.close();
                in.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        // headless environment for server side useage
		if(GraphicsEnvironment.isHeadless()) {
		    ConsoleUI userInterface = new ConsoleUI();
		    userInterface.init();
		    
		} else {
		    System.out.println("fetching data and loading gui ... ");
		    
		    SwingUtilities.invokeLater(new Runnable() {
		        public void run() {
                    BaseGUI userInterface = new BaseGUI();
		        }
		    });
		}
	}
	
	public static void main(String[] args) {
        ProgramController cont = new ProgramController();
        cont.runProgram();
    }
}
