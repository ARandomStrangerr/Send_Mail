package bin;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class TextFileOperation implements FileOperation {
    @Override
    public List<String> read(String path) throws IOException {
        LinkedList<String> lines = new LinkedList<>();
        BufferedReader br = new BufferedReader(new FileReader(path));
        for (String line = br.readLine(); line != null; line = br.readLine()) {
            lines.add(line);
        }
        br.close();
        return lines;
    }

    @Override
    public void write(String path) {

    }
}
