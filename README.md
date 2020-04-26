# final-project-team40_stockalertapp

CIT 591 Final Project:


This program allows the user to enter a stock ticker, get the useful technical and statistical analysis of its trading history, and get stock alerts displayed on the graphical user interface (GUI).

STARTING THE APPLICATION : 
The application can be initiated by way of the main method in the runner class (Runner.java). 

Personal Computers will launch the graphical user interface at which point there are instructions on adding a security ticker to the view (Hit the '+' and enter the stock ticker in the text field input box).  


Each stock request will make an API call that has a 13 second window between calls built-in to ensure compliance with the third party's requirements.  Alternatively, the user can load a file named portfolioAutos.ser by clicking the menu button, denoted by the triple bar, and selecting Load Stock List.  From the file chooser select the 'portfolioAutos.ser' file.  

The indicators we are using include the SMA, EMA, RSI, MACD, and OBV indicators. SMA is a simple moving average of the closing stock price over a number of days. EMA is an exponential moving average of the stock price, which means it is exponentially weighted towards more recent prices.  When the price is over an average, it is trending up, and when it is under an average it is trending down. RSI is the the relative strength index of the close over a number of days, and is used to see if a stock is overbought or oversold. Usually a reading of 30 is oversold, and a reading of 70 is overbought.  MACD stands for moving average convergance divergance, trend-following momentum indicator that shows the relationship between two moving averages of a security's price. OBV stands for on balance volume, and allows us to see how volume changes cumulatively over time. 

Group Members: 

Tiffany Choi (tiffchoi@seas.upenn.edu)

Joseph Kravets (kravetsj@seas.upenn.edu)

Robert Stanton (rstant@seas.upenn.edu)
