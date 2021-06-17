package utils;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

// Created by Administrator on 2016/9/28.

@SuppressWarnings("unused")
public class FragmentUtil {

    public static void add(FragmentManager fm, int id, Fragment add) {
        fm.beginTransaction().add(id, add).commit();
    }

    public static void addHide(FragmentManager fm, int id, Fragment add, Fragment hide) {
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(id, add).hide(hide).commit();
    }

}
