import java.io.Serializable;

public class OHLCV implements Serializable {

	/**
     * 
     */
    private static final long serialVersionUID = 8773796337479006667L;
    /**
	 * Class to hold open, high, low, close, volume data in treemap
	 */

	public Double open;
	public Double high;
	public Double low;
	public Double close;
	public Double volume;

	public OHLCV( Double open, Double high,Double low,Double close,Double volume) {
		
		this.open= open; 
		this.high= high; 
		this.low= low; 
		this.close= close;
		this.volume=volume;
		// TODO Auto-generated constructor stub
	}

}
