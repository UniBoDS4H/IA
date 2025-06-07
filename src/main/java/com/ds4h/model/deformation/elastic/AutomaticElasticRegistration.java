package com.ds4h.model.deformation.elastic;

import bdv.ij.ApplyBigwarpPlugin;
import bdv.viewer.Interpolation;
import bigwarp.BigWarpData;
import bigwarp.BigWarpInit;
import bigwarp.landmarks.LandmarkTableModel;
import bigwarp.transforms.BigWarpTransform;
import com.ds4h.model.alignment.automatic.pointDetector.PointDetector;
import com.ds4h.model.image.alignedImage.AlignedImage;
import ij.ImagePlus;
import net.imglib2.realtransform.BoundingBoxEstimation;
import net.imglib2.realtransform.InvertibleRealTransform;
import org.jetbrains.annotations.NotNull;
import org.opencv.core.Point;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

public class AutomaticElasticRegistration implements ElasticRegistration {
    final PointDetector detector;
    public AutomaticElasticRegistration(@NotNull final PointDetector detector) {
        this.detector = detector;
    }

    @NotNull
    @Override
    public CompletableFuture<AlignedImage> transform(@NotNull AlignedImage movingImage, @NotNull AlignedImage targetImage) {
        return CompletableFuture.supplyAsync(() -> {

            return null;
        });
    }

    @NotNull
    private List<ImagePlus> applyElasticRegistration(@NotNull final AlignedImage movingImage, @NotNull final AlignedImage targetImage, @NotNull final LandmarkTableModel landmarkTableModel) {
        final BigWarpData<?> bigwarpData = BigWarpInit.createBigWarpDataFromImages(movingImage.getAlignedImage(), targetImage.getAlignedImage());
        bigwarpData.wrapMovingSources();
        final BoundingBoxEstimation boundingBoxEstimation = new BoundingBoxEstimation(BoundingBoxEstimation.Method.CORNERS);
        final InvertibleRealTransform invertibleRealTransform = new BigWarpTransform(landmarkTableModel, BigWarpTransform.AFFINE).getTransformation();
        return ApplyBigwarpPlugin.apply(
                bigwarpData,
                landmarkTableModel,
                invertibleRealTransform,
                BigWarpTransform.AFFINE, // tform type
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
                false);
    }

    private void addLandmarks(@NotNull final LandmarkTableModel landmarkTableModel,
                              @NotNull final AlignedImage movingImage,
                              @NotNull final AlignedImage targetImage) {
        final Iterator<Point> movingPointsIterator = movingImage.getPoints().iterator();
        final Iterator<Point> targetPointsIterator = targetImage.getPoints().iterator();
        while (movingPointsIterator.hasNext()) {
            final Point movingPoint = movingPointsIterator.next();
            final Point targetPoint = targetPointsIterator.next();
            landmarkTableModel.add(new double[]{movingPoint.x, movingPoint.y}, new double[]{targetPoint.x, targetPoint.y});
        }
    }
}
