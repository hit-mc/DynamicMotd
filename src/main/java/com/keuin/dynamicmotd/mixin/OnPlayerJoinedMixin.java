package com.keuin.dynamicmotd.mixin;

import com.keuin.dynamicmotd.event.OnPlayerJoinedEvent;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class OnPlayerJoinedMixin {
	@Inject(at = @At("TAIL"), method = "onPlayerConnect")
	private void init(ClientConnection connection, ServerPlayerEntity player, CallbackInfo info) {
		OnPlayerJoinedEvent.EVENT.invoker().onPlayerJoined(player);
	}
}
