package cn.xueden.student.service.dto;

import cn.xueden.annotation.EnableXuedenQuery;
import lombok.Data;

/**功能描述：系统角色查询条件
 * @author:梁志杰
 * @date:2022/12/8
 * @description:cn.xueden.student.service.dto
 * @version:1.0
 */
@Data
public class RoleQueryCriteria {
    /**
     * 根据角色名称、角色编号模糊查询
     */
    @EnableXuedenQuery(blurry = "name,code")
    private String searchValue;
}
