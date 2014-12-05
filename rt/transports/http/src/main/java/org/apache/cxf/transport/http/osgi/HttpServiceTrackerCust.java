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
package org.apache.cxf.transport.http.osgi;

import java.util.Properties;

import javax.servlet.Servlet;

import org.apache.cxf.transport.http.DestinationRegistry;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.http.HttpService;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

final class HttpServiceTrackerCust implements ServiceTrackerCustomizer {
    private static final String CXF_CONFIG_PID = "org.apache.cxf.osgi";
    private final DestinationRegistry destinationRegistry;
    private final BundleContext context;
    private ServiceRegistration servletPublisherReg;
    private ServletExporter servletExporter;

    HttpServiceTrackerCust(DestinationRegistry destinationRegistry, BundleContext context) {
        this.destinationRegistry = destinationRegistry;
        this.context = context;
    }

    @Override
    public void removedService(ServiceReference reference, Object service) {
        servletPublisherReg.unregister();
        try {
            servletExporter.updated(null);
        } catch (ConfigurationException e) {
            // Ignore
        }
    }

    @Override
    public void modifiedService(ServiceReference reference, Object service) {
    }

    @Override
    public Object addingService(ServiceReference reference) {
        HttpService httpService = (HttpService)context.getService(reference);
        Servlet servlet = new CXFNonSpringServlet(destinationRegistry , false);
        servletExporter = new ServletExporter(servlet, httpService);
        Properties servProps = new Properties();
        servProps.put(Constants.SERVICE_PID,  CXF_CONFIG_PID);
        servletPublisherReg = context.registerService(ManagedService.class.getName(),
                                                      servletExporter, servProps);
        return null;
    }
}