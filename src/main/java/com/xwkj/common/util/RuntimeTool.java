package com.xwkj.common.util;

import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class RuntimeTool {

    public static boolean run(String cmd) {
        String[] cmds = {"/bin/bash", "-c",  cmd};
        return exec(cmds);
    }

    public static boolean exec(String [] cmds) {
        try {
            Process process = Runtime.getRuntime().exec(cmds);
            InputStreamReader ir = new InputStreamReader(process.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line;
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
