package cn.xueden.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**功能描述：上传本地文件
 * @author:梁志杰
 * @date:2022/12/23
 * @description:cn.xueden.utils
 * @version:1.0
 */
public class NativeFileUtil {

    /**
     * 上传头像
     * @param uploadFile
     *        上传文件
     * @param filePath
     *           上传路径
     * @return
     */
    public static String uploadUserIcon(MultipartFile uploadFile,String filePath){

        // 获取原始名称
        String oldName = uploadFile.getOriginalFilename();
        // 获取文件大小
        long pictureSize = uploadFile.getSize();
         // 文件扩展名
        String fileExtension = oldName.substring(oldName.lastIndexOf("."), oldName.length());
        if(fileExtension.equals(".png")||fileExtension.equals(".jpg")||fileExtension.equals(".gif")){
            File folder = new File(filePath);
            if(!folder.isDirectory()) {
                folder.mkdirs();
            }
            String newName = UUID.randomUUID().toString() +
                    oldName.substring(oldName.lastIndexOf("."), oldName.length());
            // 文件保存操作
            try {
                uploadFile.transferTo(new File(folder, newName));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return newName;
        }else{
            return null;
        }
    }
}
