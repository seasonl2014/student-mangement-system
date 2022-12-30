package cn.xueden.student.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.xueden.student.domain.Course;
import cn.xueden.student.domain.GradeClass;
import cn.xueden.student.domain.Scores;
import cn.xueden.student.domain.Student;
import cn.xueden.student.repository.ScoresRepository;
import cn.xueden.student.repository.StudentRepository;
import cn.xueden.student.service.IScoresService;
import cn.xueden.student.service.dto.ScoresQueryCriteria;

import cn.xueden.student.vo.BarEchartsSeriesModel;
import cn.xueden.student.vo.EchartsSeriesModel;
import cn.xueden.student.vo.RegisterScoresModel;
import cn.xueden.utils.PageUtil;
import cn.xueden.utils.QueryHelp;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;



/**功能描述：成绩管理业务接口实现类
 * @author:梁志杰
 * @date:2022/12/18
 * @description:cn.xueden.student.service.impl
 * @version:1.0
 */
@Service
@Transactional(readOnly = true)
public class ScoresServiceImpl implements IScoresService {

    private final ScoresRepository scoresRepository;

    private final StudentRepository studentRepository;

    public ScoresServiceImpl(ScoresRepository scoresRepository, StudentRepository studentRepository) {
        this.scoresRepository = scoresRepository;
        this.studentRepository = studentRepository;
    }

