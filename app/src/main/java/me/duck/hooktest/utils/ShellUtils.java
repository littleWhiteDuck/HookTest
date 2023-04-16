package me.duck.hooktest.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShellUtils {
    private static final String KEY_COMMAND_SHELL = "sh";
    private static final String KEY_LINE_END_CHAR = "\n";
    private static final String KEY_EXIT = "exit\n";

    public static String execCmd(String command) {
        return execCmd(new String[]{command}).trim();
    }

    public static String execCmd(String[] commands) {
        Process process = null;
        BufferedReader reader = null;
        InputStreamReader inputStream = null;
        DataOutputStream outputStream = null;
        try {
            process = Runtime.getRuntime().exec(KEY_COMMAND_SHELL);
            inputStream = new InputStreamReader(process.getInputStream());
            reader = new BufferedReader(inputStream);
            outputStream = new DataOutputStream(process.getOutputStream());
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            for (String command : commands) {
                outputStream.writeBytes(command + KEY_LINE_END_CHAR);
            }
            outputStream.writeBytes(KEY_EXIT);
            outputStream.flush();
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append(KEY_LINE_END_CHAR);
            }
            process.waitFor();
            return stringBuilder.toString();
        } catch (Exception ignored) {
            return "null";
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }

                if (reader != null) {
                    reader.close();
                }

                if (inputStream != null) {
                    inputStream.close();
                }

                if (process != null) {
                    process.destroy();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
