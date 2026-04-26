package me.enchan.cauldronextraction;

import java.util.List;
import java.util.Optional;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPointer;

public class CauldronExtractor {
    private static final List<CauldronBehaviorEntry> CAULDRON_BEHAVIORS = List.of(
            new CauldronBehaviorEntry(Blocks.WATER_CAULDRON, Items.WATER_BUCKET),
            new CauldronBehaviorEntry(Blocks.LAVA_CAULDRON, Items.LAVA_BUCKET),
            new CauldronBehaviorEntry(Blocks.POWDER_SNOW_CAULDRON, Items.POWDER_SNOW_BUCKET));

    public static Optional<ItemStack> extractFromCauldron(BlockPointer pointer, ItemStack stack) {
        var world = pointer.world();
        var facing = pointer.state().get(DispenserBlock.FACING);
        var pos = pointer.pos().offset(facing);
        var state = world.getBlockState(pos);

        var result = CAULDRON_BEHAVIORS.stream().filter(behavior -> state.isOf(behavior.target()))
                .findFirst().map(behavior -> {
                    world.setBlockState(pos, Blocks.CAULDRON.getDefaultState());
                    stack.decrement(1);

                    var returnItem = new ItemStack(behavior.result(), 1);

                    if (stack.isEmpty()) {
                        return new ItemStack(behavior.result(), 1);
                    }

                    // 同種のアイテムが複数存在する場合、結果として得られるブロックを次の空きスロットに詰め込む
                    var addedSlot = pointer.blockEntity().addToFirstFreeSlot(returnItem);
                    if (addedSlot < 0) {
                        Block.dropStack(world, pointer.pos(), returnItem);
                    }

                    return stack;
                });

        return result;
    }
}
