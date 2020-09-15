/**
 * 
 */
package com.tibco.demo.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tibco.demo.service.TibcoService;
import com.tibco.demo.service.TibcoTopicServiceImpl;

/**
 * @author Ashish Tulsankar
 * 15-Sep-2020
 */
@RestController
public class DemoController {
	
	private static final Logger logger = LogManager.getLogger(TibcoTopicServiceImpl.class);
	
	@Autowired
	private TibcoService tibcoTopicServiceImpl;
	@Autowired
	private TibcoService tibcoQueueServiceImpl;
	
	@PostMapping(value = "/producer/send/{msg}")
	public void sendUsingMsgProducer(@PathVariable String msg) {
		logger.info("sendUsingMsgProducer {}",msg);
		tibcoTopicServiceImpl.sendMessage(msg);
		tibcoQueueServiceImpl.sendMessage(msg);
	}
	
	@PostMapping(value = "/template/send/{msg}")
	public void sendUsingTemplate(@PathVariable String msg) {
		logger.info("sendUsingTemplate {} ",msg);
		tibcoTopicServiceImpl.sendMessage(msg);
		tibcoQueueServiceImpl.sendMessage(msg);
	}

}
