package com.dhy.duck.anntation;

import com.dhy.duck.framework.MyReferenceBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.env.Environment;
import org.springframework.core.type.ClassMetadata;

import java.util.Arrays;
import java.util.Set;

/**
 * @Project rduck
 * @Description 主要用途描述
 * @Author lvaolin
 * @Date 2022/9/5 下午5:33
 */
@Slf4j
public class MyClassPathScanner extends ClassPathBeanDefinitionScanner {
    public MyClassPathScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public MyClassPathScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters) {
        super(registry, useDefaultFilters);
    }

    public MyClassPathScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters, Environment environment) {
        super(registry, useDefaultFilters, environment);
    }
    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface();
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {

//        addIncludeFilter((metadataReader, metadataReaderFactory) -> {
//            ClassMetadata metadata = metadataReader.getClassMetadata();
//            if (metadata.isInterface()) {
//                return true;
//            }
//            return false;
//        });


        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        if (beanDefinitions.isEmpty()) {

        } else {
            this.processBeanDefinitions(beanDefinitions);
        }

        return beanDefinitions;
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        beanDefinitions.forEach((beanDefinitionHolder)->{
            System.out.println(beanDefinitionHolder.getBeanName());

            /** 不能转为RootBeanDefinition
             * RootBeanDefinition beanDefinition = (RootBeanDefinition) beanDefinitionHolder.getBeanDefinition();
             *      class org.springframework.context.annotation.ScannedGenericBeanDefinition cannot be cast to class org.springframework.beans.factory.support.RootBeanDefinition
             */
            ScannedGenericBeanDefinition beanDefinition = (ScannedGenericBeanDefinition) beanDefinitionHolder.getBeanDefinition();
            String beanClassName = beanDefinition.getBeanClassName();
            //重构bean定义
            /**
             * 如果采用definition.getPropertyValues()方式的话，
             *  类似definition.getPropertyValues().add("interfaceType", beanClazz);
             *  则要求在FactoryBean（本应用中即CustomFactoryBean）提供setter方法，否则会注入失败
             *  如果采用definition.getConstructorArgumentValues()，
             *  则FactoryBean中需要提供包含该属性的构造方法，否则会注入失败
             */

            //这一部分可以参考mybatis源码 设置ConstructorArgumentValues()会通过构造器初始化对象
            beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(beanClassName);
            //这一步不再介绍牵涉到aop的知识,后续会给出详细代码
            beanDefinition.setBeanClass(MyReferenceBean.class);
            //设置通过类型自动装配,这里采用的是byType方式注入，类似的还有byName等
            beanDefinition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);

        });



    }
}
