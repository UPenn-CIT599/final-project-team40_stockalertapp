/***
 * ALERTS CLASS:
 * 
 * This class maintains the list of stocks with their respective chosen alerts.
 * 
 * @author Tiffany Choi
 *
 */

public class Alerts {
	
	// test price variable
	
	double price = 45.00;
	double pricealert = 50.00;
	
	// store the alerts method
	
	
	
	/**
	 * BELOWPRICEALERT METHOD: 
	 * This method alerts the user if the price is below or equal to a certain number.
	 * @return true: if the price is below the number
	 * @return false: if the price is above the number
	 */
	
	public boolean belowPriceAlert() {
		
		if (price <= pricealert) {
			return true;
		} 

		return false;
		
	}
		
	/**
	 * ABOVEPRICEALERT METHOD: 
	 * This method alerts the user if the price is above a certain number.
	 * @return true: if the price is above the number
	 * @return false: if the price is below the number
	 */
	
	public boolean abovePriceAlert() {
		
		if (price >= pricealert) {
			return true;
		}
		
		return false;
	}
		
	/**
	 * SMACROSSOVERALERT METHOD: 
	 * This method alerts the user if the SMAs crossover
	 * @return true: if the SMA crossed over
	 * @return false: if the SMA did not cross over
	 */
	
	public boolean smaCrossoverAlert() {
		
		
		
		return false;
	}
	
	
	/**
	 * EMACROSSOVERALERT METHOD: 
	 * This method alerts the user if the EMAs crossover
	 * @return true: if the EMA crossed over
	 * @return false: if the EMA did not cross over
	 */
	
	public boolean emaCrossoverAlert() {
		
		return false;
	}
	
	
	/**
	 * BELOWRSIALERT METHOD: 
	 * This method alerts the user if the RSI is below a certain number.
	 * @return true: if the RSI is below the number
	 * @return false: if the RSI is above the number
	 */
	
	public boolean belowRSIAlert() {
		
		return false;
		
	}
		
	/**
	 * ABOVERSIALERT METHOD: 
	 * This method alerts the user if the RSI is above a certain number.
	 * @return true: if the RSI is above the number
	 * @return false: if the RSI is below the number
	 */
	
	public boolean aboveRSIAlert() {
		
		return false;
	}
	
	
	/**
	 * BELOWMACDALERT METHOD: 
	 * This method alerts the user if the MACD is below a certain number.
	 * @return true: if the MACD is below the number
	 * @return false: if the MACD is above the number
	 */
	
	public boolean belowMACDAlert() {
		
		return false;
		
	}
		
	/**
	 * ABOVEMACDALERT METHOD: 
	 * This method alerts the user if the MACD is above a certain number.
	 * @return true: if the MACD is above the number
	 * @return false: if the MACD is below the number
	 */
	
	public boolean aboveMACDAlert() {
		
		return false;
	}
	
	/**
	 * BELOWOBVALERT METHOD: 
	 * This method alerts the user if the OBV is below a certain number.
	 * @return true: if the OBV is below the number
	 * @return false: if the OBV is above the number
	 */
	
	public boolean belowOBVAlert() {
		
		return false;
		
	}
		
	/**
	 * ABOVEOBVALERT METHOD: 
	 * This method alerts the user if the OBV is above a certain number.
	 * @return true: if the OBV is above the number
	 * @return false: if the OBV is below the number
	 */
	
	public boolean aboveOBVAlert() {
		
		return false;
	}
	
}
