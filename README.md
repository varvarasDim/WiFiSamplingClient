# WiFiSamplingClient
Client of the WiFiSampling app

This is the client of the WifiSampling application. At repository WiFiSamplingServer is the server of the application in Java.
Client is written in Android. 

The client collects signal data from the wifi environment and either sends it to the server or saves it locally at the android device. Server collects the data and presents them at a graph (for a specific wifi network). 

When the app client starts, it asks for the ip of the server. There is a simple menu so that the user can either save signal data locally or sends them to the server. If the user chooses to save the data locally then a "wificlient" directory is being created and the signal data are saved there in text format. The data that are saved are: the name of the networks that are received, mac address, dbm and the date of the sampling.
