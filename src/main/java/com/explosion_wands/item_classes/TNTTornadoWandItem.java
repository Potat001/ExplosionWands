package com.explosion_wands.item_classes;

import com.explosion_wands.wands.TNTTornadoWand;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

public class TNTTornadoWandItem extends Item {
    public TNTTornadoWandItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NonNull InteractionResult use(Level level, @NonNull Player player, @NonNull InteractionHand hand) {
        if (!level.isClientSide()) {
            PrimedTnt customTnt = TNTTornadoWand.asPrimedTnt(level, player);
            if (customTnt != null) {
                level.addFreshEntity(customTnt);
            }
        }
        return InteractionResult.SUCCESS;
    }
}
