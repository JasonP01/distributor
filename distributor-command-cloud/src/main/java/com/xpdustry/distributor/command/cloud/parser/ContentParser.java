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
package com.xpdustry.distributor.command.cloud.parser;

import com.xpdustry.distributor.command.cloud.ArcCaptionKeys;
import com.xpdustry.distributor.content.TypedContentType;
import java.util.Locale;
import mindustry.Vars;
import mindustry.ctype.MappableContent;
import org.incendo.cloud.caption.CaptionVariable;
import org.incendo.cloud.component.CommandComponent;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.exception.parsing.ParserException;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.parser.ParserDescriptor;

public final class ContentParser<C, T extends MappableContent> implements ArgumentParser<C, T> {

    public static <C, T extends MappableContent> ParserDescriptor<C, T> contentParser(
            final TypedContentType<T> contentType) {
        return ParserDescriptor.of(new ContentParser<>(contentType), contentType.getContentTypeClass());
    }

    public static <C, T extends MappableContent> CommandComponent.Builder<C, T> contentComponent(
            final TypedContentType<T> contentType) {
        return CommandComponent.<C, T>builder().parser(contentParser(contentType));
    }

    private final TypedContentType<T> contentType;

    public ContentParser(final TypedContentType<T> contentType) {
        this.contentType = contentType;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ArgumentParseResult<T> parse(final CommandContext<C> ctx, final CommandInput input) {
        final var name = input.readString().toLowerCase(Locale.ROOT);
        final var content = Vars.content.getByName(contentType.getContentType(), name);
        return (content == null)
                ? ArgumentParseResult.failure(new ContentParseException(this.getClass(), ctx, name, contentType))
                : ArgumentParseResult.success((T) content);
    }

    @SuppressWarnings("serial")
    public static final class ContentParseException extends ParserException {

        private final String input;
        private final TypedContentType<?> contentType;

        @SuppressWarnings("rawtypes")
        public ContentParseException(
                final Class<? extends ContentParser> clazz,
                final CommandContext<?> ctx,
                final String input,
                final TypedContentType<?> contentType) {
            super(
                    clazz,
                    ctx,
                    ArcCaptionKeys.ARGUMENT_PARSE_FAILURE_CONTENT,
                    CaptionVariable.of("input", input),
                    CaptionVariable.of("type", contentType.getContentType().name()));
            this.input = input;
            this.contentType = contentType;
        }

        public String getInput() {
            return input;
        }

        public TypedContentType<?> getContentType() {
            return contentType;
        }
    }
}
