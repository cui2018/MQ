package com.properpush.voice.proper.platform.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Auther: cui
 * @Date: 2018/12/26 14:41
 * @Description: 记录生产消息信息
 */
@Data
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "producer_queue")
public class ProducerQueue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(columnDefinition = "varchar(25) default \"\"")
    private String ttsCode;
    @Column(columnDefinition = "varchar(500) default \"\"")
    private String msg;
    @Column(columnDefinition = "char(11) default \"\"")
    private String phone;
    @Column(columnDefinition = "char(19) default \"\"")
    private String produceTime;

    public ProducerQueue() {
    }

    public ProducerQueue(String ttsCode, String msg, String phone, String produceTime) {
        this.ttsCode = ttsCode;
        this.msg = msg;
        this.phone = phone.substring(0,11);
        this.produceTime = produceTime.substring(0,19);
    }
}
