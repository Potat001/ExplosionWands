package com.explosion_wands.item_classes;

import com.explosion_wands.wands.TNTExplodingBlocksWand;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class TNTExplodingBlocksWandItem extends Item {
    public TNTExplodingBlocksWandItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult use(Level level, Player player, InteractionHand hand) {
        return TNTExplodingBlocksWand.use(level, player);
    }
}
