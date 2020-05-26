package com.android.toolkits.methodtime;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author liujc
 * @date 2020/3/16
 * @Description 对点击事件做限制
 */
@Target({ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ClickLimit {

    int value() default 500;
}