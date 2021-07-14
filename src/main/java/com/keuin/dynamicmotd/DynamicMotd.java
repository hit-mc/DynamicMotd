package com.keuin.dynamicmotd;

import com.keuin.dynamicmotd.event.OnPlayerJoinedEvent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class DynamicMotd implements ModInitializer {
    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTING.register(server ->
                OnPlayerJoinedEvent.EVENT.register(new MotdSender(server)));
    }
}
