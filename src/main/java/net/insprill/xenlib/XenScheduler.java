package net.insprill.xenlib;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;

import java.util.concurrent.CompletableFuture;

@UtilityClass
public class XenScheduler {

    /**
     * Runs a Runnable after a certain amount of ticks.
     *
     * @return Future that completes after the task has been run.
     */
    public CompletableFuture<Void> runTaskLater(Runnable task, long delay) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskLater(XenLib.getPlugin(), () -> {
            task.run();
            future.complete(null);
        }, delay);
        return future;
    }

    /**
     * Runs a Runnable after 1 server tick.
     *
     * @return The Task ID.
     */
    public CompletableFuture<Void> runTaskLater(Runnable task) {
        return runTaskLater(task, 1L);
    }

    /**
     * Runs a Runnable every x ticks.
     *
     * @param initialDelay The time to delay the first execution.
     * @param period       The period between successive executions.
     * @return The Task ID.
     */
    public int runTaskTimer(Runnable task, long initialDelay, long period) {
        return Bukkit.getScheduler().runTaskTimer(XenLib.getPlugin(), task, initialDelay, period).getTaskId();
    }

    /**
     * Runs a Runnable at the next server tick.
     *
     * @return The Task ID.
     */
    public CompletableFuture<Void> runTask(Runnable task) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        Bukkit.getScheduler().runTask(XenLib.getPlugin(), () -> {
            task.run();
            future.complete(null);
        });
        return future;
    }

    /**
     * Runs a Runnable asynchronously.
     *
     * @return The Task ID.
     */
    public CompletableFuture<Void> runTaskAsync(Runnable task) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskAsynchronously(XenLib.getPlugin(), () -> {
            task.run();
            future.complete(null);
        });
        return future;
    }

    /**
     * Runs a Runnable after a certain amount of ticks.
     *
     * @return Future that completes after the task has been run.
     */
    public CompletableFuture<Void> runTaskAsyncLater(Runnable task, long delay) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskLaterAsynchronously(XenLib.getPlugin(), () -> {
            task.run();
            future.complete(null);
        }, delay);
        return future;
    }

}
