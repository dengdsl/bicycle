import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class DeleteClassFiles {

    public static void main(String[] args) {
        // 指定需要清理的文件夹路径
        String folderPath = "F:\\workspace\\jizhi-fake\\src\\main\\java\\com\\company\\project"; // 修改为你的目录

        // 调用删除方法
        deleteClassFiles(folderPath);
    }

    public static void deleteClassFiles(String folderPath) {
        Path directory = Paths.get(folderPath);

        if (!Files.exists(directory) || !Files.isDirectory(directory)) {
            System.out.println("目录不存在: " + folderPath);
            return;
        }

        try {
            Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    // 如果是 .class 文件，则删除
                    if (file.toString().endsWith(".class")) {
                        Files.deleteIfExists(file);
                        System.out.println("删除: " + file);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
            System.out.println("所有 .class 文件删除完成！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
