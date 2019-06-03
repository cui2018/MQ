package com.properpush.voice.proper.platform.customer;

import com.properpush.voice.proper.platform.customer.service.CustomerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Auther: cui
 * @Date: 2018/12/18 11:44
 * @Description:
 */
@Service
public class CustomerController {

    @Autowired
    CustomerServiceImpl customerService;

    /**
     * 监听方
     */
    //监听注解
    @JmsListener(destination = "customer-queue", containerFactory = "jmsListenerContainerQueue")
    public void getCustomerQueue(Map info) {
        customerService.sendMsg(info);
    }
    /*@JmsListener(destination = "customer-queue", containerFactory = "jmsListenerContainerQueue")
    public void getCustomerQueueOne(Map info) {
        customerService.saveSendProducer(info);
    }*/
    @JmsListener(destination = "producer-queue", containerFactory = "jmsListenerContainerQueue")
    public void getProducerQueue(Map info) {
        customerService.saveSendProducer(info);
    }
}
