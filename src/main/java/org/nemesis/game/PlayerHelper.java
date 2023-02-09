package org.nemesis.game;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.karlz.exchange.Corresponding;
import com.karlz.exchange.ExchangeHelper.DispatchHelper;
import com.karlz.exchange.Observable;
import com.karlz.grpc.EntriesReply;

public class PlayerHelper implements DispatchHelper<Observable, EntriesReply> {
    private final HashMap<String, Map<String, Observable>> flatMap = new HashMap<>();

    @Override
    public Map<String, Map<String, Observable>> flatMap() {
        return flatMap;
    }

    @SuppressWarnings("unchecked")
    @Override
    public EntriesReply associated() {
        EntriesReply.Builder builder = EntriesReply.newBuilder();
        flatMap().forEach((k, v) -> builder
                .addAllObservables((Collection<com.karlz.grpc.entity.Observable>) (Collection<?>) Corresponding
                        .transform(v.values())));
        return builder.build();
    }
}