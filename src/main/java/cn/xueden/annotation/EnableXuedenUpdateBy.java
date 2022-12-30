package cn.xueden.annotation;

import cn.xueden.annotation.generation.CreationUpdateByGeneration;
import org.hibernate.annotations.ValueGenerationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author:梁志杰
 * @date:2022/12/1
 * @description:cn.xueden.annotation
 * @version:1.0
 */
@ValueGenerationType(
        generatedBy = CreationUpdateByGeneration.class
)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface EnableXuedenUpdateBy {
}
