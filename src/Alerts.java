/***
 * ALERTS CLASS:
 * 
 * This class calculates the Alerts. 
 * 
 * @author Tiffany Choi
 *
 */

public class Alerts {
	
	/**
	 * BELOWALERT METHOD: 
	 * This method alerts the user if the indicator is below or equal to the saved alert.\
	 * 
	 * @return true: if the indicator is below the number
	 * @return false: if the indicator is equal or above the number
	 */
	
	public boolean belowPriceAlert(double indicator, double savedalert) {
		
		if (indicator < savedalert) {
			return true;
		} 

		return false;
	}
		
	/**
	 * ABOVEALERT METHOD: 
	 * This method alerts the user if the indicator is above the saved alert.
	 * 
	 * (Includes the SMA Crossover Alert and EMA Crossover Alert)
	 * @return true: if the indicator is equal or above the number
	 * @return false: if the indicator is below the number
	 */
	
	public boolean abovePriceAlert(double indicator, double savedalert) {
		
		if (indicator >= savedalert) {
			return true;
		}
		
		return false;
	}
}