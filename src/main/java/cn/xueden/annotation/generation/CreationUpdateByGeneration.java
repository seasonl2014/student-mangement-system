package cn.xueden.annotation.generation;

import cn.xueden.annotation.EnableXuedenUpdateBy;
import org.hibernate.tuple.AnnotationValueGeneration;
import org.hibernate.tuple.GenerationTiming;
import org.hibernate.tuple.ValueGenerator;

/**
 * @author:梁志杰
 * @date:2022/12/1
 * @description:cn.xueden.annotation.generation
 * @version:1.0
 */
public class CreationUpdateByGeneration implements AnnotationValueGeneration<EnableXuedenUpdateBy> {
    private ValueGenerator<?> generator;

    public CreationUpdateByGeneration() {

    }

    @Override
    public void initialize(EnableXuedenUpdateBy enableXuedenUpdateBy, Class<?> propertyType) {
        this.generator = UpdateByGenerators.get(propertyType);
    }

    @Override
    public GenerationTiming getGenerationTiming() {
        return GenerationTiming.ALWAYS;
    }

    @Override
    public ValueGenerator<?> getValueGenerator() {
        return this.generator;
    }

    @Override
    public boolean referenceColumnInSql() {
        return false;
    }

    @Override
    public String getDatabaseGeneratedReferencedColumnValue() {
        return null;
    }
}

