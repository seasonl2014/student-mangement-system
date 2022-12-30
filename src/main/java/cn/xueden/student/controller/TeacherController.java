package cn.xueden.student.controller;

import cn.xueden.base.BaseResult;
import cn.xueden.exception.BadRequestException;
import cn.xueden.student.domain.Teacher;
import cn.xueden.student.service.ITeacherService;
import cn.xueden.student.service.dto.TeacherQueryCriteria;
import cn.xueden.utils.PageVo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**功能描述：教师信息前端控制器
 * @author:梁志杰
 * @date:2022/12/15
 * @description:cn.xueden.student.controller
 * @version:1.0
 */
@RestController
@RequestMapping("teacher")
public class TeacherController {

    private final ITeacherService teacherService;

    public TeacherController(ITeacherService teacherService) {
        this.teacherService = teacherService;
    }

    /**
     * 获取教师列表数据
     * @param queryCriteria
     * @param pageVo
     * @return
     */
    @GetMapping
    public ResponseEntity<Object> getList(TeacherQueryCriteria queryCriteria, PageVo pageVo){
        Pageable pageable = PageRequest.of(pageVo.getPageIndex()-1,pageVo.getPageSize(), Sort.Direction.DESC, "id");
        return new ResponseEntity<>(teacherService.getList(queryCriteria,pageable), HttpStatus.OK);
    }

    /**
     * 添加教师信息
     * @param teacher
     * @return
     */
    @PostMapping
    public BaseResult addTeacher(@RequestBody Teacher teacher){
        boolean result= teacherService.addTeacher(teacher);
        if(result){
            return BaseResult.success("添加成功");
        }else {
            return BaseResult.fail("添加失败");
        }
    }

    /**
     * 根据ID获取教师详情信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public BaseResult detail(@PathVariable Long id){
        if(null==id){
            throw new BadRequestException("获取信息失败");
        }
        Teacher dbTeacher = teacherService.getById(id);
        return BaseResult.success(dbTeacher);
    }

    /**
     * 更新教师信息
     * @param teacher
     * @return
     */
    @PutMapping
    public BaseResult editTeacher(@RequestBody Teacher teacher){
        teacherService.editTeacher(teacher);
        return BaseResult.success("更新成功");
    }

    /**
     * 根据ID删除教师信息
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public BaseResult delete(@PathVariable Long id){
        if(null==id){
            throw new BadRequestException("删除信息失败");
        }
        teacherService.deleteById(id);
        return BaseResult.success("删除成功");
    }

}
