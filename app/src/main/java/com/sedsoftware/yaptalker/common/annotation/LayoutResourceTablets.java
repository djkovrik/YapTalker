package com.sedsoftware.yaptalker.common.annotation;

import android.support.annotation.LayoutRes;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LayoutResourceTablets {

    @LayoutRes int normalValue();

    @LayoutRes int tabletsValue();
}
