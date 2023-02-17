/*
 * To the extent possible under law, the ImageJ developers have waived
 * all copyright and related or neighboring rights to this tutorial code.
 *
 * See the CC0 1.0 Universal license for details:
 *     http://creativecommons.org/publicdomain/zero/1.0/
 */

package com.ds4h.imagej;

import com.ds4h.model.util.opencvLoader.OpenCVLoader;
import ij.IJ;
import ij.ImagePlus;
import com.ds4h.view.mainGUI.MainMenuGUI;
import ij.plugin.PlugIn;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;

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
		char separator = '|';
		int[][] matrix = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				System.out.print(matrix[i][j]);
				if (j != matrix[i].length - 1) {
					System.out.print(separator);
				}
			}
			System.out.println();
		}

		new Process_Pixels().run(null);
	}


	@Override
	public void run(String s) {
		OpenCVLoader.loadOpenCV();
		EventQueue.invokeLater(MainMenuGUI::new);

	}
}
