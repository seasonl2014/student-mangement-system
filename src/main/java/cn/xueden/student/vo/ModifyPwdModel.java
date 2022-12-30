package cn.xueden.student.vo;

import lombok.Data;

/**
 * @author:梁志杰
 * @date:2022/12/26
 * @description:cn.xueden.student.vo
 * @version:1.0
 */
@Data
public class ModifyPwdModel {

    /**
     * 旧密码
     */
    private String usedPass;

    /**
     * 新密码
     */
    private String newPass;

    /**
     * 登录用户ID
     */
    private Long userId;
}
