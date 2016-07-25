package com.wondertwo.csu.ui.skin;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.wondertwo.csu.R;
import com.wondertwo.csu.ui.TopLevelActivity;
import com.wondertwo.csu.utils.statusbar.StatusBarUtils;

import java.io.File;

import cn.feng.skin.manager.listener.ILoaderListener;
import cn.feng.skin.manager.loader.SkinManager;
import cn.feng.skin.manager.util.L;

/**
 * Switch Skin Activity
 *
 * Created by wondertwo on 2016/6/25.
 */
public class ChangeSkin extends TopLevelActivity {

    /**
     * Put this skin file on the root of sdcard
     *
     * eg:/mnt/sdcard/BlackFantacy.skin
     */
    private static final String SKIN_COLD_SUMMER = "ColdSummer.skin";
    private static final String SKIN_DIR = Environment.getExternalStorageDirectory() +
            File.separator + SKIN_COLD_SUMMER;

    private Button setOfficialDefault;
    private Button setColorfulLemon;

    private boolean isOfficialSelected = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin);
        // match the status bar color
        StatusBarUtils.setColor(this, Color.parseColor(getString(R.string.container_bar_color)), 0);

        initTitleBar();
        setSkinListener();
    }

    /**
     * initialize title bar
     */
    private void initTitleBar() {
        ImageButton left = (ImageButton) findViewById(R.id.title_bar_btn_left);
        ImageButton right = (ImageButton) findViewById(R.id.title_bar_btn_right);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * set skin listener
     */
    private void setSkinListener() {
        setOfficialDefault = (Button) findViewById(R.id.skin_official_default);
        setColorfulLemon = (Button) findViewById(R.id.skin_colorful_lemon);

        isOfficialSelected = !SkinManager.getInstance().isExternalSkin();
        // set onclick listener
        setOfficialDefault.setOnClickListener(new OnSkinClickListener());
        setColorfulLemon.setOnClickListener(new OnSkinClickListener());
    }

    /**
     * OnSkinClickListener
     */
    private class OnSkinClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.skin_official_default:
                    if(!isOfficialSelected){
                        SkinManager.getInstance().restoreDefaultTheme();
                        Toast.makeText(getApplicationContext(), "切换成功", Toast.LENGTH_SHORT).show();
                        setOfficialDefault.setText("官方默认(正在使用)");
                        setColorfulLemon.setText("缤纷青柠");
                        isOfficialSelected = true;
                    }
                    break;
                case R.id.skin_colorful_lemon:
                    if(!isOfficialSelected) return;

                    File skin = new File(SKIN_DIR);
                    if(skin == null || !skin.exists()) {
                        Toast.makeText(getApplicationContext(), "请检查" + SKIN_DIR + "是否存在", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    SkinManager.getInstance().load(skin.getAbsolutePath(),
                            new ILoaderListener() {
                                @Override
                                public void onStart() {
                                    L.e("start load skin");
                                }

                                @Override
                                public void onSuccess() {
                                    L.e("load skin success");
                                    Toast.makeText(getApplicationContext(), "切换成功", Toast.LENGTH_SHORT).show();
                                    setColorfulLemon.setText("缤纷青柠(正在使用)");
                                    setOfficialDefault.setText("官方默认");
                                    isOfficialSelected = false;
                                }

                                @Override
                                public void onFailed() {
                                    L.e("load skin fail");
                                    Toast.makeText(getApplicationContext(), "切换失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                    break;
            }
        }
    }
}
