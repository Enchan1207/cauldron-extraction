package me.enchan.cauldronextraction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.CauldronBlock;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPointer;

public class CauldronExtractionMod implements ModInitializer {
    public static final String ModId = "cauldron-extraction";

    public static final Logger Logger = LoggerFactory.getLogger(ModId);

    @Override
    public void onInitialize() {
        var targetItem = Items.BUCKET;
        var defaultBehavior = DispenserBlock.BEHAVIORS.get(targetItem);

        DispenserBlock.registerBehavior(targetItem, new ItemDispenserBehavior() {
            @Override
            protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                var world = pointer.world();
                var facing = pointer.state().get(DispenserBlock.FACING);
                var frontPos = pointer.pos().offset(facing);
                var state = world.getBlockState(frontPos);

                if (!(state.getBlock() instanceof CauldronBlock)) {
                    return defaultBehavior.dispense(pointer, stack);
                }

                Logger.info("me at the cauldron");

                return stack;
            }
        });

    }
}
