package com.explosion_wands.item_classes;

import com.explosion_wands.wands.FireballShotgunWand;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class FireballShotgunWandItem extends Item {
    public FireballShotgunWandItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            Projectile projectile = FireballShotgunWand.asFireballProjectile(level, player);
            if (projectile != null) {
                level.addFreshEntity((Entity) projectile);
            }
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemStack);
    }
}
