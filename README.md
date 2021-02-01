# Distributed-QuantLib
This project is a distributed system for financial.
As QuantLib is a single thread program. This project is to show how can accelerate computation among server farms.
Each of computer communicates via Apache Kafka to distributes workload and communicates between different type of services.

## Design
The project is designed by use Microservice Pattern.

## Services
Currently, it has 4 services.
1.	Main Server service, as a front-end to get Rest API then communicate to other services
2.	Pricer service, to compute quantitative finance by use JNI
3.	Data Provider service, to work with databases. Both in-memory database (Redis) or persistent database (PostgreSQL)
4.	Position service, to calculate for sensitivity analysis

![Image of System](https://raw.githubusercontent.com/na-ho/Distributed-QuantLib/main/Doc/SystemView.png)

## Financial instruments
Support 2 types of financial instruments to demonstrate distributed
1.	European Option Monte Carlo (Sobol) 
2.	Fixed-Rate Bond

## Technology
Java for Main Server service, Pricer servce, and Data Provider service.
Scala for Position service.
Spring boot for all services.
Apache Kafka for all services.
C++ for QuantJNI library.
JNI for Pricer and QuantJNI library.

