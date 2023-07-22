package bg.sofia.uni.fmi.todoist.validation;

public class Validator {

    private Validator() {
    }

    public static void stringArgument(String string, String context) {
        if (string == null || string.isBlank()) {
            throw new IllegalArgumentException(context + " is null or blank");
        }
    }

    public static void objectArgument(Object object, String context) {
        if (object == null) {
            throw new IllegalArgumentException(context + " is null");
        }
    }

}
