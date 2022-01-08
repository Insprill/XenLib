package net.insprill.xenlib.commands;

import com.google.common.reflect.ClassPath;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.insprill.xenlib.XenLib;
import net.insprill.xenlib.XenUtils;
import net.insprill.xenlib.commands.args.XenLibArgHelp;
import net.insprill.xenlib.commands.args.XenLibArgPlInfo;
import net.insprill.xenlib.localization.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Command implements TabExecutor {

    public static final String NO_ARG = "NO_ARG";

    @Getter
    private final Map<String, ICommandArgument> commandArgs = new HashMap<>();
    @Getter
    @Setter
    private int commandsPerPage = 7;

    @SneakyThrows
    public Command(String name, String packageName) {
        PluginCommand command = XenLib.getPlugin().getCommand(name);
        command.setExecutor(this);
        command.setTabCompleter(this);

        Set<Class<?>> classes = new HashSet<>(getClasses(packageName));
        classes.addAll(Arrays.asList(XenLibArgHelp.class, XenLibArgPlInfo.class)); // When minimized these classes get removed, so it's easier to just manually add them.

        for (Class<?> argClass : classes) {
            ICommandArgument arg = null;
            for (Constructor<?> constructor : argClass.getConstructors()) {
                if (constructor.getParameterCount() == 0) {
                    arg = (ICommandArgument) constructor.newInstance();
                } else if (constructor.getParameterCount() == 1 && constructor.getParameterTypes()[0] == Command.class) {
                    arg = (ICommandArgument) constructor.newInstance(this);
                }
            }
            if (arg == null)
                continue;

            registerCommand(arg);
        }
    }

    @SneakyThrows
    @SuppressWarnings("UnstableApiUsage")
    private static Set<Class<?>> getClasses(String packageName) {
        return ClassPath.from(XenLib.getPlugin().getClass().getClassLoader())
                .getAllClasses()
                .parallelStream()
                .filter(clazz -> clazz.getPackageName().equalsIgnoreCase(packageName))
                .map(ClassPath.ClassInfo::load)
                .filter(ICommandArgument.class::isAssignableFrom)
                .collect(Collectors.toSet());
    }

    public void registerCommand(ICommandArgument arg) {
        if (commandArgs.containsKey(arg.getBaseArg())) {
            new IllegalArgumentException(
                    "Tried to register arg " + arg.getBaseArg() + " to " + arg.getClass().getSimpleName() + " but it is already registered to " + commandArgs.get(arg.getBaseArg()).getClass().getSimpleName()
            ).printStackTrace();
            return;
        }
        XenUtils.registerPermission(arg.getPermission());
        commandArgs.put(arg.getBaseArg(), arg);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        ICommandArgument arg = (args.length == 0) ? commandArgs.get(NO_ARG) : commandArgs.get(args[0]);

        if (arg == null) {
            Lang.send(sender, "commands.unknown-command", "%label%;" + label);
            return true;
        }
        if (!hasPermission(sender, arg)) {
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
                    .filter(e -> hasPermission(sender, commandArgs.get(e)))
                    .filter(e -> !e.equals(NO_ARG))
                    .collect(Collectors.toList());
        } else {
            ICommandArgument arg = commandArgs.get(args[0]);

            if (arg == null)
                return Collections.emptyList();
            if (!hasPermission(sender, arg))
                return Collections.emptyList();
            if (arg.isPlayerOnly() && !(sender instanceof Player))
                return Collections.emptyList();

            completions = arg.tabComplete(sender, args);
            if (completions == null) completions = Collections.emptyList();
        }
        completions = StringUtil.copyPartialMatches(args[args.length - 1], completions, new ArrayList<>());
        Collections.sort(completions);
        return completions;
    }

    private boolean hasPermission(CommandSender sender, ICommandArgument arg) {
        String perm = arg.getPermission();
        if (perm == null || perm.isEmpty()) {
            return true;
        }
        return (perm.equals("op"))
                ? sender.isOp()
                : sender.hasPermission(perm);
    }

}
