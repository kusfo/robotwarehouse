package com.teenagemutantninjacoders.robotwarehouse.display;

/**
 * Created by JordiRM on 14/12/2018.
 */
public class AssetFile {
    public String path;
    public Class type;

    public AssetFile(String path, Class type) {
        this.path = path;
        this.type = type;
    }
}
