# High-availability-shopping-system
A high availability shopping(ecommerce) system using SpringBoot, Spring Cloud(Microservices architecture), Spring Cloud Gateway, resillience4j, Kafka, Redis and MySQL.

# Back-end Tech

## Redis
Redis is used as a cache in this project due to its high throughput. Compared to MySQL, it can take advantage of the reading speed as a cache. In my project, the Redis is responsible for storing deal and product information before the deal starts, then used to deal with a large number of requests for querying and updating deals. For instance, the Redis would be able to update the number of items held and the number of available items in stock, and it would limit the number of an item that one user could buy as well.

Another usage is the locks in lua. I used `eval()` to execute the lua script, and the lua will be considered as a command which is atomic. For instance, we can execute Lua scripts in Redis to implement distributed locks. 

https://redis.com/ebook/part-3-next-steps/chapter-11-scripting-redis-with-lua/11-2-rewriting-locks-and-semaphores-with-lua/11-2-1-why-locks-in-lua/

## MySQL & Mybatis
Mybatis is a framework supporting SQL for operating MySQL

## Kafka
I used Kafka in this project to implement a message queue. Kafka is doing excellent in the aspect of stream processing. In this project, Kafka is used for slowing down the requests - putting them in a queue, and picking them by consumers one by one - so that MySQL will not get crashed when it comes to a large number of operations. For the reason that Kafka has no function of delaying messages, I also implemented a task scheduler to finish the 15-minutes check for the order status.

## Time Counter for deals(not fully implemented in this project)

A synchronized time counter could be achieved by getting the time difference from the server, instead of clients' local times.

## CDN
CDN(Content Delivery Network) could be used for storing static resources so that users could access websites faster.

## Spring Cloud Microservices 

I used two Spring Cloud **Eureka** servers as a registry to manage address for my services.

## Spring Cloud Gateway

To be done.

## OpenFeign

Going to change RestTemplate to OpenFeign.

## Sluth & ZipKin

To be done.

## resilience4j
To Do - circuit breaker, rate limiter, etc.

### Why use resilience4j?
- The downstream service may be down and the request will keep going to the down service, exhausting network resources, and slowing performances.
- Bad user experience
- The failure of one service could cascade to other services throughout the whole application.

In this project, the **rate limiter** was used to limit the rate of accessing the website.

# Front End - React

To be done.