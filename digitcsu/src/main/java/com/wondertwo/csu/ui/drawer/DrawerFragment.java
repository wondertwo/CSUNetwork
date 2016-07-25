package com.wondertwo.csu.ui.drawer;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wondertwo.csu.R;
import com.wondertwo.csu.ui.skin.ChangeSkin;

/**
 *
 * Created by wondertwo on 2016/7/22.
 */
public class DrawerFragment extends Fragment {

    private DrawerLayout mDrawerLatout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View drawerLayout = inflater.inflate(R.layout.fragment_drawer, container, false);
        Button settingSkin = (Button) drawerLayout.findViewById(R.id.menu_setting_skin);
        settingSkin.setOnClickListener(new OnDrawerClickListener());
        //drawerLayout.findViewById(R.id.menu_online_topup).setOnClickListener();
        //drawerLayout.findViewById(R.id.menu_user_info).setOnClickListener();
        return drawerLayout;
    }

    /**
     * On Drawer Click Listener
     */
    private class OnDrawerClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.menu_setting_skin:
                    mDrawerLatout.closeDrawer(GravityCompat.START);
                    getActivity().startActivity(new Intent(getActivity(), ChangeSkin.class));
                    break;
            }
        }
    }

    /**
     * accept the drawer layout object, to close drawer behind start other activity.
     * @param drawerLayout
     */
    public void setDrawerLayout(DrawerLayout drawerLayout) {
        this.mDrawerLatout = drawerLayout;
    }
}
