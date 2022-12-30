package cn.xueden.student.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.xueden.student.domain.Course;

import cn.xueden.student.repository.CourseRepository;
import cn.xueden.student.service.ICourseService;
import cn.xueden.student.service.dto.CourseQueryCriteria;
import cn.xueden.utils.PageUtil;
import cn.xueden.utils.QueryHelp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**功能描述：课程信息业务接口实现类
 * @author:梁志杰
 * @date:2022/12/14
 * @description:cn.xueden.student.service.impl
 * @version:1.0
 */
@Service
@Transactional(readOnly = true)
public class CourseServiceImpl implements ICourseService {

    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    /**
     * 获取课程列表数据
     * @param queryCriteria
     * @param pageable
     * @return
     */
    @Override
    public Object getList(CourseQueryCriteria queryCriteria, Pageable pageable) {
        Page<Course> page = courseRepository.findAll((root, query, criteriaBuilder) -> QueryHelp.getPredicate(root,queryCriteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page);
    }

    /**
     * 添加课程信息
     * @param course
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addCourse(Course course) {
        Course dbCourse = courseRepository.save(course);
        return dbCourse.getId()!=null;
    }

    /**
     * 根据id获取课程信息
     * @param id
     * @return
     */
    @Override
    public Course getById(Long id) {
        return courseRepository.findById(id).orElseGet(Course::new);
    }

    /**
     * 更新课程信息
     * @param course
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editCourse(Course course) {
        Course dbCourse = courseRepository.getReferenceById(course.getId());
        BeanUtil.copyProperties(course,dbCourse, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        courseRepository.save(dbCourse);
    }

    /**
     * 根据id删除课程信息
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        courseRepository.deleteById(id);
    }

    /**
     * 获取所有课程
     * @return
     */
    @Override
    public List<Course> queryAll() {
        return courseRepository.findAll();
    }

    /**
     * 统计课程门数
     * @return
     */
    @Override
    public long getCount() {
        return courseRepository.count();
    }
}
