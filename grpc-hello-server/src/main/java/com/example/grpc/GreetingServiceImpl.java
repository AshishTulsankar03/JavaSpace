package com.example.grpc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.grpc.GreetingServiceOuterClass.HelloRequest;
import com.example.grpc.GreetingServiceOuterClass.HelloResponse;

import io.grpc.Status;
import io.grpc.stub.ServerCallStreamObserver;
import io.grpc.stub.StreamObserver;

public class GreetingServiceImpl extends GreetingServiceGrpc.GreetingServiceImplBase {
	private static Logger log = LogManager.getLogger(GreetingServiceImpl.class);

	@Override
	public void greeting(GreetingServiceOuterClass.HelloRequest request,
			StreamObserver<GreetingServiceOuterClass.HelloResponse> responseObserver) {

		log.info("Received~ {}",request);

		// You must use a builder to construct a new Protobuffer object as response
		GreetingServiceOuterClass.HelloResponse response = GreetingServiceOuterClass.HelloResponse.newBuilder()
				.setGreeting("Hello, " + request.getName())
				.build();
		
		// Use responseObserver to send a single response back
		log.info("Sending~ {}",response);		
		responseObserver.onNext(response);

		// When you are done, you must call onCompleted.
		responseObserver.onCompleted();
	}

	@Override
	public void responsiveGreets(GreetingServiceOuterClass.HelloRequest request,
			StreamObserver<GreetingServiceOuterClass.HelloResponse> responseObserver) {

		log.info("Received~ {}",request);

		GreetingServiceOuterClass.HelloResponse response = GreetingServiceOuterClass.HelloResponse.newBuilder()
				.setGreeting("Hey, " + request.getName())
				.build();


		// Clients may invoke onNext at most once for server streaming calls, but may receive many onNext callback.
		log.info("Sending~ {}",response);		
		responseObserver.onNext(response);
		log.info("Sending~ {}",response);
		responseObserver.onNext(response);
		log.info("Sending~ {}",response);
		responseObserver.onNext(response);

		// When you are done, you must call onCompleted.
		responseObserver.onCompleted();
	}

	@Override
	public io.grpc.stub.StreamObserver<com.example.grpc.GreetingServiceOuterClass.HelloRequest> bidirGreets(
			io.grpc.stub.StreamObserver<com.example.grpc.GreetingServiceOuterClass.HelloResponse> responseObserver) {

		final ServerCallStreamObserver<HelloResponse> serverCallStreamObserver =(ServerCallStreamObserver<HelloResponse>) responseObserver;
		// Swaps to manual flow control
		serverCallStreamObserver.disableAutoRequest();

		
		class OnReadyHandler implements Runnable {

			private boolean wasReady = false;

			@Override
			public void run() {
				if (serverCallStreamObserver.isReady() && !wasReady) {
					wasReady = true;
					log.info("Ready to stream");
					// Signal the request sender to send one message. This happens when isReady() turns true, signaling that
					// the receive buffer has enough free space to receive more messages. Calling request() serves to prime
					// the message pump.
					serverCallStreamObserver.request(1);
				}
			}
		}

		final OnReadyHandler onReadyHandler = new OnReadyHandler();
		serverCallStreamObserver.setOnReadyHandler(onReadyHandler);

		return new StreamObserver<GreetingServiceOuterClass.HelloRequest>() {

			@Override
			public void onNext(HelloRequest request) {
				// Process the request and send a response or an error.
				try {
					// Accept and enqueue the request.
					String name = request.getName();
					log.info("Received~ {} ",request);

					// Simulate server "work"
					Thread.sleep(100);

					// Send a response.
					String message = "Hi " + name;
					HelloResponse reply = HelloResponse.newBuilder().setGreeting(message).build();
					log.info("Sending~ {}",reply);
					responseObserver.onNext(reply);

					// Check the provided ServerCallStreamObserver to see if it is still ready to accept more messages.
					if (serverCallStreamObserver.isReady()) {
						// Signal the sender to send another request.
						serverCallStreamObserver.request(1);
					} else {
						onReadyHandler.wasReady = false;
					}
				} catch (Throwable throwable) {
					throwable.printStackTrace();
					responseObserver.onError(
							Status.UNKNOWN.withDescription("Error handling request").withCause(throwable).asException());
				}
			}

			@Override
			public void onError(Throwable t) {
				// End the response stream if the client presents an error.
				log.trace("onError~ {}",t);
				responseObserver.onCompleted();
			}

			@Override
			public void onCompleted() {
				// Signal the end of work when the client ends the request stream.
				log.info("Streaming done");
				responseObserver.onCompleted();
			}

		};
	}

	@Override
	public io.grpc.stub.StreamObserver<com.example.grpc.GreetingServiceOuterClass.HelloRequest> requestingGreets(
			io.grpc.stub.StreamObserver<com.example.grpc.GreetingServiceOuterClass.HelloResponse> responseObserver) {
			
		StringBuilder sb=new StringBuilder(50);
		return new StreamObserver<GreetingServiceOuterClass.HelloRequest>() {

			@Override
			public void onNext(HelloRequest value) {
				sb.append(value.getName());
				log.info("Received~ {} "+value);
			}

			@Override
			public void onError(Throwable t) {
				log.trace("onError~ {}",t);
				responseObserver.onCompleted();
			}

			@Override
			public void onCompleted() {
				
				GreetingServiceOuterClass.HelloResponse response = GreetingServiceOuterClass.HelloResponse.newBuilder()
						.setGreeting(sb.toString())
						.build();
				log.info("Sending~ {} ",response);
				responseObserver.onNext(response);
				responseObserver.onCompleted();
			}
		};
	}
}