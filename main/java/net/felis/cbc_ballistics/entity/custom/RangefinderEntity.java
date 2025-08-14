package net.felis.cbc_ballistics.entity.custom;


import net.felis.cbc_ballistics.entity.ModEntities;
import net.felis.cbc_ballistics.util.RangefinderResults;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.minecraftforge.event.ForgeEventFactory;

public class RangefinderEntity extends Projectile {

    private RangefinderResults results;

    public RangefinderEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);

    }

    public RangefinderEntity(Level level, RangefinderResults results) {
        super(ModEntities.RANGEFINDERENTITY.get(), level);
        this.results = results;
    }


    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        BlockState blockstate = this.level().getBlockState(pResult.getBlockPos());
        blockstate.onProjectileHit(this.level(), blockstate, pResult, this);
        if(results != null) {
            results.setResults(pResult.getBlockPos());
        }
        super.onHitBlock(pResult);
        this.discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        if(results != null) {
            if(pResult.getEntity().getType() == EntityType.CAT) {
                results.cat();
            } else {
                results.setResults(pResult.getEntity().position());
            }
        }
        super.onHitEntity(pResult);
        this.discard();
    }

    @Override
    protected void defineSynchedData() {

    }

    public void tick() {
        super.tick();
        boolean hitBlock = false;
        for(int i = 0; i < 64 && !hitBlock; i ++) {
            hitBlock = subtick();
        }
        if(!hitBlock) {
            if(results != null) {
                results.fail();
            }
            this.discard();
        }
    }

    public boolean subtick() {
        this.checkInsideBlocks();
        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitresult.getType() != HitResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, hitresult)) {
            if(!(hitresult.getType() == HitResult.Type.ENTITY && ((EntityHitResult)hitresult).getEntity().equals(getOwner()))) {
                this.onHit(hitresult);
                return true;
            }
        }
        Vec3 vec3 = this.getDeltaMovement();
        double d5 = vec3.x;
        double d6 = vec3.y;
        double d1 = vec3.z;
        double d7 = this.getX() + d5;
        double d2 = this.getY() + d6;
        double d3 = this.getZ() + d1;
        float f = 0.99F;
        this.setDeltaMovement(vec3.scale((double)f));
        this.setPos(d7, d2, d3);
        this.checkInsideBlocks();
        return false;
    }
}


