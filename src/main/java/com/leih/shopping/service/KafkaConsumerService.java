package com.leih.shopping.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.leih.shopping.db.dao.DealDao;
import com.leih.shopping.db.dao.OrderDao;
import com.leih.shopping.db.po.Deal;
import com.leih.shopping.db.po.Order;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class KafkaConsumerService {
    @Autowired
    DealDao dealDao;
    @Autowired
    OrderDao orderDao;
    @Autowired
    RedisService redisService;
    Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
    @KafkaListener(topics = {"order"},groupId = "deal-shopping")
    public void getOrder(String msg){
        JSONObject jsonObject = JSON.parseObject(msg);
        //get order object
        Order order = JSON.toJavaObject(jsonObject, Order.class);
        //set up create time
        order.setCreateTime(new Date());
        logger.info("Order created: "+order.getOrderNo()+", price: "+order.getOrderAmount());
        //lock stock
        boolean lockStockResult = dealDao.lockStock(order.getDealId());
        if(lockStockResult){
            //status 0: out of stock, status 1: success and waiting for being paid, status 2: paid.
            order.setOrderStatus(1);
        }else{
            order.setOrderStatus(0);
        }
        //insert the order to mysql database
        orderDao.insertOrder(order);
    }


    @KafkaListener(topics = {"checkOrder_15_minutes_final"},groupId = "check_order")
    public void checkOrder(String msg){
        //msg is the order no
            Order order = orderDao.queryOrder(msg);
            if(order!=null){
                if(order.getOrderStatus()==2){
                    logger.info("Check order: the order has been paid");
                    //if the order has been paid, do nothing
                }else{
                    logger.info("Check order: the order has been cancelled due to not being paid within 15 minutes");
                    order.setOrderStatus(99);//close the order
                    orderDao.updateOrder(order);
                    //revert stock back, update deal
                    dealDao.revertBackStock(order.getDealId());
                    //remove the user from the restricted list
                    redisService.removeUserLimits(order.getDealId(), order.getUserId());
                    logger.info("The user has been removed from the restricted list since the order has been cancelled");
                }
            }

    }

}
