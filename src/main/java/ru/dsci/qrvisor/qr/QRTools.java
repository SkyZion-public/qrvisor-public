package ru.dsci.qrvisor.qr;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.*;

@Slf4j
public class QRTools {

    private static final String FILE_FORMAT = "png";
    public static final String CHARSET = "UTF-8";
    private static final QRSIze DEFAULT_VERSION = QRSIze.MEDIUM;
    private static final HashMap<QRSIze, Integer> qrSizes;

    public static String getTextFromQR(String url) throws IOException {
        Result result;
        try {
            result = decodeBitmap(getBitmapFromUrl(url));
        } catch (RuntimeException | MalformedURLException e) {
            log.debug(String.format("decodeQR: %s", e.getMessage()));
            throw new IOException(String.format("Unable to decrypt QR-code: %s", e.getMessage()));
        }
        return result.getText();
    }

    public static String encodeText(String text, int width, int height)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Hashtable hashtable = new Hashtable();
        hashtable.put(EncodeHintType.CHARACTER_SET, CHARSET);
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hashtable);
        Path path = FileSystems.getDefault().getPath(String.format("./images/%s.%s", UUID.randomUUID(), FILE_FORMAT));
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
        return path.toAbsolutePath().toString();
    }

    public static String encodeText(String text, QRSIze QRSIze)
            throws WriterException, IOException {
        int size = getQRSize(QRSIze);
        return encodeText(text, size, size);
    }

    public static String encodeText(String text)
            throws WriterException, IOException {
        return encodeText(text, DEFAULT_VERSION);
    }

    static {
        qrSizes = new HashMap<>(Map.ofEntries(
                new AbstractMap.SimpleEntry<>(QRSIze.SMALL, 256),
                new AbstractMap.SimpleEntry<>(QRSIze.MEDIUM, 512),
                new AbstractMap.SimpleEntry<>(QRSIze.LARGE, 1024))
        );
    }

    private static int getQRSize(QRSIze QRSIze) {
        return qrSizes.get(QRSIze);
    }

    private static BinaryBitmap getBitmapFromUrl(String url) throws IOException {
        BinaryBitmap binaryBitmap;
        try {
            binaryBitmap = new BinaryBitmap(new HybridBinarizer(
                    new BufferedImageLuminanceSource(ImageIO.read(new URL(url)))));
        } catch (IOException e) {
            log.error(String.format("{QRTools.getBitmapFromUrl}: %s", e.getMessage()));
            throw new IOException(String.format("Unable to decrypt QR-code: %s", e.getMessage()));
        }
        return binaryBitmap;
    }

    private static Result decodeBitmap(BinaryBitmap binaryBitmap) {
        Result result;
        try {
            result = new MultiFormatReader().decode(binaryBitmap);
        } catch (NotFoundException e) {
            throw new IllegalArgumentException(String.format("Image does not contain QR-code: %s", e.getMessage()));
        }
        return result;
    }
}
