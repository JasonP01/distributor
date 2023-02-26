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

import fr.xpdustry.distributor.api.command.i.CommandContext;
import fr.xpdustry.distributor.api.command.i.CommandRegistry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class SimpleCommandContext<C> implements CommandContext<C> {

    private final CommandRegistry<C> registry;
    private final C caller;
    private final String input;
    // TODO Use typesafe key ?
    private final Map<String, Object> arguments = new HashMap<>();
    private final Map<String, List<Object>> flags = new HashMap<>();

    SimpleCommandContext(final CommandRegistry<C> registry, final C caller, final String input) {
        this.registry = registry;
        this.caller = caller;
        this.input = input;
    }

    // TODO Solve the NullAway problem, checker-framework does not have a UnknownNullness annotation, it annoys me
    @SuppressWarnings({"unchecked", "NullAway"})
    @Override
    public <T> T getArgument(final String name) {
        return (T) this.arguments.get(name);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getArgument(final String name, final T defaultValue) {
        return (T) this.arguments.getOrDefault(name, defaultValue);
    }

    @Override
    public boolean hasFlag(final String name) {
        return this.flags.containsKey(name);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getFlagValue(final String name) {
        final List<Object> values = this.flags.get(name);
        if (values == null || values.isEmpty()) {
            throw new IllegalArgumentException("Flag " + name + " is not present");
        }
        return (T) values.get(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getFlagValue(final String name, final T defaultValue) {
        final List<Object> values = this.flags.get(name);
        if (values == null || values.isEmpty()) {
            return defaultValue;
        }
        return (T) values.get(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> getFlagValues(final String name) {
        final List<Object> values = this.flags.get(name);
        if (values == null) {
            throw new IllegalArgumentException("Flag " + name + " is not present");
        }
        return Collections.unmodifiableList((List<T>) values);
    }

    @Override
    public CommandRegistry<C> getRegistry() {
        return this.registry;
    }

    @Override
    public C getCaller() {
        return this.caller;
    }

    @Override
    public String getInput() {
        return this.input;
    }

    void setArgument(final String name, final Object value) {
        this.arguments.put(name, value);
    }

    void addFlagValue(final String name, final @Nullable Object value) {
        this.flags.computeIfAbsent(name, k -> new ArrayList<>()).add(value);
    }
}
