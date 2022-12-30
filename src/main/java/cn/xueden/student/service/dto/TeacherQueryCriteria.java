package cn.xueden.student.service.dto;

import cn.xueden.annotation.EnableXuedenQuery;
import lombok.Data;

/**功能描述：教师信息查询条件
 * @author:梁志杰
 * @date:2022/12/16
 * @description:cn.xueden.student.service.dto
 * @version:1.0
 */
@Data
public class TeacherQueryCriteria {
    /**
     * 根据教师姓名、教师工号、手机号、qq模糊查询
     */
    @EnableXuedenQuery(blurry = "name,teachno,phone,qq")
    private String searchValue;
}
