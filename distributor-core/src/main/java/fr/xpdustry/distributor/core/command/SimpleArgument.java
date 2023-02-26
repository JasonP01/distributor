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
import fr.xpdustry.distributor.api.command.i.parser.ArgumentParser;
import java.util.Optional;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class SimpleArgument<C> implements Argument<C> {

    private final String name;
    private final ArgumentParser<C, ?> parser;
    private final Type type;
    private final @Nullable String defaultValue;

    private SimpleArgument(
            final String name,
            final ArgumentParser<C, ?> parser,
            final Type type,
            final @Nullable String defaultValue) {
        this.name = name;
        this.parser = parser;
        this.type = type;
        this.defaultValue = defaultValue;
    }

    public SimpleArgument(final String name, final ArgumentParser<C, ?> parser) {
        this(name, parser, Type.REQUIRED, null);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Argument<C> withName(final String name) {
        return new SimpleArgument<>(name, this.parser, this.type, this.defaultValue);
    }

    @Override
    public Type getType() {
        return this.type;
    }

    @Override
    public Argument<C> withType(final Type type) {
        return new SimpleArgument<>(this.name, this.parser, type, this.defaultValue);
    }

    @Override
    public ArgumentParser<C, ?> getParser() {
        return this.parser;
    }

    @Override
    public <T> Argument<C> withParser(final ArgumentParser<C, T> parser) {
        return new SimpleArgument<>(this.name, parser, this.type, this.defaultValue);
    }

    @Override
    public Optional<String> getDefaultValue() {
        return Optional.ofNullable(this.defaultValue);
    }

    @Override
    public Argument<C> withDefaultValue(final @Nullable String defaultValue) {
        return new SimpleArgument<>(this.name, this.parser, this.type, defaultValue);
    }
}
