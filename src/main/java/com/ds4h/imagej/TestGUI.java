package com.ds4h.imagej;

import com.ds4h.view.mainGUI.MainMenuGUI;
import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;

import java.awt.*;
import ij.plugin.PlugIn;


public class TestGUI implements PlugIn{
    protected ImagePlus image;

    // image property members
    private int width;
    private int height;

    // plugin parameters
    public double value;
    public String name;


    /**
     * Main method for debugging.
     *
     * For debugging, it is convenient to have a method that starts ImageJ, loads
     * an image and calls the plugin, e.g. after setting breakpoints.
     *
     * @param args unused
     */

    public static void main(String[] args) throws Exception {
		// start ImageJ
		new ImageJ();
		// run the plugin
		IJ.runPlugIn(Image_Alignment.class.getName(), "");

    }


    @Override
    public void run(String s) {
        EventQueue.invokeLater(MainMenuGUI::new);
    }
}
