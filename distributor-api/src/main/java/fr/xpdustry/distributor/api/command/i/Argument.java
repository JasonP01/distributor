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
import java.util.Optional;
import org.checkerframework.checker.nullness.qual.Nullable;

// TODO add a metadata field ?
public interface Argument<C> {

    static <C> Argument<C> required(final String name, final ArgumentParser<C, ?> parser) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    static <C> Argument<C> optional(final String name, final ArgumentParser<C, ?> parser) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    String getName();

    Argument<C> withName(String name);

    Type getType();

    Argument<C> withType(Type type);

    ArgumentParser<C, ?> getParser();

    <T> Argument<C> withParser(final ArgumentParser<C, T> parser);

    Optional<String> getDefaultValue();

    Argument<C> withDefaultValue(final @Nullable String defaultValue);

    enum Type {
        REQUIRED,
        OPTIONAL
    }
}
