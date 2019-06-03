package com.properpush.voice.proper.platform.producer;

import com.properpush.voice.proper.common.ActiveManagerService;
import com.properpush.voice.proper.conf.ActivemqConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: cui
 * @Date: 2018/12/18 11:43
 * @Description:
 */
@RestController
@RequestMapping(value = "/producerboot", produces = "application/json")
public class ProducerController {

    /**
     * 注入ActiveManager
     */
    @Autowired
    private ActiveManagerService activeManager;
    @Autowired
    ActivemqConfiguration activemqConfiguration;

    /**
     * 新增消息队列
     */
    @RequestMapping(value = "/add/queue/{sendTtsCode}", method = RequestMethod.GET)
    public ResponseEntity addQueue(@PathVariable String sendTtsCode, String phone) throws Exception {
        Map<String, String> map = new HashMap<>(2);
        map.put("sendTtsCode", sendTtsCode);
        for(int i=0; i<5;i++){
            map.put("phone", phone+i);
            new Thread(()->{
                try {
                    //传入语音服务队列
                    activeManager.send(map, this.activemqConfiguration.productActiveMQAllQueue());
                    //传入mongoDB队列
                    //activeManager.send(map, this.activemqConfiguration.productActiveMQMongoQueue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
        Map<String,String> maep = new HashMap<>();
        maep.put("data","SUCCESS");
        return new ResponseEntity(maep, HttpStatus.OK);
    }

}
