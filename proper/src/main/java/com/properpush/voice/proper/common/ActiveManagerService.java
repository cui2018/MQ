package com.properpush.voice.proper.common;

import javax.jms.Destination;
import java.util.Map;

/**
 * @Auther: cui
 * @Date: 2018/12/24 11:37
 * @Description:
 */
public interface ActiveManagerService {
    void send(Map<String, String> map, Destination destination) throws Exception;
    void delaySend(Map map, Destination destination, long time) throws Exception;
}
