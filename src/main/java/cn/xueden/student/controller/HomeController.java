package cn.xueden.student.controller;

import cn.xueden.base.BaseResult;
import cn.xueden.student.service.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author:梁志杰
 * @date:2022/12/22
 * @description:cn.xueden.student.controller
 * @version:1.0
 */
@RestController
@RequestMapping("home")
public class HomeController {

    private final IStudentService studentService;

    private final IGradeClassService gradeClassService;

    private final ITeacherService teacherService;

    private final ICourseService courseService;

    private final IScoresService scoresService;

    public HomeController(IStudentService studentService, IGradeClassService gradeClassService, ITeacherService teacherService, ICourseService courseService, IScoresService scoresService) {
        this.studentService = studentService;
        this.gradeClassService = gradeClassService;
        this.teacherService = teacherService;
        this.courseService = courseService;
        this.scoresService = scoresService;
    }

    /**
     * 后台首页统计
     * @return
     */
    @GetMapping
    public BaseResult getIndexTotal(){
        Map<String,Object> resultMap = new HashMap<>();
        // 统计学生人数
        long studentNums = studentService.getCount();
        resultMap.put("studentNums",studentNums);

        // 统计班级数量
        long classNums = gradeClassService.getCount();
        resultMap.put("classNums",classNums);

        // 统计教师个数
        long teacherNums = teacherService.getCount();
        resultMap.put("teacherNums",teacherNums);

        // 统计课程门数
        long courseNums = courseService.getCount();
        resultMap.put("courseNums",courseNums);

        // 所有学科成绩对比
        HashMap<String, Object> scoresMap = scoresService.getAllSubjectScoreContrast();
        resultMap.put("scores",scoresMap);
        return BaseResult.success(resultMap);
    }
}

