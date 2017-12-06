package com.zhiyou.colleageapp.appui.life;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 根据values/dimens.xml, 自动计算比例并生成不同分辨率的dimens.xml
 * 注意用dp和sp，不要用dip，否则生成可能会出错；xml值不要有空格
 * Created by chuyh on 2016/5/16 0016.
 */
public class DimenTool {
    public static void gen() {

        String path = "E:/collegeapp/ColleageApp/app/src/main/res";

        File file = new File(path + "/values/custom_dimens.xml");//这里如果找不到使用绝对路径即可
        BufferedReader reader = null;
//添加分辨率
        StringBuilder sw480 = new StringBuilder();
        StringBuilder sw600 = new StringBuilder();
        StringBuilder sw720 = new StringBuilder();
        StringBuilder sw800 = new StringBuilder();
        StringBuilder w820 = new StringBuilder();

        try {
            System.out.println("生成不同分辨率：");
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束

            while ((tempString = reader.readLine()) != null) {

                if (tempString.contains("</dimen>")) {
                    //tempString = tempString.replaceAll(" ", "");
                    String start = tempString.substring(0, tempString.indexOf(">") + 1);
                    String end = tempString.substring(tempString.lastIndexOf("<") - 2);
                    int num = Integer.valueOf(tempString.substring(tempString.indexOf(">") + 1, tempString.indexOf("</dimen>") - 2));

                    //主要核心就再这里了，如下3种分辨率分别缩小 0.6、0.75、0.9倍(根据实际情况自己随意DIY)
                    sw480.append(start).append((int) Math.round(num * 0.6)).append(end).append("\n");
                    sw600.append(start).append((int) Math.round(num * 0.75)).append(end).append("\n");
                    sw720.append(start).append((int) Math.round(num * 0.9)).append(end).append("\n");
                    sw800.append(tempString).append("\n");
                    w820.append(tempString).append("\n");

                } else {
                    sw480.append(tempString).append("\n");
                    sw600.append(tempString).append("\n");
                    sw720.append(tempString).append("\n");
                    sw800.append(tempString).append("\n");
                    w820.append(tempString).append("\n");
                }
                line++;
            }
            reader.close();
            System.out.println("<!--  sw480 -->");
            System.out.println(sw480);
            System.out.println("<!--  sw600 -->");
            System.out.println(sw600);

            System.out.println("<!--  sw720 -->");
            System.out.println(sw720);
            System.out.println("<!--  sw800 -->");
            System.out.println(sw800);

            String sw480file = "/custom_dimens.xml";
            String sw600file = "/custom_dimens.xml";
            String sw720file = "/custom_dimens.xml";
            String sw800file = "/custom_dimens.xml";
            String w820file = "/custom_dimens.xml";

            writeFile(path + "/values-sw480dp-land", sw480file, sw480.toString());
            writeFile(path + "/values-sw600dp-land", sw600file, sw600.toString());
            writeFile(path + "/values-sw720dp-land", sw720file, sw720.toString());
            writeFile(path + "/values-sw800dp-land", sw800file, sw800.toString());
            writeFile(path + "/values-w820dp", w820file, w820.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

//        PrintWriter out = null;
//        try {
//            out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
//            out.println(s);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        out.close();

    public static void writeFile(String path, String name, String s) {

        File f0 = new File(path);
        if (!f0.exists()) {
            f0.mkdirs();
        }

        File f = new File(path + name);
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileWriter fw = new FileWriter(f);
            BufferedWriter out = new BufferedWriter(fw);
            out.write(s, 0, s.length() - 1);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("写文件 end");
    }

    public static void main(String[] args) {
        gen();
    }
}
