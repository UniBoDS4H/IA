package com.ds4h.model.util.saveProject;

import com.ds4h.controller.savingController.SaveAsEnum;
import com.ds4h.model.image.alignedImage.AlignedImage;
import com.ds4h.model.util.directoryManager.directoryCreator.DirectoryCreator;
import com.ds4h.model.util.saveProject.saveReferenceMatrix.SaveMatrix;
import ij.IJ;
import ij.ImagePlus;
import ij.plugin.RGBStackMerge;
import ij.process.*;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
public class SaveImages {
    private final static String DIRECTORY = "DS4H_AlignedImages";

    private SaveImages(){

    }

    /**
     * Save all the images inside the selected path.
     * @param images all the images that will be stored.
     * @param path the path where the images will be stored.
     */
    public static void saveImages(final List<AlignedImage> images, final String path, final SaveAsEnum saveAsType,
                                  final boolean isOrderAscending, final boolean isTargetImageForeground) {
        final String dir = DirectoryCreator.createDirectory(path, DIRECTORY);
        final boolean isDirEmpty = dir.isEmpty();
        switch (saveAsType) {
            case SAVE_AS_PROJECT:
                if (!dir.isEmpty()) {
                    SaveImages.save(images.stream().map(AlignedImage::getAlignedImage).collect(Collectors.toList()), path + "/" + dir);
                    SaveMatrix.saveMatrix(images, path + "/" + dir);
                } else {
                    SaveImages.save(images.stream().map(AlignedImage::getAlignedImage).collect(Collectors.toList()), path);
                }
                break;
            case SAVE_AS_TIFF:
                for (final AlignedImage img : images) {
                    if (!isDirEmpty) {
                        IJ.saveAsTiff(img.getAlignedImage(), path + "/" + dir + "/" + img.getAlignedImage().getTitle());
                    } else {
                        IJ.saveAsTiff(img.getAlignedImage(), path + "/" + img.getAlignedImage().getTitle());
                    }
                }
                SaveMatrix.saveMatrix(images, path + "/" + dir);
                break;
            case SAVE_AS_MOSAIC:
                final ImagePlus mosaicImage = createMosaicImage(images, isOrderAscending, isTargetImageForeground);

                if (!isDirEmpty) {
                    IJ.saveAsTiff(mosaicImage, path + "/" + dir + "/MosaicImage");
                } else {
                    IJ.saveAsTiff(mosaicImage, path + "/MosaicImage");
                }
                break;
            case SAVE_AS_COMPOSITE:
                final ImagePlus compositeImage = createCompositeImage(images);

                if (!isDirEmpty) {
                    IJ.saveAsTiff(compositeImage, path + "/" + dir + "/CompositeImage");
                } else {
                    IJ.saveAsTiff(compositeImage, path + "/CompositeImage");
                }
                break;
        }
    }

    /**
     * Save the images inside the path.
     * @param images the images to be saved.
     * @param path the path where will be stored the images.
     */
    public static void save(final List<ImagePlus> images, final String path){
        images.forEach(image -> IJ.save(image, path+"/"+image.getTitle()));
    }

    /**
     * Save a single image inside the path.
     * @param image the image to be stored.
     * @param path the path where will be stored the image.
     */
    public static void save(final ImagePlus image, final String path){
        if(!path.isEmpty() && Objects.nonNull(image)) {
            IJ.save(image, path + "/" + image.getTitle());
        }
    }

    private static void addImage(final ImageProcessor base, final ImageProcessor overlay, final boolean is8Bit) {
        int width = base.getWidth();
        int height = base.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (is8Bit) {
                    int overlayValue = overlay.getPixel(x, y);
                    if (overlayValue != 0) {  // Assuming 0 is the background color in 8-bit images
                        base.putPixel(x, y, overlayValue);
                    }
                } else {
                    int[] overlayPixel = overlay.getPixel(x, y, null);
                    if (!(overlayPixel[0] == 0 && overlayPixel[1] == 0 && overlayPixel[2] == 0)) {  // Not black
                        base.putPixel(x, y, overlayPixel);
                    }
                }
            }
        }
    }

    private static ImagePlus createMosaicImage(final List<AlignedImage> images, final boolean isOrderAscending,
                                               final boolean isTargetImageForeground) {
        final ImagePlus mosaicImage;
        final ImageProcessor mosaicProcessor;
        final List<AlignedImage> noTargetImages;

        if (isTargetImageForeground) {
            mosaicImage = images.get(0).getAlignedImage();
            mosaicProcessor = mosaicImage.getProcessor();
            noTargetImages = new ArrayList<>(
                    images.subList(1, images.size())
            );
        } else {
            final ImageProcessor blackProcessor;
            if (images.get(0).getAlignedImage().getType() == ImagePlus.GRAY8)
                blackProcessor = new ByteProcessor(images.get(0).getAlignedImage().getWidth(),
                        images.get(0).getAlignedImage().getHeight());
            else
                blackProcessor = new ColorProcessor(images.get(0).getAlignedImage().getWidth(),
                        images.get(0).getAlignedImage().getHeight());
            mosaicImage = new ImagePlus("", blackProcessor);
            mosaicProcessor = mosaicImage.getProcessor();
            noTargetImages = new ArrayList<>(images);
        }

        if (!isOrderAscending)
            Collections.reverse(noTargetImages);

        for (final AlignedImage img : noTargetImages) {
            final ImageProcessor imgProcessor = img.getProcessor();

            for (int y = 0; y < imgProcessor.getHeight(); y++) {
                for (int x = 0; x < imgProcessor.getWidth(); x++) {
                    if (mosaicProcessor.getPixel(x, y) == 0 && imgProcessor.getPixel(x, y) != 0)
                        mosaicProcessor.putPixel(x, y, imgProcessor.getPixel(x, y));
                }
            }
        }

        return mosaicImage;
    }

    private static ImagePlus createCompositeImage(final List<AlignedImage> images) {
        final List<ImagePlus> copyList = new ArrayList<>();

        for (int i = 0; i < images.size(); i++) {
            copyList.add(images.get(i).getAlignedImage());
            if (copyList.get(i).getType() != ImagePlus.GRAY8)
                new ImageConverter(copyList.get(i)).convertToGray8();
        }

        final ImagePlus combinedImage = RGBStackMerge.mergeChannels(copyList.toArray(new ImagePlus[0]),
                false);
        new ImageConverter(combinedImage).convertToRGB();

        return combinedImage;
    }
}
