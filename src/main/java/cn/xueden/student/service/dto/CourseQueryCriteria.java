package cn.xueden.student.service.dto;

import cn.xueden.annotation.EnableXuedenQuery;
import lombok.Data;

/**功能描述：课程查询条件
 * @author:梁志杰
 * @date:2022/12/14
 * @description:cn.xueden.student.service.dto
 * @version:1.0
 */
@Data
public class CourseQueryCriteria {

    /**
     * 根据课程编号、课程名称模糊查询
     */
    @EnableXuedenQuery(blurry = "courseno,coursename")
    private String searchValue;
}
