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

public interface CommandContext<C> {

    static <C> CommandContext<C> create(final CommandRegistry<C> registry, final C caller, final String input) {
        throw new UnsupportedOperationException();
    }

    <T> T getArgument(final String name);

    <T> T getArgument(final String name, final T defaultValue);

    boolean hasFlag(final String name);

    <T> T getFlagValue(final String name);

    <T> T getFlagValue(final String name, final T defaultValue);

    <T> List<T> getFlagValues(final String name);

    CommandRegistry<C> getRegistry();

    C getCaller();

    String getInput();
}
