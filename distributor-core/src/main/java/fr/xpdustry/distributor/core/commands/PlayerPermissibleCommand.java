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
package fr.xpdustry.distributor.core.commands;

import fr.xpdustry.distributor.api.permission.PermissibleManager;
import fr.xpdustry.distributor.api.permission.PermissionService;
import fr.xpdustry.distributor.api.permission.PlayerPermissible;
import fr.xpdustry.distributor.api.util.Magik;
import java.util.Optional;

public final class PlayerPermissibleCommand extends PermissibleCommand<PlayerPermissible> {

    public PlayerPermissibleCommand(final PermissionService service) {
        super(service);
    }

    @Override
    protected Optional<PlayerPermissible> findPermissible(final String input) {
        final var players = Magik.findPlayers(input);
        if (players.isEmpty() && Magik.isUuid(input)) {
            return Optional.of(this.getManager().findOrCreateById(input));
        } else if (players.size() == 1) {
            return Optional.of(this.getManager().findOrCreateById(players.get(0).uuid()));
        } else {
            return Optional.empty();
        }
    }

    @Override
    protected PermissibleManager<PlayerPermissible> getManager() {
        return this.getPermissionManager().getPlayerPermissionManager();
    }
}
