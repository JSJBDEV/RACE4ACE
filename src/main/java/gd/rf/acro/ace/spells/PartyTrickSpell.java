package gd.rf.acro.ace.spells;

import gd.rf.acro.ace.Utils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class PartyTrickSpell extends Spell {
    @Override
    public String spellType() {
        return "tap";
    }

    @Override
    public String element() {
        return "fire";
    }

    @Override
    public int tier() {
        return 0;
    }

    @Override
    public int cost() {
        return 1;
    }

    @Override
    public void onTapBlockFace(LivingEntity caster, BlockPos tapped, Direction direction) {
        super.onTapBlockFace(caster, tapped, direction);
        BlockPos loc = tapped.add(direction.getVector());
        FireworkRocketEntity entity = new FireworkRocketEntity(caster.world,caster,loc.getX(),loc.getY(),loc.getZ(), Utils.createRocketStack());
        caster.world.spawnEntity(entity);
    }
}
