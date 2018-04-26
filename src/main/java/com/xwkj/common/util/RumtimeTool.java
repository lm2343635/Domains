package com.xwkj.common.util;

import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class RumtimeTool {

    public static void run(String cmd) {
        String[] cmds = {"/bin/bash", "-c",  cmd};
        exec(cmds);
    }

    public static void exec(String [] cmds) {
        try {
            Process process = Runtime.getRuntime().exec(cmds);
            InputStreamReader ir = new InputStreamReader(process.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line;
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
