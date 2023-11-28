package me.astero.unifiedstoragemod.networking.packets;

import me.astero.unifiedstoragemod.menu.StorageControllerMenu;
import me.astero.unifiedstoragemod.networking.ModNetwork;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.event.network.CustomPayloadEvent;

import java.util.Arrays;
import java.util.Optional;

public class GetCraftingRecipesEntityPacket implements EntityPacket {







    public GetCraftingRecipesEntityPacket() {



    }

    public GetCraftingRecipesEntityPacket(FriendlyByteBuf buffer) {

        // Decode your packet data here


    }

    @Override
    public void encode(FriendlyByteBuf buffer) {


    }







    public static void handle(GetCraftingRecipesEntityPacket packet, CustomPayloadEvent.Context context) {


        context.enqueueWork(() -> {




            if (!context.isClientSide()) {


                ServerPlayer player = context.getSender();


                if(player.containerMenu instanceof StorageControllerMenu menu) {



                    Optional<RecipeHolder<CraftingRecipe>> optional =
                            player.level().getServer().getRecipeManager()
                                    .getRecipeFor(RecipeType.CRAFTING, menu.craftSlots, player.level());


                    changeCraftingResult(ItemStack.EMPTY, menu, player);

                    RecipeHolder<CraftingRecipe> recipeHolder = optional.get();
                    CraftingRecipe craftingRecipe = recipeHolder.value();
                    ItemStack result = craftingRecipe.assemble(menu.craftSlots, player.level().registryAccess());





                    changeCraftingResult(result, menu, player);



                }




            }

        });

        context.setPacketHandled(true);
    }

    private static void changeCraftingResult(ItemStack stack, StorageControllerMenu menu, ServerPlayer player) {
        ModNetwork.sendToClient(new SendCraftingResultEntityPacket(stack), player);

        menu.changeCraftingResultSlot(stack); // server side
    }
}
