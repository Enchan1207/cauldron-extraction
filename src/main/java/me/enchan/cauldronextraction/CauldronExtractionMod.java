package me.enchan.cauldronextraction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPointer;

public class CauldronExtractionMod implements ModInitializer {
    public static final String MOD_ID = "cauldron-extraction";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        var targetItem = Items.BUCKET;
        var defaultBehavior = DispenserBlock.BEHAVIORS.get(targetItem);

        DispenserBlock.registerBehavior(targetItem, new ItemDispenserBehavior() {
            @Override
            protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                var result = CauldronExtractor
                        .extractFromCauldron(pointer, stack)
                        .orElseGet(() -> defaultBehavior.dispense(pointer, stack));

                return result;
            }
        });
    }
}
