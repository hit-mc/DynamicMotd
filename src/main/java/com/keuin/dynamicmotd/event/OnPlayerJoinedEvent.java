package com.keuin.dynamicmotd.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;

public class OnPlayerJoinedEvent {

    public OnPlayerJoinedEvent() {
    }

    public static final Event<OnPlayerJoinedEventCallback> EVENT =
            EventFactory.createArrayBacked(OnPlayerJoinedEventCallback.class, callbacks -> player -> {
                for (OnPlayerJoinedEventCallback callback : callbacks) {
                    callback.onPlayerJoined(player);
                }
            });

    @FunctionalInterface
    public interface OnPlayerJoinedEventCallback {
        void onPlayerJoined(ServerPlayerEntity player);
    }
}
