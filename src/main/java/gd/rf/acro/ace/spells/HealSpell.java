package gd.rf.acro.ace.spells;

import net.minecraft.entity.LivingEntity;

public class HealSpell extends Spell {


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
        return 5;
    }

    @Override
    public void snapCast(LivingEntity caster) {
        super.snapCast(caster);
        caster.heal(2);
    }
}
