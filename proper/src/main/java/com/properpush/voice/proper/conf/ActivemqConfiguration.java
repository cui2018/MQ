package com.properpush.voice.proper.conf;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.broker.region.policy.RedeliveryPolicyMap;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: cui
 * @Date: 2018/12/19 15:50
 * @Description:
 */
@Component
@Configuration
@PropertySource("classpath:config/activeMq.properties")
public class ActivemqConfiguration {
    @Value("${MQ.brokerurl}")
    private String BROKER_URL;
    @Value("${MQ.username}")
    private String USERNAME;
    @Value("${MQ.password}")
    private String PASSWORD;
    @Value("${MQ.customerqueue}")
    private String ALIQUEUE;
    @Value("${MQ.producerqueue}")
    private String MONGO_ALIQUEUE;
    @Value("${MQ.allqueue}")
    private String ALLQUEUE;
    @Value("${MQ.maximumRedeliveries}")
    private int MAXIMUMREDELIVERIES;
    @Value("${MQ.redeliveryDelay}")
    private int REDELIVERYDELAY;
    @Value("${MQ.maxConnections}")
    private int MAXCONNECTIONS;
    @Value("${MQ.FALSE}")
    private boolean FALSE;
    @Value("${MQ.TRUE}")
    private boolean TRUE;


    @Bean
    public ActiveMQDestination productActiveMQQueue() {
        ActiveMQDestination queue = new ActiveMQQueue(ALIQUEUE);
        return queue;
    }

    @Bean
    public ActiveMQDestination productActiveMQMongoQueue() {
        return new ActiveMQQueue(MONGO_ALIQUEUE);
    }

    @Bean
    public ActiveMQDestination productActiveMQAllQueue() {
        return new ActiveMQQueue(ALLQUEUE);
    }

    @Bean
    public RedeliveryPolicy redeliveryPolicyActiveMQQueue() {
        //重发次数 延时、延时系数、延时指数开关、目标（重发等待时间1s, 2s, 4s, 8s）
        RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
        redeliveryPolicy.setMaximumRedeliveries(MAXIMUMREDELIVERIES);
        redeliveryPolicy.setRedeliveryDelay(REDELIVERYDELAY);
        redeliveryPolicy.setUseExponentialBackOff(FALSE);
        redeliveryPolicy.setDestination(productActiveMQQueue());
        return redeliveryPolicy;
    }

    @Bean
    public RedeliveryPolicy redeliveryPolicyActiveMQMongoQueue() {
        //重发次数 延时、延时系数、延时指数开关、目标（重发等待时间1s, 2s, 4s, 8s）
        RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
        redeliveryPolicy.setMaximumRedeliveries(MAXIMUMREDELIVERIES);
        redeliveryPolicy.setRedeliveryDelay(REDELIVERYDELAY);
        redeliveryPolicy.setUseExponentialBackOff(FALSE);
        redeliveryPolicy.setDestination(productActiveMQMongoQueue());
        return redeliveryPolicy;
    }

    @Bean
    public RedeliveryPolicy redeliveryPolicyActiveMQAllQueue() {
        //重发次数 延时、延时系数、延时指数开关、目标（重发等待时间1s, 2s, 4s, 8s）
        RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
        redeliveryPolicy.setMaximumRedeliveries(MAXIMUMREDELIVERIES);
        redeliveryPolicy.setRedeliveryDelay(REDELIVERYDELAY);
        redeliveryPolicy.setUseExponentialBackOff(FALSE);
        redeliveryPolicy.setDestination(productActiveMQAllQueue());
        return redeliveryPolicy;
    }

    @Bean
    public RedeliveryPolicyMap redeliveryPolicyMap() {
        RedeliveryPolicyMap redeliveryPolicyMap = new RedeliveryPolicyMap();
        List<RedeliveryPolicy> queueList = new ArrayList<>();
        queueList.add(redeliveryPolicyActiveMQQueue());
        queueList.add(redeliveryPolicyActiveMQMongoQueue());
        queueList.add(redeliveryPolicyActiveMQAllQueue());
        redeliveryPolicyMap.setRedeliveryPolicyEntries(queueList);
        return redeliveryPolicyMap;
    }

    @Bean
    public SingleConnectionFactory pooledConnectionFactory() {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(BROKER_URL);
        activeMQConnectionFactory.setRedeliveryPolicyMap(redeliveryPolicyMap());
        PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory(activeMQConnectionFactory);
        pooledConnectionFactory.setMaxConnections(MAXCONNECTIONS);
        SingleConnectionFactory singleConnectionFactory = new SingleConnectionFactory(pooledConnectionFactory);
        singleConnectionFactory.setReconnectOnException(TRUE);
        return singleConnectionFactory;
    }

    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerQueue() {
        DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
        //开启事务 不开启事务，消息在异常的情况下是不会重试的
        bean.setSessionTransacted(TRUE);
        bean.setConnectionFactory(pooledConnectionFactory());
        return bean;
    }

    @Bean
    public JmsMessagingTemplate jmsMessagingTemplate() {
        JmsMessagingTemplate jmsMessagingTemplate = new JmsMessagingTemplate(pooledConnectionFactory());
        return jmsMessagingTemplate;
    }
}
