package com.benjamintolman.taskcommander.Utils;

public class ValidationUtility {

    public static boolean isEmailValid(String email) {

        if(!email.contains("@") || !email.contains(".")) {
            return false;
        }

        // There is no . after the @ symbol.
        if(email.lastIndexOf(".") < email.indexOf("@")) {
            return false;
        }

        // There is more than one @ symbol present.
        if(email.indexOf("@") != email.lastIndexOf("@")) {
            return false;
        }

        // Email ends in a .
        if(email.lastIndexOf(".") == (email.length() - 1)) {
            return false;
        }

        // Split the email into two parts and check each half for validity.
        String[] parts = email.split("@");

        if(parts[0].trim().length() == 0) {
            return false;
        }

        // First part ends in a period.
        if(parts[0].charAt(parts[0].length()-1) == '.') {
            return false;
        }

        if(parts[0].startsWith(".")) {
            return false;
        }

        // Second part starts with a period.
        if(parts[1].startsWith(".")) {
            return false;
        }

        char[] chars = email.toCharArray();
        for(char c : chars){
            if(!Character.isLetter(c) && !Character.isDigit(c) && c != '@' && c != '.'){
                return false;
            }
        }

        return true;
    }

    public static boolean validatePhone(String number){

        if(number.matches("[0-9]+")){
            //this is all digits
            if(number.length() == 10){
                //this is 10 digits long.
                return true;
            }
        }
        return false;
    }

    public static boolean validatename(String name){

            if(name.length() <= 30){
                return true;
            }

        return false;
    }

    public static boolean validatePassword(String password){
        if(password.length() == 10){
            return true;
        }

        return false;
    }

    public static boolean validateCompanyCode(String companyCode){
        if(companyCode.length() <= 40){
            return true;
        }

        return false;
    }

    //todo validate job fields

    public static boolean validateSize(String input, int size){

        if(input.length() <= size){

            return true;
        }

        return false;
    }
}
