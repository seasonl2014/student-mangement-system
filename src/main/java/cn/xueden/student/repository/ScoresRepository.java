package cn.xueden.student.repository;

import cn.xueden.student.domain.Scores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 功能描述：成绩管理持久层
 * @author Administrator
 */
public interface ScoresRepository extends JpaRepository<Scores, Long>, JpaSpecificationExecutor<Scores> {
    /**
     * 功能描述：根据课程ID和学生ID查询成绩信息
     * @param courseId
     * @param id
     * @return
     */
    Scores getCourseByCourseIdAndStudentId(Long courseId, Long id);

    /**
     * 根据班级ID和课程ID统计成绩
     * @param courseId
     * @param gradeClassId
     * @return
     */
    List<Scores> findAllByCourseIdAndGradeClassId(Long courseId, Long gradeClassId);

    /**
     * 根据学科ID获取所有成绩记录
     * @param courseId
     * @return
     */
    List<Scores> findAllByCourseId(Long courseId);
}