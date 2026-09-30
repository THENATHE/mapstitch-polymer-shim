package com.thenathe.mapstitchcompat.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.thenathe.mapstitchcompat.WireRegistries;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(MappedRegistry.class)
public abstract class RegistryWireIdsMixin<T> {
    @Shadow @Final private ResourceKey<? extends Registry<T>> key;

    @ModifyReturnValue(method = "getId", at = @At("RETURN"))
    private int mapstitchcompat$outgoingId(int raw) {
        return WireRegistries.writing() ? WireRegistries.toWire(key.identifier(), raw, PacketContext.get()) : raw;
    }

    @ModifyVariable(method = {"byId", "get(I)Ljava/util/Optional;"}, at = @At("HEAD"), argsOnly = true)
    private int mapstitchcompat$incomingId(int wire) {
        return WireRegistries.reading() ? WireRegistries.toServer(key.identifier(), wire, PacketContext.get()) : wire;
    }
}
