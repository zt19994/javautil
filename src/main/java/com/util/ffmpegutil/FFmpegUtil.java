package com.util.ffmpegutil;

import com.util.fileutil.FileUtil;
import com.util.traceutil.StackTraceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * FFmpeg 工具类
 *
 * @author zt1994 2020/3/2 16:40
 */
public class FFmpegUtil {

    private static final Logger logger = LoggerFactory.getLogger(FFmpegUtil.class);

    private static final String FFMPEG_PATH = "ffmpeg";

    private static final String ZERO = "0";


    /**
     * 合并mp4视频片段，目前只能合并mp4格式的视频文件
     * ffmpeg -i 1.mp4 -vcodec copy -acodec copy -vbsf h264_mp4toannexb 1.ts
     * ffmpeg -i 2.mp4 -vcodec copy -acodec copy -vbsf h264_mp4toannexb 2.ts
     * ffmpeg -i "concat:1.ts|2.ts" -acodec copy -vcodec copy -absf aac_adtstoasc output.mp4
     *
     * @param inputVideoFragmentPaths 输入视频文件路径列表
     * @param outVideoPath            输出合并的视频文件路径
     * @param threadPool              执行线程池
     * @param coverName               封面名称
     */
    public static void concatVideoFragments(ArrayList<String> inputVideoFragmentPaths,
                                            String outVideoPath,
                                            ExecutorService threadPool,
                                            String coverName) {
        logger.info("开始拼接MP4视频片段");
        if (inputVideoFragmentPaths.size() == 0) {
            logger.info("MP4视频片段为空");
            return;
        }

        if (inputVideoFragmentPaths.size() == 1) {
            logger.info("单个MP4视频片段");
            File inputVideoFile = new File(inputVideoFragmentPaths.get(0));
            if (inputVideoFile.exists() && !inputVideoFile.isDirectory()) {
                File outVideoFile = new File(outVideoPath);
                if (!outVideoFile.isDirectory()) {
                    inputVideoFile.renameTo(outVideoFile);
                }
            }
            // 单个MP4视频生成封面
            String parentPath = outVideoPath.substring(0, outVideoPath.lastIndexOf('/') + 1);
            generateFirstFrameFromVideoFile(outVideoPath, parentPath + coverName);
            return;
        }

        CountDownLatch latch = new CountDownLatch(inputVideoFragmentPaths.size());
        logger.info("多个MP4视频片段：{}", inputVideoFragmentPaths.size());
        long start = System.currentTimeMillis();
        for (String path : inputVideoFragmentPaths) {
            StringBuilder sbMp4ToTs = new StringBuilder();
            sbMp4ToTs.append(FFMPEG_PATH);
            sbMp4ToTs.append(" -i ");
            sbMp4ToTs.append(path);
            sbMp4ToTs.append(" -vcodec copy -acodec copy -vbsf h264_mp4toannexb ");
            sbMp4ToTs.append(path.replace(".mp4", ".ts"));
            threadPool.submit(() -> {
                execCmd(sbMp4ToTs.toString());
                latch.countDown();
                logger.info("count = " + latch.getCount());
            });
        }

        threadPool.submit(() -> {
            // 等所有的ts文件都生成完毕了再合成最后的mp4
            try {
                latch.await();
            } catch (InterruptedException e) {
                logger.info("错误：{}", StackTraceUtil.getStackTraceInfo(e));
            }
            String parentPath = outVideoPath.substring(0, outVideoPath.lastIndexOf('/') + 1);
            String tsFiles = waitUntilNoTempFileOrTimeout(parentPath, 3);
            StringBuilder sbConcat = new StringBuilder();
            sbConcat.append(FFMPEG_PATH);
            sbConcat.append(" -i concat:");
            sbConcat.append(tsFiles);
            sbConcat.append(" -acodec copy -vcodec copy -absf aac_adtstoasc ");
            sbConcat.append(outVideoPath);
            /*execCmd("ffmpeg -i concat:/mnt/nfs/spaces/5533c753-5152-4dd5-8c8c-e35688772981/20190430170009+0800.ts|" +
                    "/mnt/nfs/spaces/5533c753-5152-4dd5-8c8c-e35688772981/20190430170151+0800.ts|" +
                    "/mnt/nfs/spaces/5533c753-5152-4dd5-8c8c-e35688772981/20190430170241+0800.ts|" +
                    "/mnt/nfs/spaces/5533c753-5152-4dd5-8c8c-e35688772981/20190430170301+0800.ts|" +
                    " -acodec copy -vcodec copy -absf aac_adtstoasc /mnt/nfs/spaces/5533c753-5152-4dd5-8c8c-e35688772981/mix.mp4");*/
            logger.info("合并ts字符串{}", tsFiles);
            logger.info("----------------------开始执行合并ts的命令----------------------");
            execCmd(sbConcat.toString());
            long end = System.currentTimeMillis();
            logger.info("执行合并mp4的操作耗时：" + (end - start) + "ms");
            // 删除生成的临时ts文件，节省空间
            deleteAllTsFile(parentPath);
            // 生成封面图片
            generateFirstFrameFromVideoFile(outVideoPath, parentPath + coverName);
        });
    }


