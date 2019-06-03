package com.properpush.voice.proper.platform.customer.service;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.dyvmsapi.model.v20170525.SingleCallByTtsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.properpush.voice.proper.ali.api.Vms;
import com.properpush.voice.proper.platform.entity.ProducerQueue;
import com.properpush.voice.proper.platform.entity.UserQueue;
import com.properpush.voice.proper.platform.entity.repository.ProducerQueueRep;
import com.properpush.voice.proper.platform.entity.repository.UserQueueRep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Auther: cui
 * @Date: 2018/12/24 13:50
 * @Description:
 */
@Service
public class CustomerServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);
    @Autowired
    UserQueueRep userQueueRep;
    @Autowired
    ProducerQueueRep producerQueueRep;

    public void sendMsg(Map info) {
        //发送语音服务消息
        SingleCallByTtsResponse singleCallByTtsResponse = null;
        String ttsCode = (String) info.get("ttsCode");
        String phone = (String) info.get("phone");
        info.remove("sendTtsCode");
        try {
            singleCallByTtsResponse = Vms.singleCallByTts(info);
            logger.info("阿里云语音服务发送成功，语音模板：{}，用户手机号：{}", ttsCode, phone);
        } catch (ClientException e) {
            logger.info("阿里云语音服务发送失败，语音模板：{}，用户手机号：{}，异常：{}", ttsCode, phone, e.toString());
        }
        //发送用户服务信息
        UserQueue user = new UserQueue();
        //阿里云返回描述和发送用户的信息
//        user.setMsg(singleCallByTtsResponse.getCode() + "-" + singleCallByTtsResponse.getMessage() + "-" + JSONObject.toJSONString(info));
        user.setMsg(JSONObject.toJSONString(info));
        user.setPhone(phone.substring(0, 11));
        user.setTtsCode(ttsCode);
        userQueueRep.save(user);
        /*if ("OK".equalsIgnoreCase(singleCallByTtsResponse.getCode())) {
            user.setStatus("1");
        }*/

    }

    public void saveSendProducer(Map<String, String> info) {
        producerQueueRep.save(new ProducerQueue(info.get("ttsCode"), info.get("msg"), info.get("phone"), info.get("produceTime")));
    }
}
