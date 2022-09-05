package com.dhy.duck.anntation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @Project rduck
 * @Description 注解处理器
 * @Author lvaolin
 * @Date 2022/8/28 下午10:23
 */
@Slf4j
public class AnntationProcesser implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {
    private ResourceLoader resourceLoader;
    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata,
                                        BeanDefinitionRegistry registry) {

        Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes("com.dhy.duck.anntation.Duck");
        System.out.println(annotationAttributes.get("enable"));
        System.out.println(annotationAttributes.get("scanBasePackage"));

        // 构建一个classPath扫描器
        MyClassPathScanner scanner = new MyClassPathScanner(registry,false);
        scanner.setResourceLoader(this.resourceLoader);
        AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(MyReference.class);
        scanner.addIncludeFilter(annotationTypeFilter);

        // 获取需要扫描的包路径
        List<String> basePackages = new ArrayList<>();
        Map<String, Object> attributes = metadata.getAnnotationAttributes(Duck.class.getCanonicalName());
        for (String pkg : (String[]) attributes.get("scanBasePackage")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        // 添加当前项目包
        basePackages.add(ClassUtils.getPackageName(metadata.getClassName()));
        log.info(Arrays.toString(basePackages.toArray()));
        for (String basePackage : basePackages) {
            Set<BeanDefinitionHolder> candidateComponents = scanner.doScan(basePackage);
        }
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
