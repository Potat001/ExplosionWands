package com.explosion_wands.customFunctions;

import com.explosion_wands.tick.TickQueue;
import com.explosion_wands.tick.TickQueueManager;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class CustomTnt extends PrimedTnt {

    public CustomTnt(EntityType<? extends CustomTnt> type, Level level) {
        super(type, level);
    }

    //Ability to separate the values for the explosion power of TNTs for different classes
    float explosionPower = 4.0F; //Default: 4.0F
    double defaultGravity = 0.04; //Default: 0.04

    //CUSTOM-MADE

    //onGround makes the primedTNT only explode when it hits a horizontal surface, not a vertical surface
    boolean explodeOnContact = false;
    //Type of entity that will spawn after explosion
    EntityType<?> entityToSpawn = EntityType.CHICKEN;
    //Amount of entities spawned after explosion
    int entityAmount = 20;
    //If entities will spawn after explosion, before the primed TNT is discarded
    boolean entitySpawnAfterExplosion = false;
    //If the shape of the spawned entities resemble a circle
    boolean isCircle = true;
    boolean isTornado = false;
    boolean killEntitiesAfterLoop = true;
    //Changing x-direction
    double xChange = 0;
    //Changing y-direction
    double yChange = 0;
    //Changing z-direction
    double zChange = 0;
    double yIncrement = 0;
    //Amplitude of the circle
    double amplitude = 10;
    //How long the delay for the after spawn effects are after the primed TNT is first discarded
    int initialExplosionDelayCounter = 40; //Ticks
    //How long the delay for the spawn effects are after the initial delay are
    //Whether the after spawn effects should explode each entity individually or every entity at once
    boolean individualEntityExplosions = true;
    //Whether the TNT should be discarded after its first use (explosion) or not
    boolean discardTNT = true;
    //If the TNT has exploded
    boolean exploded = false;
    //Decides if the entities spawned after the explosion will happen instantaneously or not
    boolean gradualEntitySpawnAfterExplosion = true;
    //The frequency of explosions between the last and second-to-last primedTNT
    int lastExplosionTick = -1;
    //Per primedTNT
    int explosionAmount = 0;
    //List<runnable>: List that stores tasks (pieces of code to run later)
    //Runnable: Functional interface with the method run()
    //Essentially stores the code in a QUEUE to be used later in onPostEntitySpawning() when !QUEUE.isEmpty
    private final List<Runnable> QUEUE = new ArrayList<>();
    //Amount of ticks since the primed TNT first exploded
    //Helper method, won't have to write QUEUE.add(() -> { "code" });
    public void add(Runnable task) {
        QUEUE.add(task);
    }

    @Override
    public void tick() {
        //Inherits logic from tick(), where we only override what's specified under. Otherwise, we have to put *all* the logic that tick() uses here
        super.tick();
        if(shouldExplode()) {
            discardOnFirstUse();
            explode();
            onPostExplode();
        }
    }

    //If the primed TNT should explode given *these* conditions
    protected boolean shouldExplode() {

        return ((getFuse() <= 0 && !level().isClientSide())
                || ((this.horizontalCollision || this.verticalCollision)
                || hitEntity()
                && explodeOnContact));
    }

    //Determines if the customTnt has made contact with an entity's bounding box
    protected boolean hitEntity() {
        double dirX = this.getX();
        double dirY = this.getY();
        double dirZ = this.getZ();
        int reachEntities = 128;
        int inflate = 100;
        Vec3 playerLookDir = this.getLookAngle();
        playerLookDir.add(dirX, dirY, dirZ).normalize();
        Vec3 customTntPos = new Vec3(getX(), getY(), getZ());
        EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(
                this,
                customTntPos,
                customTntPos,
                this.getBoundingBox().expandTowards(playerLookDir.scale(reachEntities)).inflate(inflate),
                entity -> entity instanceof Entity
                && entity.isAlive()
                && !entity.isRemoved()
                && entity != this,
                0);
        return entityHitResult != null;
    }

    //Responsible for exploding the TNT at its current position
    protected void explode() {
        if(level() instanceof ServerLevel serverLevel && !entitySpawnAfterExplosion) {
            level().explode(
                    this,
                    getX(),
                    getY(),
                    getZ(),
                    explosionPower,
                    Level.ExplosionInteraction.TNT
            );
            if(entityToSpawn == null) {
                serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, getX(), getY(), getZ(), 700, 3, 3, 3, 0.2);
            }
            if(!discardTNT) {
                serverLevel.sendParticles(ParticleTypes.FLAME, getX(), getY() - 1, getZ(), 700, 1.5, 1.5, 1.5, 0.1);
            }

            exploded = true;
        }
    }

    //What happens after the explosion(s) occurs
    protected void onPostExplode() {
        double angleStep;
        if(isTornado) {
            angleStep = Math.PI / ((double) entityAmount / 4);
        } else {
            angleStep = Math.PI / ((double) entityAmount / 2);
        }
        int amountPerTick;
        if(!gradualEntitySpawnAfterExplosion) {
            amountPerTick = 4;
        } else {
            amountPerTick = entityAmount;
        }
        TickQueue queue = TickQueueManager.createQueue(entityAmount, amountPerTick);
        List<Entity> spawnedEntities = new ArrayList<>();
        final double[] angle = {Math.toRadians(0)};
        final double[] changePosition = {0};
        final double[] changeX = {getXChange()};
        final double[] changeY = {getYChange()};
        final double[] incrementY = {getYIncrement()};
        final double[] changeZ = {getZChange()};
        if(entityToSpawn != null && level() instanceof ServerLevel server && entitySpawnAfterExplosion) {
            for (int i = 0; i < entityAmount; i++) {
                Entity entity = entityToSpawn.create(server, EntitySpawnReason.TRIGGERED);
                int finalI = i;
                queue.add(() -> {
                    if(entity != null) {
                        if(isCircle) {
                                entity.setPos(
                                        getX() + changeX[0] + (Math.cos(angle[0]) * amplitude),
                                        getY() + changeY[0] + incrementY[0],
                                        getZ() + changeZ[0] + (Math.sin(angle[0]) * amplitude));
                            //Don't really need the player changing the x and z values for the spawning of entities, since
                            //it offsets the spawn point for the entities. The y value only changes the height of the
                            //spawned entities, so it's useful to have
                            changeX[0] = xChange;
                            changeY[0] = yChange;
                            incrementY[0] += yIncrement;
                            changeZ[0] = zChange;
                            angle[0] += angleStep;
                            changePosition[0] += Math.PI / ((double) (entityAmount / 4) / 2);
                            //The shape is no longer hardcoded to be a circle, so the player can have
                            //a lot more options for how they want the entity spawn positions to look
                        } else {
                            entity.setPos(
                                    getX() + changeX[0],
                                    getY() + changeY[0],
                                    getZ() + changeZ[0]);
                            changeX[0] += xChange;
                            changeY[0] += yChange;
                            changeZ[0] += zChange;
                        }
                        server.addFreshEntity(entity);
                        if(entityToSpawn != EntityType.TNT) {
                            //Used in to apply the LivingEntityMixin logic to this entity only, instead of globally to all entities of this type
                            entity.addTag("no_drops");
                        }
                        //Adds the spawned entities to a list so we are able to use them later outside the loop
                        spawnedEntities.add(entity);
                        entity.setNoGravity(!isTornado);
                        if((finalI % 6) == 1) {
                            //Performance improvement: Spawns a particle effect on each entity that satisfy the modulus criteria instead of on each entity
                            server.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, entity.getX(), entity.getY(), entity.getZ(), 50, 2, 2, 2, 0.8);
                        }
                    }
                });
            }
            if(!gradualEntitySpawnAfterExplosion && killEntitiesAfterLoop) {
                queue.onComplete(() -> {
                    int spawnedEntitiesNumber = 0;
                    //Iterates through every entity from the list
                    for (Entity e : spawnedEntities) {
                        if(e.isAlive()) {
                            //Currently kills all entities instantly
                            e.kill(server);
                            if((spawnedEntitiesNumber % 4) == 1) {
                                server.sendParticles(ParticleTypes.FLAME, e.getX(), e.getY(), e.getZ(), 100, 3, 3, 3, 0.4);
                            }
                            spawnedEntitiesNumber++;
                        }
                    }
                });
            }
        }
    }

    protected void discardOnFirstUse() {
        if (level() instanceof ServerLevel serverLevel) {
            int currentTick = (int) level().getGameTime();
            int ticksSinceLastExplosion;
            //Checks the time between the primedTNT explosions
            if (lastExplosionTick == -1 && explosionAmount == 0) {
                ticksSinceLastExplosion = 20;
            } else {
                ticksSinceLastExplosion = currentTick - lastExplosionTick;
            }
            explosionAmount++;
            if (exploded) {
                setDefaultGravity(0.30);
                lastExplosionTick = (int) level().getGameTime();
            }
            //Failsafe if the time between the primedTNT explosions is less than or equal to 1, which prevents the primedTNT
            //from continuously exploding in one spot, aka when its effective explosion power is less than the block it's standing on
            if (discardTNT) {
                this.discard();
            }
            if(ticksSinceLastExplosion <= 1 || explosionAmount >= 200) {
                    this.discard();
                serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, getX(), getY() + 2, getZ(), 2000, 4, 4, 4, 0.1);
            }
            //Debugging
            //System.out.println("Last explosion tick: " + ticksSinceLastExplosion);
            //System.out.println("Explosion amount: " + explosionAmount);
        }
    }

    //Getters and setters
    protected float getExplosionPower() {
        return explosionPower;
    }

    public void setExplosionPower(float power) {
        this.explosionPower = power;
    }

    //How strong the gravity of the primed TNT is
    @Override
    protected double getDefaultGravity() {
        return defaultGravity;
    }

    public void setDefaultGravity(double gravity) {
        this.defaultGravity = gravity;
    }

    //Makes the TNT explode on contact
    protected boolean getExplodeOnContact() {
        return explodeOnContact;
    }

    public void setExplodeOnContact(boolean contactExplode) {
        this.explodeOnContact = contactExplode;
    }

    //If entities will spawn after the explosion
    public boolean getEntitySpawnAfterExplosion() {
        return entitySpawnAfterExplosion;
    }

    public void setEntitySpawnAfterExplosion(boolean entitiesSpawn) {
        this.entitySpawnAfterExplosion = entitiesSpawn;
    }

    //Entity that is spawned after the explosion
    public void setEntityToSpawn(final EntityType<?> entityType) {
        this.entityToSpawn = entityType;
    }

    public EntityType<?> getEntityToSpawn() {
        return entityToSpawn;
    }

    //Entity spawn amount
    public int getEntityAmount() {
        return entityAmount;
    }

    public void setEntityAmount(int amount) {
        this.entityAmount = amount;
    }

    //If the shape of the mobs spawning is a circle or not
    public boolean getCircle() {
        return isCircle;
    }

    public void setCircle(boolean circle) {
        this.isCircle = circle;
    }

    //If it's a circle: Get/set the amplitude of the circle
    public double getAmplitude() {
        return this.amplitude;
    }

    public void setAmplitude(double amp) {
        this.amplitude = amp;
    }

    //If not circle: change the repeating x direction values
    public double getXChange() {
        return xChange;
    }

    public void setXChange(double x) {
        this.xChange = x;
    }

    //Change the repeating Y direction values
    public double getYChange() {
        return yChange;
    }

    public void setYChange(double y) {
        this.yChange = y;
    }
    //How much Y increments by every loop
    public double getYIncrement() {
        return yIncrement;
    }

    public void setYIncrement(double y) {
        this.yIncrement = y;
    }

    //Change the repeating z direction values
    public double getZChange() {
        return zChange;
    }

    //Change the repeating Z direction values
    public void setZChange(double z) {
        this.zChange = z;
    }

    //If the primed tnt should be discarded when it first explodes (first use). If yes, continues exploding
    //after it's first time use, only discarded when the fuse timer = 0. If no, explodes once, is discarded afterward.
    public boolean getDiscardOnFirstUse() {
        return this.discardTNT;
    }

    public void setDiscardOnFirstUse(boolean discard) {
        this.discardTNT = discard;
    }

    //Individual, delayed explosions on each entity if setAfterSpawnEffects = true
    public boolean getIndividualEntityExplosions() {
        return individualEntityExplosions;
    }

    public void setIndividualEntityExplosions(boolean individualExplosions) {
        this.individualEntityExplosions = individualExplosions;
    }

    //Delay when entities will explode after the primed TNT in itself has exploded
    public int getInitialExplosionDelayCounter() {
        return initialExplosionDelayCounter;
    }

    public void setInitialExplosionDelayCounter(int explosionDelay) {
        this.initialExplosionDelayCounter = explosionDelay;
    }

    public boolean getHasExploded() {
        return exploded;
    }

    public void setHasExploded(boolean hasExploded) {
        this.exploded = hasExploded;
    }

    //When entities are set to spawn, this decides whether the queue should
    //spawn the entities gradually at a set interval, or spawn them all at the same time
    public boolean getGradualEntitySpawnAfterExplosion() {
        return gradualEntitySpawnAfterExplosion;
    }

    public void setGradualEntitySpawnAfterExplosion(boolean gradualSpawns) {
        this.gradualEntitySpawnAfterExplosion = gradualSpawns;
    }

    public boolean getTornado() {
        return isTornado;
    }

    public void setTornado(boolean tornado) {
        this.isTornado = tornado;
    }

    public boolean getKillEntitiesAfterLoop() {
        return killEntitiesAfterLoop;
    }

    public void setKillEntitiesAfterLoop(boolean kill) {
        this.killEntitiesAfterLoop = kill;
    }
}
