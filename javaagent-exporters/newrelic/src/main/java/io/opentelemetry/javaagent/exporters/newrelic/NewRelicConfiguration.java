/*
 * Copyright 2020 New Relic Corporation. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.javaagent.exporters.newrelic;

import java.util.Properties;

class NewRelicConfiguration {
  // TODO use OTel naming convention prefixes: otel. and OTEL_
  static final String DEFAULT_NEW_RELIC_SERVICE_NAME = "(unknown service)";

  // System properties
  static final String OTEL_NEW_RELIC_API_KEY_PROP = "otel.newrelic.api.key";
  static final String OTEL_NEW_RELIC_ENABLE_AUDIT_LOGGING_PROP = "otel.newrelic.enable.audit.logging";
  static final String OTEL_NEW_RELIC_SERVICE_NAME_PROP = "otel.newrelic.service.name";
  static final String OTEL_NEW_RELIC_TRACE_URI_OVERRIDE_PROP = "otel.newrelic.trace.uri.override";
  static final String OTEL_NEW_RELIC_METRIC_URI_OVERRIDE_PROP = "otel.newrelic.metric.uri.override";
  // this should not be used, now that we have both span and metric exporters. Support is here
  // for any users who might still be using it.
  static final String OTEL_NEW_RELIC_URI_OVERRIDE_PROP = "otel.newrelic.uri.override";

  // Environment variables
  static final String OTEL_NEW_RELIC_API_KEY_ENV = "OTEL_NEW_RELIC_API_KEY";
  static final String OTEL_NEW_RELIC_ENABLE_AUDIT_LOGGING_ENV = "OTEL_NEW_RELIC_ENABLE_AUDIT_LOGGING";
  static final String OTEL_NEW_RELIC_SERVICE_NAME_ENV = "OTEL_NEW_RELIC_SERVICE_NAME";
  static final String OTEL_NEW_RELIC_TRACE_URI_OVERRIDE_ENV = "OTEL_NEW_RELIC_TRACE_URI_OVERRIDE";
  static final String OTEL_NEW_RELIC_METRIC_URI_OVERRIDE_ENV = "OTEL_NEW_RELIC_METRIC_URI_OVERRIDE";
  // this should not be used, now that we have both span and metric exporters. Support is here
  // for any users who might still be using it.
  static final String OTEL_NEW_RELIC_URI_OVERRIDE_ENV = "OTEL_NEW_RELIC_URI_OVERRIDE";

  private final Properties config;

  NewRelicConfiguration(Properties config) {
    this.config = config;
  }

  String getApiKey() {
    String env = System.getenv(OTEL_NEW_RELIC_API_KEY_ENV);
    return env != null ? env : config.getProperty(OTEL_NEW_RELIC_API_KEY_PROP, "");
  }

  boolean shouldEnableAuditLogging() {
    String env = System.getenv(OTEL_NEW_RELIC_ENABLE_AUDIT_LOGGING_ENV);
    return env != null ? Boolean.valueOf(env) : Boolean.valueOf(config.getProperty(
        OTEL_NEW_RELIC_ENABLE_AUDIT_LOGGING_PROP, "false"));
  }

  // note: newrelic.service.name key will not be required once service.name is guaranteed to be
  // provided via the Resource in the SDK.  See
  // https://github.com/newrelic/opentelemetry-exporter-java/issues/62
  // for the tracking issue.
  static String getServiceName(Properties config) {
    String env = System.getenv(OTEL_NEW_RELIC_SERVICE_NAME_ENV);
    return env != null ? env : config.getProperty(OTEL_NEW_RELIC_SERVICE_NAME_PROP, DEFAULT_NEW_RELIC_SERVICE_NAME);
  }

  String getServiceName() {
    return getServiceName(config);
  }

  boolean isMetricUriSpecified() {
    return isSpecified(getMetricUri());
  }

  String getMetricUri() {
    String env = System.getenv(OTEL_NEW_RELIC_METRIC_URI_OVERRIDE_ENV);
    return env != null ? env : config.getProperty(OTEL_NEW_RELIC_METRIC_URI_OVERRIDE_PROP, "");
  }

  boolean isTraceUriSpecified() {
    return isSpecified(getTraceUri());
  }

  String getTraceUri() {
    String deprecateEnv = System.getenv(OTEL_NEW_RELIC_URI_OVERRIDE_ENV);
    String deprecatedUriOverride = deprecateEnv != null ? deprecateEnv : config.getProperty(
        OTEL_NEW_RELIC_URI_OVERRIDE_PROP, "");

    String env = System.getenv(OTEL_NEW_RELIC_TRACE_URI_OVERRIDE_ENV);
    return env != null ? env : config.getProperty(OTEL_NEW_RELIC_TRACE_URI_OVERRIDE_PROP, deprecatedUriOverride);
  }

  private boolean isSpecified(String s) {
    return s != null && !s.isEmpty();
  }
}
