package com.properpush.voice.proper.ali.entity;

/**
 * @Auther: cui
 * @Date: 2018/12/19 09:46
 * @Description:
 */

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
/**
 *@Description 定义读取阿里云语音配置
 *@Author  cui
 *@Date  2018/12/19
 */
@Data
@Component
@ConfigurationProperties(prefix="ali")
@PropertySource("classpath:config/ali.properties")
public class DefineEntity {
    private String product;
    private String domain;
    private String accessKeyId;
    private String accessKeySecret;
    private String ttsIdentityCode;
    private String ttsLoginCode;
    private String ttsLoginErrorCode;
    private String ttsRegisterCode;
    private String ttsActConfirmCode;
    private String ttsRePasswordCode;
    private String ttsInfoChangeCode;
    private String calledShowNumber;
    private String brokerUrl;
    private String queue;
}
