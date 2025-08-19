// src/main/java/com/benjaminbatte/platform/common/web/versioning/ApiV1.java
package com.benjaminbatte.platform.common.web.versioning;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/** Marker annotation to attach a /v1 prefix to controller paths. */
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface ApiV1 {}
