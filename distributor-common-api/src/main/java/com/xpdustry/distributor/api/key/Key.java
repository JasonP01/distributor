/*
 * Distributor, a feature-rich framework for Mindustry plugins.
 *
 * Copyright (C) 2024 Xpdustry
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
package com.xpdustry.distributor.api.key;

import com.xpdustry.distributor.internal.annotation.DistributorDataClass;
import org.immutables.value.Value;

@DistributorDataClass
@Value.Immutable
public interface Key {

    String MINDUSTRY_NAMESPACE = "mindustry";

    static Key of(final String name) {
        return KeyImpl.of(name, MINDUSTRY_NAMESPACE);
    }

    static Key of(final String name, final String namespace) {
        return KeyImpl.of(name, namespace);
    }

    String getName();

    String getNamespace();
}
