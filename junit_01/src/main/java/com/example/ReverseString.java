package com.example;


public class ReverseString {

    public static void main(String[] args) {
        ReverseString rvs = new ReverseString();
        System.out.println(rvs.reverseString("34354"));
    }

    public String reverseString(String input) {
        char[] charArray = input.toCharArray();
        int left = 0;
        int right = input.length() - 1;
        while (left < right) {
            char temp = charArray[left];
            charArray[left] = charArray[right];
            charArray[right] = temp;
            left++;
            right--;
        }
       // StringBuilder result = new StringBuilder();

        return String.valueOf(charArray);
        // for (char ch : charArray)
        //     result.append(ch);

        //return result.toString();
    }
}
