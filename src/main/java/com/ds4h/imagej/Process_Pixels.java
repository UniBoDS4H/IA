/*
 * To the extent possible under law, the ImageJ developers have waived
 * all copyright and related or neighboring rights to this tutorial code.
 *
 * See the CC0 1.0 Universal license for details:
 *     http://creativecommons.org/publicdomain/zero/1.0/
 */

package com.ds4h.imagej;

import com.ds4h.model.util.opencvLoader.OpenCVLoader;
import ij.ImagePlus;
import com.ds4h.view.mainGUI.MainMenuGUI;
import ij.plugin.PlugIn;

import java.awt.*;

/**
 * A template for processing each pixel of either
 * GRAY8, GRAY16, GRAY32 or COLOR_RGB images.
 *
 * Authors DS4H Team : Iorio Matteo & Vincenzi Fabio
 */
/*
	FUNCTIONALITIES :
		TODO: Add the possibility to change colors, contrast ecc ecc for each image
		TODO: Add the possibility to see the alignments points of the AutomaticAlignment for the Semi-Automatic
 */
public class Process_Pixels implements PlugIn {


	public static void main(String[] args) throws Exception {
		OpenCVLoader.loadOpenCV();
		new Process_Pixels().run(null);
	}


	@Override
	public void run(String s) {
		EventQueue.invokeLater(MainMenuGUI::new);
	}
}
