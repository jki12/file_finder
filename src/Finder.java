import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Finder {
    private static final int MAX_DEPTH = 30;
    private static ArrayList<String> foundFiles = new ArrayList<>();
    private static StringBuilder sb = new StringBuilder();
    private static String ignoreDir = ".git";
    private static String target = "";

    private static String getFormatter(int depth, boolean isDir, boolean isLast)
    {
        sb.setLength(0); // clear.

        if (!isDir) {
            for (int i = 0; i < depth + 1; ++i) sb.append("|    ");

            if (isLast) sb.replace(sb.length() - "|    ".length(), sb.length(), "/");
        }
        else {
            for (int i = 0; i < depth; ++i) sb.append("|    ");
            sb.append("+----");
        }

        return sb.toString();
    }

    private static void dfs(File file, int depth)
    {
        if (MAX_DEPTH <= depth) {
            return;
        }

        var list = file.list();

        for (int i = 0; i < list.length; ++i) {
            if (ignoreDir.equals(list[i])) {
                continue;
            }

            String path = file.getPath() + "\\" + list[i];

            if (target.equals(list[i])) {
                foundFiles.add(path);
            }

            boolean isDir = Files.isDirectory(Paths.get(path));
            var formatter = getFormatter(depth, isDir, false);

            System.out.println(formatter + list[i]);

            if (isDir) {
                dfs(new File(path), depth + 1);
            }
        }

        System.out.println(getFormatter(depth, false, true)); // 가독성 높이려고 넣었음.
    }

    public static void main(String[] args) {
        var in = new Scanner(System.in);

        System.out.println("+-----------------------------------+");
        System.out.println("|       최대 검색 깊이는 30입니다       |");
        System.out.println("|       예외 및 에러 처리 안했엉        |");
        System.out.println("+-----------------------------------+");

        System.out.print("dir path : ");
        var dir = new File(in.nextLine());

        System.out.print("search file name : ");
        target = in.nextLine();

        dfs(dir, 0);

        if (foundFiles.size() == 0) {
            System.out.println("no matching files");
        }

        for (int i = 0; i < foundFiles.size(); ++i) {
            System.out.println(foundFiles.get(i));
        }
    }
}
