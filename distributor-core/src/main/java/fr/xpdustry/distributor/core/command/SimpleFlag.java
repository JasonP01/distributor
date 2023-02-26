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

import fr.xpdustry.distributor.api.command.i.Flag;
import fr.xpdustry.distributor.api.command.i.parser.ArgumentParser;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class SimpleFlag<C> implements Flag<C> {

    private final String name;
    private final List<String> aliases;
    private final Type type;
    private final @Nullable ArgumentParser<C, ?> parser;

    private SimpleFlag(
            final String name,
            final List<String> aliases,
            final Type type,
            @Nullable final ArgumentParser<C, ?> parser) {
        this.name = name;
        this.aliases = aliases;
        this.type = type;
        this.parser = parser;
    }

    public SimpleFlag(final String name, final Type type) {
        this(name, List.of(), type, null);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Flag<C> withName(final String name) {
        return new SimpleFlag<>(name, this.aliases, this.type, this.parser);
    }

    @Override
    public List<String> getAliases() {
        return Collections.unmodifiableList(this.aliases);
    }

    @Override
    public List<String> getAllAliases() {
        final var aliases = new ArrayList<>(this.aliases);
        aliases.add(this.name);
        return aliases;
    }

    @Override
    public Flag<C> withAliases(final String... aliases) {
        return new SimpleFlag<>(this.name, List.of(aliases), this.type, this.parser);
    }

    @Override
    public Type getType() {
        return this.type;
    }

    @Override
    public Flag<C> withType(final Type type) {
        return new SimpleFlag<>(this.name, this.aliases, type, this.parser);
    }

    @Override
    public Optional<ArgumentParser<C, ?>> getValueParser() {
        return Optional.ofNullable(this.parser);
    }

    @Override
    public Flag<C> withValueParser(final @Nullable ArgumentParser<C, ?> parser) {
        return new SimpleFlag<>(this.name, this.aliases, this.type, parser);
    }
}
