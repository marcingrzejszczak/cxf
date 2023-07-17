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

package org.apache.cxf.observation;

import org.apache.cxf.message.Exchange;
import org.apache.cxf.observation.CxfObservationDocumentation.LowCardinalityKeys;
import org.apache.cxf.service.model.BindingOperationInfo;

import io.micrometer.common.KeyValue;
import io.micrometer.common.KeyValues;

/**
 *
 */
public class DefaultMessageInObservationConvention implements MessageInObservationConvention {

    public static final DefaultMessageInObservationConvention INSTANCE = new DefaultMessageInObservationConvention();

    @Override
    public KeyValues getLowCardinalityKeyValues(MessageInContext context) {
        return CxfObservationConventionUtil.getLowCardinalityKeyValues(context.getEffectiveMessage());
    }

    @Override
    public String getName() {
        return "rpc.server.duration";
    }

    @Override
    public String getContextualName(MessageInContext context) {
        return CxfObservationConventionUtil.getContextualName(context.getMessage().getExchange());
    }
}
