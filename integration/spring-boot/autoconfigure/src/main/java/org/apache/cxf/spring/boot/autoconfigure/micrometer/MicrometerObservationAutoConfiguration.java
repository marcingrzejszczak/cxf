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

package org.apache.cxf.spring.boot.autoconfigure.micrometer;

import org.apache.cxf.tracing.micrometer.MessageInObservationConvention;
import org.apache.cxf.tracing.micrometer.MessageOutObservationConvention;
import org.apache.cxf.tracing.micrometer.ObservationClientFeature;
import org.apache.cxf.tracing.micrometer.ObservationFeature;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.micrometer.observation.ObservationRegistry;

@Configuration
@AutoConfigureAfter(ObservationAutoConfiguration.class)
@ConditionalOnClass({ ObservationRegistry.class, ObservationFeature.class })
@ConditionalOnProperty(name = "cxf.observation.enabled", matchIfMissing = true)
@ConditionalOnBean(ObservationRegistry.class)
public class MicrometerObservationAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ObservationFeature.class)
    public ObservationFeature observationFeature(ObservationRegistry registry,
                                                 ObjectProvider<MessageInObservationConvention> convention) {
        return new ObservationFeature(registry, convention.getIfAvailable());
    }

    @Bean
    @ConditionalOnMissingBean(ObservationClientFeature.class)
    public ObservationClientFeature observationClientFeature(ObservationRegistry registry,
                                                             ObjectProvider<MessageOutObservationConvention> convention) {
        return new ObservationClientFeature(registry, convention.getIfAvailable());
    }
}
