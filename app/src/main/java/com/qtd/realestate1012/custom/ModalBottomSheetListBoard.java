package com.qtd.realestate1012.custom;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qtd.realestate1012.HousieApplication;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.activity.CreateBoardActivity;
import com.qtd.realestate1012.activity.LoginActivity;
import com.qtd.realestate1012.adapter.BoardAdapter;
import com.qtd.realestate1012.constant.ApiConstant;
import com.qtd.realestate1012.constant.AppConstant;
import com.qtd.realestate1012.model.Board;
import com.qtd.realestate1012.model.BoardHasHeart;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by DELL on 8/23/2016.
 */
public class ModalBottomSheetListBoard extends BottomSheetDialogFragment {

    private View view;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private BoardAdapter adapter;
    private ArrayList<Board> arrayListBoards;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bottom_sheet_dialog_fragment_list_board, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        initData();
        initView();
    }

    private void initView() {
        DisplayMetrics displaymetrics = Resources.getSystem().getDisplayMetrics();
        int width = displaymetrics.widthPixels;

        CoordinatorLayout.LayoutParams layoutParams = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
    }

    private void initData() {
        Bundle bundle = getArguments();

        arrayListBoards = new ArrayList<>();

        arrayListBoards.add(new BoardHasHeart("ladkjf", "homes", 3, "aldkfj", true));
        arrayListBoards.add(new BoardHasHeart("ladkjf", "homes", 3, "aldkfj", false));
        arrayListBoards.add(new BoardHasHeart("ladkjf", "homes", 3, "aldkfj", false));
        arrayListBoards.add(new BoardHasHeart("ladkjf", "homes", 3, "aldkfj", true));

        adapter = new BoardAdapter(arrayListBoards, true);

    }

    @OnClick(R.id.fabAddBoard)
    void onClick() {
        if (!HousieApplication.getInstance().getSharedPreUtils().getBoolean(AppConstant.USER_LOGGED_IN, false)) {
            Intent intentLogin = new Intent(getActivity(), LoginActivity.class);
            startActivityForResult(intentLogin, AppConstant.REQUEST_CODE_SIGN_IN);
            return;
        }

        Intent intent = new Intent(getActivity(), CreateBoardActivity.class);

        String listBoard = "";
        for (Board board : arrayListBoards) {
            listBoard += (board.getName() + "-");
        }
        intent.putExtra(ApiConstant.LIST_BOARD, listBoard);

        startActivityForResult(intent, AppConstant.REQUEST_CODE_CREATE_BOARD);
    }
}
