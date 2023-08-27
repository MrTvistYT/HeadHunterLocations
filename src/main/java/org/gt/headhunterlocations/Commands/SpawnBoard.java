package org.gt.headhunterlocations.Commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gt.headhunterlocations.Objects.AllMain;
import org.gt.headhunterlocations.Objects.Board;
import org.jetbrains.annotations.NotNull;

public class SpawnBoard implements CommandExecutor {
    AllMain main;
    public SpawnBoard(AllMain main){
        this.main = main;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(commandSender instanceof Player)){
            return true;
        }

        Player player = (Player) commandSender;
        Location playerLoc = player.getLocation().getBlock().getLocation();

        playerLoc.add(0.5, 0.6, 0.5);

        Board board = new Board(playerLoc, main);
        main.getData().setBoard(board);

        return false;
    }
}
