package net.insprill.xenlib;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;

@UtilityClass
public class XenScheduler {

    /**
     * Runs a Runnable after a certain amount of ticks.
     *
     * @return The Task ID.
     */
    public int runTaskLater(Runnable task, long delay) {
        return Bukkit.getScheduler().runTaskLater(XenLib.getPlugin(), task, delay).getTaskId();
    }

    /**
     * Runs a Runnable after 1 server tick.
     *
     * @return The Task ID.
     */
    public int runTaskLater(Runnable task) {
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
    public int runTask(Runnable task) {
        return Bukkit.getScheduler().runTask(XenLib.getPlugin(), task).getTaskId();
    }


    /**
     * Runs a Runnable asynchronously.
     *
     * @return The Task ID.
     */
    public int runTaskAsync(Runnable task) {
        return Bukkit.getScheduler().runTaskAsynchronously(XenLib.getPlugin(), task).getTaskId();
    }

}
