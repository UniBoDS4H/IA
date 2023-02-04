package com.ds4h.model.util.export;

import com.ds4h.model.alignedImage.AlignedImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipExporter {
    public static void exportToZip(final AlignedImage image, final String path) throws IOException {
        final BufferedImage buffImage = image.getAlignedImage().getBufferedImage();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(buffImage, "", outputStream);
        outputStream.flush();

        final ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(Paths.get(path)));
        final ZipEntry entry = new ZipEntry(image.getAlignedImage().getTitle());
        zipOutputStream.putNextEntry(entry);
        zipOutputStream.write(outputStream.toByteArray());
        zipOutputStream.closeEntry();
        zipOutputStream.close();
    }
}
