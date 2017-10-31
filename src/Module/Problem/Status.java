package Module.Problem;


public enum Status {
    CREATE;

    public static int toInt(Status status) {
        switch (status) {
            case CREATE:
                return 1;
            default:
                return 1;
        }
    }

    public static String toString(Status status) {
        switch (status) {
            case CREATE:
                return "CREATE";
            default:
                return "CREATE";
        }
    }
}