package com.azat4dev.booking.shared.infrastructure.api.bus.annotations;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RepliesWith {

    Class<?>[] value();
}
