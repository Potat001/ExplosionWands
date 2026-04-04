package com.explosion_wands.item_classes;

import com.explosion_wands.wands.TNTChickenWand;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class TNTChickenWandItem extends Item {
    public TNTChickenWandItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide()) {
            PrimedTnt customTnt = TNTChickenWand.asPrimedTnt(level, player);
            if (customTnt != null) {
                level.addFreshEntity(customTnt);
            }
        }
        return InteractionResult.SUCCESS;
    }
}
