package com.ds4h.model.util;

public class NameBuilder {
    private String finalName = "";
    private static final String TAG_NAME = "_Aligned", DOT = ".";
    public static final String DOT_SEPARATOR = "\\.";
    public NameBuilder(){}

    public NameBuilder parseName(final String input){
        if(!input.isEmpty()){
            this.finalName = input;
        }
        return this;
    }

    public NameBuilder splitBy(final String separator){
        if(!this.finalName.isEmpty() && this.finalName.contains(separator)){
            final String[] parts = this.finalName.split(separator);
            final String fileNameWithoutExtension = parts[0];
            final String fileExtension = parts[1];
            this.finalName = fileNameWithoutExtension+NameBuilder.TAG_NAME+NameBuilder.DOT+fileExtension;
        }
        return this;
    }
    public String getFinalName(){
        return this.finalName;
    }
}
