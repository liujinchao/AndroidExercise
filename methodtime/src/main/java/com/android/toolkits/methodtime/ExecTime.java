package com.android.toolkits.methodtime;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: liujc
 * @date: 2020/3/16
 * @Description: TODO(这里用一句话描述这个类的作用)
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExecTime {
}
