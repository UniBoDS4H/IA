/*
 * To the extent possible under law, the ImageJ developers have waived
 * all copyright and related or neighboring rights to this tutorial code.
 *
 * See the CC0 1.0 Universal license for details:
 *     http://creativecommons.org/publicdomain/zero/1.0/
 */
package com.ds4h.imagej;
import com.ds4h.controller.opencvController.OpencvController;
import com.ds4h.view.mainGUI.MainMenuGUI;
import ij.IJ;
import ij.plugin.PlugIn;
import java.awt.*;

/**
 * A template for processing each pixel of either
 * GRAY8, GRAY16, GRAY32 or COLOR_RGB images.
 *
 * DS4H Team:
 * @author Iorio Matteo
 * @author Vincenzi Fabio
 */
public class Process_Pixels implements PlugIn {
	public static void main(String[] args) throws Exception {
		new Process_Pixels().run(null);
	}


	@Override
	public void run(String s) {

		OpencvController.loadLibrary();
		/*
		final Mat m = Mat.zeros(10, 10, CvType.CV_8U);
		m.put(0,0, 1);
		m.put(9,9, 1);
		m.put(1, 1, 3);
		for (int i = 0;  i < 10; i++){
			for (int j = 0; j < 10; j++){
				System.out.print(Arrays.toString(m.get(i, j)));
			}
			System.out.println(" ");
		}
		System.gc();
		System.out.println("-----");
		final Mat z = new Mat(m.getNativeObjAddr());
		for (int i = 0;  i < 10; i++){
			for (int j = 0; j < 10; j++){
				System.out.print(Arrays.toString(z.get(i, j)));
			}
			System.out.println(" ");

		}
		*/
		try {

			IJ.log("[BEGIN] Heap Size: " + IJ.maxMemory());
			EventQueue.invokeLater(MainMenuGUI::new);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}