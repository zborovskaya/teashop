package by.zborovskaya.final_project.service.validation;

public class PasswordChangeValidator {
    private final static String PASSWORD_PATTERN = "^[a-zA-Z_0-9]{5,50}$";

    public static boolean isPasswordValid(String password){
        return password.matches(PASSWORD_PATTERN);
    }

    public static boolean isPasswordConfirmed(String newPassword, String confirmedPassword){
        return newPassword.equals(confirmedPassword);
    }

}
