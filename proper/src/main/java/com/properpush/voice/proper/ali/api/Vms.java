package com.properpush.voice.proper.ali.api;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dyvmsapi.model.v20170525.SingleCallByTtsRequest;
import com.aliyuncs.dyvmsapi.model.v20170525.SingleCallByTtsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.properpush.voice.proper.ali.entity.DefineEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created on 17/6/10.
 * 语音API产品的DEMO程序,工程中包含了一个VmsDemo类，直接通过
 * 执行main函数即可体验语音产品API功能(只需要将AK替换成开通了云通信-语音产品功能的AK即可)
 * 工程依赖了2个jar包(存放在工程的libs目录下)
 * 1:aliyun-java-sdk-core.jar
 * 2:aliyun-java-sdk-dyvmsapi.jar
 *
 * 备注:Demo工程编码采用UTF-8
 */
public class Vms {

    @Autowired
    private static DefineEntity defineEntity;

    /**
     * 文本转语音外呼
     * @return
     * @throws ClientException
     */
    public static SingleCallByTtsResponse singleCallByTts(Map map) throws ClientException {
        //可自助调整超时时间
        //System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        //System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", defineEntity.getAccessKeyId(), defineEntity.getAccessKeySecret());
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", defineEntity.getProduct(), defineEntity.getDomain());
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象-具体描述见控制台-文档部分内容
        SingleCallByTtsRequest request = new SingleCallByTtsRequest();
        //必填-被叫显号,可在语音控制台中找到所购买的显号
        request.setCalledShowNumber(defineEntity.getCalledShowNumber());
        //必填-被叫号码
        request.setCalledNumber((String) map.get("phone"));
        //必填-Tts模板ID
        request.setTtsCode((String) map.get("ttsCode"));
        map.remove("phone");
        map.remove("ttsCode");
        //可选-当模板中存在变量时需要设置此值
        request.setTtsParam(JSONObject.toJSONString(map));
        //可选-播放音量大小
        //request.setVolume(100);
        //可选-播放次数(最多3次)
        //request.setPlayTimes(1);
        //可选-外部扩展字段,此ID将在回执消息中带回给调用方
        request.setOutId("yourOutId");
        //hint 此处可能会抛出异常，注意catch
        SingleCallByTtsResponse singleCallByTtsResponse = acsClient.getAcsResponse(request);
        return singleCallByTtsResponse;
    }




}
