package cn.xueden.student.controller;

import cn.xueden.base.BaseResult;
import cn.xueden.exception.BadRequestException;
import cn.xueden.student.domain.Course;
import cn.xueden.student.domain.SysRole;


import cn.xueden.student.service.IRoleService;
import cn.xueden.student.service.dto.RoleQueryCriteria;

import cn.xueden.utils.PageVo;
import cn.xueden.utils.ResultVo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**功能描述：系统角色前端控制器
 * @author:梁志杰
 * @date:2022/12/8
 * @description:cn.xueden.student.controller
 * @version:1.0
 */
@RestController
@RequestMapping("role")
public class RoleController {

    private final IRoleService roleService;

    public RoleController(IRoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * 获取角色列表数据
     * @param queryCriteria
     * @param pageVo
     * @return
     */
    @GetMapping
    public ResponseEntity<Object> getList(RoleQueryCriteria queryCriteria, PageVo pageVo){
        Pageable pageable = PageRequest.of(pageVo.getPageIndex()-1,pageVo.getPageSize(), Sort.Direction.DESC, "id");
        return new ResponseEntity<>(roleService.getList(queryCriteria,pageable), HttpStatus.OK);
    }

    /**
     * 添加角色信息
     * @param sysRole
     * @return
     */
    @PostMapping
    public BaseResult addRole(@RequestBody SysRole sysRole){
        boolean result= roleService.addRole(sysRole);
        if(result){
            return BaseResult.success("添加成功");
        }else {
            return BaseResult.fail("添加失败");
        }
    }

    /**
     * 根据ID获取角色详情信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public BaseResult detail(@PathVariable Long id){
        if(null==id){
            throw new BadRequestException("获取信息失败");
        }
        SysRole dbSysRole = roleService.getById(id);
        return BaseResult.success(dbSysRole);
    }

    /**
     * 更新角色信息
     * @param sysRole
     * @return
     */
    @PutMapping
    public BaseResult editRole(@RequestBody SysRole sysRole){
        roleService.editRole(sysRole);
        return BaseResult.success("更新成功");
    }

    /**
     * 根据ID删除角色信息
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public BaseResult delete(@PathVariable Long id){
        if(null==id){
            throw new BadRequestException("删除信息失败");
        }
        roleService.deleteById(id);
        return BaseResult.success("删除成功");
    }

    /**
     * 获取所有角色信息
     * @param
     * @return
     */
    @GetMapping(value = "/all")
    public BaseResult getAll(){
        List<SysRole> list =  roleService.queryAll();
        List<ResultVo> result = list.stream().map(temp -> {
            ResultVo obj = new ResultVo();
            obj.setName(temp.getName());
            obj.setId(temp.getId());
            return obj;
        }).collect(Collectors.toList());
        return BaseResult.success(result);
    }
}
