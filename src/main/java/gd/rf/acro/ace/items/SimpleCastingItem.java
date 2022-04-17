package gd.rf.acro.ace.items;

import gd.rf.acro.ace.ACE;
import gd.rf.acro.ace.Utils;
import gd.rf.acro.ace.spells.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class SimpleCastingItem extends Item implements IRenderableCastingDevice{

    
    private int mana;
    private int maxSpells;
    private int manaRegen;
    public SimpleCastingItem(Settings settings, int mana, int maxSpells,int manaRegen) {
        super(settings);
        this.mana=mana;
        this.maxSpells=maxSpells;
        this.manaRegen=manaRegen;
    }

    
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        //This bit is to add spells to your casting device
        if(user.getOffHandStack().getItem() instanceof DustyTomeItem)
        {
            NbtCompound tag = user.getMainHandStack().getNbt();
            Spell onBook = Spells.getSpellByName(user.getOffHandStack().getNbt().getString("spell"));
            NbtList spells = (NbtList) tag.get("spellsEquipped");
            if(spells.size()<tag.getInt("maxSpells"))
            {
                addSpell(user.getMainHandStack(),onBook);
            }
            else
            {
                removeSpell(user.getMainHandStack(),getEquipped(user.getMainHandStack()));
                addSpell(user.getMainHandStack(),onBook);
            }
            user.dropItem(user.getOffHandStack().copy(),true);
            user.getOffHandStack().decrement(1);
            return super.use(world, user, hand);
        }

        //This bit is actual casting, specifically snapcasting
        Spell spell = getEquipped(user.getStackInHand(hand));
        if(spell!=null && hand==Hand.MAIN_HAND && spell.spellType().contains("snap"))
        {
            NbtCompound tag = user.getStackInHand(hand).getNbt();
            if(tag.getInt("mana")>spell.cost())
            {
                spell.snapCast(user);
                tag.putInt("mana",tag.getInt("mana")-spell.cost());
                user.getStackInHand(hand).setNbt(tag);
            }

        }
        return super.use(world, user, hand);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        Spell spell = getEquipped(stack);
        if(spell!=null && hand==Hand.MAIN_HAND && spell.spellType().contains("touch"))
        {
            NbtCompound tag = stack.getNbt();
            if(tag.getInt("mana")>spell.cost())
            {
                spell.onTouchCast(user,entity);
                tag.putInt("mana",tag.getInt("mana")-spell.cost());
                stack.setNbt(tag);
            }
        }
        return super.useOnEntity(stack, user, entity, hand);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        Spell spell = getEquipped(context.getStack());
        if(spell!=null && context.getHand()==Hand.MAIN_HAND && spell.spellType().contains("tap"))
        {
            NbtCompound tag = context.getStack().getNbt();
            if(tag.getInt("mana")>spell.cost())
            {
                spell.onTapBlock(context.getPlayer(),context.getBlockPos());
                spell.onTapBlockFace(context.getPlayer(),context.getBlockPos(),context.getSide());
                tag.putInt("mana",tag.getInt("mana")-spell.cost());
                context.getStack().setNbt(tag);
            }

        }
        return super.useOnBlock(context);
    }


    public Spell getEquipped(ItemStack stack) {
        if(stack.hasNbt() && stack.getNbt().contains("spellsEquipped"))
        {
            NbtCompound tag = stack.getNbt();

            NbtList spells = (NbtList) tag.get("spellsEquipped");
            return Spells.getSpellByName(spells.getString(tag.getInt("selected")));
        }
        return null;

    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        NbtCompound tag = new NbtCompound();
        if(!stack.hasNbt())
        {

            tag.putInt("selected",0);
            NbtList list = new NbtList();
            tag.put("spellsEquipped",list);
            tag.putInt("maxSpells",maxSpells);
            tag.putInt("mana",mana);
            tag.putInt("maxMana",mana);
            tag.putInt("manaRegen",manaRegen);
            stack.setNbt(tag);
            if(stack.getItem()== ACE.MASTER_SPELL_BOOK)
            {
                addSpell(stack, Spells.REGISTRY.toArray(new Spell[]{}));
            }
        }
        tag=stack.getNbt();
        //mana regen (per second)
        if(world.getTimeOfDay()%20L==0L)
        {
            tag.putInt("mana", Math.min(tag.getInt("mana") + tag.getInt("manaRegen"), tag.getInt("maxMana")));
            stack.setNbt(tag);
        }

    }

    public void scrollMinus(ItemStack stack)
    {
        NbtCompound tag = stack.getNbt();
        NbtList spells = (NbtList) tag.get("spellsEquipped");

        if(tag.getInt("selected")>0)
        {
            tag.putInt("selected",tag.getInt("selected")-1);
        }
        else
        {
            tag.putInt("selected", spells.size()-1);
        }
        stack.setNbt(tag);
    }
    public void scrollPlus(ItemStack stack)
    {
        NbtCompound tag = stack.getNbt();
        NbtList spells = (NbtList) tag.get("spellsEquipped");
        if(tag.getInt("selected")< spells.size()-1)
        {
            tag.putInt("selected",tag.getInt("selected")+1);
        }
        else
        {
            tag.putInt("selected",0);
        }
        stack.setNbt(tag);

    }
    public void addSpell(ItemStack stack,Spell... spell)
    {

        NbtCompound tag = stack.getNbt();
        NbtList spells = (NbtList) tag.get("spellsEquipped");

        if(spell.length>0)
        {
            for (int i = 0; i < spell.length; i++)
            {
                if(spells.size()<tag.getInt("maxSpells"))
                {
                    spells.add(NbtString.of(spell[i].name()));
                }
            }

        }
        System.out.println(spells.toString());
        tag.put("spellsEquipped",spells);
        stack.setNbt(tag);


    }
    public void removeSpell(ItemStack stack, Spell spell)
    {
        NbtCompound tag = stack.getNbt();
        NbtList spells = (NbtList) tag.get("spellsEquipped");
        for (int i = 0; i < spells.size(); i++) {
            if(spells.getString(i).equals(spell.name()))
            {
                spells.remove(i);
                tag.put("spellsEqipped",spells);
                stack.setNbt(tag);
                return;
            }
        }
    }
}
