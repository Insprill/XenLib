package net.insprill.xenlib.commands.args;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import net.insprill.fetch4j.Params;
import net.insprill.fetch4j.Response;
import net.insprill.fetch4j.exception.FetchException;
import net.insprill.xenlib.XenLib;
import net.insprill.xenlib.commands.ICommandArgument;
import net.insprill.xenlib.localization.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static net.insprill.fetch4j.Fetch.fetch;
import static net.insprill.fetch4j.Params.params;

public class XenLibArgPlInfo implements ICommandArgument {

    @Setter
    @Getter
    private static String hastebinLink = "https://paste.insprill.net/";
    @Setter
    @Getter
    private static String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36";

    @Override
    public String getBaseArg() {
        return "plinfo";
    }

    @Override
    public Map<String, Boolean> getSubArgs() {
        return ImmutableMap.of("skipPlugins", false);
    }

    @Override
    public String getDescription() {
        return "Create a hastebin with some information about your server.";
    }

    @Override
    public void process(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        StringBuilder builder = new StringBuilder();

        builder.append(XenLib.getPlugin().getName()).append(": ").append(XenLib.getPlugin().getDescription().getVersion()).append("\n");
        builder.append("Server: ").append(Bukkit.getVersion()).append("\n");
        builder.append("API: ").append(Bukkit.getBukkitVersion()).append("\n");
        builder.append("JVM-Name: ").append(System.getProperty("java.vm.name")).append("\n");
        builder.append("JVM-Version: ").append(System.getProperty("java.vm.version")).append("\n");
        builder.append("Architecture: ").append(System.getProperty("os.arch")).append("\n");
        builder.append("OS-Name: ").append(System.getProperty("os.name")).append("\n");
        builder.append("OS-Version: ").append(System.getProperty("os.version")).append("\n");

        if (args.length != 2 || !args[1].equalsIgnoreCase("skipPlugins")) {
            builder.append("Enabled-Plugins:").append("\n");

            Arrays.stream(Bukkit.getPluginManager().getPlugins())
                    .filter(Plugin::isEnabled)
                    .sorted(Comparator.comparing(Plugin::getName))
                    .forEach(pl -> {
                        builder.append("    ")
                                .append(pl.getName()).append(": ")
                                .append(pl.getDescription().getVersion()).append("\n");
                    });
        }

        try {
            Response res = fetch(hastebinLink + "documents", params()
                    .method(Params.Method.POST)
                    .userAgent(userAgent)
                    .timeout(10_000)
                    .body(builder.toString()));
            JsonObject object = new Gson().fromJson(res.getBody(), JsonObject.class);
            String link = hastebinLink + object.get("key").getAsString() + ".yml";
            Lang.send(sender, "commands.plinfo.success", "%link%;" + link);
        } catch (FetchException exception) {
            Lang.send(sender, "commands.plinfo.fail", "%error%;" + exception.getMessage());
        }
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String[] args) {
        return Collections.emptyList();
    }

    @Override
    public String getPermission() {
        return "op";
    }

}