    /**
     * 执行命令
     *
     * @param cmd
     */
    private static void execCmd(String cmd) {
        logger.info("执行命令：" + cmd);
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            // 需要启用一个线程去读取缓冲区中的数据，不让java虚拟机线程阻塞
            new RunThread(process.getInputStream(), "INFO").start();
            new RunThread(process.getErrorStream(), "ERROR").start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                logger.info("execCmd 执行成功");
            } else {
                logger.error("execCmd 执行失败");
            }
        } catch (Exception e) {
            logger.info("错误：{}", StackTraceUtil.getStackTraceInfo(e));
        }
    }


    /**
     * 执行命令带返回结果
     *
     * @param cmd
     * @return
     */
    public static String execCmdWithReturn(String cmd) {
        logger.info("执行命令：" + cmd);
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            InputStream inputStream = process.getErrorStream();
            return read(inputStream);
        } catch (Exception e) {
            logger.info("错误：{}", StackTraceUtil.getStackTraceInfo(e));
        }
        return "";
    }


    /**
     * 获取视频时长
     *
     * @param videoPath
     * @return
     */
    public static int getVideoDuration(String videoPath) {
        List<String> commands = new ArrayList<>();
        commands.add(FFMPEG_PATH);
        commands.add("-i");
        commands.add(videoPath);
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commands);
            final Process p = builder.start();

            //从输入流中读取视频信息
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();

            //从视频信息中解析时长
            String regexDuration = "Duration: (.*?), start: (.*?), bitrate: (\\d*) kb\\/s";
            Pattern pattern = Pattern.compile(regexDuration);
            Matcher m = pattern.matcher(sb.toString());
            if (m.find()) {
                int time = getTimeLen(m.group(1));
                logger.info(videoPath + ",视频时长：" + time + ", 开始时间：" + m.group(2) + ",比特率：" + m.group(3) + "kb/s");
                return time;
            }
        } catch (Exception e) {
            logger.info("错误：{}", StackTraceUtil.getStackTraceInfo(e));
        }
        return 0;
    }


    /**
     * 获取时间长
     *
     * @param timeLen
     * @return
     */
    private static int getTimeLen(String timeLen) {
        int min = 0;
        String[] strings = timeLen.split(":");
        if (strings[0].compareTo(ZERO) > 0) {
            //秒
            min += Integer.valueOf(strings[0]) * 60 * 60;
        }
        if (strings[1].compareTo(ZERO) > 0) {
            min += Integer.valueOf(strings[1]) * 60;
        }
        if (strings[2].compareTo(ZERO) > 0) {
            min += Math.round(Float.valueOf(strings[2]));
        }
        return min;
    }


    /**
     * 读取流
     *
     * @param inputStream
     * @return
     */
    private static String read(InputStream inputStream) {
        InputStreamReader isr = new InputStreamReader(inputStream);
        BufferedReader br = new BufferedReader(isr);
        String line;
        StringBuilder sb = new StringBuilder();
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            logger.info("错误：{}", StackTraceUtil.getStackTraceInfo(e));
        } finally {
            try {
                isr.close();
            } catch (IOException e) {
                logger.info("错误：{}", StackTraceUtil.getStackTraceInfo(e));
            }
        }
        return sb.toString();
    }


    /**
     * run 线程
     */
    static class RunThread extends Thread {
        private String printType;
        private InputStream is;

        RunThread(InputStream is, String printType) {
            this.is = is;
            this.printType = printType;
        }

        @Override
        public void run() {
            try {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line = null;
                while ((line = br.readLine()) != null) {
                    logger.info(printType + " > " + line);
                }
            } catch (IOException ioe) {
                logger.info("错误：{}", StackTraceUtil.getStackTraceInfo(ioe));
            }
        }
    }


    /**
     * 等待指定目录下没有temp文件了，或者搜索超时了，就返回指定目录下的ts文件的路径字符串
     *
     * @param searchPath
     * @param timeout
     * @return
     */
    public static String waitUntilNoTempFileOrTimeout(String searchPath, int timeout) {
        boolean hasTempFile = true;
        StringBuilder tsFiles = new StringBuilder();
        while (hasTempFile && timeout > 0) {
            hasTempFile = false;
            tsFiles = new StringBuilder();
            File folder = new File(searchPath);
            File[] files = folder.listFiles();
            if (files == null) {
                break;
            }
            File[] orderFiles = FileUtil.orderByDate(files);
            for (File file : orderFiles) {
                logger.info("等待指定目录下没有temp文件或超时:{}", file.getAbsolutePath());
                if (file.getName().endsWith(".temp")) {
                    hasTempFile = true;
                    break;
                } else if (file.getName().endsWith(".ts")) {
                    tsFiles.append(file.getAbsolutePath());
                    tsFiles.append("|");
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.info("错误：{}", StackTraceUtil.getStackTraceInfo(e));
            }

            timeout--;
        }
        logger.info("拼接后的ts文件字符串：" + tsFiles.toString());
        return tsFiles.toString();
    }


    /**
     * 删除ts文件
     *
     * @param filePath
     */
    private static void deleteAllTsFile(String filePath) {
        File folder = new File(filePath);
        if (folder.exists() && folder.isDirectory()) {
            File[] f = folder.listFiles();
            if (f == null) {
                return;
            }
            for (File file : f) {
                if (file.getName().endsWith(".ts")) {
                    logger.info("删除ts文件：{}", file.getName());
                    file.delete();
                }
            }
        }
    }


    /**
     * MP4视频生成封面
     *
     * @param sourceFile
     * @param destFile
     */
    private static void generateFirstFrameFromVideoFile(String sourceFile, String destFile) {
        StringBuffer sb = new StringBuffer();
        sb.append(FFMPEG_PATH);
        sb.append(" -y -i ");
        sb.append(sourceFile);
        sb.append(" -vframes 1 -f mjpeg -an ");
        sb.append(destFile);
        execCmd(sb.toString());
    }
}
