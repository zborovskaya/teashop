package by.zborovskaya.final_project.service.validation;

public class SignUpValidator {
    private final static String LOGIN_PASSWORD_PATTERN = "^[a-zA-Z_0-9]{4,20}$";

    public static boolean isLoginValid(String login){
        return login.matches(LOGIN_PASSWORD_PATTERN);
    }

    public static boolean isPasswordValid(String password){
        return password.matches(LOGIN_PASSWORD_PATTERN);
    }
}
