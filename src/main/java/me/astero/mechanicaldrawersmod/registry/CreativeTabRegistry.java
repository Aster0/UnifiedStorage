package me.astero.mechanicaldrawersmod.registry;

import me.astero.mechanicaldrawersmod.utils.ModUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
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

    // Creates a creative tab with the id "examplemod:example_tab" for the example item, that is placed after the combat tab
    private static final RegistryObject<CreativeModeTab> MECHANICAL_DRAWER_TAB = CREATIVE_MODE_TABS.register(
            "mechanical_drawer", () -> CreativeModeTab.builder()
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(() -> Items.ACACIA_BUTTON.getDefaultInstance())
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
