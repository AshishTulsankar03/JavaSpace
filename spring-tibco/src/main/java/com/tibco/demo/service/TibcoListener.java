/**
 * 
 */
package com.tibco.demo.service;

import javax.jms.Message;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.annotation.JmsListeners;
import org.springframework.stereotype.Service;

/**
 * @author Ashish Tulsankar
 * 15-Sep-2020
 */

@Service
public class TibcoListener {

	private static final Logger logger = LogManager.getLogger(TibcoListener.class);

	@JmsListeners(value ={
			@JmsListener(destination = "demo.topic", containerFactory = "topicJmsListenerContainerFactory"),
			@JmsListener(destination = "demo.queue", containerFactory = "queueJmsListenerContainerFactory")
			})
	public void processMessages(Message message) {
		
		logger.info("Message received {}", message);

	}

}
