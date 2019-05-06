package manager;
@FunctionalInterface
public interface SwitchToUserPage {
    /**
     * After a user has logged in many of the preferences in the pages, such as the games available for them to open for
     * editing in the authoring environment, are toggled to the specific account that has logged in, so it was necessary to
     * be able to transfer this information between the login environment of the launcher and the area of the launcher
     * where the user can go to create or play games
     * @author Anna Darwish
     */
    void switchUserPage(String userName);

}
