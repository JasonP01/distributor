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
package fr.xpdustry.distributor.api.plugin;

import arc.util.CommandHandler;

public interface PluginListener {

    default void onPluginInit(final ExtendedPlugin plugin) {}

    default void onPluginServerCommandsRegistration(final ExtendedPlugin plugin, final CommandHandler handler) {}

    default void onPluginClientCommandsRegistration(final ExtendedPlugin plugin, final CommandHandler handler) {}

    default void onPluginLoad(final ExtendedPlugin plugin) {}

    default void onPluginUpdate(final ExtendedPlugin plugin) {}

    default void onPluginExit(final ExtendedPlugin plugin) {}
}
