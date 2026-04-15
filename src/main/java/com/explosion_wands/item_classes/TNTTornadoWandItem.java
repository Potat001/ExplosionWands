package com.explosion_wands.item_classes;

import com.explosion_wands.wands.TNTTornadoWand;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class TNTTornadoWandItem extends Item {
    public TNTTornadoWandItem(Properties properties) {
        super(properties);
    }
    /*
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            PrimedTnt customTnt = TNTTornadoWand.asPrimedTnt(level, player);
            if (customTnt != null) {
                level.addFreshEntity(customTnt);
            }
        }
        return InteractionResultHolder.success(itemStack);
    }
     */
}
