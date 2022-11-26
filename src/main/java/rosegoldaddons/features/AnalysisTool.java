package rosegoldaddons.features;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import rosegoldaddons.Main;
import rosegoldaddons.utils.ChatUtils;

import javax.vecmath.Vector2f;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.List;

public class AnalysisTool {
    // a tool to copy player rotations to clipboard and paste into desmos
    public static List<Vector2f> componentList = new ArrayList<>();

    public static String targetPlayer = "hael99";

    public static void copyToClipboard() {
        StringBuilder stringBuilder = new StringBuilder();
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        stringBuilder.append("[");
        if (componentList.size() == 0) {
            ChatUtils.sendMessage("No entries in list, returning.");
            return;
        }
        for (Vector2f vec2f : componentList) {
            String str = "(" + vec2f.x + ", " + vec2f.y + "), ";
            stringBuilder.append(str);
        }
        stringBuilder.append("]");
        StringSelection stringSelection = new StringSelection(stringBuilder.toString());
        clipboard.setContents(stringSelection, null);
    }

    @SubscribeEvent
    public void tickEvent(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && Main.analysisTool) {
            if (Main.mc.theWorld == null) return;
            List<Entity> entityList = Main.mc.theWorld.getLoadedEntityList();
            for (Entity entity : entityList) {
                if (entity instanceof EntityPlayer) {
                    String name = ((EntityPlayer) entity).getDisplayNameString();
                    if (name.contains(" ")) continue;
                    if (entity.posY > 200F) ChatUtils.sendMessage("name is " + name + " and y is " + entity.posY);
                    if (name.equals(targetPlayer)) {
                        float xComponent = entity.rotationYaw;
                        float yComponent = entity.rotationPitch;
                        Vector2f vec2f = new Vector2f(xComponent, yComponent);
                        componentList.add(vec2f);
                    }
                }
            }
        }
    }

    /*@SubscribeEvent
    public void packetEvent(ReceivePacketEvent event) {
        if (Main.analysisTool) {
            if (Main.mc.theWorld == null) return;
            //ChatUtils.sendMessage("success 1");
            if (event.packet instanceof S14PacketEntity.S17PacketEntityLookMove) {
                //ChatUtils.sendMessage("success 2");
                S14PacketEntity.S17PacketEntityLookMove packet = (S14PacketEntity.S17PacketEntityLookMove) event.packet;
                Entity entity = packet.getEntity(Main.mc.theWorld);
                if (entity instanceof EntityPlayer) {
                    //ChatUtils.sendMessage("success 3");
                    String name = ((EntityPlayer) entity).getDisplayNameString();
                    if (name.contains(" ")) return;
                    if (entity.posY > 200F) ChatUtils.sendMessage("name is " + name + " and y is " + entity.posY);
                    if (!name.equals(targetPlayer)) return;
                    //ChatUtils.sendMessage("success 4");
                    float xComponent = packet.func_149066_f();
                    float yComponent = packet.func_149063_g();
                    Vector2f vec2f = new Vector2f(xComponent, yComponent);
                    componentList.add(vec2f);
                }
            }
        }
    }*/
}
