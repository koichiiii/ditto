/*
 * Copyright (c) 2023 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.ditto.connectivity.model.mqtt;

import java.util.Objects;
import java.util.OptionalLong;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

/**
 * Representation of the MQTT 5 message expiry interval.
 * The minimum seconds is {@value MIN_INTERVAL_SECONDS}.
 * The maximum seconds is {@value MAX_INTERVAL_SECONDS}.
 */
@Immutable
public class MessageExpiryInterval {

    public static final long MIN_INTERVAL_SECONDS = 1L;

    public static final long MAX_INTERVAL_SECONDS = 4_294_967_295L;

    @Nullable private final Long seconds;

    private MessageExpiryInterval(@Nullable final Long seconds) {
        this.seconds = seconds;
    }

    public static MessageExpiryInterval of(final long seconds)
            throws IllegalMessageExpiryIntervalSecondsException {
        if (seconds < MIN_INTERVAL_SECONDS || seconds > MAX_INTERVAL_SECONDS) {
            throw new IllegalMessageExpiryIntervalSecondsException(
                    String.format("Expected message expiry interval seconds to be within [%d, %d] but it was <%d>.",
                            MIN_INTERVAL_SECONDS,
                            MAX_INTERVAL_SECONDS,
                            seconds),
                    null);
        }

        return new MessageExpiryInterval(seconds);
    }

    public static MessageExpiryInterval empty() {
        return new MessageExpiryInterval(null);
    }

    public OptionalLong getAsOptionalLong() {
        return seconds != null ?
                OptionalLong.of(seconds) :
                OptionalLong.empty();
    }

    @Override
    public boolean equals(@Nullable final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MessageExpiryInterval that = (MessageExpiryInterval) o;
        return Objects.equals(seconds, that.seconds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seconds);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [" +
                "seconds=" + seconds +
                "]";
    }
}
