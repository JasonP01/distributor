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
package fr.xpdustry.distributor.api.command.i;

import java.util.List;
import java.util.Optional;
import org.checkerframework.checker.nullness.qual.Nullable;

// TODO add a metadata field ?
public interface Command<C> {

    static <C> Command<C> create(final String name) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    String getName();

    Command<C> withName(final String name);

    List<String> getAliases();

    Command<C> withAliases(final List<String> aliases);

    Command<C> withAliases(final String... aliases);

    List<String> getAllAliases();

    List<Argument<C>> getArguments();

    Command<C> withArguments(final List<Argument<C>> arguments);

    Command<C> withArgument(final Argument<C> argument);

    List<Flag<C>> getFlags();

    Command<C> withFlags(final List<Flag<C>> flags);

    Command<C> withFlag(final Flag<C> flag);

    List<Command<C>> getSubcommands();

    Command<C> withSubcommands(final List<Command<C>> subcommands);

    Command<C> withSubcommand(final Command<C> subcommand);

    Optional<CommandExecutor<C>> getExecutor();

    Command<C> withExecutor(final @Nullable CommandExecutor<C> executor);
}
