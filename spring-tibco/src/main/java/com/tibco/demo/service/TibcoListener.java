/**
 * 
 */
package com.tibco.demo.service;

import javax.jms.Message;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import com.tibco.demo.TibcoConstants;

/**
 * @author Ashish Tulsankar
 * 15-Sep-2020
 */

@Service
public class TibcoListener {

	private static final Logger logger = LogManager.getLogger(TibcoListener.class);

	@JmsListener(destination = TibcoConstants.INCOMING_TOPIC, containerFactory = "topicJmsListenerContainerFactory")
	public void processTopicMessages(Message message) {

		logger.info("Topic ~ JMS Message received {}",message);

	}


	@JmsListener(destination = TibcoConstants.INCOMING_QUEUE, containerFactory = "queueJmsListenerContainerFactory")
	public void processQueueMessages(Message message) {

		logger.info("Queue ~ JMS Message received {}", message);

	}
	
	// Configure multiple listeners at once
	//	@JmsListeners(value ={
	//			@JmsListener(destination = TibcoConstants.INCOMING_TOPIC, containerFactory = "topicJmsListenerContainerFactory"),
	//			@JmsListener(destination = TibcoConstants.INCOMING_QUEUE, containerFactory = "queueJmsListenerContainerFactory")
	//			})
	//	public void processMessages(Message message) {
	//		
	//		logger.info("Message received {}", message);
	//
	//	}



}
