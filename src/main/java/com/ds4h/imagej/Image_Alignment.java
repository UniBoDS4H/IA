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
 * DS4H Team:
 * @author Iorio Matteo, matteo.iorio01@gmail.com
 * @author Vincenzi Fabio, fabio.vincenzi2001@gmail.com
 */
public class Image_Alignment implements PlugIn {
	public static void main(String[] args) {
		new Image_Alignment().run(null);
	}

	@Override
	public void run(String s) {
		try {
			OpencvController.loadLibrary();
			IJ.log("[BEGIN] Heap Size: " + IJ.maxMemory());
			EventQueue.invokeLater(MainMenuGUI::new);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}