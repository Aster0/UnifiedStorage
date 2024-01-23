package me.astero.unifiedstoragemod.client.events;

import me.astero.unifiedstoragemod.blocks.StorageControllerBlock;
import me.astero.unifiedstoragemod.registry.BlockRegistry;
import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = ModUtils.MODID, value = Dist.CLIENT)
public class ClientEvents {

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


//    @SubscribeEvent
//    public static void registerBlockColors(final RegisterColorHandlersEvent.Block event) {
//
//        System.out.println("register");
//
//        event.register((state, world, pos, tintIndex) -> {
//
//
//
//
//            return tintIndex == 1 ? state.getValue(StorageControllerBlock.COLOR).getFireworkColor() : -1;
//        }, BlockRegistry.STORAGE_CONTROLLER_BLOCK.get());
//    }

    @SubscribeEvent
    public static void registerBlockColors(PlayerEvent.ItemCraftedEvent event) {

        System.out.println("register " + event.getCrafting() + " " + event.getInventory());

    }



}
