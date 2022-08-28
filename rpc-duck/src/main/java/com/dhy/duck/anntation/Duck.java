package com.dhy.duck.anntation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Project rduck
 * @Description 主要用途描述
 * @Author lvaolin
 * @Date 2022/8/28 下午10:17
 */
@Import(AnntationProcesser.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Duck {
    boolean enable() default true;
    String scanBasePackage();
}
