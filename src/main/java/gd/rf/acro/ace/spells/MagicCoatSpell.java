package gd.rf.acro.ace.spells;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class MagicCoatSpell extends Spell {
    @Override
    public String spellType() {
        return "snap";
    }

    @Override
    public String element() {
        return "water";
    }

    @Override
    public int tier() {
        return 0;
    }

    @Override
    public int cost() {
        return 10;
    }


    @Override
    public void snapCast(LivingEntity caster) {
        super.snapCast(caster);
        caster.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE,500));
    }
}
