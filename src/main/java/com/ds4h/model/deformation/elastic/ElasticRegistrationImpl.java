package com.ds4h.model.deformation.elastic;

import bdv.ij.ApplyBigwarpPlugin;
import bdv.viewer.Interpolation;
import bigwarp.BigWarpData;
import bigwarp.BigWarpInit;
import bigwarp.landmarks.LandmarkTableModel;
import bigwarp.transforms.BigWarpTransform;
import com.ds4h.model.image.AnalyzableImage;
import com.ds4h.model.image.alignedImage.AlignedImage;
import ij.IJ;
import ij.ImagePlus;
import net.imglib2.realtransform.BoundingBoxEstimation;
import net.imglib2.realtransform.InvertibleRealTransform;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import org.jetbrains.annotations.NotNull;
import org.opencv.core.Point;

import java.util.*;

public class ElasticRegistrationImpl implements ElasticRegistration {
    private int max_points;
    public ElasticRegistrationImpl() {
        this.max_points = 4;
    }

    public void setMaxPoints(int max_points) {
        if (max_points > 0 && max_points <= 100) {
            this.max_points = max_points;
        }
    }

    @NotNull
    @Override
    public List<AlignedImage> transformImages(@NotNull final AnalyzableImage targetImage, @NotNull final List<AnalyzableImage> movingImages) {
        IJ.log("[ELASTIC] Transforming Images");
        final List<AlignedImage> output = new LinkedList<>();
        movingImages.stream().filter(Objects::nonNull).forEach(image -> {
            final AlignedImage transformedImage = this.transformImage(image, targetImage);
            output.add(transformedImage);
        });
        return output;
    }

    @NotNull
    @Override
    public AlignedImage transformImage(@NotNull AnalyzableImage movingImage, @NotNull AnalyzableImage targetImage) {
        if (movingImage.totalPoints() > 0 && targetImage.totalPoints() > 0) {
            IJ.log("[ELASTIC] Transforming Image");
            this.initilizeSources(movingImage, targetImage);
            final LandmarkTableModel landmarkTableModel = new LandmarkTableModel(2);
            this.addLandmarks(landmarkTableModel, movingImage, targetImage);
            return this.applyElasticRegistration(movingImage, targetImage, landmarkTableModel);
        }
        throw new IllegalArgumentException("Cannot transform the image " + movingImage);
    }

    /**
     * Create the BigWarp sources, that will be later on used for the elastic registration.
     * @param movingImage: the image that will be deformed in order to make it like the target image.
     * @param targetImage: the fixed image used for the comparison.
     */
    private void initilizeSources(@NotNull final AnalyzableImage movingImage, @NotNull final AnalyzableImage targetImage) {
        final ImagePlus movingImg = movingImage.getImagePlus();
        final ImagePlus targetImg = targetImage.getImagePlus();
        final BigWarpData<UnsignedByteType> bigWarpData = BigWarpInit.initData();
        BigWarpInit.add(bigWarpData, BigWarpInit.createSources(bigWarpData, movingImg, 0, 0, true));
        BigWarpInit.add(bigWarpData, BigWarpInit.createSources(bigWarpData, targetImg, 1, 0, false));
        bigWarpData.wrapMovingSources();
    }

    /**
     * Real logic for the elastic registration, this method applies the BigWarp logic on the input images. The output returned from this method is a new version of the
     * moving image.
     * @param movingImage: the image that will be deformed during the registration.
     * @param targetImage: the fixed image.
     * @param landmarkTableModel: collection of all landmarks both from moving image and target image.
     * @return a deformed image.
     */
    @NotNull
    private AlignedImage applyElasticRegistration(@NotNull final AnalyzableImage movingImage, @NotNull final AnalyzableImage targetImage, @NotNull final LandmarkTableModel landmarkTableModel) {
        final BigWarpData<?> bigwarpData = BigWarpInit.createBigWarpDataFromImages(movingImage.getImagePlus(), targetImage.getImagePlus());
        bigwarpData.wrapMovingSources();
        final BoundingBoxEstimation boundingBoxEstimation = new BoundingBoxEstimation(BoundingBoxEstimation.Method.VOLUME);
        final InvertibleRealTransform invertibleRealTransform = new BigWarpTransform(landmarkTableModel, BigWarpTransform.TPS).getTransformation();
        final ImagePlus deformedImage = ApplyBigwarpPlugin.apply(
                bigwarpData,
                landmarkTableModel,
                invertibleRealTransform,
                BigWarpTransform.TPS, // tform type
                ApplyBigwarpPlugin.TARGET, // fov option
                null,
                boundingBoxEstimation,
                ApplyBigwarpPlugin.TARGET,
                null,
                null,
                null,
                Interpolation.NEARESTNEIGHBOR,
                false, // virtual
                1, // nThreads
                true,
                null, // writeOpts
                false).get(0);
        return new AlignedImage(deformedImage.getProcessor(), movingImage.getName());
    }

    /**
     * This method adds all the detected points inside the LandmarkTableModel. All this points will then be used by the
     * BigWarp logic for the elastic registration.
     * @param landmarkTableModel: collector of points.
     * @param movingImage: the moving image that will be deformed.
     * @param targetImage: the fixed image.
     */
    private void addLandmarks(@NotNull final LandmarkTableModel landmarkTableModel,
                              @NotNull final AnalyzableImage movingImage,
                              @NotNull final AnalyzableImage targetImage) {
        final Iterator<Point> movingPointsIterator = movingImage.getPoints().iterator();
        final Iterator<Point> targetPointsIterator = targetImage.getPoints().iterator();
        int currentSize = 0;
        while (movingPointsIterator.hasNext() && targetPointsIterator.hasNext() && currentSize < this.max_points) {
            final Point movingPoint = movingPointsIterator.next();
            final Point targetPoint = targetPointsIterator.next();
            landmarkTableModel.add(new double[]{movingPoint.x, movingPoint.y}, new double[]{targetPoint.x, targetPoint.y});
            currentSize++;
        }
    }
}
