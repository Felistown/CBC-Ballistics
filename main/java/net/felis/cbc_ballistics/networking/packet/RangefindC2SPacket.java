package net.felis.cbc_ballistics.networking.packet;

import net.felis.cbc_ballistics.entity.custom.RangefinderEntity;
import net.felis.cbc_ballistics.item.ModItems;
import net.felis.cbc_ballistics.item.custom.RangefinderItem;
import net.felis.cbc_ballistics.util.RangefinderResults;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RangefindC2SPacket {

    public RangefindC2SPacket() {

    }

    public RangefindC2SPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            ServerLevel level = (ServerLevel)player.level();
            RangefinderResults results = new RangefinderResults();
            Item thing = player.getUseItem().getItem();
            if(thing == ModItems.RANGEFINDER.get()) {
                ((RangefinderItem)thing).setResults(results);
            }
            RangefinderEntity ray = new RangefinderEntity(Minecraft.getInstance().level, results);
            ray.shootFromRotation(player, player.getXRot(),player.getYRot(), 0.0f, 10f, 0.0f);
            ray.setPos(player.getX(), player.getEyeY() - Float.MIN_VALUE, player.getZ());
            ray.setOwner(player);
            level.addFreshEntity(ray);
        });
        return true;
    }
}
