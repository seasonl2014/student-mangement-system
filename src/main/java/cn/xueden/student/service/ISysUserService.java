package cn.xueden.student.service;

import cn.xueden.student.domain.SysUser;
import cn.xueden.student.service.dto.UserQueryCriteria;
import cn.xueden.student.vo.ModifyPwdModel;
import org.springframework.data.domain.Pageable;

/**功能描述：系统用户业务接口
 * @author:梁志杰
 * @date:2022/12/2
 * @description:cn.xueden.student.service
 * @version:1.0
 */
public interface ISysUserService {

    /**
     * 登录
     * @param sysUser
     * @return
     */
    SysUser login(SysUser sysUser);

    /**
     * 获取用户列表数据
     * @param queryCriteria
     * @param pageable
     * @return
     */
    Object getList(UserQueryCriteria queryCriteria, Pageable pageable);

    /**
     * 添加用户信息
     * @param sysUser
     * @return
     */
    boolean addUser(SysUser sysUser);

    /**
     * 根据ID获取用户信息
     * @param id
     * @return
     */
    SysUser getById(Long id);

    /**
     * 更新用户信息
     * @param sysUser
     * @return
     */
    void editUser(SysUser sysUser);

    /**
     * 根据ID删除用户信息
     * @param id
     */
    void deleteById(Long id);

    /**
     * 更新个人密码
     * @param modifyPwdModel
     */
    boolean updatePwd(ModifyPwdModel modifyPwdModel);
}
