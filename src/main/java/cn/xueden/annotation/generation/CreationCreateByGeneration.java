package cn.xueden.annotation.generation;

import cn.xueden.annotation.EnableXuedenCreateBy;

import org.hibernate.tuple.AnnotationValueGeneration;
import org.hibernate.tuple.GenerationTiming;
import org.hibernate.tuple.ValueGenerator;

/**功能描述：创建者ID生成器
 * @author:梁志杰
 * @date:2022/12/1
 * @description:cn.xueden.annotation.generation
 * @version:1.0
 */
public class CreationCreateByGeneration implements AnnotationValueGeneration<EnableXuedenCreateBy> {
    private ValueGenerator<?> generator;

    public CreationCreateByGeneration() {
    }

    @Override
    public void initialize(EnableXuedenCreateBy enableXuedenUpdateBy, Class<?> propertyType) {
        this.generator = CreateByGenerators.get(propertyType);
    }

    @Override
    public GenerationTiming getGenerationTiming() {
        return GenerationTiming.INSERT;
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
