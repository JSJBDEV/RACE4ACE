package gd.rf.acro.ace.mixin;

import gd.rf.acro.ace.ACE;
import gd.rf.acro.ace.entities.BoltEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
//https://github.com/CammiePone/Arcanus/blob/1.17-dev/src/main/java/dev/cammiescorner/arcanus/core/mixin/client/ClientPlayNetworkHandlerMixin.java
@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPlayNetworkHandler {

    @Shadow
    private ClientWorld world;

    @Inject(method = "onEntitySpawn", at = @At("TAIL"))
    private void onEntitySpawn(EntitySpawnS2CPacket packet, CallbackInfo callbackInfo) {
        EntityType<?> type = packet.getEntityTypeId();
        double x = packet.getX();
        double y = packet.getY();
        double z = packet.getZ();
        Entity entity = null;

        if(type == ACE.BOLT_ENTITY_TYPE)
            entity = new BoltEntity(world, x, y, z);

        if(entity != null) {
            if(entity instanceof PersistentProjectileEntity projectile)
                projectile.setOwner(world.getEntityById(packet.getEntityData()));
            int id = packet.getId();
            entity.updateTrackedPosition(x, y, z);
            entity.refreshPositionAfterTeleport(x, y, z);
            entity.setPitch((packet.getPitch() * 360F) / 256F);
            entity.setYaw((packet.getYaw() * 360F) / 256F);
            entity.setId(id);
            entity.setUuid(packet.getUuid());
            world.addEntity(id, entity);
        }
    }
}
