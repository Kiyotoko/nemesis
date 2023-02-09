package org.nemesis.grpc;

import java.util.HashMap;
import java.util.Map;

import com.karlz.entity.Creator;
import com.karlz.exchange.ExchangeHelper.StoreHelper;
import com.karlz.exchange.Reference;
import com.karlz.grpc.exchange.HostingGrpc;
import com.karlz.grpc.exchange.HostingGrpc.HostingBlockingStub;
import com.karlz.grpc.game.ChangeRequest;
import com.karlz.grpc.game.NemesisGrpc;
import com.karlz.grpc.game.NemesisGrpc.NemesisBlockingStub;
import com.karlz.grpc.game.Party;
import com.karlz.grpc.game.StatusRequest;
import com.karlz.grpc.game.StatusResponse;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class NemesisClient {
    private final ManagedChannel channel;
    private final NemesisBlockingStub nemesisBlockingStub;
    private final HostingBlockingStub hostingBlockingStub;

    public NemesisClient(String name, int port) {
        channel = ManagedChannelBuilder.forAddress(name, port).build();
        nemesisBlockingStub = NemesisGrpc.newBlockingStub(channel);
        hostingBlockingStub = HostingGrpc.newBlockingStub(channel);
    }

    private final StoreHelper<Reference<?>, StatusResponse> helper = new StoreHelper<Reference<?>, StatusResponse>() {
        private final HashMap<String, Creator<Reference<?>>> creatorsMap = new HashMap<>();
        private final HashMap<String, Map<String, Reference<?>>> flatMap = new HashMap<>();

        @Override
        public Map<String, Creator<Reference<?>>> creatorsMap() {
            return creatorsMap;
        }

        @Override
        public Map<String, Map<String, Reference<?>>> flatMap() {
            return flatMap;
        }

        @Override
        public void update(StatusResponse reference) {
            for (Party observable : reference.getPartiesList()) {
                save(observable.getSuper().getType(), observable.getSuper().getId());
            }
        }
    };

    public void status() {
        nemesisBlockingStub.status(StatusRequest.newBuilder().build());
    }

    public void command() {
        nemesisBlockingStub.change(ChangeRequest.newBuilder().build());
    }

    public StoreHelper<Reference<?>, StatusResponse> getHelper() {
        return helper;
    }
}
