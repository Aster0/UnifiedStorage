package me.astero.unifiedstoragemod.client;

import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = ModUtils.MODID, value = Dist.CLIENT)
public class PlayerInteractionEvent {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {

//
//        if(event.getKey() == GLFW.GLFW_KEY_SPACE) {
//
//            Player player = Minecraft.getInstance().player;
//
//            player.getAbilities().flying = true;
//        }

    }


}
