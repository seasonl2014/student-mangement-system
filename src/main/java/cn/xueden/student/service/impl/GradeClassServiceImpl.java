package cn.xueden.student.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.xueden.student.domain.GradeClass;

import cn.xueden.student.repository.GradeClassRepository;
import cn.xueden.student.service.IGradeClassService;
import cn.xueden.student.service.dto.GradeClassQueryCriteria;

import cn.xueden.utils.PageUtil;
import cn.xueden.utils.QueryHelp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**功能描述：班级信息业务实现类
 * @author:梁志杰
 * @date:2022/12/9
 * @description:cn.xueden.student.service.impl
 * @version:1.0
 */
@Service
@Transactional(readOnly = true)
public class GradeClassServiceImpl implements IGradeClassService {

    private final GradeClassRepository gradeClassRepository;

    public GradeClassServiceImpl(GradeClassRepository gradeClassRepository) {
        this.gradeClassRepository = gradeClassRepository;
    }

    /**
     * 获取班级列表数据
     * @param queryCriteria
     * @param pageable
     * @return
     */
    @Override
    public Object getList(GradeClassQueryCriteria queryCriteria, Pageable pageable) {
       Page<GradeClass> page = gradeClassRepository.findAll(((root, criteriaQuery, cb) -> QueryHelp.getPredicate(root, queryCriteria, cb)),pageable);
        return PageUtil.toPage(page);
    }

    /**
     * 新增班级信息
     * @param gradeClass
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addGradeClass(GradeClass gradeClass) {
        GradeClass dbGradeClass = gradeClassRepository.save(gradeClass);
        return dbGradeClass.getId()!=null;
    }

    /**
     * 根据ID获取班级信息
     * @param id
     * @return
     */
    @Override
    public GradeClass getById(Long id) {
        return gradeClassRepository.findById(id).orElseGet(GradeClass::new);
    }

    /**
     * 更新班级信息
     * @param gradeClass
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editGradeClass(GradeClass gradeClass) {
        GradeClass dbGradeClass = gradeClassRepository.getReferenceById(gradeClass.getId());
        BeanUtil.copyProperties(gradeClass,dbGradeClass, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        gradeClassRepository.save(dbGradeClass);
    }

    /**
     * 根据id删除班级信息
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        gradeClassRepository.deleteById(id);
    }

    /**
     * 获取所有班级信息
     * @param gradeClassQueryCriteria
     * @return
     */
    @Override
    public List<GradeClass> queryAll(GradeClassQueryCriteria gradeClassQueryCriteria) {
        return gradeClassRepository.findAll();
    }

    /**
     * 统计班级数量
     * @return
     */
    @Override
    public long getCount() {
        return gradeClassRepository.count();
    }
}
