package com.dhy.duck.anntation;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;
import java.util.Set;

/**
 * @Project rduck
 * @Description 注解处理器
 * @Author lvaolin
 * @Date 2022/8/28 下午10:23
 */
public class AnntationProcesser implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {

        Map<String, Object> annotationAttributes = annotationMetadata.getAnnotationAttributes("com.dhy.duck.anntation.Duck");
        System.out.println(annotationAttributes.get("enable"));
        System.out.println(annotationAttributes.get("scanBasePackage"));
    }
}
