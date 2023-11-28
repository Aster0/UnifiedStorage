package me.astero.unifiedstoragemod.registry;

import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CreativeTabRegistry {


    private static final List<Supplier<? extends Item>> TAB_ITEMS = new ArrayList<>();
    private static final List<Supplier<? extends Item>> TAB_BLOCK_ITEMS = new ArrayList<>();
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(
            Registries.CREATIVE_MODE_TAB, ModUtils.MODID);

    private static final RegistryObject<CreativeModeTab> UNIFIED_STORAGE_TAB = CREATIVE_MODE_TABS.register(
            "unified_storage", () -> CreativeModeTab.builder()
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .title(Component.translatable("creative." + ModUtils.MODID + ".tabName"))
                    .icon(() -> new ItemStack(ItemRegistry.NETWORK_CARD.get()))
                    .displayItems((parameters, output) -> {

                        for(Supplier<? extends Item> supplier : TAB_ITEMS) {
                            Item item = supplier.get();
                            output.accept(item);

                        }



                    }).build());



    public static <T extends Item> RegistryObject<T> addToCreativeTab(RegistryObject<T> itemLike) {


        TAB_ITEMS.add(itemLike);
        return itemLike;

    }


}
