package heima.it.safe.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 作者：张琦 on 2016/5/14 20:48
 * 邮箱：759308541@qq.com
 *
 * 网络访问服务器 解析JSON文件
 * 判断是否有更新
 */
public class StreamJson {
    public static String ReadJson(InputStream is){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] b = new byte[1024*8];
        int len = 0;
        try {
            while((len = is.read(b))!=-1){
                baos.write(b,0,len);
            }
            //解析完成
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toString();
    }
}
