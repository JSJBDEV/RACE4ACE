package gd.rf.acro.ace.spells;

import gd.rf.acro.ace.ACE;
import gd.rf.acro.ace.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.math.Box;

import java.util.List;

public class DodgeSpell extends Spell {
    @Override
    public String spellType() {
        return "snap";
    }

    @Override
    public String element() {
        return "air";
    }

    @Override
    public int tier() {
        return 1;
    }

    @Override
    public int cost() {
        return 5;
    }

    @Override
    public void snapCast(LivingEntity caster) {
        super.snapCast(caster);
        System.out.println(Utils.random(-2,2));
        caster.addVelocity(Utils.random(-2,2),1,Utils.random(-2,2));
        caster.addStatusEffect(new StatusEffectInstance(ACE.AERIAL_EFFECT,1000));
        //TODO: might want to do some damage negation of some kind
    }
}
