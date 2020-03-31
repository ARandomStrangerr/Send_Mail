import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

public class TestStarter {
    public static void main (String[] args) throws IOException {
        File file = new File("/Users/thanhdo/Downloads/20161028_173717.jpg");
        byte[] bytesArr = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        fis.read(bytesArr);
        System.out.println(file.getAbsolutePath().substring(file.getAbsolutePath().indexOf(".")));
    }
}
