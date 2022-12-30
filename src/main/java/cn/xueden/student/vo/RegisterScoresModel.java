package cn.xueden.student.vo;

import lombok.Data;

/**功能描述：登记班级学科成绩参数
 * @author:梁志杰
 * @date:2022/12/19
 * @description:cn.xueden.student.vo
 * @version:1.0
 */
@Data
public class RegisterScoresModel {

    /**
     * 班级ID
     */
    private Long gradeClassId;

    /**
     * 课程ID
     */
    private Long courseId;
}
