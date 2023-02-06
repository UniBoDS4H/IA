package com.ds4h.model.util;

import java.util.Objects;

public class NameBuilder {
    private String finalName = "";
    private static final String TAG_NAME = "_Aligned", DOT = ".";
    public static final String DOT_SEPARATOR = "\\.";
    public NameBuilder(){}

    public NameBuilder parseName(final String input){
        if(Objects.nonNull(input) && !input.isEmpty()){
            this.finalName = input;
        }
        return this;
    }

    public NameBuilder splitBy(final String separator){
        if(Objects.nonNull(separator) && !this.finalName.isEmpty() && this.finalName.contains(separator)){
            final String[] parts = this.finalName.split(separator);
            final String fileNameWithoutExtension = parts[0];
            final String fileExtension = parts[1];
            System.out.println(fileNameWithoutExtension);
            //TODO: FIX THIS, THE SEPARATOR IS NOT CORRECT
            this.finalName = fileNameWithoutExtension+"_"+NameBuilder.TAG_NAME+NameBuilder.DOT+fileExtension;
        }
        System.out.println(Objects.nonNull(separator) + " " +!this.finalName.isEmpty()
                + " " + separator);
        return this;
    }
    public String getFinalName(){
        return this.finalName;
    }
}
