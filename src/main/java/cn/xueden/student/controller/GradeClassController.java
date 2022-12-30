package cn.xueden.student.controller;

import cn.xueden.base.BaseResult;
import cn.xueden.exception.BadRequestException;
import cn.xueden.student.domain.GradeClass;
import cn.xueden.student.service.IGradeClassService;
import cn.xueden.student.service.dto.GradeClassQueryCriteria;

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

/**功能描述：班级信息前端控制器
 * @author:梁志杰
 * @date:2022/12/9
 * @description:cn.xueden.student.controller
 * @version:1.0
 */
@RestController
@RequestMapping("gradeclass")
public class GradeClassController {

    private final IGradeClassService gradeClassService;

    public GradeClassController(IGradeClassService gradeClassService) {
        this.gradeClassService = gradeClassService;
    }

    /**
     * 获取班级列表数据
     * @param queryCriteria
     * @param pageVo
     * @return
     */
    @GetMapping
    public ResponseEntity<Object> getList(GradeClassQueryCriteria queryCriteria, PageVo pageVo){
        Pageable pageable = PageRequest.of(pageVo.getPageIndex()-1,pageVo.getPageSize(), Sort.Direction.DESC, "id");
        return new ResponseEntity<>(gradeClassService.getList(queryCriteria,pageable), HttpStatus.OK);
    }

    /**
     * 添加班级信息
     * @param gradeClass
     * @return
     */
    @PostMapping
    public BaseResult addGradeClass(@RequestBody GradeClass gradeClass){
        boolean result= gradeClassService.addGradeClass(gradeClass);
        if(result){
            return BaseResult.success("添加成功");
        }else {
            return BaseResult.fail("添加失败");
        }
    }

    /**
     * 根据ID获取班级详情信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public BaseResult detail(@PathVariable Long id){
        if(null==id){
            throw new BadRequestException("获取信息失败");
        }
        GradeClass dbGradeClass = gradeClassService.getById(id);
        return BaseResult.success(dbGradeClass);
    }

    /**
     * 更新班级信息
     * @param gradeClass
     * @return
     */
    @PutMapping
    public BaseResult editGradeClass(@RequestBody GradeClass gradeClass){
        gradeClassService.editGradeClass(gradeClass);
        return BaseResult.success("更新成功");
    }

    /**
     * 根据ID删除班级信息
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public BaseResult delete(@PathVariable Long id){
        if(null==id){
            throw new BadRequestException("删除信息失败");
        }
        gradeClassService.deleteById(id);
        return BaseResult.success("删除成功");
    }

    /**
     * 获取所有班级信息
     * @param
     * @return
     */
    @GetMapping(value = "/all")
    public BaseResult getAll(){
        List<GradeClass> list =  gradeClassService.queryAll( new GradeClassQueryCriteria());
        List<ResultVo> result = list.stream().map(temp -> {
            ResultVo obj = new ResultVo();
            obj.setName(temp.getName());
            obj.setId(temp.getId());
            return obj;
        }).collect(Collectors.toList());
        return BaseResult.success(result);
    }

}
