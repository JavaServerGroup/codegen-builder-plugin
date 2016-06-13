package com.jtool.codegenbuilderplugin.finder;

import com.jtool.codegenbuilderplugin.BuilderMojo;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileFinder {

    public static List<File> findAllFileNeedToParse(BuilderMojo builderMojo) {

        //检查scanBasePackageStr, 必须且不为空。
        if (StringUtils.isBlank(builderMojo.getScanBasePackage())) {
            throw new RuntimeException("设置扫描的路径");
        } else {
            builderMojo.setScanBasePackage(builderMojo.getScanBasePackage().trim());
        }

        File scanBasePackageFile = makeScanPackageFile(builderMojo);

        return findAllFileNeedToParse(scanBasePackageFile);
    }

    //递归找出所有需要分析的文件(.java结尾的文件)
    private static List<File> findAllFileNeedToParse(File file) {

        List<File> result = new ArrayList<>();

        File[] files = file.listFiles();

        if (files != null && files.length > 0) {
            for (File fileItem : files) {
                if (fileItem.isDirectory()) {
                    result.addAll(findAllFileNeedToParse(fileItem));
                } else if (fileItem.getAbsolutePath().endsWith(".java")) {
                    result.add(fileItem);
                }
            }
        }

        return result;
    }

    //生成需要递归扫描的路径对应的File实例
    private static File makeScanPackageFile(BuilderMojo builderMojo) {
        String scanBasePackagePath = makePackagePath(builderMojo.getScanBasePackage());
        File scanBasePackageFile = new File(builderMojo.getBasedir().getAbsolutePath() + builderMojo.getScanSource() + scanBasePackagePath);
        builderMojo.getLog().debug("准备扫描的路径：" + scanBasePackageFile.getAbsolutePath());
        if (!scanBasePackageFile.exists()) {
            throw new RuntimeException("设置的扫描路径有误:" + scanBasePackageFile.getAbsolutePath());
        }
        return scanBasePackageFile;
    }

    //将包名转化为路径
    private static String makePackagePath(String packageName) {
        String result = "";
        for (String str : packageName.split("\\.")) {
            result += str + "/";
        }
        return result;
    }
}
