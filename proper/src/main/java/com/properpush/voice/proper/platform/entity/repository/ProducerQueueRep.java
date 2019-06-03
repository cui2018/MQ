package com.properpush.voice.proper.platform.entity.repository;

import com.properpush.voice.proper.platform.entity.ProducerQueue;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Auther: cui
 * @Date: 2018/12/26 18:14
 * @Description:
 */
public interface ProducerQueueRep extends JpaRepository<ProducerQueue, Integer> {
}
