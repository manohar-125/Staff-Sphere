package staffsphere.util;

import staffsphere.model.User;

public class CurrentUser {
    private static User loggedInUser;

    public static void set(User user){
        loggedInUser = user;
    }

    public static User get() {
        return loggedInUser;
    }

    public static boolean isAdminOrHr() {
        return loggedInUser != null && (loggedInUser.getRole().equalsIgnoreCase("admin") || loggedInUser.getRole().equalsIgnoreCase("hr"));
    }
}
