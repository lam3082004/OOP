package Common.Helpers;

import java.util.UUID;

public class FilenameHelper {
    public static String generateRandomFileName(String extension) {
        // Tạo UUID ngẫu nhiên
        String randomFileName = UUID.randomUUID().toString();

        // Thêm phần mở rộng nếu có
        return extension != null && !extension.isEmpty() ? randomFileName + "." + extension : randomFileName;
    }
}
