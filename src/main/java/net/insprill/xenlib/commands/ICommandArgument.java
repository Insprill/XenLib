package net.insprill.xenlib.commands;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public interface ICommandArgument {

    /**
     * @return The first argument represented. E.g. /mypl <b><i>reload</i></b>.
     */
    String getBaseArg();

    /**
     * @return A Map of all arguments that come after the initial. Key is the argument name, value is whether the argument is required. Used in the help page.
     */
    default Map<String, Boolean> getSubArgs() {
        return Collections.emptyMap();
    }

    /**
     * @return A description of what this argument does. Used in the help page.
     */
    String getDescription();

    /**
     * @return The permission required to use the argument.
     */
    @Nullable
    String getPermission();

    /**
     * @return Whether this argument can only be used by a player.
     */
    default boolean isPlayerOnly() {
        return false;
    }

    /**
     * Called when the command is executed.
     *
     * @param sender CommandSender who initiated the command.
     * @param label  The command's label.
     * @param args   All command arguments.
     */
    boolean process(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args);

    /**
     * @param sender CommandSender who initiated the command.
     * @param args   All command arguments.
     * @return A list of String's for tab-completion.
     */
    @Nullable
    List<String> tabComplete(@NotNull CommandSender sender, @NotNull String[] args);

}
