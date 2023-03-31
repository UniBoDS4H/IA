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
		try {

			IJ.log("[BEGIN] Heap Size: " + IJ.maxMemory());
			EventQueue.invokeLater(MainMenuGUI::new);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}