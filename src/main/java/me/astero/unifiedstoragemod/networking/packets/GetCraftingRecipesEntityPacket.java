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





    private ItemStack itemStack, itemStackToStore;
    private int slot;
    private boolean populateCraftingSlot;


    public GetCraftingRecipesEntityPacket(ItemStack itemStack, int slot, boolean populateCraftingSlot, ItemStack itemStackToStore) {

        this.itemStack = itemStack;
        this.slot = slot;
        this.populateCraftingSlot = populateCraftingSlot;
        this.itemStackToStore = itemStackToStore;


    }

    public GetCraftingRecipesEntityPacket(FriendlyByteBuf buffer) {

        // Decode your packet data here
        this.itemStack = buffer.readItem();
        this.slot = buffer.readInt();
        this.populateCraftingSlot = buffer.readBoolean();
        this.itemStackToStore = buffer.readItem();



    }

    @Override
    public void encode(FriendlyByteBuf buffer) {

        buffer.writeItemStack(this.itemStack, false);
        buffer.writeInt(this.slot);
        buffer.writeBoolean(this.populateCraftingSlot);
        buffer.writeItemStack(this.itemStackToStore, false);


    }







    public static void handle(GetCraftingRecipesEntityPacket packet, CustomPayloadEvent.Context context) {


        context.enqueueWork(() -> {




            if (!context.isClientSide()) {


                ServerPlayer player = context.getSender();


                if(player.containerMenu instanceof StorageControllerMenu menu) {



                    if(packet.populateCraftingSlot) {
                        // try to store first whatever is in the crafting slot
                        if(!menu.canInsertItemIntoInventory(packet.itemStackToStore,
                                packet.itemStackToStore.getCount(), packet.slot)) {

                            return; // if we cannot store, we shouldn't move forward to putting a new item on the grid.
                        }



                        // check if enough
                        if(menu.canRemoveItemFromInventory(packet.itemStack, true, true, 1)) {
                            menu.populateCraftSlots(packet.itemStack, packet.slot);
                        }


                        return; // because SlotsChanged will trigger the recipe change
                    }


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
