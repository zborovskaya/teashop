package by.zborovskaya.final_project.service;

public class QuantityValidator {
    private final static String NUMBER_PATTERN = "[0-9]+(\\,?||\\.?)[0-9]*";

    public static boolean isNumber(String quantity) {
        return quantity.matches(NUMBER_PATTERN);
    }
}
