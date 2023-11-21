//package me.astero.unifiedstoragemod.items;
//
//import me.astero.unifiedstoragemod.Config;
//import me.astero.unifiedstoragemod.blocks.entity.DrawerGridControllerEntity;
//import me.astero.unifiedstoragemod.items.data.CustomBlockPosData;
//import me.astero.unifiedstoragemod.utils.ModUtils;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.network.chat.Component;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.world.InteractionHand;
//import net.minecraft.world.InteractionResult;
//import net.minecraft.world.InteractionResultHolder;
//import net.minecraft.world.MenuProvider;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.inventory.AbstractContainerMenu;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.context.UseOnContext;
//import net.minecraft.world.level.ClipContext;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.EnderChestBlock;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
//import net.minecraft.world.phys.BlockHitResult;
//
//import java.util.Arrays;
//
//public class WirelessStorage extends Item {
//
//
//    private CustomBlockPosData storageLocation;
//
//    public WirelessStorage(Properties properties) {
//        super(properties);
//    }
//
//    public void saveNbt(ItemStack stack, String key, String value) {
//
//        CompoundTag nbt = stack.getTag();
//
//        if(nbt == null)
//            nbt = new CompoundTag();
//
//
//        final String sValue = ModUtils.serializeBlockPosNbt(value);
//
//
//        CompoundTag innerNbt = new CompoundTag();
//        innerNbt.putString(key, sValue);
//
//
//
//
//        nbt.put(ModUtils.MODID, innerNbt);
//
//        stack.setTag(nbt);
//
//
//    }
//
//    public CustomBlockPosData loadNbt(ItemStack itemStack, String key) {
//
//        CompoundTag nbt = itemStack.getTag();
//        System.out.println("NBT " + nbt);
//
//
//
//        CustomBlockPosData customBlockPosData = null;
//
//        if(nbt != null) {
//            nbt = nbt.getCompound(ModUtils.MODID);
//
//            String[] eValue = nbt.getString(key).split(", ");
//
//            System.out.println(Arrays.asList(eValue));
//
//            customBlockPosData = ModUtils.convertStringToBlockData(eValue);
//
//
//
//
//        }
//
//
//
//
//
//
//
//        return customBlockPosData;
//
//
//
//
//    }
//
//
//    @Override
//    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
//
//        if(level.isClientSide()) {
//
//            ItemStack stack = player.getItemInHand(hand);
//
//
//
//            if(player.isCrouching()) {
//                BlockHitResult blockHitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
//
//                BlockEntity blockEntity = level.getBlockEntity(blockHitResult.getBlockPos());
//
//                if(blockEntity instanceof DrawerGridControllerEntity) {
//
//                    // link
//
//                    saveNbt(stack, "storage_location", blockEntity.getBlockPos().toString());
//                    // Send the action bar message to the player
//                    player.displayClientMessage(Component.translatable("language."
//                            + ModUtils.MODID + ".wireless_storage_linked"), true);
//
//
//                    return super.use(level, player, hand);
//                }
//            }
//
//            System.out.println(storageLocation == null);
//
//            if(storageLocation == null) {
//
//                System.out.println("then not going in?");
//                storageLocation = loadNbt(stack, "storage_location");
//
//                if(storageLocation == null) { // if still null
//                    player.displayClientMessage(Component.translatable("language."
//                            + ModUtils.MODID + ".wireless_storage_not_linked"), true);
//
//
//                    return super.use(level, player, hand);
//                }
//
//
//                // if linked
////                BlockEntity blockEntity = level.getBlockEntity(storageLocation.getBlockPos());
////
////                if(blockEntity instanceof DrawerGridControllerEntity drawerGridControllerEntity) {
////
////                    ServerPlayer serverPlayer = (ServerPlayer) level.getPlayerByUUID(player.getUUID());
////
////                    drawerGridControllerEntity.createMenu(serverPlayer.containerCounter,
////                            player.getInventory(), player);
////
////                }
//
//
//            }
//
//
//
//        }
//        else {
//
//
//
//
//                BlockEntity blockEntity = level.getBlockEntity(storageLocation.getBlockPos());
//
//                if(blockEntity instanceof DrawerGridControllerEntity drawerGridControllerEntity) {
//
//                    ServerPlayer serverPlayer = (ServerPlayer) level.getPlayerByUUID(player.getUUID());
//                    MenuProvider menuProvider = (MenuProvider) level.getBlockEntity(blockEntity.getBlockPos());
//
//                    serverPlayer.openMenu(menuProvider);
//
//                }
//
//        }
//
//        return super.use(level, player, hand);
//    }
//
//}
