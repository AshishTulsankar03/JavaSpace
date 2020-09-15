/**
 * 
 */
package com.tibco.demo.service;

import javax.annotation.PostConstruct;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;



/**
 * @author Ashish Tulsankar
 * 14-Sep-2020
 */
@Service
public class TibcoQueueServiceImpl implements TibcoService{

	private static final Logger logger = LogManager.getLogger(TibcoQueueServiceImpl.class);

	private ConnectionFactory connectionFactory;
	private JmsMessagingTemplate jmsMessagingTemplate;
	private String queueName;

	private MessageProducer messageProducer;
	private Connection connection;
	private Session session;

	@Autowired
	public TibcoQueueServiceImpl(ConnectionFactory factory,
			JmsMessagingTemplate messagingTemplate,
			@Value("${tibco.queueName}") String queue) {
		this.connectionFactory=factory;
		this.jmsMessagingTemplate=messagingTemplate;
		this.queueName=queue;
	}

	/**
	 * Method to initialize the {@link Session} & {@link Connection} via configured {@link ConnectionFactory}.
	 * <br> builds {@link MessageProducer} for {@link Queue}
	 * <br> For more details,
	 * Refer <a href="https://docs.oracle.com/javaee/7/api/javax/jms/Connection.html#createSession-boolean-int-">createSession</a>
	 * @return {@link Session}
	 */
	@PostConstruct
	public void init() {


		try {

			// Initialize messageProducer with Queue as destination 
			connection  = connectionFactory.createConnection();	
			session     = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
			Queue queue = session.createQueue(queueName);
			messageProducer = session.createProducer(queue);

			// Initialize messagingTemplate with Queue as destination 
			jmsMessagingTemplate.setDefaultDestination(queue);

			connection.start();
		} catch (JMSException e) {
			logger.error("Exception while creating connection {}", e);
		}
	}


	@Override
	public void sendMessage(String msgData) {
		try {
			logger.info("sendUsingQueue");

			Message message= session.createTextMessage(msgData);
			messageProducer.send(message);

		} catch (JMSException e) {
			logger.error("Exception occurred {}", e);
		}

	}

	@Override
	public void sendViaMsgTemplate(String payload) {
		// TODO Auto-generated method stub

	}


}
