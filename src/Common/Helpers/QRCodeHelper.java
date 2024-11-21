package Common.Helpers;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class QRCodeHelper {
    public static String generateQRCode(String input, String extension) {
        String filePath = "";
        try {
            String linkPath = "src\\Assets\\img\\";
            String fileName = FilenameHelper.generateRandomFileName(extension);
            String charset = "UTF-8";

            filePath += linkPath + fileName;
            Map <EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

            BitMatrix matrix = new MultiFormatWriter().encode(
              new String(input.getBytes(charset), charset),
                    BarcodeFormat.QR_CODE,
                    250,
                    250,
                    hintMap
            );
            MatrixToImageWriter.writeToFile(matrix, extension, new File(filePath));
        } catch (Exception e) {
            System.out.println("An error occur");
        }
        return filePath;
    }
}
