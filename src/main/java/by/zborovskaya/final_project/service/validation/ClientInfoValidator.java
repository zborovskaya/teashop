package by.zborovskaya.final_project.service.validation;

public class ClientInfoValidator {

    private final static String EMAIL_PATTERN = "^[A-Za-z0-9\\.]{1,30}@[a-z]{2,7}\\.[a-z]{2,4}$";
    private final static String NAME_PATTERN =  "^[A-ZА-Я][a-zа-я]+$";
    private final static String PHONE_PATTERN = "(29|33|25|44)[0-9]{7}";

    public static boolean isEmailValid(String email){
        return email.matches(EMAIL_PATTERN);
    }

    public static boolean isFirstNameValid(String firstName){
        return firstName.matches(NAME_PATTERN);
    }

    public static boolean isLastNameValid(String lastName){
        return lastName.matches(NAME_PATTERN);
    }

    public static boolean isPhoneValid(String phone){
        return phone.matches(PHONE_PATTERN);
    }

}
