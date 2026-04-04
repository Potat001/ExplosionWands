package com.explosion_wands.item_classes;

import com.explosion_wands.wands.FireballHitscanWand;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class FireballHitscanWandItem extends Item {
    public FireballHitscanWandItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide()) {
            Projectile projectile = FireballHitscanWand.asFireballProjectile(level, player);
            if (projectile != null) {
                level.addFreshEntity(projectile);
            }
        }
        return InteractionResult.SUCCESS;
    }
}
