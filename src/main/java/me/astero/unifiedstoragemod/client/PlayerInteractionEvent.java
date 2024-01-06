package me.astero.unifiedstoragemod.client;

import me.astero.unifiedstoragemod.registry.BlockRegistry;
import me.astero.unifiedstoragemod.renderer.items.wings.StorageWingsLayer;
import me.astero.unifiedstoragemod.utils.AsteroLogger;
import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = ModUtils.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PlayerInteractionEvent {

//    @SubscribeEvent
//    public static void onKeyInput(InputEvent.Key event) {
//
//
//        if(event.getKey() == GLFW.GLFW_KEY_SPACE) {
//
//            Player player = Minecraft.getInstance().player;
//
//            player.getAbilities().flying = true;
//        }
//
//    }


    @SubscribeEvent
    public static void blockColorHandlerEvent(final RegisterColorHandlersEvent.Block event) {

        System.out.println("Test tint");

        event.register((state, world, pos, tintIndex) -> {



            return tintIndex == 1 ? GrassColor.getDefaultColor() : -1;
        }, BlockRegistry.STORAGE_CONTROLLER_BLOCK.get());
    }


}
