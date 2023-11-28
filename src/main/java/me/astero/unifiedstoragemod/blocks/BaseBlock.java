package me.astero.unifiedstoragemod.blocks;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseBlock extends Block {

    private List<Component> shiftComponents = new ArrayList<>();
    public BaseBlock(Properties properties) {
        super(properties);
    }

    public abstract List<Component> addShiftText(ItemStack itemStack);



    @Override
    public void appendHoverText(ItemStack stack,
                                @Nullable BlockGetter getter, List<Component> components, TooltipFlag flag) {


        if(Screen.hasShiftDown()) {



            components.addAll(addShiftText(stack));



            return;
        }

        components.add(Component.translatable("lore.unifiedstorage.default"));

    }
}
