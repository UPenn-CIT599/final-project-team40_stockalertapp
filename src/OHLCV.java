
/**
 * Class to hold open, high, low, close, volume data in TreeMap
 * 
 * @author Joseph Kravets
 *
 */
public class OHLCV {

    public Double open;
    public Double high;
    public Double low;
    public Double close;
    public Double volume;

    public OHLCV(Double open, Double high, Double low, Double close, Double volume) {

	this.open = open;
	this.high = high;
	this.low = low;
	this.close = close;
	this.volume = volume;

    }

}
