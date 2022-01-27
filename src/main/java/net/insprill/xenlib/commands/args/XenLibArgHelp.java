package net.insprill.xenlib.commands.args;

import lombok.Getter;
import lombok.Setter;
import net.insprill.xenlib.ColourUtils;
import net.insprill.xenlib.XenLib;
import net.insprill.xenlib.XenMath;
import net.insprill.xenlib.commands.Command;
import net.insprill.xenlib.commands.ICommandArgument;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class XenLibArgHelp implements ICommandArgument {

    private final Command cmd;
    @Getter
    @Setter
    private int commandsPerPage = 7;

    public XenLibArgHelp(Command cmd) {
        this.cmd = cmd;
    }

    @Override
    public String getBaseArg() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Shows all commands, their arguments, and what they do.";
    }

    @Override
    public @Nullable String getPermission() {
        return null;
    }

    @Override
    public void process(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        long page = (args.length >= 2 && XenMath.isInteger(args[1]))
                ? Integer.parseInt(args[1])
                : 1;
        page = XenMath.clamp(page, 1, cmd.getCommandArgs().size() / commandsPerPage);

        String header = (page == 1)
                ? ColourUtils.format("&e===========< &6Help &e>===========")
                : ColourUtils.format("&e=======< &6Help &e- &6Page " + page + " &e>=======");

        sender.sendMessage(header);

        List<Map.Entry<String, ICommandArgument>> argEntries = cmd.getCommandArgs().entrySet().stream()
                .skip(commandsPerPage * (page - 1))
                .limit(commandsPerPage)
                .filter(x -> !x.getKey().equals(Command.NO_ARG))
                .filter(x -> Command.hasPermission(sender, x.getValue()))
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toList());

        for (Map.Entry<String, ICommandArgument> entry : argEntries) {
            ICommandArgument argument = entry.getValue();
            if (argument.isPlayerOnly() && !(sender instanceof Player))
                continue;
            StringBuilder argumentBuilder = new StringBuilder();
            argumentBuilder.append("&2/").append(label).append(" ").append(entry.getKey());
            for (Map.Entry<String, Boolean> argEntry : argument.getSubArgs().entrySet()) {
                boolean optional = argEntry.getValue();
                argumentBuilder.append(" ");
                argumentBuilder.append((optional) ? "&7(" : "&8[");
                argumentBuilder.append(argEntry.getKey());
                argumentBuilder.append((optional) ? ")" : "]");
            }
            argumentBuilder.append(" &f- &a").append(entry.getValue().getDescription());
            sender.sendMessage(ColourUtils.format(argumentBuilder.toString()));
        }

        int fillerLength = ChatColor.stripColor(header).length() - (XenLib.getPlugin().getName().length() + 4);
        StringBuilder footerBuilder = new StringBuilder();
        footerBuilder.append("&e");
        for (int i = 0; i < fillerLength >> 1; i++) {
            footerBuilder.append("=");
        }
        footerBuilder.append("< &6").append(XenLib.getPlugin().getName()).append(" &e>");
        for (int i = 0; i < fillerLength >> 1; i++) {
            footerBuilder.append("=");
        }
        sender.sendMessage(ColourUtils.format(footerBuilder.toString()));
    }

    @Override
    public @Nullable List<String> tabComplete(@NotNull CommandSender sender, @NotNull String[] args) {
        if (args.length != 2)
            return null;

        List<String> returnArgs = new ArrayList<>();
        for (int i = 1; i <= Math.ceil((float) cmd.getCommandArgs().size() / (float) commandsPerPage); i++) {
            returnArgs.add(i + "");
        }
        return returnArgs;
    }

}
