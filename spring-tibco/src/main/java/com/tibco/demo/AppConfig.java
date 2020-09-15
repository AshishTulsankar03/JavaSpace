/**
 * 
 */
package com.tibco.demo;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.Topic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsMessagingTemplate;

import com.tibco.tibjms.TibjmsConnectionFactory;

/**
 * Represents configuration class
 * @author Ashish Tulsankar
 * 14-Sep-2020
 */
@Configuration
@EnableJms
public class AppConfig {

	@Bean
	public ConnectionFactory connectionFactory(){

		TibjmsConnectionFactory connectionFactory = new TibjmsConnectionFactory("tcp://localhost:7222"); 
		connectionFactory.setUserName("admin");
		connectionFactory.setUserPassword("admin");

		// Initial attempts to establish connection with 3 attempts & delay of 1000 milliseconds
		// connectionFactory.setConnAttemptCount(3);
		// connectionFactory.setConnAttemptDelay(1000);

		return connectionFactory;
	}


	@Bean
	public JmsMessagingTemplate jmsMessagingTemplate() {

		return new JmsMessagingTemplate(connectionFactory());

	}

	/**
	 * Builds container definition for {@link Topic}
	 * @return {@link DefaultJmsListenerContainerFactory}
	 */
	@Bean(name = "topicJmsListenerContainerFactory")
	public DefaultJmsListenerContainerFactory topicJmsListenerContainerFactory() {

		DefaultJmsListenerContainerFactory topicListenerFactory = new DefaultJmsListenerContainerFactory();
		//  Don't set it so that it will be unique always.
		//  listenerContainerFactory.setClientId("demo-client-id");
		topicListenerFactory.setConnectionFactory(connectionFactory());
		topicListenerFactory.setPubSubDomain(true); 
		topicListenerFactory.setSessionTransacted(true);
		topicListenerFactory.setConcurrency("5");


		return topicListenerFactory;
	}

	/**
	 * Builds container definition for {@link Queue}
	 * @return {@link DefaultJmsListenerContainerFactory}
	 */
	@Bean(name = "queueJmsListenerContainerFactory")
	public DefaultJmsListenerContainerFactory queueJmsListenerContainerFactory() {

		DefaultJmsListenerContainerFactory queueListenerFactory = new DefaultJmsListenerContainerFactory();
		//  Don't set it so that it will be unique always.
		//  listenerContainerFactory.setClientId("demo-client-id");
		queueListenerFactory.setConnectionFactory(connectionFactory());
		queueListenerFactory.setPubSubDomain(false); 
		queueListenerFactory.setSessionTransacted(true);
		queueListenerFactory.setConcurrency("5");


		return queueListenerFactory;
	}
}
