package cn.xueden.student.vo;

import lombok.Data;

import java.util.List;

/**柱形图返回结果集对象
 * @author:梁志杰
 * @date:2022/12/20
 * @description:cn.xueden.student.vo
 * @version:1.0
 */
@Data
public class BarEchartsSeriesModel {
    private List<Double> data;
    private String type;
    private String name;
}
