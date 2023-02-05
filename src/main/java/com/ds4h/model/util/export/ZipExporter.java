package com.ds4h.model.util.export;

import com.ds4h.model.alignedImage.AlignedImage;
import ij.io.FileSaver;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
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
    public static void exportToZip(final List<AlignedImage> imageList, final String path) throws IOException  {
        final File targetZipFile = new File(path, "ds4h_aligned_image.zip");
        final ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(targetZipFile.toPath()));

        for (AlignedImage imp : imageList) {
            final File outputFile = new File(imp.getAlignedImage().getTitle());
            final BufferedImage bim = imp.getAlignedImage().getBufferedImage();
            final ZipEntry entry = new ZipEntry(imp.getAlignedImage().getTitle());
            zos.putNextEntry(entry);
            ImageIO.write(bim, "tif", zos);
            //sbim.createGraphics().drawImage(bim, 0, 0, null);
        }
        zos.close();
    }
}
