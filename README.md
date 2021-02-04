# Distributed-QuantLib
This project is a distributed system for the financial system.  
As QuantLib is a single thread program. This project is to show how can accelerate computation among server farms.  
Each computer communicates via Apache Kafka to distributes workload and communicates between different types of services.  
Scalability is archivable for Pricer service without restart all the services to increase or reduce computation nodes.    
With thread pool that connects directly to C++ from JVM for achieving concurrency of execution in a computer  

## Design
The project is a design by using  Microservices architecture  
Thread pool with LinkedBlockingQueue for each pool worker  

## Services
Currently, it has 4 services.
1.	Main Server service, as a front-end to get Rest API then communicate to other services
2.	Pricer service, to compute quantitative finance by use JNI
3.	Data Provider service, to work with databases. Both in-memory database (Redis) and persistent database (PostgreSQL)
4.	Position service, to calculate for sensitivity analysis

![Image of System](https://raw.githubusercontent.com/na-ho/Distributed-QuantLib/main/Doc/SystemView.png)

## Financial instruments
Support 2 types of financial instruments to demonstrate distributed (currently)
1.	European Option Monte Carlo (Sobol) 
2.	Fixed-Rate Bond

## Technology
1.	Java for Main Server service, Pricer service, and Data Provider service.
2.	Scala for Position service.
3.	Spring boot for all services.
4.	Apache Kafka for all services.
5.	C++, QuantLib, boost 1.75.0 for QuantJNI library.
6.	JNI for Pricer and QuantJNI library.

Java/Scala IDE : IntelliJ IDEA  
JDK : OpenJDK 15  
Build tool : Gradle  
C++ IDE : Microsoft Visual Studio 2019  
Message Broker : kafka_2.13-2.7.0, apache-zookeeper-3.6.2  
Databases : Redis 6.0.6, PostgreSQL 12.5  

## Setup
1.	Set the port of the service and Kafka for each service in application.yml
2.	Set database connection for DataProvider service in application.yml
3.	Create a table for PostgreSQL with
	```
	CREATE TABLE public."EuropeanOptionMCSobolDataParam" (
		id int4 NOT NULL DEFAULT nextval('europeanoptionmcsoboldataparam_id_seq'::regclass),
		optiontype int4 NOT NULL,
		evaluationdate_year int4 NOT NULL,
		evaluationdate_month int4 NOT NULL,
		evaluationdate_day int4 NOT NULL,
		settlementdate_year int4 NOT NULL,
		settlementdate_month int4 NOT NULL,
		settlementdate_day int4 NULL,
		maturitydate_year int4 NOT NULL,
		maturitydate_month int4 NOT NULL,
		maturitydate_day int4 NOT NULL,
		riskfreerate float8 NOT NULL,
		dividendyield float8 NOT NULL,
		volatility float8 NOT NULL,
		strike float8 NOT NULL,
		underlying float8 NOT NULL,
		timesteps int4 NOT NULL,
		nsamples int4 NOT NULL,
		CONSTRAINT europeanoptionmcsoboldataparam_pk PRIMARY KEY (id)
	);
	```
	
## Running
1.	Start ZooKeepper then Kafka
2.	Start Redis, PostgreSQL
3.	Start Each of the services for Main Server service and Data Provider service
	For Pricer service, it can start as needed
4.	Test Rest API from Main Server
