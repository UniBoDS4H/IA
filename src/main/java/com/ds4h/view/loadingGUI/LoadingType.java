package com.ds4h.view.loadingGUI;

public enum LoadingType {
    ALGORITHM("The algorithm is running", "<html>The algorithm is running, please wait.<br/> </html>"),
    IMPORT("Import Project", "<html>The import is started. Please wait.<br/> </html>"),
    EXPORT("Export Project", "<html>The export is started. Please wait.<br/> </html"),
    LOAD("Load Images", "<html>The images are being loaded. Please wait.<br/> </html");


    private final String title;
    private final String description;

    LoadingType(final String title, final String description){
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
