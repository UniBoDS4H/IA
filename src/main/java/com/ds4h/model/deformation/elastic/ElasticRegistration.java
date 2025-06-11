package com.ds4h.model.deformation.elastic;

import com.ds4h.model.image.AnalyzableImage;
import com.ds4h.model.image.alignedImage.AlignedImage;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public interface ElasticRegistration {
    /**
     * Perform an elastic registration using the two input images.
     * @param movingImages moving images that will be subject to the registration.
     * @param targetImage target image, this will be fixed.
     * @return A future containing the transformed image.
     */
    @NotNull
    List<AlignedImage> transformImages(@NotNull final AnalyzableImage targetImage, @NotNull final List<AnalyzableImage> movingImages);

    @NotNull
    AlignedImage transformImage(@NotNull final AnalyzableImage targetImage, @NotNull final AnalyzableImage movingImage);
}
