package cn.xueden.student.controller;

import cn.xueden.base.BaseResult;
import cn.xueden.email.MailService;
import cn.xueden.exception.BadRequestException;
import cn.xueden.student.domain.SysUser;
import cn.xueden.student.service.ISysUserService;
import cn.xueden.student.service.dto.UserQueryCriteria;
import cn.xueden.student.vo.ModifyPwdModel;
import cn.xueden.utils.HutoolJWTUtil;
import cn.xueden.utils.NativeFileUtil;
import cn.xueden.utils.PageVo;
import cn.xueden.utils.XuedenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**功能描述：系统用户前端控制器
 * @author:梁志杰
 * @date:2022/12/3
 * @description:cn.xueden.student.controller
 * @version:1.0
 */
@RestController
@RequestMapping("user")
public class UserController {

    /**
     * 头像存放路径
     */
    @Value("${user.icon}")
    private String userIcon;

    /**
     * 邮件发送方
     */
    @Value("${spring.mail.username}")
    private String from;

    private final ISysUserService sysUserService;

    private final MailService mailService;

    public UserController(ISysUserService sysUserService, MailService mailService) {
        this.sysUserService = sysUserService;
        this.mailService = mailService;
    }

    /**
     * 获取用户列表数据
     * @param queryCriteria
     * @param pageVo
     * @return
     */
    @GetMapping
    public ResponseEntity<Object> getList(UserQueryCriteria queryCriteria, PageVo pageVo){
        Pageable pageable = PageRequest.of(pageVo.getPageIndex()-1,pageVo.getPageSize(), Sort.Direction.DESC, "id");
        return new ResponseEntity<>(sysUserService.getList(queryCriteria,pageable), HttpStatus.OK);
    }

    /**
     * 添加用户信息
     * @param sysUser
     * @return
     */
    @PostMapping
    public BaseResult addUser(@RequestBody SysUser sysUser){
        boolean result= sysUserService.addUser(sysUser);
        if(result){
            return BaseResult.success("添加成功");
        }else {
            return BaseResult.fail("添加失败");
        }
    }

    /**
     * 根据ID获取用户详情信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public BaseResult detail(@PathVariable Long id){
        if(null==id){
            throw new BadRequestException("获取信息失败");
        }
        SysUser dbSysUser = sysUserService.getById(id);
        return BaseResult.success(dbSysUser);
    }

    /**
     * 更新用户信息
     * @param sysUser
     * @return
     */
    @PutMapping
    public BaseResult editUser(@RequestBody SysUser sysUser){
        sysUserService.editUser(sysUser);
        return BaseResult.success("更新成功");
    }

    /**
     * 根据ID删除用户信息
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public BaseResult delete(@PathVariable Long id){
        if(null==id){
            throw new BadRequestException("删除信息失败");
        }
        sysUserService.deleteById(id);
        return BaseResult.success("删除成功");
    }

    /**
     * 上传头像
     * @param file
     * @return
     */
    @PostMapping("userIcon")
    public BaseResult uploadFile(@RequestParam("fileResource") MultipartFile file){
        if(file==null){
            return BaseResult.fail("文件不能为空");
        }
        try {
            String tempFileResource = NativeFileUtil.uploadUserIcon(file,userIcon);
            Map<String,Object> result = new HashMap<>();
            result.put("userIcon",tempFileResource);
            return BaseResult.success(result);
        }catch (Exception e){
            e.printStackTrace();
            return BaseResult.fail(e.getMessage());
        }

    }

    /**
     * 修改个人信息
     * @param sysUser
     * @return
     */
    @PutMapping("updateInfo")
    public BaseResult updateInfo(@RequestBody SysUser sysUser, HttpServletRequest request){
        // 获取登录用户Id
        String token = (String)request.getServletContext().getAttribute("token");
        Long userId = HutoolJWTUtil.parseToken(token);
        sysUser.setId(userId);
        sysUserService.editUser(sysUser);
        return BaseResult.success("更新成功");
    }

    /**
     * 发送验证码
     * @param email
     * @return
     */
    @GetMapping("sendEmail")
    public BaseResult sendEmail(@RequestParam("email")String email, HttpServletRequest request){
        // 发送到旧邮箱
        if(email==null||email==""){
            // 获取登录用户Id
            String token = (String)request.getServletContext().getAttribute("token");
            Long userId = HutoolJWTUtil.parseToken(token);
            SysUser dbSysUser = sysUserService.getById(userId);
            email = dbSysUser.getEmail();
        }
        int code = XuedenUtil.randomSixNums();
        String content = "验证码："+code+"此验证码用于更换邮箱绑定，请勿将验证码告知他人，有效期3分钟，请妥善保管。";
        mailService.sendSimpleMail(from,email,email,"修改邮箱验证码",content);
        request.getServletContext().setAttribute("code",code);
        return BaseResult.success();
    }

    /**
     * 校验验证码
     * @param code
     * @return
     */
    @GetMapping("verifyCode")
    public BaseResult verifyCode(@RequestParam("code")Integer code, HttpServletRequest request){

        if(code==null){
            return BaseResult.fail("验证码不存在！");
        }
        System.out.println("request.getServletContext().getAttribute(\"code\"):::"+request.getServletContext().getAttribute("code"));
        Integer sessionCode = (Integer) request.getServletContext().getAttribute("code");
       if(sessionCode==null){
           return BaseResult.fail("验证码已过期！");
       }
       if(!sessionCode.equals(code)){
           return BaseResult.fail("验证码输入不正确，请重新输入！");
       }
        return BaseResult.success();
    }

    /**
     * 更改绑定邮箱
     * @param code
     * @return
     */
    @PutMapping("updateEmail")
    public BaseResult updateEmail(@RequestParam("code")Integer code,@RequestParam("email")String email, HttpServletRequest request){

        if(code==null|| email==null){
            return BaseResult.fail("验证码或者邮箱不存在！");
        }
        Integer sessionCode = (Integer) request.getServletContext().getAttribute("code");
        if(sessionCode==null){
            return BaseResult.fail("验证码已过期！");
        }
        if(!sessionCode.equals(code)){
            return BaseResult.fail("验证码输入不正确，请重新输入！");
        }
        // 获取登录用户Id
        String token = (String)request.getServletContext().getAttribute("token");
        Long userId = HutoolJWTUtil.parseToken(token);
        SysUser tempSysUser = new SysUser();
        tempSysUser.setEmail(email);
        tempSysUser.setId(userId);
        sysUserService.editUser(tempSysUser);
        return BaseResult.success();
    }

    /**
     * 更新个人密码
     * @param modifyPwdModel
     * @return
     */
    @PutMapping("updatePwd")
    public BaseResult updatePwd(@RequestBody ModifyPwdModel modifyPwdModel, HttpServletRequest request){
        if(modifyPwdModel==null){
            return BaseResult.fail("更新失败！");
        }
        // 获取登录用户Id
        String token = (String)request.getServletContext().getAttribute("token");
        Long userId = HutoolJWTUtil.parseToken(token);
        modifyPwdModel.setUserId(userId);
        boolean result=sysUserService.updatePwd(modifyPwdModel);
        if(result){
            return BaseResult.success("更新成功！");
        }else {
            return BaseResult.fail("更新失败！");
        }

    }

}
