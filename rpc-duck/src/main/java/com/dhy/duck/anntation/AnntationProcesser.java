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
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry,false);
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
            Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents(basePackage);
            // 构建信息
            if (!candidateComponents.isEmpty()) {
                for (BeanDefinition bd : candidateComponents) {
                    // 注入数据
                    String className = bd.getBeanClassName();

//                    try {
//                        // 这里如果直接使用Class.forName(className) 可能会找不到类
//                        Class clazz = resourceLoader.getClassLoader().loadClass(className);
//                        BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition(clazz);
//                        String beanId = importBeanNameGenerator.generateBeanName(bd, registry);
//
//                        // 这里还可以设置依赖项，是否懒加载，构造方法参数等等与类定义有关的参数
//                        definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
//                        AbstractBeanDefinition beanDefinition = definition.getBeanDefinition();
//                        beanDefinition.setAttribute(FactoryBean.OBJECT_TYPE_ATTRIBUTE, className);
//                        BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, beanId);
//                        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
//                    } catch (ClassNotFoundException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        }
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
