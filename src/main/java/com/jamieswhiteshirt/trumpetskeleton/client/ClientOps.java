package com.jamieswhiteshirt.trumpetskeleton.client;

import com.jamieswhiteshirt.trumpetskeleton.TrumpetSkeleton;
import com.jamieswhiteshirt.trumpetskeleton.register.Entities;
import com.jamieswhiteshirt.trumpetskeleton.register.Items;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = TrumpetSkeleton.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public class ClientOps {
	@SubscribeEvent
	public static void setupClient(final FMLClientSetupEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(
				Entities.TRUMPET_SKELETON_ENTITY.get(),
				SkeletonRenderer::new
		);
	}

	@SubscribeEvent
	public static void setupItemColours(final ColorHandlerEvent.Item event) {
		event.getItemColors().register(
				(itemColor, itemsIn) -> Items.TRUMPET_SKELETON_SPAWN_EGG.get().getColor(itemsIn),
				Items.TRUMPET_SKELETON_SPAWN_EGG.get()
		);
	}
}
