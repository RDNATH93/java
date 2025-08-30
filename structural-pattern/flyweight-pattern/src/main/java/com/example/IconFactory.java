package com.example;

import java.util.HashMap;
import java.util.Map;

public class IconFactory {
    private Map<String,Icon> iconCache= new HashMap<>();

    public Icon getIcon(String key){
        if(iconCache.get(key)!=null){
            return iconCache.get(key);
        }else{
            Icon icon;
            if("file".equalsIgnoreCase(key)){
                icon = new FileIcon("document","document.png");
            }
            else if("folder".equalsIgnoreCase(key)){
                icon = new FolderIcon("yellow","folder.png");
            }else{
                throw new IllegalArgumentException("");
            }
            // Store the created icon in the cache
            iconCache.put(key,icon);
            return icon;
        }
    }
} 