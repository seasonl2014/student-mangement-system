package cn.xueden.student.service.dto;

import cn.xueden.annotation.EnableXuedenQuery;
import lombok.Data;

/**功能描述：学生信息查询条件
 * @author:梁志杰
 * @date:2022/12/11
 * @description:cn.xueden.student.service.dto
 * @version:1.0
 */
@Data
public class StudentQueryCriteria {

    /**
     * 根据学生姓名、学号、手机号、qq模糊查询
     */
    @EnableXuedenQuery(blurry = "name,stuno,phone,qq")
    private String searchValue;
}
