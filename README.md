# AlluxioWeb
This is a prototype of Alluxio new WEBUI. It use jetty + spring + velocity. 

## How to run it

if you want to change the port, please modify `DefaultAlluxioMaster.startServingWebServer()`. 

1. git clone https://github.com/maobaolong/AlluxioWeb.git
2. mvn compiler:compile resources:resources 
3. mvn exec:java -Dexec.mainClass="alluxio.master.AlluxioMaster"

## Update

1. Implements general page through RESTFul api. 