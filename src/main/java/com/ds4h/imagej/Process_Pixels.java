/*
 * To the extent possible under law, the ImageJ developers have waived
 * all copyright and related or neighboring rights to this tutorial code.
 *
 * See the CC0 1.0 Universal license for details:
 *     http://creativecommons.org/publicdomain/zero/1.0/
 */

package com.ds4h.imagej;

import ij.ImagePlus;
import com.ds4h.view.mainGUI.MainMenuGUI;
import ij.plugin.PlugIn;

import java.awt.*;

/**
 * A template for processing each pixel of either
 * GRAY8, GRAY16, GRAY32 or COLOR_RGB images.
 *
 * @author Johannes Schindelin
 */

public class Process_Pixels implements PlugIn {
	protected ImagePlus image;

	public String name = "Miglior plugin al mondo";

	/**
	 * Main method for debugging.
	 *
	 * For debugging, it is convenient to have a method that starts ImageJ, loads
	 * an image and calls the plugin, e.g. after setting breakpoints.
	 *
	 * @param args unused
	 */

	public static void main(String[] args) throws Exception {
		new Process_Pixels().run(null);
	}


	@Override
	public void run(String s) {
		EventQueue.invokeLater(MainMenuGUI::new);
	}
}
