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
package fr.xpdustry.distributor.core.command;

import arc.util.CommandHandler;
import fr.xpdustry.distributor.api.command.i.Argument;
import fr.xpdustry.distributor.api.command.i.Argument.Type;
import fr.xpdustry.distributor.api.command.i.Command;
import fr.xpdustry.distributor.api.command.i.CommandContext;
import fr.xpdustry.distributor.api.command.i.CommandRegistry;
import fr.xpdustry.distributor.api.command.i.Flag;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public final class SimpleCommandRegistry<C> implements CommandRegistry<C> {

    private final Executor executor;
    private final CommandHandler handler;
    private Command<C> root = Command.create("root");

    public SimpleCommandRegistry(final Executor executor, final CommandHandler handler) {
        this.executor = executor;
        this.handler = handler;
    }

    @Override
    public void register(final Command<C> command) {
        this.verify(command, Collections.emptySet());
        this.root = this.root.withSubcommand(command);
    }

    @Override
    public CompletableFuture<CommandContext<C>> handle(final C caller, final String input) {
        final Queue<String> queue = new ArrayDeque<>(Arrays.asList(input.split(" ", -1)));
        final var future = new CompletableFuture<CommandContext<C>>();
        this.executor.execute(() -> {
            try {
                final var context = new SimpleCommandContext<>(this, caller, input);
                this.parse(context, queue);
                future.complete(context);
            } catch (final Exception e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }

    @Override
    public RegistrationPolicy getPolicy() {
        return RegistrationPolicy.RELOCATE;
    }

    private void verify(final Command<C> command, final Set<String> previous) {
        var hasOptional = false;

        for (final var argument : command.getArguments()) {
            if (hasOptional) {
                throw new IllegalArgumentException(
                        "Command " + command + " has a required argument after an optional one");
            }
            if (argument.getType() == Argument.Type.OPTIONAL) {
                hasOptional = true;
            }
        }

        if (command.getExecutor().isEmpty() && command.getSubcommands().isEmpty()) {
            throw new IllegalArgumentException("Command " + command + " has no executor");
        }

        final Set<String> flags = new HashSet<>(previous);
        final Set<String> subcommands = new HashSet<>();
        for (final var subcommand : command.getSubcommands()) {
            for (final var alias : subcommand.getAllAliases()) {
                if (!subcommands.add(alias)) {
                    throw new IllegalArgumentException("Command " + command + " has a collision with the subcommand "
                            + subcommand + " on alias " + alias);
                }
            }

            for (final var flag : command.getFlags()) {
                for (final var alias : flag.getAllAliases()) {
                    if (!flags.add(alias)) {
                        throw new IllegalArgumentException(
                                "Command " + command + " has a collision with the flag " + flag + " on alias " + alias);
                    }
                }
            }

            this.verify(subcommand, flags);
        }
    }

    private void parse(final SimpleCommandContext<C> context, final Queue<String> input) {
        final List<Flag<C>> flags = new ArrayList<>();
        var command = this.root;
        while (!input.isEmpty()) {
            if (this.parseFlag(context, flags, input)) {
                continue;
            }

            for (final var argument : command.getArguments()) {
                final var result = argument.getParser().parse(context, input);
                if (result.getFailure().isPresent()) {
                    if (argument.getType() == Type.OPTIONAL /* Check if exception is no input */) {
                        break;
                    }
                    throw new IllegalArgumentException(
                            "Parsing failure " + result.getFailure().get().getMessage());
                }
                context.setArgument(argument.getName(), result.getSuccess().orElseThrow());
            }

            if (input.isEmpty()) {
                break;
            }

            final var subcommand = command.getSubcommands().stream()
                    .filter(c -> c.getAllAliases().contains(input.peek()))
                    .findFirst();

            if (subcommand.isPresent()) {
                command = subcommand.get();
                input.remove();
            } else {
                throw new IllegalArgumentException("Unknown subcommand " + input.peek());
            }
        }

        command.getExecutor()
                .orElseThrow(() -> new IllegalArgumentException("Missing command executor."))
                .execute(context);
    }

    private boolean parseFlag(
            final SimpleCommandContext<C> context, final List<Flag<C>> flags, final Queue<String> input) {
        if (input.isEmpty() || !input.peek().startsWith("-")) {
            return false;
        }

        final List<Flag<C>> results;
        final var content = input.peek();

        if (content.startsWith("--")) {
            final var name = content.substring(2);
            if (name.isEmpty()) {
                throw new IllegalArgumentException("Flag name is empty");
            }
            for (final var flag : flags) {
                if (flag.getName().equals(name)) {
                    results = Collections.singletonList(flag);
                    break;
                }
            }
            throw new IllegalArgumentException("Unknown flag " + name);
        } else {
            final var aliases = Arrays.asList(content.substring(1).split(""));
            if (aliases.isEmpty()) {
                throw new IllegalArgumentException("Flag name is empty");
            }
            results = new ArrayList<>(aliases.size());
            for (final var alias : aliases) {
                final var flag = flags.stream()
                        .filter(f -> f.getAliases().contains(alias))
                        .findFirst();
                if (flag.isEmpty()) {
                    throw new IllegalArgumentException("Unknown flag " + alias);
                }
                results.add(flag.get());
            }
        }

        for (final var flag : results) {
            if (flag.getType() == Flag.Type.SINGLE && context.hasFlag(flag.getName())) {
                throw new IllegalArgumentException("Flag " + flag.getName() + " can only be used once");
            }

            if (flag.getValueParser().isPresent()) {
                final var result = flag.getValueParser().get().parse(context, input);
                if (result.getFailure().isPresent()) {
                    throw new IllegalArgumentException("Failed to parse flag " + flag.getName() + ": "
                            + result.getFailure().get());
                }
                context.addFlagValue(flag.getName(), result.getSuccess().orElseThrow());
            } else {
                context.addFlagValue(flag.getName(), null);
            }
        }

        input.remove();
        return true;
    }
}
