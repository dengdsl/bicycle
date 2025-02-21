
import java.io.*;
import java.nio.file.*;
import java.util.regex.*;

public class UnicodeToChineseConverter {

    public static void main(String[] args) {


        String folderPath = "D:\\onecent_code\\fake\\src\\main\\java\\com\\company\\project";
        File folder = new File(folderPath);

        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("指定的路径不是一个有效的文件夹！");
            return;
        }

        // 遍历文件夹并处理 .java 文件
        processFolder(folder);
    }

    /**
     * 遍历文件夹并处理所有 .java 文件
     */
    private static void processFolder(File folder) {
        File[] files = folder.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                // 递归处理子文件夹
                processFolder(file);
            } else if (file.getName().endsWith(".java")) {
                // 处理 .java 文件
                processJavaFile(file);
            }
        }
    }

    /**
     * 处理单个 .java 文件
     */
    private static void processJavaFile(File javaFile) {
        try {
            // 读取文件内容
            String content = new String(Files.readAllBytes(javaFile.toPath()));

            // 替换 Unicode 转义序列为中文
            String updatedContent = convertUnicodeToChinese(content);

            // 将更新后的内容写回文件
            Files.write(javaFile.toPath(), updatedContent.getBytes());

            System.out.println("已处理文件: " + javaFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("处理文件时出错: " + javaFile.getAbsolutePath());
            e.printStackTrace();
        }
    }

    /**
     * 将字符串中的 Unicode 转义序列替换为中文
     */
    private static String convertUnicodeToChinese(String input) {
        // 正则表达式匹配 Unicode 转义序列（如 \u4F60）
        Pattern pattern = Pattern.compile("\\\\u([0-9a-fA-F]{4})");
        Matcher matcher = pattern.matcher(input);

        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            // 提取 Unicode 码点（十六进制）
            String hexCode = matcher.group(1);
            // 将十六进制转换为字符
            char ch = (char) Integer.parseInt(hexCode, 16);
            // 替换匹配的内容
            matcher.appendReplacement(result, String.valueOf(ch));
        }

        // 添加剩余部分
        matcher.appendTail(result);

        return result.toString();
    }
}