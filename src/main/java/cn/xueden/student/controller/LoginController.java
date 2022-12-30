package cn.xueden.student.controller;

import cn.xueden.base.BaseResult;
import cn.xueden.student.domain.SysUser;
import cn.xueden.student.service.ISysUserService;
import cn.xueden.utils.HutoolJWTUtil;
import cn.xueden.utils.Md5Util;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**功能描述：系统后台登录前端控制器
 * @author:梁志杰
 * @date:2022/12/2
 * @description:cn.xueden.student.controller
 * @version:1.0
 */
@RestController
public class LoginController {

    private final ISysUserService sysUserService;

    public LoginController(ISysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @PostMapping("login")
    public BaseResult login(@RequestBody SysUser sysUser, HttpServletRequest request){
        SysUser dbSysUser = sysUserService.login(sysUser);
        if(dbSysUser==null){
            return BaseResult.fail("登录失败，账号不存在");
        } else if (!dbSysUser.getPassword().equals(Md5Util.MD5(sysUser.getPassword()))) {
            return BaseResult.fail("登录失败，密码不正确");
        } else if (dbSysUser.getStatus()==0) {
            return BaseResult.fail("登录失败，账号被封禁");
        }
        // 生成token
        String token = HutoolJWTUtil.createToken(dbSysUser);
        request.getServletContext().setAttribute("token",token);
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("username",dbSysUser.getUsername());
        resultMap.put("realname",dbSysUser.getRealname());
        resultMap.put("token",token);
        resultMap.put("email",dbSysUser.getEmail());
        resultMap.put("sex",dbSysUser.getSex());
        resultMap.put("userIcon",dbSysUser.getUserIcon());
        resultMap.put("createTime",dbSysUser.getCreateTime());
        resultMap.put("role",dbSysUser.getSysRole());
        return BaseResult.success("登录成功",resultMap);
    }

    /**
     * 退出系统
     * @param request
     * @return
     */
    @GetMapping("loginOut")
    public BaseResult loginOut(HttpServletRequest request){
        request.getServletContext().removeAttribute("token");
        return BaseResult.success("退出成功");
    }
}
