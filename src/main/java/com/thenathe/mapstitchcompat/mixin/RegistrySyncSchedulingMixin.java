package com.thenathe.mapstitchcompat.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.thenathe.mapstitchcompat.ClientCapabilities;
import net.fabricmc.fabric.impl.registry.sync.RegistrySyncManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;

/** Requests Fabric's existing PLAY channel advertisement before registry map construction. */
@Mixin(value = RegistrySyncManager.class, remap = false)
public abstract class RegistrySyncSchedulingMixin {
    @WrapMethod(method = "configureClient")
    private static void mapstitchcompat$queryChannelsBeforeRegistrySync(
            ServerConfigurationPacketListenerImpl listener, MinecraftServer server,
            Operation<Void> original) {
        ClientCapabilities.beforeRegistrySync(listener, () -> original.call(listener, server));
    }
}
