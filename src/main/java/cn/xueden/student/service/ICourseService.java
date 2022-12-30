package cn.xueden.student.service;

import cn.xueden.student.domain.Course;
import cn.xueden.student.service.dto.CourseQueryCriteria;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**功能描述：课程信息业务接口
 * @author:梁志杰
 * @date:2022/12/14
 * @description:cn.xueden.student.service
 * @version:1.0
 */
public interface ICourseService {
    /**
     * 获取课程列表数据
     * @param queryCriteria
     * @param pageable
     * @return
     */
    Object getList(CourseQueryCriteria queryCriteria, Pageable pageable);

    /**
     * 添加课程信息
     * @param course
     * @return
     */
    boolean addCourse(Course course);

    /**
     * 根据id获取课程信息
     * @param id
     * @return
     */
    Course getById(Long id);

    /**
     * 更新课程信息
     * @param course
     */
    void editCourse(Course course);

    /**
     * 根据id删除课程信息
     * @param id
     */
    void deleteById(Long id);

    /**
     * 获取所有课程
     * @return
     */
    List<Course> queryAll();

    /**
     * 统计课程门数
     * @return
     */
    long getCount();
}
