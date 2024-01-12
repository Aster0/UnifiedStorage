package me.astero.unifiedstoragemod.integrations.jei.recipes;

import com.mojang.blaze3d.vertex.PoseStack;
import me.astero.unifiedstoragemod.recipes.StorageControllerColoredRecipe;
import me.astero.unifiedstoragemod.registry.BlockRegistry;
import me.astero.unifiedstoragemod.utils.ModUtils;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingRecipe;

import java.util.ArrayList;
import java.util.List;

public class StorageControllerColorCategory implements IRecipeCategory<StorageControllerColoredRecipe> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(ModUtils.MODID, "textures/gui/storage_controller_dye_recipe.png");
    private final IDrawable controllerIcon, background;

    public static final RecipeType<StorageControllerColoredRecipe>
            RECIPE_TYPE = RecipeType.create(ModUtils.MODID, "storage_controller_colors",
            StorageControllerColoredRecipe.class);



    public StorageControllerColorCategory(IGuiHelper guiHelper) {


        this.controllerIcon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new
                ItemStack(BlockRegistry.STORAGE_CONTROLLER_BLOCK_COLORED.get(DyeColor.WHITE).get()));
        this.background = guiHelper.createDrawable(TEXTURE, 0, 0, 150, 71);


    }

    public static List<StorageControllerColoredRecipe> getRecipes() {

        List<StorageControllerColoredRecipe> recipes = new ArrayList<>();

        BlockRegistry.STORAGE_CONTROLLER_BLOCK_COLORED.forEach((key, value) -> {
            recipes.add(new StorageControllerColoredRecipe(DyeItem.byColor(key),
                    value.get()));

        });


        return recipes;
    }


    @Override
    public RecipeType<StorageControllerColoredRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }


    @Override
    public Component getTitle() {
        return Component.literal("Dyeing Controllers");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.controllerIcon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, StorageControllerColoredRecipe recipe, IFocusGroup focuses) {

        builder.addSlot(RecipeIngredientRole.INPUT, 27, 9).addItemStack(recipe.input().getDefaultInstance());
        builder.addSlot(RecipeIngredientRole.INPUT, 61, 25).addItemStack(BlockRegistry.STORAGE_CONTROLLER_BLOCK_COLORED
                .get(DyeColor.WHITE).get().asItem().getDefaultInstance());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 107, 25).addItemStack(recipe.output().asItem().getDefaultInstance());
    }

    @Override
    public void draw(StorageControllerColoredRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {

        Font font = Minecraft.getInstance().font;

        PoseStack poseStack = guiGraphics.pose();

        poseStack.pushPose();

        poseStack.scale(0.5f, 0.5f, 0.7f);
        poseStack.translate(42, 72, 0);

        guiGraphics.drawString(font, Component.translatable("gui." + ModUtils.MODID + ".right_click.lang"),
                0, 0, 0xFF404646, false);

        poseStack.popPose();

        poseStack.pushPose();

        poseStack.translate(35, 50, 0);
        poseStack.scale(0.8f, 0.8f, 0.8f);


        for(Component component : ModUtils.breakComponentLine(Component.translatable("jei." + ModUtils.MODID + ".storage_color_recipe_instruction"))) { {



            guiGraphics.drawString(font, component,
                    0, 0, 0xFF404646, false);

            poseStack.translate(0, 10, 0);


        }}



        poseStack.popPose();

    }
}
