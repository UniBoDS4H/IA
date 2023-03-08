package com.ds4h.controller.alignmentController.semiAutomaticController;

import com.ds4h.controller.alignmentController.AlignmentControllerInterface;
import com.ds4h.controller.pointController.PointController;
import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.alignment.Alignment;
import com.ds4h.model.alignment.AlignmentEnum;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 *
 */
public class SemiAutomaticController implements AlignmentControllerInterface {
    private final Alignment alignment = new Alignment();
    public SemiAutomaticController(){

    }

    /**
     *
     * @return
     */
    @Override
    public List<AlignedImage> getAlignedImages() {
        return new LinkedList<>(this.alignment.alignedImages());
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isAlive() {
        return alignment.isAlive();
    }

    /**
     *
     * @return
     */
    @Override
    public String name() {
        return "Semi-Automatic algorithm";
    }

    /**
     *
     * @param pointController
     */
    public void align(final PointController pointController) {
        /*if(!this.alignment.isAlive() && Objects.nonNull(pointController) && Objects.nonNull(pointController.getCornerManager())){
            this.alignment.alignImages(pointController.getCornerManager(), AlignmentEnum.SEMIAUTOMATIC);
        }*/
    }
}
