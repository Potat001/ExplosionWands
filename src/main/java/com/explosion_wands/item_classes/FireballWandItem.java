package com.explosion_wands.item_classes;

import com.explosion_wands.wands.FireballWand;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class FireballWandItem extends Item {
    public FireballWandItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide()) {
            Projectile projectile = FireballWand.asFireballProjectile(level, player);
            level.addFreshEntity(projectile);
        }
        return FireballWand.use(this, level, player, hand);
    }
}
