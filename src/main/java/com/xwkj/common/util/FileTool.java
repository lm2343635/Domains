package com.xwkj.common.util;

import java.io.*;
import java.util.ArrayList;


public class FileTool {

    /**
     * Copy file from the source to the target.
     *
     * @param sourceFile
     * @param targetFile
     * @throws IOException
     */
    public static void copyFile(File sourceFile, File targetFile) {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1)
                outBuff.write(b, 0, len);
            outBuff.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inBuff != null)
                    inBuff.close();
                if (outBuff != null)
                    outBuff.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Copy file from the source path to the target path.
     *
     * @param sourceFile
     * @param targetFile
     */
    public static void copyFile(String sourceFile, String targetFile) {
        copyFile(new File(sourceFile), new File(targetFile));
    }

    /**
     * Move file from the srouce to the target.
     *
     * @param sourceFile
     * @param targetFile
     */
    public static void moveFile(File sourceFile, File targetFile) {
        copyFile(sourceFile, targetFile);
        sourceFile.delete();
    }

    /**
     * Move file from the srouce path to the target path.
     *
     * @param sourceFile
     * @param targetFile
     */
    public static void moveFile(String sourceFile, String targetFile) {
        moveFile(new File(sourceFile), new File(targetFile));
    }

    /**
     * Create a directory if not existing.
     *
     * @param path
     */
    public static void createDirectoryIfNotExsit(String path) {
        File dir = new File(path);
        if (!dir.exists())
            dir.mkdir();
    }

    /**
     *
     * @param path
     * @return
     */
    public static boolean isEmptyDirectory(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        if (files.length == 0)
            return true;
        return false;
    }

    /**
     * Get file formate
     *
     * @param fileName
     * @return
     */
    public static String getFormat(String fileName) {
        int index = fileName.lastIndexOf(".");
        String format = fileName.substring(index + 1);
        return format;
    }

    /**
     * 得到文件名
     *
     * @param fileName 文件全名
     * @return 文件名
     */
    public static String getName(String fileName) {
        int index = fileName.lastIndexOf(".");
        String name = fileName.substring(0, index);
        return name;
    }

    /**
     * Get all files of a same format in a path.
     *
     * @param path
     * @param format
     * @return
     */
    public static ArrayList<File> getFiles(String path, String format) {
        File dir = new File(path);
        File[] files = dir.listFiles();
        ArrayList<File> fileList = new ArrayList<File>();
        for (int i = 0; i < files.length; i++) {
            if (format.equals(getFormat(files[i].getName()))) {
                fileList.add(files[i]);
            }
        }
        return fileList;
    }

    /**
     * Rename file
     *
     * @param path
     * @param fileName
     * @param newFileName
     * @return
     */
    public static void modifyFileName(String path, String fileName, String newFileName) {
        copyFile(path + File.separator + fileName, path + File.separator + newFileName);
        File file = new File(path + File.separator + fileName);
        file.delete();
    }

    /**
     * Delete all files
     *
     * @param path
     * @return
     */
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);
                delDirectory(path + "/" + tempList[i]);
                flag = true;
            }
        }
        return flag;
    }

    /**
     * Delete directory.
     *
     * @param path
     */
    public static void delDirectory(String path) {
        try {
            delAllFile(path);
            String filePath = path;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
