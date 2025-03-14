package com.cxd.cmcc.util;

import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

/**
 * @Description: 复制指定目录下的文件到目标目录，并保持原目录结构
 * @Author: caixd
 * @Author: caixd
 * @Date: 2025/3/13 17:54
 */
public class PdfDirectoryCopier {

    public static void main(String[] args) {


        Path sourceDir = Paths.get("D:\\LenovoQMDownload\\图灵五期");
        Path targetDir = Paths.get("D:\\File\\document\\java宝典\\图灵资料");

        try {
            copyPdfFiles(sourceDir, targetDir);
            System.out.println("复制完成！");
        } catch (IOException e) {
            System.err.println("发生错误: " + e.getMessage());
        }
    }

    private static void copyPdfFiles(Path sourceDir, Path targetDir) throws IOException {
        try (Stream<Path> fileStream = Files.walk(sourceDir)) {
            fileStream
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().toLowerCase().endsWith(".pdf"))
                .forEach(path -> copyFile(path, sourceDir, targetDir));
        }
    }

    private static void copyFile(Path sourceFile, Path sourceDir, Path targetDir) {
        try {
            // 计算相对路径
            Path relativePath = sourceDir.relativize(sourceFile);
            Path destination = targetDir.resolve(relativePath);
            
            // 创建目标目录结构
            Files.createDirectories(destination.getParent());
            
            // 复制文件（覆盖已存在的文件）
            Files.copy(sourceFile, destination, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("已复制: " + sourceFile + " 到 " + destination);
        } catch (IOException e) {
            System.err.println("复制失败 [" + sourceFile + "]: " + e.getMessage());
        }
    }
}