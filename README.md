# HTTP-server-implementation
A cool and simple HTTP Server implementation using pure Java

https://github.com/user-attachments/assets/4bdf55c0-ed12-4f54-a0c1-c34ab0648a12

## Overview

This project is a simple yet robust HTTP server implementation using Java to handle basic HTTP requests and serve predefined routes. The server supports multithreading, allowing it to handle multiple client connections simultaneously. It's an excellent project for understanding the basics of HTTP, server programming, and multithreading in Java.

## Features

- **Multithreaded Handling**: Supports simultaneous client connections.
- **Default Routes**:
  - `/`: A welcoming page with the current date and time.
  - `/time`: Returns the current time in JSON format.
  - `/health`: Reports the server's health status.
  - `/stats`: Displays basic server statistics like active threads and available memory.
- **Customizable Routes**: Easily add new routes with specific handlers.
- **HTML and JSON Responses**: Supports serving both HTML and JSON responses.
- **Error Handling**: Handles invalid routes with a `404 Not Found` response.

## Requirements

1. Java Development Kit (JDK): Version 8 or higher.
2. terminal or CLI interface

## **Usage**

### **1. Clone the Repository**
```
git clone https://github.com/<your-username>/SimpleHttpServer.git
cd SimpleHttpServer
```
### **2. Compile and run the Server**
```
javac SimpleHttpServer.java
java SimpleHttpServer
```


### **2. Accessing the Server through your browser**
just visit:
http://localhost:8080/
OR
http://127.0.0.1:8080/
<br><br>
> You can also check the data sent by the user on the server side. This includes the headers and other details from the HTTP request such as User-Agent, Accept, and more as shown below

![image](https://github.com/user-attachments/assets/6d537986-24bd-42e6-a475-e11e752f332b)


## How to add new Routes:
The server provides a simple interface to add custom routes. Here’s an example:
```
server.addRoute("/hello", (request) -> 
    server.createHtmlResponse("<h1>Hello, World!</h1>")
);
```
After adding this code, the route `/hello` will serve a basic HTML response.


## Sample Curl outputs:

Request: `curl http://localhost:8080/time`

Response: 
```
{"time": "2024-11-19T19:15:45.9178453"}
```
<br>

Request: `curl http://localhost:8080/health`

Response: 
```
{"status": "healthy", "uptime": "1732023983004"}
```


## Support

For any issues or questions, please contact me [here](mailto:psmerekar@gmail.com).
