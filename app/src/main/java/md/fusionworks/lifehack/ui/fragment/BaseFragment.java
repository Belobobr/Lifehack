package md.fusionworks.lifehack.ui.fragment;

import android.app.Fragment;

import md.fusionworks.lifehack.di.HasComponent;

/**
 * Created by ungvas on 10/22/15.
 */
public class BaseFragment extends Fragment {

    protected <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
    }
}
