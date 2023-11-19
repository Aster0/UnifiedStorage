package me.astero.mechanicaldrawersmod.client;

import me.astero.mechanicaldrawersmod.registry.BlockEntityRegistry;
import me.astero.mechanicaldrawersmod.renderer.DrawerItemRenderer;
import me.astero.mechanicaldrawersmod.utils.AsteroLogger;
import me.astero.mechanicaldrawersmod.utils.ModUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ModUtils.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class PlayerInteractionEvent {




//
//    @SubscribeEvent
//    public static void onRightClick(PlayerInteractEvent e) {
//        AsteroLogger.info("ww");
//
//
//
//
//
//
//
//
//
//
//    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {


        event.registerBlockEntityRenderer(BlockEntityRegistry.DRAWER_BLOCK_ENTITY.get(),
                DrawerItemRenderer::new);

        AsteroLogger.info("RENDER LOADED");
    }



}
