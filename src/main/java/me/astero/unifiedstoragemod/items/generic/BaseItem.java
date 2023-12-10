package me.astero.unifiedstoragemod.items.generic;

import me.astero.unifiedstoragemod.items.data.UpgradeType;
import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseItem extends Item {

    private List<Component> shiftComponents = new ArrayList<>();

    private UpgradeType upgradeType;
    private MutableComponent shiftText;

    public BaseItem(Properties properties, UpgradeType upgradeType, MutableComponent shiftText) {
        super(properties);
        this.upgradeType = upgradeType;
        this.shiftText = shiftText;
    }

    public List<Component> addShiftText() {


        return ModUtils.
                breakComponentLine(this.shiftText);

    }


    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {


        String upgradeTypeLore = "";
        Component upgradeTypeComponent = null;

        System.out.println(upgradeType);
        if(this.upgradeType == UpgradeType.UPGRADE) {
            upgradeTypeComponent = Component.translatable("lore.unifiedstorage.upgrade_card_item");
        }
        else if(this.upgradeType == UpgradeType.NETWORK) {
            upgradeTypeComponent = Component.translatable("lore.unifiedstorage.network_card_item");
        }

        if(upgradeTypeComponent == null) {
            upgradeTypeComponent = Component.literal("");
        }


        components.add(upgradeTypeComponent);



        if(Screen.hasShiftDown()) {

            components.addAll(addShiftText());

            return;
        }

        components.addAll(ModUtils.breakComponentLine(Component.translatable("lore.unifiedstorage.default")));


    }
}
