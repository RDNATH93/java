package com.example.isogram;

import java.util.*;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        System.out.println("Is Isogram "+checkIsogram("rahul"));
    }

    private static boolean checkIsogram(String input){
            // Map map= new HashMap<String, Integer>();
            // for(int i=0;i<input.length();i++){
            //     if(map.get(input.charAt(i))!=null){
            //         return false;
            //     }else{
            //         map.put(input.charAt(i),1);
            //     }
            // }
            // return true;
        
        char[] chars= input.toCharArray();
        Set<Character> charSet= new HashSet<Character>();
        for(char ch: chars){
            if(charSet.contains(ch)){
              return false;
            }  
            charSet.add(ch);
        }
            return true;

    }       
}