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

import fr.xpdustry.distributor.api.command.i.parser.ArgumentParser;
import java.util.List;
import java.util.Optional;
import org.checkerframework.checker.nullness.qual.Nullable;

// TODO add a metadata field ?
public interface Flag<C> {

    static <C> Flag<C> single(final String name) {
        throw new UnsupportedOperationException();
    }

    static <C> Flag<C> single(final String name, final ArgumentParser<C, ?> parser) {
        throw new UnsupportedOperationException();
    }

    static <C> Flag<C> repeatable(final String name) {
        throw new UnsupportedOperationException();
    }

    static <C> Flag<C> repeatable(final String name, final ArgumentParser<C, ?> parser) {
        throw new UnsupportedOperationException();
    }

    String getName();

    Flag<C> withName(final String name);

    List<String> getAliases();

    List<String> getAllAliases();

    Flag<C> withAliases(final String... aliases);

    Type getType();

    Flag<C> withType(final Type type);

    Optional<ArgumentParser<C, ?>> getValueParser();

    Flag<C> withValueParser(final @Nullable ArgumentParser<C, ?> parser);

    enum Type {
        SINGLE,
        REPEATABLE
    }
}
