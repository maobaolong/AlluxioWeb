# AlluxioWeb
This is a prototype of Alluxio new WEBUI. It use jetty + spring + velocity. 

## How to run it

For convenient, i write the port `49999` in source code, if you want to change the port, please modify `DefaultAlluxioMaster.startServingWebServer()`.
 
Don't worry about this, i'll new a property key to custom it after this module merged into Alluxio.

1. git clone https://github.com/maobaolong/AlluxioWeb.git
2. mvn compiler:compile resources:resources 
3. mvn exec:java -Dexec.mainClass="alluxio.master.AlluxioMaster"
4. open [localhost:49999/home](localhost:49999/home) 

## Update

1. Implements general page through RESTFul api. 