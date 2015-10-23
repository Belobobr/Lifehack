package md.fusionworks.lifehack.ui.view;

import android.view.ViewGroup;

/**
 * Created by ungvas on 10/18/15.
 */
public interface NavigationDrawerView extends View {

    void setupDrawerLayout();

    void setTitle(String title);

    void populateDrawerItems();

    void createDrawerItems();

    android.view.View makeDrawerItem(final int itemId, ViewGroup container);

    int getSelfDrawerItem();

    boolean isSeparator(int itemId);

    boolean isSimpleActivity(int itemId);

    void formatDrawerItem(android.view.View view, int itemId, boolean selected, boolean isActive);

    void onDrawerItemClicked(final int itemId);

    void setSelectedDrawerItem(int itemId);

    void openDrawer();

    void closeDrawer();
}
