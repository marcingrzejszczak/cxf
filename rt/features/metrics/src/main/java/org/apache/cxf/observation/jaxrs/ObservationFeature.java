/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.cxf.observation.jaxrs;

import org.apache.cxf.jaxrs.ext.Nullable;
import org.apache.cxf.observation.MessageInObservationConvention;

import io.micrometer.observation.ObservationRegistry;
import jakarta.ws.rs.core.Feature;
import jakarta.ws.rs.core.FeatureContext;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ObservationFeature implements Feature {
    private final ObservationRegistry observationRegistry;

    private final MessageInObservationConvention messageInObservationConvention;

    public ObservationFeature(final ObservationRegistry observationRegistry) {
        this(observationRegistry, null);
    }

    public ObservationFeature(final ObservationRegistry observationRegistry,
                              @Nullable MessageInObservationConvention messageInObservationConvention) {
        this.observationRegistry = observationRegistry;
        this.messageInObservationConvention = messageInObservationConvention;
    }

    @Override
    public boolean configure(FeatureContext context) {
        context.register(new ObservationProvider(observationRegistry, messageInObservationConvention));
        context.register(new ObservationContextProvider(observationRegistry));
        return true;
    }
}
