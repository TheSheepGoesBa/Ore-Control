/*
 * MIT License
 *
 * Copyright (c) 2019 - 2022 Marvin (DerFrZocker)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package de.derfrzocker.feature.common.value.bool;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.derfrzocker.feature.api.util.Parser;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public class FixedBooleanType extends BooleanType {

    public static final NamespacedKey KEY = NamespacedKey.fromString("feature:fixed_boolean");
    public static final FixedBooleanType INSTANCE = new FixedBooleanType();
    public static final Parser<BooleanValue> PARSER = new Parser<>() {
        @Override
        public JsonElement toJson(BooleanValue v) {
            FixedBooleanValue value = (FixedBooleanValue) v;
            JsonObject jsonObject = new JsonObject();

            jsonObject.addProperty("value", value.getValue());

            return jsonObject;
        }

        @Override
        public FixedBooleanValue fromJson(JsonElement jsonElement) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            boolean value = false;
            if (jsonObject.has("value")) {
                value = jsonObject.get("value").getAsBoolean();
            }

            return new FixedBooleanValue(value);
        }
    };

    private FixedBooleanType() {
    }

    @Override
    public Parser<BooleanValue> getParser() {
        return PARSER;
    }

    @Override
    public FixedBooleanValue createNewValue() {
        return new FixedBooleanValue(false);
    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        return KEY;
    }
}
