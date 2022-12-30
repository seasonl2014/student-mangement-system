package cn.xueden.utils;

/**工具类
 * @author:梁志杰
 * @date:2022/12/25
 * @description:cn.xueden.utils
 * @version:1.0
 */
public class XuedenUtil {

    /**
     * 随机生成六位数
     * @return
     */
   public  static int randomSixNums(){
       int code = (int) ((Math.random()*9+1)*100000);
       return code;
   }

    public static void main(String[] args) {
        System.out.println(XuedenUtil.randomSixNums());
    }
}
