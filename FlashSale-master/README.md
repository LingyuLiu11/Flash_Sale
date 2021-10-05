# FlashSale
This is a flash sale project. 

Highlights:

· Established a ticket selling web system with Spring Boot, Navicat, Redis, MyBatis, Maven, MySQL.

· Implemented the database control with Spring Boot and MyBatis.

· Built a selling system using Redis Lua to prevent overselling and using Kafka message queues to cancel timeout orders
  and reduce system resource consumption.
  
· Enabled static web page technology with Thymeleaf and cache preheating technology with Redis to reduce database
  access by 94%.
  
· Processed tasks asynchronously using message queues, improved overall system performance from 30k QPS to 150k
  QPS and improved overall system throughput.
  
· Stopped 99% crawlers and malicious requests using Sentinel for current limiting control.
