package com.yourname.emptyingaxe;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EmptyingAxeItem extends AxeItem {
    public EmptyingAxeItem(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext ctx) {
        World world = ctx.getWorld();
        if (world.isClient()) return ActionResult.SUCCESS;

        var player = ctx.getPlayer();
        if (player == null || !player.isSneaking()) return ActionResult.PASS;

        BlockPos pos = ctx.getBlockPos();
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        boolean emptied = false;

        if (block instanceof ChestBlock chestBlock) {
            Inventory inv = ChestBlock.getInventory(chestBlock, state, world, pos, false);
            if (inv != null) {
                clearInventory(inv);
                emptied = true;
            }
        } else {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof ChestBlockEntity chestBe) { clearInventory(chestBe); emptied = true; }
            else if (be instanceof BarrelBlockEntity barrelBe) { clearInventory(barrelBe); emptied = true; }
            else if (be instanceof ShulkerBoxBlockEntity shulkerBe) { clearInventory(shulkerBe); emptied = true; }
        }

        if (emptied) {
            world.playSound(null, pos, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.6f, 1.2f);
            player.getItemCooldownManager().set(this, 10);
            player.sendMessage(Text.literal("Container emptied."), true);
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    private void clearInventory(Inventory inv) {
        for (int i = 0; i < inv.size(); i++) inv.setStack(i, ItemStack.EMPTY);
    }
}
