/*
 * Distributor, a feature-rich framework for Mindustry plugins.
 *
 * Copyright (C) 2022 Xpdustry
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package fr.xpdustry.distributor.scheduler;

import cloud.commandframework.tasks.*;
import java.util.concurrent.*;
import mindustry.mod.*;
import org.jetbrains.annotations.*;

final class SimplePluginTaskSynchronizer implements TaskSynchronizer {

  private final PluginScheduler scheduler;
  private final Plugin plugin;

  SimplePluginTaskSynchronizer(final PluginScheduler scheduler, final Plugin plugin) {
    this.scheduler = scheduler;
    this.plugin = plugin;
  }

  @Override
  public <I> CompletableFuture<Void> runSynchronous(@NotNull I input, @NotNull TaskConsumer<I> consumer) {
    final var future = new CompletableFuture<Void>();
    scheduler.syncTask(plugin, () -> {
      consumer.accept(input);
      future.complete(null);
    });
    return future;
  }

  @Override
  public <I, O> CompletableFuture<O> runSynchronous(@NotNull I input, @NotNull TaskFunction<I, O> function) {
    final var future = new CompletableFuture<O>();
    scheduler.syncTask(plugin, () -> {
      future.complete(function.apply(input));
    });
    return future;
  }

  @Override
  public <I> CompletableFuture<Void> runAsynchronous(@NotNull I input, @NotNull TaskConsumer<I> consumer) {
    final var future = new CompletableFuture<Void>();
    scheduler.asyncTask(plugin, () -> {
      consumer.accept(input);
      future.complete(null);
    });
    return future;
  }

  @Override
  public <I, O> CompletableFuture<O> runAsynchronous(@NotNull I input, @NotNull TaskFunction<I, O> function) {
    final var future = new CompletableFuture<O>();
    scheduler.syncTask(plugin, () -> {
      future.complete(function.apply(input));
    });
    return future;
  }
}
