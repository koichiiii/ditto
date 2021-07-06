/*
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
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
package org.eclipse.ditto.policies.model;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.eclipse.ditto.base.model.acks.AcknowledgementRequest;
import org.eclipse.ditto.base.model.common.DittoDuration;
import org.eclipse.ditto.base.model.json.Jsonifiable;
import org.eclipse.ditto.json.JsonArray;
import org.eclipse.ditto.json.JsonFactory;
import org.eclipse.ditto.json.JsonFieldDefinition;
import org.eclipse.ditto.json.JsonObject;

/**
 * Represents announcement settings of a {@link Subject}.
 *
 * @since 2.0.0
 */
@Immutable
public interface SubjectAnnouncement extends Jsonifiable<JsonObject> {

    /**
     * Returns a new {@link SubjectAnnouncement} with the given configuration.
     *
     * @param beforeExpiry duration before expiry when an announcement should be sent, or null if no announcement should
     * be sent.
     * @param whenDeleted whether an announcement should be sent when the subject is deleted.
     * @return the new {@link SubjectAnnouncement}.
     */
    static SubjectAnnouncement of(@Nullable final DittoDuration beforeExpiry, final boolean whenDeleted) {
        return new ImmutableSubjectAnnouncement(beforeExpiry, whenDeleted, Collections.emptyList(),
                null);
    }

    /**
     * Returns a new {@link SubjectAnnouncement} with the given configuration.
     *
     * @param beforeExpiry duration before expiry when an announcement should be sent, or null if no announcement should
     * be sent.
     * @param whenDeleted whether an announcement should be sent when the subject is deleted.
     * @param requestedAcksBeforeExpiry acknowledgement requests for subject deletion announcements before expiry.
     * @param requestedAcksTimeout timeout of acknowledgement requests.
     * @return the new {@link SubjectAnnouncement}.
     */
    static SubjectAnnouncement of(@Nullable final DittoDuration beforeExpiry, final boolean whenDeleted,
            final List<AcknowledgementRequest> requestedAcksBeforeExpiry,
            @Nullable DittoDuration requestedAcksTimeout) {
        return new ImmutableSubjectAnnouncement(beforeExpiry, whenDeleted, requestedAcksBeforeExpiry,
                requestedAcksTimeout);
    }

    /**
     * Returns a new {@link SubjectAnnouncement} with the configuration given in the
     * JSON object.
     *
     * @param jsonObject the JSON representation.
     * @return the new {@link SubjectAnnouncement}.
     */
    static SubjectAnnouncement fromJson(final JsonObject jsonObject) {
        return ImmutableSubjectAnnouncement.fromJson(jsonObject);
    }

    /**
     * Returns the duration before expiry when an announcement should be sent, or an empty optional if no announcement
     * should be sent before expiry.
     *
     * @return duration of the optional announcement window.
     */
    Optional<DittoDuration> getBeforeExpiry();

    /**
     * Returns whether an announcement should be sent when the subject is deleted.
     *
     * @return whether the expiry is expired or not.
     */
    boolean isWhenDeleted();

    /**
     * Returns acknowledgement requests to fulfill for subject deletion announcements before expiry.
     *
     * @return the acknowledgement requests.
     */
    List<AcknowledgementRequest> getRequestedAcksBeforeExpiry();

    /**
     * Returns timeout of acknowledgement requests.
     *
     * @return the timeout.
     */
    Optional<DittoDuration> getRequestedAcksTimeout();

    /**
     * Returns a copy of this object with the field {@code beforeExpiry} replaced.
     *
     * @param beforeExpiry the new value.
     * @return the copy.
     */
    SubjectAnnouncement setBeforeExpiry(@Nullable DittoDuration beforeExpiry);

    /**
     * Fields of the JSON representation of a {@code SubjectAnnouncement} object.
     */
    final class JsonFields {

        /**
         * Field to store the duration before expiry when an announcement should be sent.
         */
        public static final JsonFieldDefinition<String> BEFORE_EXPIRY =
                JsonFactory.newStringFieldDefinition("beforeExpiry");

        /**
         * Field to store whether an announcement should be sent upon subject deletion.
         */
        public static final JsonFieldDefinition<Boolean> WHEN_DELETED =
                JsonFactory.newBooleanFieldDefinition("whenDeleted");

        /**
         * Field to store requested acknowledgements for announcements before expiry.
         */
        public static final JsonFieldDefinition<JsonArray> REQUESTED_ACKS_BEFORE_EXPIRY =
                JsonFactory.newJsonArrayFieldDefinition("requestedAcks/beforeExpiry");

        /**
         * Field to store timeout waiting for requested acknowledgements.
         */
        public static final JsonFieldDefinition<String> REQUESTED_ACKS_TIMEOUT =
                JsonFactory.newStringFieldDefinition("requestedAcks/timeout");

        private JsonFields() {}
    }
}
