package gd.rf.acro.ace.spells;

import gd.rf.acro.ace.ACE;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.math.Box;

import java.util.List;

public class IcarusSpell extends Spell {
    @Override
    public String spellType() {
        return "snap";
    }

    @Override
    public String element() {
        return "fire";
    }

    @Override
    public int tier() {
        return 1;
    }

    @Override
    public int cost() {
        return 10;
    }

    @Override
    public void snapCast(LivingEntity caster) {
        super.snapCast(caster);
        List<LivingEntity> entities = caster.world.getEntitiesByClass(LivingEntity.class,new Box(caster.getPos().add(-10,-10,-10),caster.getPos().add(10,10,10)),LivingEntity::isAlive);
        entities.forEach(entity ->
        {
            entity.addStatusEffect(new StatusEffectInstance(ACE.WIND_CALL_EFFECT,1000));
            entity.addStatusEffect(new StatusEffectInstance(ACE.UNDEADENED_EFFECT,1000));
        }
        );
    }
}