    /**
     * 获取成绩列表数据
     * @param queryCriteria
     * @param pageable
     * @return
     */
    @Override
    public Object getList(ScoresQueryCriteria queryCriteria, Pageable pageable) {
        Page<Scores> page = scoresRepository.findAll((root, query, criteriaBuilder) -> QueryHelp.getPredicate(root,queryCriteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page);
    }

    /**
     * 登记班级学科成绩
     * @param scoresModel
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registerScores(RegisterScoresModel scoresModel) {

        // 根据班级ID获取该班级下的所有学生
        List<Student> studentList = studentRepository.findAllByGradeClassId(scoresModel.getGradeClassId());

        for(Student student: studentList){
            // 根据课程ID和学生ID查询成绩信息
            Scores dbScores = scoresRepository.getCourseByCourseIdAndStudentId(scoresModel.getCourseId(),student.getId());
            if(dbScores==null){
                // 新增记录
                dbScores = new Scores();
                dbScores.setType("未批改");
                dbScores.setScore(0);
                dbScores.setRemarks("初始成绩");
                dbScores.setStudent(student);
                Course tempCourse = new Course();
                // 课程
                tempCourse.setId(scoresModel.getCourseId());
                dbScores.setCourse(tempCourse);
                // 班级
                GradeClass gradeClass = new GradeClass();
                gradeClass.setId(scoresModel.getGradeClassId());
                dbScores.setGradeClass(gradeClass);
                scoresRepository.save(dbScores);
            }

        }
    }


    /**
     * 更新成绩
     * @param scores
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editScores(Scores scores) {
        Scores dbScores = scoresRepository.getReferenceById(scores.getId());
        dbScores.setType("已批改");
        BeanUtil.copyProperties(scores,dbScores, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        scoresRepository.save(dbScores);

    }

    /**
     * 删除成绩信息
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        scoresRepository.deleteById(id);
    }

    /**
     * 统计班级科目成绩
     * @param courseId
     * @param gradeClassId
     * @return
     */
    @Override
    public List<EchartsSeriesModel> getScoreCensus(Long courseId, Long gradeClassId) {
        List<EchartsSeriesModel> data = new ArrayList<>();
        // 根据班级ID和课程ID获取所有成绩信息
        List<Scores> scoresList = scoresRepository.findAllByCourseIdAndGradeClassId(courseId,gradeClassId);

        // 成绩优秀人数
        AtomicInteger excellentNums= new AtomicInteger();
        EchartsSeriesModel excellentEchartsSeriesModel= new EchartsSeriesModel();
        // 成绩是良的人数
        AtomicInteger goodNums = new AtomicInteger();
        EchartsSeriesModel goodEchartsSeriesModel= new EchartsSeriesModel();
        // 成绩一般的人数
        AtomicInteger commonNums = new AtomicInteger();
        EchartsSeriesModel commonEchartsSeriesModel= new EchartsSeriesModel();
        // 成绩差的人数
        AtomicInteger badNums = new AtomicInteger();
        EchartsSeriesModel badEchartsSeriesModel= new EchartsSeriesModel();
        // 成绩不及格的人数
        AtomicInteger failNums = new AtomicInteger();
        EchartsSeriesModel failEchartsSeriesModel= new EchartsSeriesModel();
        scoresList.stream().forEach(item-> {
            // 统计优秀人数
            if(item.getScore()>=90){
                excellentNums.getAndIncrement();
                excellentEchartsSeriesModel.setName("优");
                excellentEchartsSeriesModel.setValue(excellentNums.intValue());
             // 统计成绩是良好的人数
            }else if(item.getScore()>=80&&item.getScore()<90) {
                goodNums.getAndIncrement();
                goodEchartsSeriesModel.setName("良");
                goodEchartsSeriesModel.setValue(goodNums.intValue());
             // 统计成绩一般的人数
            }else if(item.getScore()>=70&&item.getScore()<80) {
                commonNums.getAndIncrement();
                commonEchartsSeriesModel.setName("一般");
                commonEchartsSeriesModel.setValue(commonNums.intValue());
                // 统计成绩较差的人数
            }else if(item.getScore()>=60&&item.getScore()<70) {
                badNums.getAndIncrement();
                badEchartsSeriesModel.setName("较差");
                badEchartsSeriesModel.setValue(badNums.intValue());
            }else {
                failNums.getAndIncrement();
                failEchartsSeriesModel.setName("不及格");
                failEchartsSeriesModel.setValue(failNums.intValue());
            }
        });

        // 优秀的人数
        if(excellentNums.intValue()!=0){
            data.add(excellentEchartsSeriesModel);
        }
        // 良好的人数
        if(goodNums.intValue()!=0){
            data.add(goodEchartsSeriesModel);
        }
        // 一般的人数
        if(commonNums.intValue()!=0){
            data.add(commonEchartsSeriesModel);
        }
        // 较差的人数
        if(badNums.intValue()!=0){
            data.add(badEchartsSeriesModel);
        }
        // 不及格的人数
        if(failNums.intValue()!=0){
            data.add(failEchartsSeriesModel);
        }

        return data;
    }

    /**
     * 班级学科成绩对比
     * @param courseId
     * @return
     */
    @Override
    public HashMap<String, Object> getScoresContrastCensus(Long courseId) {
        List<BarEchartsSeriesModel> barEchartsSeriesList = new ArrayList<>();
        // 获取该学科下的所有成绩记录
        List<Scores> scoresList = scoresRepository.findAllByCourseId(courseId);
        
        // 统计方法同时统计同组的最大值、最小值、计数、求和、平均数信息
        HashMap<GradeClass, DoubleSummaryStatistics> resultGradeClass = scoresList.stream()
                .collect(Collectors.groupingBy(Scores::getGradeClass, HashMap::new, Collectors.summarizingDouble(Scores::getScore)));
        // 平均成绩
        List<Double> averageList = new ArrayList<>();
        // 最高成绩
        List<Double> maxList = new ArrayList<>();
        // 最低成绩
        List<Double> minList = new ArrayList<>();
        // 班级总人数
        List<Double> countList = new ArrayList<>();
        // 班级学科总成绩
        List<Double> sumList = new ArrayList<>();

        // 横坐标
        List<String> categoryList = new ArrayList<>();
        resultGradeClass.forEach((k, v) -> {
            // 班级名称
            categoryList.add(k.getName());
            // 平均成绩,保留两位小数
            v.getAverage();
            BigDecimal bigDecimal = new BigDecimal(v.getAverage());
           double average = bigDecimal.setScale(2, RoundingMode.HALF_UP).doubleValue();
            averageList.add(average);
            // 最高成绩
            v.getMax();
            maxList.add(v.getMax());
            // 最低成绩
            v.getMin();
            minList.add(v.getMin());
            // 班级总人数
            countList.add((double)v.getCount());
            // 总成绩
            sumList.add(v.getSum());
        });

        // 平均成绩
        BarEchartsSeriesModel averageBarEchartsSeriesModel = new BarEchartsSeriesModel();
        averageBarEchartsSeriesModel.setData(averageList);
        averageBarEchartsSeriesModel.setType("bar");
        averageBarEchartsSeriesModel.setName("平均成绩");
        barEchartsSeriesList.add(averageBarEchartsSeriesModel);
        // 最高成绩
        BarEchartsSeriesModel maxBarEchartsSeriesModel = new BarEchartsSeriesModel();
        maxBarEchartsSeriesModel.setData(maxList);
        maxBarEchartsSeriesModel.setType("bar");
        maxBarEchartsSeriesModel.setName("最高成绩");
        barEchartsSeriesList.add(maxBarEchartsSeriesModel);
        // 最低成绩
        BarEchartsSeriesModel minBarEchartsSeriesModel = new BarEchartsSeriesModel();
        minBarEchartsSeriesModel.setData(minList);
        minBarEchartsSeriesModel.setType("bar");
        minBarEchartsSeriesModel.setName("最低成绩");
        barEchartsSeriesList.add(minBarEchartsSeriesModel);
        // 班级总人数
        BarEchartsSeriesModel countBarEchartsSeriesModel = new BarEchartsSeriesModel();
        countBarEchartsSeriesModel.setData(countList);
        countBarEchartsSeriesModel.setType("bar");
        countBarEchartsSeriesModel.setName("总人数");
        barEchartsSeriesList.add(countBarEchartsSeriesModel);
        // 班级学科总成绩
        BarEchartsSeriesModel sumBarEchartsSeriesModel = new BarEchartsSeriesModel();
        sumBarEchartsSeriesModel.setData(sumList);
        sumBarEchartsSeriesModel.setType("bar");
        sumBarEchartsSeriesModel.setName("总成绩");
        barEchartsSeriesList.add(sumBarEchartsSeriesModel);

        // 定义返回对象
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("categoryList",categoryList);
        resultMap.put("barEchartsSeriesList",barEchartsSeriesList);
        return resultMap;
    }

    /**
     * 所有学科成绩对比
     * @return
     */
    @Override
    public HashMap<String, Object> getAllSubjectScoreContrast() {
        List<BarEchartsSeriesModel> barEchartsSeriesList = new ArrayList<>();
        // 获取该学科下的所有成绩记录
        List<Scores> scoresList = scoresRepository.findAll();

        // 统计方法同时统计同组的最大值、最小值、计数、求和、平均数信息
        HashMap<Course, DoubleSummaryStatistics> resultGradeClass = scoresList.stream()
                .collect(Collectors.groupingBy(Scores::getCourse, HashMap::new, Collectors.summarizingDouble(Scores::getScore)));
        // 平均成绩
        List<Double> averageList = new ArrayList<>();
        // 最高成绩
        List<Double> maxList = new ArrayList<>();
        // 最低成绩
        List<Double> minList = new ArrayList<>();
        // 总人数
        List<Double> countList = new ArrayList<>();
        // 横坐标
        List<String> categoryList = new ArrayList<>();
        resultGradeClass.forEach((k, v) -> {
            // 学科名称
            categoryList.add(k.getCoursename());
            // 平均成绩,保留两位小数
            v.getAverage();
            BigDecimal bigDecimal = new BigDecimal(v.getAverage());
            double average = bigDecimal.setScale(2, RoundingMode.HALF_UP).doubleValue();
            averageList.add(average);
            // 最高成绩
            v.getMax();
            maxList.add(v.getMax());
            // 最低成绩
            v.getMin();
            minList.add(v.getMin());
            // 总人数
            countList.add((double)v.getCount());

        });

        // 平均成绩
        BarEchartsSeriesModel averageBarEchartsSeriesModel = new BarEchartsSeriesModel();
        averageBarEchartsSeriesModel.setData(averageList);
        averageBarEchartsSeriesModel.setType("bar");
        averageBarEchartsSeriesModel.setName("平均成绩");
        barEchartsSeriesList.add(averageBarEchartsSeriesModel);
        // 最高成绩
        BarEchartsSeriesModel maxBarEchartsSeriesModel = new BarEchartsSeriesModel();
        maxBarEchartsSeriesModel.setData(maxList);
        maxBarEchartsSeriesModel.setType("bar");
        maxBarEchartsSeriesModel.setName("最高成绩");
        barEchartsSeriesList.add(maxBarEchartsSeriesModel);
        // 最低成绩
        BarEchartsSeriesModel minBarEchartsSeriesModel = new BarEchartsSeriesModel();
        minBarEchartsSeriesModel.setData(minList);
        minBarEchartsSeriesModel.setType("bar");
        minBarEchartsSeriesModel.setName("最低成绩");
        barEchartsSeriesList.add(minBarEchartsSeriesModel);
        // 班级总人数
        BarEchartsSeriesModel countBarEchartsSeriesModel = new BarEchartsSeriesModel();
        countBarEchartsSeriesModel.setData(countList);
        countBarEchartsSeriesModel.setType("bar");
        countBarEchartsSeriesModel.setName("总人数");
        barEchartsSeriesList.add(countBarEchartsSeriesModel);

        // 定义返回对象
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("categoryList",categoryList);
        resultMap.put("barEchartsSeriesList",barEchartsSeriesList);
        return resultMap;
    }
}
