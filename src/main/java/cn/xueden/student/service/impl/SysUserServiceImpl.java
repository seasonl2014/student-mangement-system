package cn.xueden.student.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.xueden.student.domain.SysUser;
import cn.xueden.student.repository.SysUserRepository;
import cn.xueden.student.service.ISysUserService;
import cn.xueden.student.service.dto.UserQueryCriteria;
import cn.xueden.student.vo.ModifyPwdModel;
import cn.xueden.utils.Md5Util;
import cn.xueden.utils.PageUtil;
import cn.xueden.utils.QueryHelp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**功能描述：系统用户业务接口实现类
 * @author:梁志杰
 * @date:2022/12/2
 * @description:cn.xueden.student.service.impl
 * @version:1.0
 */
@Service
@Transactional(readOnly = true)
public class SysUserServiceImpl implements ISysUserService {

    private final SysUserRepository sysUserRepository;

    public SysUserServiceImpl(SysUserRepository sysUserRepository) {
        this.sysUserRepository = sysUserRepository;
    }

    /**
     * 登录
     * @param sysUser
     * @return
     */
    @Override
    public SysUser login(SysUser sysUser) {
        SysUser dbSysUser = sysUserRepository.findByusername(sysUser.getUsername());
        return dbSysUser;
    }

    /**
     * 获取用户列表数据
     * @param queryCriteria
     * @param pageable
     * @return
     */
    @Override
    public Object getList(UserQueryCriteria queryCriteria, Pageable pageable) {
        Page<SysUser> page = sysUserRepository.findAll((root, query, criteriaBuilder) -> QueryHelp.getPredicate(root,queryCriteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page);
    }

    /**
     * 新增用户信息
     * @param sysUser
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addUser(SysUser sysUser) {
        sysUser.setPassword(Md5Util.MD5(sysUser.getPassword()));
        SysUser dbSysUser = sysUserRepository.save(sysUser);
        return dbSysUser.getId()!=null;
    }

    /**
     * 根据ID获取用户信息
     * @param id
     * @return
     */
    @Override
    public SysUser getById(Long id) {
        return sysUserRepository.findById(id).orElseGet(SysUser::new);
    }

    /**
     * 更新用户信息
     * @param sysUser
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editUser(SysUser sysUser) {
       SysUser dbSysUser = sysUserRepository.getReferenceById(sysUser.getId());
        BeanUtil.copyProperties(sysUser,dbSysUser, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        sysUserRepository.save(dbSysUser);
    }

    /**
     * 根据ID删除用户信息
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        sysUserRepository.deleteById(id);
    }

    /**
     * 更新个人密码
     * @param modifyPwdModel
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePwd(ModifyPwdModel modifyPwdModel) {
        // 获取根据Id获取用户信息
        SysUser dbUser = sysUserRepository.getReferenceById(modifyPwdModel.getUserId());
        // 判断输入旧密码是否正确
        String dbPwd = dbUser.getPassword();
        String usePwd = Md5Util.MD5(modifyPwdModel.getUsedPass());
        if(!usePwd.equals(dbPwd)){
            return false;
        }else {
            String newPwd = Md5Util.MD5(modifyPwdModel.getNewPass());
            dbUser.setPassword(newPwd);
            sysUserRepository.save(dbUser);
            return true;
        }

    }
}
