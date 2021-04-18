/**
 * @BelongsProject： apiface
 * @BelongsPackage： io.github.dayeshing.apiface.core.util
 * @Author： Daye Shing
 * @CreateTime： 2021-03-02 02:52
 * @Description: <p>  </p>
 */
package io.github.dayeshing.apiface.core.util;


import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @Description: <p> TODO </p>
 * @ClassName: GZIPUtil
 * @ClassAllName: io.github.dayeshing.apiface.core.util.GZIPUtil
 * @Author： Daye Shing
 */
public class GZIPUtil {

    /**
     * 压缩字符串为byte数组
     * @param str 要进行压缩的字符串
     */
    public static byte[] compress(byte[] str) {
        ByteArrayOutputStream byteArrayOutputStream = null;
        GZIPOutputStream gzipOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            write(str, gzipOutputStream);
            gzipOutputStream.close();//重要 否则byteArrayOutputStream返回的数据不完整 将剩余压缩数据写入输出流并关闭底层流。继续自DeflaterOutputStream
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ObjectUtil.close(byteArrayOutputStream);
        }
        return null;
    }
    public static void write(byte[] data, OutputStream output) throws IOException {
        if (data != null) {
            output.write(data);
        }
    }

    /**
     * 解压gzip格式的byte数组
     * @param bytes gzip格式byte数组
     */
    public static byte[] uncompress(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = null;
        ByteArrayInputStream byteArrayInputStream = null;
        GZIPInputStream gzipInputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayInputStream = new ByteArrayInputStream(bytes);
            gzipInputStream = new GZIPInputStream(byteArrayInputStream);
            copy(gzipInputStream, byteArrayOutputStream,new byte[4096]);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //释放流资源
            ObjectUtil.close(gzipInputStream);
            ObjectUtil.close(byteArrayInputStream);
        }
        return null;
    }
    public static long copy(InputStream input, OutputStream output, byte[] buffer) throws IOException {
        long count;
        int n;
        for(count = 0L; -1 != (n = input.read(buffer)); count += (long)n) {
            output.write(buffer, 0, n);
        }
        return count;
    }

}
