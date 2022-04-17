package gd.rf.acro.ace.spells;

import gd.rf.acro.ace.Utils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

public class BlinkSpell extends Spell {
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
        return 5;
    }

    @Override
    public void snapCast(LivingEntity caster) {
        super.snapCast(caster);
        HitResult result = caster.raycast(50,1,true);
        Utils.castBeamToPos(caster,new BlockPos(result.getPos()),caster.world,new float[]{1,0,0},1);
        caster.teleport(result.getPos().x,result.getPos().y,result.getPos().z,true);

    }
}
