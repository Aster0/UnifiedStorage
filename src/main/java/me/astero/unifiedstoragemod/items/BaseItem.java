package me.astero.unifiedstoragemod.items;

import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseItem extends Item {

    private List<Component> shiftComponents = new ArrayList<>();
    public BaseItem(Properties properties) {
        super(properties);
    }

    public abstract List<Component> addShiftText(ItemStack itemStack);

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {



        if(Screen.hasShiftDown()) {

            components.addAll(addShiftText(stack));

            return;
        }

        components.addAll(ModUtils.breakComponentLine(Component.translatable("lore.unifiedstorage.default")));


    }
}
