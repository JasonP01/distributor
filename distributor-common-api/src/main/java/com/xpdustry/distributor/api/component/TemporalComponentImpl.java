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
package com.xpdustry.distributor.api.component;

import com.xpdustry.distributor.api.component.style.ComponentStyle;
import java.time.Instant;
import java.time.format.FormatStyle;
import java.time.temporal.Temporal;

final class TemporalComponentImpl extends AbstractComponent<TemporalComponent, TemporalComponent.Builder>
        implements TemporalComponent {

    private final Temporal temporal;
    private final TemporalFormat fallbackStyle;

    TemporalComponentImpl(final ComponentStyle style, final Temporal temporal, final TemporalFormat fallbackStyle) {
        super(style);
        this.temporal = temporal;
        this.fallbackStyle = fallbackStyle;
    }

    @Override
    public Temporal getTemporal() {
        return this.temporal;
    }

    @Override
    public TemporalFormat getFallbackFormat() {
        return fallbackStyle;
    }

    @Override
    public TemporalComponent.Builder toBuilder() {
        return new Builder(this);
    }

    static final class Builder extends AbstractComponent.Builder<TemporalComponent, TemporalComponent.Builder>
            implements TemporalComponent.Builder {

        private Temporal temporal;
        private TemporalFormat fallbackFormat = TemporalFormat.ofDateTime(FormatStyle.SHORT);

        public Builder() {
            this.temporal = Instant.now();
        }

        public Builder(final TemporalComponent component) {
            super(component);
            this.temporal = component.getTemporal();
        }

        @Override
        public Builder setTemporal(final Temporal temporal) {
            this.temporal = temporal;
            return this;
        }

        @Override
        public Builder setFallbackFormat(final TemporalFormat fallbackFormat) {
            this.fallbackFormat = fallbackFormat;
            return this;
        }

        @Override
        public TemporalComponent build() {
            return new TemporalComponentImpl(style.build(), temporal, fallbackFormat);
        }
    }
}
