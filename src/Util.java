import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;

public class Util {
    public static final String EMPTY_STRING = "";

    private static final StringBuilder SB = new StringBuilder();

    private Util() {
    }

    public static boolean hasFileExtension(String fileName) { // ex) a.
        int index = fileName.lastIndexOf('.');

        return (index != -1 && index != fileName.length() - 1);
    }

    public static String getExtension(String fileName) {
        if (!hasFileExtension(fileName)) return EMPTY_STRING;

        SB.setLength(0);

        for (int i = fileName.lastIndexOf('.') + 1; i < fileName.length(); ++i) {
            SB.append(fileName.charAt(i));
        }

        return SB.toString();
    }

    public static String removeFileExtension(String fileName) {
        if (!hasFileExtension(fileName)) {
            return fileName;
        }

        SB.setLength(0);

        for (int i = 0; i < fileName.lastIndexOf('.'); ++i) {
            SB.append(fileName.charAt(i));
        }

        return SB.toString();
    }

    public static String readFile(final String path) {
        SB.setLength(0);

        try {
            File file = new File(path);
            BufferedReader fileReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8));

            String s;
            while ((s = fileReader.readLine()) != null) {
                SB.append(s);
                SB.append(System.lineSeparator());
            }

        } catch (Exception e) {
            e.printStackTrace();

            return EMPTY_STRING;
        }

        return SB.toString();
    }

    public static boolean compare(final String s1, final String s2, boolean ignoreCase) {
        return (ignoreCase ? s1.equalsIgnoreCase(s2) : s1.equals(s2));
    }
}
