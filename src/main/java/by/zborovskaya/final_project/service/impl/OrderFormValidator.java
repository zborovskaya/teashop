package by.zborovskaya.final_project.service.impl;

public class OrderFormValidator {
    private final static String NAME_PATTERN = "^[A-Z][a-z]+$";
    private final static String PHONE_PATTERN = "^\\+375[0-9]{9}$";

    public static boolean isNameValid(String firstName) {
        return firstName.matches(NAME_PATTERN);
    }

    public static boolean isPhoneValid(String phone) {
        return phone.matches(PHONE_PATTERN);
    }
}
