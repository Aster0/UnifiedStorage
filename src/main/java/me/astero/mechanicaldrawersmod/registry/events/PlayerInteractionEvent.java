package me.astero.mechanicaldrawersmod.registry.events;

import me.astero.mechanicaldrawersmod.registry.blocks.entity.DrawerBlockEntity;
import me.astero.mechanicaldrawersmod.utils.AsteroLogger;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class PlayerInteractionEvent {





    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent e) {
        AsteroLogger.info("ww");










    }


}
