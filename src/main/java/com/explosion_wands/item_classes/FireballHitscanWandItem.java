package com.explosion_wands.item_classes;

import com.explosion_wands.wands.FireballHitscanWand;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

public class FireballHitscanWandItem extends Item {
    public FireballHitscanWandItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NonNull InteractionResult use(Level level, @NonNull Player player, @NonNull InteractionHand hand) {
        if (!level.isClientSide()) {
            Projectile projectile = FireballHitscanWand.asFireballProjectile(level, player);
            if (projectile != null) {
                level.addFreshEntity(projectile);
            }
        }
        return InteractionResult.SUCCESS;
    }
}
