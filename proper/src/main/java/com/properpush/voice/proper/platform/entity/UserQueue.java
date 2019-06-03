package com.properpush.voice.proper.platform.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Auther: cui
 * @Date: 2018/12/18 13:28
 * @Description: 记录已发送用户语音服务
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "senduser_queue")
public class UserQueue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(columnDefinition = "char(1) default 0")
    private String state = "0";
    @Column(columnDefinition = "varchar(25) default \"\"")
    private String ttsCode;
    @Column(columnDefinition = "varchar(500) default \"\"")
    private String msg;
    @Column(columnDefinition = "char(11) default \"\"")
    private String phone;
    @Column(columnDefinition = "char(19) default \"\"")
    private String sendTime = getTime();
    private String getTime(){
        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return  dtf.format(ldt);
    }
}
