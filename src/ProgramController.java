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
        
        // headless environment for server side use
	    
		if(GraphicsEnvironment.isHeadless()) {
		    ConsoleUI userInterface = new ConsoleUI();
		    userInterface.init();
		    
		} else {
		    
		    SwingUtilities.invokeLater(new Runnable() {
		        public void run() {
                    BaseGUI userInterface = new BaseGUI();
		        }
		    });
		}
	}
}
