package com.example.a3lbotcontrol;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.a3lbotcontrol.activity.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tv_count_down)
    TextView tvCountDown;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initCountDown();
    }

    // 全屏显示
    private void setFullScreen() {
        // 如果该类是 extends Activity ，下面这句代码起作用
        // 去除ActionBar(因使用的是NoActionBar的主题，故此句有无皆可)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 如果该类是 extends AppCompatActivity， 下面这句代码起作用
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        // 去除状态栏，如 电量、Wifi信号等
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    // 倒计时逻辑
    private void initCountDown() {
        // 判断当前Activity是否isFinishing()，
        // 避免在finish，所有对象都为null的状态下执行CountDown造成内存泄漏
        if (!isFinishing()) {
            timer = new CountDownTimer(1000 * 4, 1000) {
                @SuppressLint("SetTextI18n")
                @Override
                public void onTick(long millisUntilFinished) {
                    // TODO: 耗时操作，如异步登录
                    // ......
                    int time = (int) millisUntilFinished;
                    tvCountDown.setText(time / 1000 + " 跳过");
                    tvCountDown.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            checkToJump();
                        }
                    });
                }

                @Override
                public void onFinish() {
                    checkToJump();
                }
            }.start();
        }
    }

    // 首次进入引导页判断
    private void checkToJump() {
        //  TODO：首次安装判断
        // 如果是首次打开，则跳至引导页；否则跳至主界面
        // 这里先不放引导页，直接跳到主界面
        startActivity(new Intent(MainActivity.this, HomeActivity.class));
        // 回收内存
        destoryTimer();
        finish();
    }

    public void destoryTimer() {
        // 避免内存泄漏
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }


}
