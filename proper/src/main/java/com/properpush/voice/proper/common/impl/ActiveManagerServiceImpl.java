package com.properpush.voice.proper.common.impl;

import com.alibaba.fastjson.JSONObject;
import com.properpush.voice.proper.ali.entity.DefineEntity;
import com.properpush.voice.proper.common.ActiveManagerService;
import com.properpush.voice.proper.conf.ActivemqConfiguration;
import org.apache.activemq.ScheduledMessage;
import org.eclipse.jetty.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * @Auther: cui
 * @Date: 2018/12/18 11:40
 * @Description:
 */
@Component
public class ActiveManagerServiceImpl implements ActiveManagerService {
    private static final Logger logger = LoggerFactory.getLogger(ActiveManagerServiceImpl.class);
    @Autowired
    ActivemqConfiguration activemqConfiguration;
    @Autowired
    DefineEntity defineEntity;

    /**
     * @Description 即时发送
     * @Params 数据 模式
     * @Return
     * @Author cui
     * @Date 2018/12/18
     */
    @Override
    public void send(Map map, Destination destination) throws Exception {
        String ttsCode = (String) this.getGetMethod(defineEntity, (String) map.get("sendTtsCode"));
        if (StringUtil.isBlank(ttsCode)) {
            logger.info("增加消息失败，语音模板：{}，用户手机号：{}", ttsCode, map.get("phone"));
            return;
        }
        map.put("produceTime", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
        map.put("ttsCode", ttsCode);
        try {
            this.activemqConfiguration.jmsMessagingTemplate().convertAndSend(destination, map);
            logger.info("增加消息成功，语音模板：{}，用户手机号：{}", ttsCode, map.get("phone"));
        } catch (MessagingException e) {
            logger.warn("增加消息异常，语音模板：{}，用户手机号：{}", ttsCode, map.get("phone"));
        }
    }

    /**
     * @Description 获取ttsCode
     * @Params
     * @Return
     * @Author cui
     * @Date 2018/12/24
     */
    private Object getGetMethod(Object ob, String name) throws Exception {
        Method[] m = ob.getClass().getMethods();
        for (int i = 0; i < m.length; i++) {
            if (("get" + name).toLowerCase().equals(m[i].getName().toLowerCase())) {
                return m[i].invoke(ob);
            }
        }
        return null;
    }

    /**
     * @Description 延时发送
     * @Params
     * @Return
     * @Author cui
     * @Date 2018/12/18
     */
    @Override
    public void delaySend(Map map, Destination destination, long time) throws Exception {
        String ttsCode = (String) this.getGetMethod(defineEntity, (String) map.get("ttsCode"));
        if (StringUtil.isBlank(ttsCode)) {
            logger.info("增加消息失败，语音模板：{}，用户手机号：{}", ttsCode, map.get("phone"));
            return;
        }
        map.put("ttsCode", ttsCode);
        //获取连接工厂
        ConnectionFactory connectionFactory = this.activemqConfiguration.jmsMessagingTemplate().getConnectionFactory();
        try {
            //获取连接
            Connection connection = connectionFactory.createConnection();
            connection.start();
            //获取session
            Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            // 创建一个消息队列（队列模式）
            //Destination destination = session.createQueue(queueName);
            //创建生产者
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            String text = JSONObject.toJSONString(map);
            TextMessage message = session.createTextMessage(text);
            //设置延迟时间
            message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, time);
            //发送
            producer.send(message);
            session.commit();
            producer.close();
            session.close();
            connection.close();
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
