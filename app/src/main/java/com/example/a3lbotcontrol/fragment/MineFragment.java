package com.example.a3lbotcontrol.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.a3lbotcontrol.R;
import com.example.a3lbotcontrol.activity.DrawActivity;
import com.example.a3lbotcontrol.activity.PersonActivity;
import com.example.a3lbotcontrol.util.GreenDaoManager;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment {

    View view;
    Unbinder unbinder;
    int index = 1;

    @BindView(R.id.civ_image)
    CircleImageView civImage;
    @BindView(R.id.et_mine_iphone)
    EditText etMineIphone;
    @BindView(R.id.ibtn_user)
    ImageButton ibtnUser;
    @BindView(R.id.btn_history)
    Button btnHistory;
    @BindView(R.id.btn_mn)
    Button btnMn;

    public MineFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_mine, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.ibtn_user, R.id.btn_history, R.id.btn_mn})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.ibtn_user:
                Intent intent = new Intent(getContext(), PersonActivity.class);
                intent.putExtra("iphone", etMineIphone.getText().toString());
                startActivity(intent);
                break;
            case R.id.btn_history:
                startActivity(new Intent(getContext(), DrawActivity.class));
                break;
            case R.id.btn_mn:
                GreenDaoManager.getInstance().saveBeanIH(2021, 4, index, (int) (Math.random() * 200), ((new Random()).nextInt(100 - 50) + 50), (int) (Math.random() * 90));
                index++;
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
