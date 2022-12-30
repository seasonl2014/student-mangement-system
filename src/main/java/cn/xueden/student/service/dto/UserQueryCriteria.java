package cn.xueden.student.service.dto;

import cn.xueden.annotation.EnableXuedenQuery;
import lombok.Data;

/**功能描述：系统用户查询条件参数
 * @author:梁志杰
 * @date:2022/12/4
 * @description:cn.xueden.student.service.dto
 * @version:1.0
 */
@Data
public class UserQueryCriteria {

    /**
     * 根据用户名、真实姓名、邮箱模糊查询
     */
    @EnableXuedenQuery(blurry = "username,realname,email")
    private String searchValue;


    /**
     * 根据真实姓名模糊查询
     */
    @EnableXuedenQuery
    private String sex;


    /**
     * 状态
     */
    @EnableXuedenQuery()
    private String status;
}
