/**
 * 
 */
package com.tibco.demo.service;

import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.Topic;

import org.springframework.jms.core.JmsMessagingTemplate;

/**
 * @author Ashish Tulsankar
 * 15-Sep-2020
 */
public interface TibcoService {
	
	/**
	 * Method gets the underlying {@link Session}, builds the {@link Topic} or {@link Queue} & sends the
	 * received message using {@link MessageProducer}
	 * @param msgData
	 */
	public void sendMessage(String msgData);
	
	/**
	 * Method to send message using {@link JmsMessagingTemplate}
	 * @param payload as Message content
	 */
	public void sendViaMsgTemplate(String payload);
}
