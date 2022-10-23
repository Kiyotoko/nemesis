package org.nemesis.grpc;

import java.util.Base64;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import io.grpc.ConnectivityState;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class RemoteClient {
	private static final Logger logger = Logger.getLogger(RemoteClient.class.getName());

	private final ManagedChannel channel;
//	private final RemoteControlBlockingStub blockingStub;

	public RemoteClient() {
		channel = ManagedChannelBuilder.forAddress("localhost", 7777).usePlaintext().build();
//		blockingStub = RemoteControlGrpc.newBlockingStub(channel);
		logger.info("Client started");
	}

	public void stop() throws Exception {
		if (channel != null) {
			channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
			logger.info("Client shutdown");
		}
	}

	public boolean isRunning() {
		ConnectivityState state = channel.getState(true);
		if (state == ConnectivityState.IDLE) {
			return false;
		}
		if (state == ConnectivityState.TRANSIENT_FAILURE | state == ConnectivityState.SHUTDOWN) {
			if (!channel.isShutdown())
				try {
					stop();
				} catch (Exception e) {
					e.printStackTrace();
				}
			return false;
		}
		return true;
	}

	public String generateToken(String username) {
		return Base64.getEncoder().encodeToString(username.getBytes());
	}
}
