package cn.xueden.student.domain;

import cn.xueden.base.BaseEntity;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Data;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**功能描述：班级信息实体类
 * @author:梁志杰
 * @date:2022/12/9
 * @description:cn.xueden.student.domain
 * @version:1.0
 */
@Data
@Entity
@Table(name = "s_grade_class")
@org.hibernate.annotations.Table(appliesTo = "s_grade_class",comment="班级信息表")
public class GradeClass extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * 班级编号
     */
    @Column(name = "code")
    private String code;

    /**
     * 年级
     */
    @Column(name = "grade")
    private Integer grade;

    /**
     * 班级名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 班级
     */
    @Column(name="clazz")
    private Integer clazz;

    @JsonIgnoreProperties(ignoreUnknown = true, value = {"gradeClass"})
    @OneToMany(mappedBy = "gradeClass",fetch=FetchType.EAGER)
    private List<Student> students = new ArrayList<>();

}
