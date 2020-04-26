# final-project-team40_stockalertapp

CIT 591 Final Project:


This program allows the user to enter a stock ticker, get the useful technical and statistical analysis of its trading history, and get stock alerts displayed on the graphical user interface (GUI).

STARTING THE APPLICATION : 
The application can be initiated by way of the main method in the runner class (Runner.java). 

Personal Computers will launch the graphical user interface at which point there are instructions on adding a security ticker to the view (Hit the '+' and enter the stock ticker in the text field input box).  

<img src=StockAlertAppShot2.png>

Each stock request will make an call from the free stock data api https://www.alphavantage.co/, which allows 5 calls per minute so it can be slow.  Alternatively, the user can load a file named portfolioAutos.ser by clicking the menu button, denoted by the triple bar, and selecting Load Stock List.  From the file chooser select the 'portfolioAutos.ser' file.  


<img src=StockAlertAppShot6.png>


Upon loading a list of securities will be displayed to the left of the charting window.

<img src=StockAlertAppShot1.png>

The indicators we are using include the SMA, EMA, RSI, MACD, and OBV indicators. SMA is a simple moving average of the closing stock price over a number of days. EMA is an exponential moving average of the stock price, which means it is exponentially weighted towards more recent prices.  When the price is over an average, it is trending up, and when it is under an average it is trending down. RSI is the the relative strength index of the close over a number of days, and is used to see if a stock is overbought or oversold. Usually a reading of 30 is oversold, and a reading of 70 is overbought.  MACD stands for moving average convergance divergance, trend-following momentum indicator that shows the relationship between two moving averages of a security's price. OBV stands for on balance volume, and allows us to see how volume changes cumulatively over time. 

Indicators may be added and removed by right clicking on a Stock Button to the left.

<img src=StockAlertAppShot5.png>

From there a Dialog box will provide available options for a user to make changes to the alerts on a given stock.

<img src=StockAlertAppShot4.png>

When the user is done.  They may save the List of Securities, using the menu button's Save Stock List option, as a java serialized object (yourList.ser).

<img src=StockAlertAppShot7.png>

Group Members: 

Tiffany Choi (tiffchoi@seas.upenn.edu)

Joseph Kravets (kravetsj@seas.upenn.edu)

Robert Stanton (rstant@seas.upenn.edu)
