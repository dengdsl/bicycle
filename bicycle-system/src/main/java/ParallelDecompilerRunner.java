import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ParallelDecompilerRunner {

    public static void main(String[] args) {
        File inputFolder = new File("D:\\onecent_code\\fake\\com");
        File outputFolder = new File("D:\\onecent_code\\fake\\output");

        if (!inputFolder.exists() || !inputFolder.isDirectory()) {
            System.err.println("Input folder does not exist or is not a directory.");
            System.exit(1);
        }

        // 若输出目录不存在，则创建
        if (!outputFolder.exists()) {
            outputFolder.mkdirs();
        }

        // 创建固定线程池，线程数设置为可用的 CPU 核数
        int nThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(nThreads);

        // 开始递归处理文件夹
        processDirectory(inputFolder, inputFolder, outputFolder, executor);

        // 关闭线程池并等待所有任务结束（最多等待 1 小时）
        executor.shutdown();
        try {
            if (!executor.awaitTermination(1, TimeUnit.HOURS)) {
                System.err.println("Timeout waiting for decompilation tasks to finish.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 递归遍历目录，遇到 .class 文件提交反编译任务
     *
     * @param baseFolder   输入目录的根，用于计算相对路径
     * @param currentFolder 当前遍历的目录
     * @param outputFolder  总的输出目录
     * @param executor      用于并行执行反编译任务的线程池
     */
    private static void processDirectory(File baseFolder, File currentFolder, File outputFolder, ExecutorService executor) {
        File[] files = currentFolder.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                processDirectory(baseFolder, file, outputFolder, executor);
            } else if (file.isFile() && file.getName().endsWith(".class")) {
                // 提交反编译任务到线程池
                executor.submit(() -> {
                    // 调用 Procyon 反编译器命令：java -jar procyon-decompiler-0.6.0.jar <classFile> -o <outputDir>
                    ProcessBuilder pb = new ProcessBuilder(
                            "java",
                            "-jar",
                            "C:\\Users\\Dragon\\Desktop\\procyon-decompiler-0.6.0.jar",
                            file.getAbsolutePath(),
                            "-o",
                            outputFolder.getAbsolutePath()
                    );
                    pb.redirectErrorStream(true);
                    try {
                        Process process = pb.start();
                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                System.out.println(line);
                            }
                        }
                        int exitCode = process.waitFor();
                        if (exitCode != 0) {
                            System.err.println("Error decompiling file: " + file.getAbsolutePath());
                        } else {
                            System.out.println("Decompiled: " + file.getAbsolutePath());
                        }
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }
}
