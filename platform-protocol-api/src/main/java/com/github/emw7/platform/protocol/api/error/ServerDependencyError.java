package com.github.emw7.platform.protocol.api.error;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Target;

@Documented
@Inherited
@Target(ElementType.TYPE)
public @interface ServerDependencyError {
}
