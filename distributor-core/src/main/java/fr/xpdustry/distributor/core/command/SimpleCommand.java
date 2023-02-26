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

import fr.xpdustry.distributor.api.command.i.Argument;
import fr.xpdustry.distributor.api.command.i.Command;
import fr.xpdustry.distributor.api.command.i.CommandExecutor;
import fr.xpdustry.distributor.api.command.i.Flag;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class SimpleCommand<C> implements Command<C> {

    private final String name;
    private final List<String> aliases;
    private final List<Argument<C>> arguments;
    private final List<Command<C>> subcommands;
    private final List<Flag<C>> flags;
    private final @Nullable CommandExecutor<C> executor;

    public SimpleCommand(
            final String name,
            final List<String> aliases,
            final List<Argument<C>> arguments,
            final List<Command<C>> subcommands,
            final List<Flag<C>> flags,
            @Nullable final CommandExecutor<C> executor) {
        this.name = name;
        this.aliases = aliases;
        this.arguments = arguments;
        this.subcommands = subcommands;
        this.flags = flags;
        this.executor = executor;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Command<C> withName(final String name) {
        return new SimpleCommand<>(name, this.aliases, this.arguments, this.subcommands, this.flags, this.executor);
    }

    @Override
    public List<String> getAliases() {
        return Collections.unmodifiableList(this.aliases);
    }

    @Override
    public Command<C> withAliases(final List<String> aliases) {
        return new SimpleCommand<>(
                this.name, new ArrayList<>(aliases), this.arguments, this.subcommands, this.flags, this.executor);
    }

    @Override
    public Command<C> withAliases(final String... aliases) {
        return new SimpleCommand<>(
                this.name, List.of(aliases), this.arguments, this.subcommands, this.flags, this.executor);
    }

    @Override
    public List<String> getAllAliases() {
        final List<String> aliases = new ArrayList<>(this.aliases);
        aliases.add(this.name);
        return aliases;
    }

    @Override
    public List<Argument<C>> getArguments() {
        return Collections.unmodifiableList(this.arguments);
    }

    @Override
    public Command<C> withArguments(final List<Argument<C>> arguments) {
        return new SimpleCommand<>(
                this.name, this.aliases, new ArrayList<>(arguments), this.subcommands, this.flags, this.executor);
    }

    @Override
    public Command<C> withArgument(final Argument<C> argument) {
        final var arguments = new ArrayList<>(this.arguments);
        arguments.add(argument);
        return new SimpleCommand<>(this.name, this.aliases, arguments, this.subcommands, this.flags, this.executor);
    }

    @Override
    public List<Flag<C>> getFlags() {
        return Collections.unmodifiableList(this.flags);
    }

    @Override
    public Command<C> withFlags(final List<Flag<C>> flags) {
        return new SimpleCommand<>(
                this.name, this.aliases, this.arguments, this.subcommands, new ArrayList<>(flags), this.executor);
    }

    @Override
    public Command<C> withFlag(final Flag<C> flag) {
        final var flags = new ArrayList<>(this.flags);
        flags.add(flag);
        return new SimpleCommand<>(this.name, this.aliases, this.arguments, this.subcommands, flags, this.executor);
    }

    @Override
    public List<Command<C>> getSubcommands() {
        return Collections.unmodifiableList(this.subcommands);
    }

    @Override
    public Command<C> withSubcommands(final List<Command<C>> subcommands) {
        return new SimpleCommand<>(
                this.name, this.aliases, this.arguments, new ArrayList<>(subcommands), this.flags, this.executor);
    }

    @Override
    public Command<C> withSubcommand(final Command<C> subcommand) {
        final var subcommands = new ArrayList<>(this.subcommands);
        subcommands.add(subcommand);
        return new SimpleCommand<>(this.name, this.aliases, this.arguments, subcommands, this.flags, this.executor);
    }

    @Override
    public Optional<CommandExecutor<C>> getExecutor() {
        return Optional.ofNullable(this.executor);
    }

    @Override
    public Command<C> withExecutor(final @Nullable CommandExecutor<C> executor) {
        return new SimpleCommand<>(this.name, this.aliases, this.arguments, this.subcommands, this.flags, executor);
    }
}
