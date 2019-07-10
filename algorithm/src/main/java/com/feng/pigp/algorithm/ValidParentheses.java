package com.feng.pigp.algorithm;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class ValidParentheses {

    public static void main(String[] args) {

        ValidParentheses validParentheses = new ValidParentheses();
        System.out.println(validParentheses.isValid("]"));
    }

    public boolean isValid(String s) {

        if(s==null){
            return true;
        }

        Map<Character, Character> map = new HashMap<>();
        map.put(')', '(');
        map.put('}','{');
        map.put(']', '[');

        char[] array = s.toCharArray();
        Stack<Character> stack = new Stack<>();

        for(int i=0; i<array.length; i++){
            if(!map.containsKey(array[i])){
                stack.push(array[i]);
                continue;
            }

            if(stack.isEmpty()){
                return false;
            }

            if(map.get(array[i])!=stack.pop()){
                return false;
            }
        }

        return stack.isEmpty();
    }
}