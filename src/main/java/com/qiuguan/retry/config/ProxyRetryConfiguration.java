package com.qiuguan.retry.config;

import com.qiuguan.retry.ann.Retryable;
import com.qiuguan.retry.interceptor.AnnotationAwareRetryInterceptor;
import org.aopalliance.aop.Advice;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.IntroductionAdvisor;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.aop.support.annotation.AnnotationClassFilter;
import org.springframework.aop.support.annotation.AnnotationMethodMatcher;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author created by qiuguan on 2021/12/22 16:53
 */
@SuppressWarnings("all")
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration
public class ProxyRetryConfiguration extends AbstractPointcutAdvisor implements IntroductionAdvisor, BeanFactoryAware, InitializingBean {

    private Advice advice;

    private Pointcut pointcut;

    private BeanFactory beanFactory;

    @Override
    public ClassFilter getClassFilter() {
        return this.pointcut.getClassFilter();
    }

    @Override
    public void validateInterfaces() throws IllegalArgumentException {

    }

    @Override
    public Class<?>[] getInterfaces() {
        return new Class[]{Retryable.class};
    }

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        return this.advice;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.advice = buildAdvice();
        this.pointcut = buildPointcut();

        if (this.advice instanceof BeanFactoryAware) {
            ((BeanFactoryAware)this.advice).setBeanFactory(this.beanFactory);
        }
    }

    private Pointcut buildPointcut() {
        return new AnnotationClassOrMethodPointcut(Retryable.class);
    }


    private Advice buildAdvice() {
        AnnotationAwareRetryInterceptor interceptor = new AnnotationAwareRetryInterceptor();
        return interceptor;
    }


    /**
     * Pointcut and MethodMatcher
     */
    private final class AnnotationClassOrMethodPointcut extends StaticMethodMatcherPointcut {

        private final MethodMatcher methodResolver;

        public AnnotationClassOrMethodPointcut(Class<? extends Annotation> annotationType) {
            this.methodResolver = new AnnotationMethodMatcher(annotationType);
            //setClassFilter(new AnnotationClassFilter(annotationType));
            setClassFilter(new AnnotationClassOrMethodFilter(annotationType));
        }

        /**
         * 这个方法是实现了 MethodMatcher 接口，用来过滤方法的
         * @param method
         * @param targetClass
         * @return
         *
         * 过滤方法这里，其实前部分总是返回true(当然了，类中得有@Retryable 标注方法）
         * 这样就算调用一个普通方法，也还是会通过拦截器，只不过spring 是在拦截器中进行了判断
         * 我觉得可以提前，将这里的条件进行修改
         *
         *  @Override
         *  public boolean matches(Method method, Class<?> targetClass) {
         *     return getClassFilter().matches(targetClass) || this.methodResolver.matches(method, targetClass);
         *  }
         */


         @Override
         public boolean matches(Method method, Class<?> targetClass) {
             return this.methodResolver.matches(method, targetClass);
         }
    }


    /**
     * 过滤类是否符合预期 -- ClassFilter
     */
    private final class AnnotationClassOrMethodFilter extends AnnotationClassFilter {

        private AnnotationMethodsResolver resolver;

        public AnnotationClassOrMethodFilter(Class<? extends Annotation> annotationType) {
            super(annotationType, true);
            this.resolver = new AnnotationMethodsResolver(annotationType);
        }

        @Override
        public boolean matches(Class<?> clazz) {
            return super.matches(clazz) || this.resolver.hasAnnotatedMethods(clazz);
        }
    }

    private static class AnnotationMethodsResolver {

        private Class<? extends Annotation> annotationType;

        public AnnotationMethodsResolver(Class<? extends Annotation> annotationType) {
            this.annotationType = annotationType;
        }

        public boolean hasAnnotatedMethods(Class<?> clazz) {
            final AtomicBoolean found = new AtomicBoolean(false);

            ReflectionUtils.doWithMethods(clazz, new ReflectionUtils.MethodCallback() {
                @Override
                public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
                    if (found.get()) {
                        return;
                    }

                    Annotation annotation = AnnotationUtils.findAnnotation(method, annotationType);
                    if (annotation != null) {
                        found.set(true);
                    }
                }

            }, new ReflectionUtils.MethodFilter() {
                @Override
                public boolean matches(Method method) {
                    return !ReflectionUtils.isObjectMethod(method);
                }
            });

            return found.get();
        }
    }
}
