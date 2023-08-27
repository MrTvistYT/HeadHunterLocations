package org.gt.headhunterlocations.Commands;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.LookClose;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.gt.headhunterlocations.Objects.AllMain;
import org.jetbrains.annotations.NotNull;

public class SpawnNPC implements CommandExecutor {
    AllMain main;
    public SpawnNPC(AllMain main){
        this.main = main;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(commandSender instanceof Player)){
            return true;
        }
        Player player = (Player) commandSender;
        Location location = player.getLocation();

        NPC main = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "Охотник");
        main.spawn(location);

        SkinTrait skin = main.getTrait(SkinTrait.class);
        skin.setSkinName("stewie__");

        LookClose lk = main.getTrait(LookClose.class);
        lk.setPerPlayer(true);
        lk.setHeadOnly(false);
        lk.setRealisticLooking(true);

        lk.lookClose(true);

        return false;
    }
}
