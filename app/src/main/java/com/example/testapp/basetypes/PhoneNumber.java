package com.example.testapp.basetypes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumber {
    private String PhoneNumber;

    public PhoneNumber(String phoneNumber) throws Exception{
        String phoneNumberRegex = "(\\+977)?[1-9][0-9]{9}";
        Pattern pattern = Pattern.compile(phoneNumberRegex);
        Matcher matcher = pattern.matcher(phoneNumber);
        if(matcher.matches())
            PhoneNumber = phoneNumber;
        else
            throw new Exception("Invalid phone number");
    }
}
