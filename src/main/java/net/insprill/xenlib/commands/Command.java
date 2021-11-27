package net.insprill.xenlib.commands;

import com.google.common.reflect.ClassPath;
import lombok.Getter;
import lombok.SneakyThrows;
import net.insprill.xenlib.ColourUtils;
import net.insprill.xenlib.XenLib;
import net.insprill.xenlib.XenMath;
import net.insprill.xenlib.XenUtils;
import net.insprill.xenlib.localization.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Command implements TabExecutor {

    private static final String NO_ARG = "NO_ARG";
    private static final int MAX_COMMANDS_PER_PAGE = 7;

    @Getter
    private final Map<String, ICommandArgument> commandArgs = new HashMap<>();

    @SneakyThrows
    @SuppressWarnings("UnstableApiUsage")
    public Command(String name, String packageName) {
        PluginCommand command = XenLib.getPlugin().getCommand(name);
        command.setExecutor(this);
        command.setTabCompleter(this);

        Set<Class<?>> classes = ClassPath.from(XenLib.getPlugin().getClass().getClassLoader())
                .getAllClasses()
                .parallelStream()
                .filter(clazz -> clazz.getPackageName().equalsIgnoreCase(packageName))
                .map(ClassPath.ClassInfo::load)
                .filter(ICommandArgument.class::isAssignableFrom)
                .collect(Collectors.toSet());

        for (Class<?> argClass : classes) {
            ICommandArgument arg = (ICommandArgument) argClass.getConstructors()[0].newInstance();
            if (commandArgs.containsKey(arg.getBaseArg())) {
                new IllegalArgumentException("Tried to register arg " + arg.getBaseArg() + " to " + argClass.getSimpleName() + " but it is already registered to " + commandArgs.get(arg.getBaseArg()).getClass().getSimpleName())
                        .printStackTrace();
                continue;
            }

            XenUtils.registerPermission(arg.getPermission());

            commandArgs.put(arg.getBaseArg(), arg);
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        // Help page.
        if (args.length == 1 && args[0].equalsIgnoreCase("help")) {

            int page = (args.length == 2 && XenMath.isInteger(args[1]))
                    ? Integer.parseInt(args[1])
                    : 1;

            sender.sendMessage(ColourUtils.format("&e===========< &6Help &e>==========="));

            List<Map.Entry<String, ICommandArgument>> argEntries = commandArgs.entrySet().stream()
                    .skip(MAX_COMMANDS_PER_PAGE * page)
                    .limit(MAX_COMMANDS_PER_PAGE)
                    .filter(x -> !x.getKey().equals(NO_ARG))
                    .sorted(Map.Entry.comparingByKey())
                    .collect(Collectors.toList());

            for (Map.Entry<String, ICommandArgument> entry : argEntries) {
                StringBuilder argumentBuilder = new StringBuilder();
                argumentBuilder.append("&2/").append(label).append(" ").append(entry.getKey());
                for (Map.Entry<String, Boolean> argEntry : entry.getValue().getSubArgs().entrySet()) {
                    boolean optional = argEntry.getValue();
                    argumentBuilder.append(" ");
                    argumentBuilder.append((optional) ? "&7(" : "&8[");
                    argumentBuilder.append(argEntry.getKey());
                    argumentBuilder.append((optional) ? ")" : "]");
                }
                argumentBuilder.append(" &f- &a").append(entry.getValue().getDescription());
                sender.sendMessage(ColourUtils.format(argumentBuilder.toString()));
            }
            int fillerLength = 30 - (XenLib.getPlugin().getName().length() + 4);
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
            return true;
        }

        ICommandArgument arg = (args.length == 0) ? commandArgs.get(NO_ARG) : commandArgs.get(args[0]);

        if (arg == null) {
            Lang.send(sender, "commands.unknown-command", "%label%;" + label);
            return true;
        }
        if (arg.getPermission() != null && !sender.hasPermission(arg.getPermission())) {
            Lang.send(sender, "commands.no-permission");
            return true;
        }
        if (!(sender instanceof Player) && arg.isPlayerOnly()) {
            Lang.send(sender, "commands.player-only");
            return true;
        }

        arg.process(sender, label, args);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command cmd, @NotNull String alias, @NotNull String[] args) {
        List<String> completions;
        if (args.length == 1) {
            completions = commandArgs.keySet().parallelStream()
                    .filter(e -> commandArgs.get(e).getPermission() == null || sender.hasPermission(commandArgs.get(e).getPermission()))
                    .filter(e -> !e.equals(NO_ARG))
                    .collect(Collectors.toList());
            completions.add("help");
        } else {
            ICommandArgument arg = commandArgs.get(args[0]);

            if (arg == null)
                return Collections.emptyList();
            if (arg.getPermission() != null && !sender.hasPermission(arg.getPermission()))
                return Collections.emptyList();
            if (!(sender instanceof Player) && !arg.isPlayerOnly())
                return Collections.emptyList();

            completions = arg.tabComplete(sender, args);
            if (completions == null) completions = Collections.emptyList();
        }
        completions = StringUtil.copyPartialMatches(args[args.length - 1], completions, new ArrayList<>());
        Collections.sort(completions);
        return completions;
    }

}
