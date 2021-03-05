/**
 * @BelongsProject： apiface
 * @BelongsPackage： com.daysh.apiface.core.util
 * @Author： Daye Shing
 * @CreateTime： 2020-08-16 22:48
 * @Description: <p>  </p>
 */
package com.daysh.apiface.core.util;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.Collection;
import java.util.Map;

/**
 * @Description: <p>  对象工具类 </p>
 * @ClassName: ObjectUtil
 * @ClassAllName: com.daysh.apiface.core.util.ObjectUtil
 * @Author： Daye Shing
 * @Date： 2020-08-16 22:48
 */
public class ObjectUtil {

    public static boolean isNotEmpty(Map map) {
        return !isEmpty(map);
    }

    public static boolean isEmpty(Map map) {
        return (map == null || map.isEmpty());
    }

    public static boolean isEmpty(Collection coll) {
        return (coll == null || coll.isEmpty());
    }

    public static boolean isNotEmpty(Collection coll) {
        return !isEmpty(coll);
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean equals(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equals(str2);
    }
    public static boolean equalsIgnoreCase(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equalsIgnoreCase(str2);
    }

    public static void copy(File source, File target) {
        if(source.exists() && source.isFile()){
            FileChannel in = null;
            FileChannel out = null;
            try {
                in = new FileInputStream(source).getChannel();
                out = new FileOutputStream(target).getChannel();
                out.transferFrom(in, 0, in.size());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                close(in);
                close(out);
            }
        }
    }

    public static void write(File target, byte[] bytes) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(target);
            out.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(out);
        }
    }

    public static byte[] read(InputStream in) {
        byte[] ret = new byte[0];
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            ret = baos.toByteArray();
            close(baos);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(in);
        }
        return ret;
    }

    /**
     * close
     *
     * @param io -
     * @return void -
     * @throws
     * @description: <p> 关闭io </p>
     * @methodName close
     * @author Daye Shing
     * @date 2020/8/16 23:01
     * @since 1.0
     */
    public static void close(Closeable io) {
        if (io != null) {
            try {
                io.close();
            } catch (IOException e) {
            }
        }
    }

}
