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
package fr.xpdustry.distributor.api.scheduler;

import arc.util.Time;

/**
 * A {@code PluginTimeSource} provides the current time in milliseconds.
 */
@FunctionalInterface
public interface PluginTimeSource {

    /**
     * Returns a {@code PluginTimeSource} using {@link Time#globalTime} to provide the current time.
     */
    static PluginTimeSource arc() {
        return () -> (long) (Time.globalTime * 16);
    }

    /**
     * Returns a {@code PluginTimeSource} using {@link System#currentTimeMillis()} to provide the current time.
     */
    static PluginTimeSource standard() {
        return System::currentTimeMillis;
    }

    /**
     * Returns the current time in milliseconds.
     */
    long getCurrentMillis();
}