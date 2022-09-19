package utils;

import java.io.File;

public class FileUtils {
  public static String removeExtension(String filename) {
    File file = new File(filename);
    String result = file.getName();
    return result.substring(0, result.lastIndexOf('.'));
  }
}
