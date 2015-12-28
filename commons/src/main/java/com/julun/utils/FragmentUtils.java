package com.julun.utils;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by danjp on 2015/12/25.
 */
public class FragmentUtils {

    private FragmentUtils(){}

    public static Fragment replaceFragment(FragmentManager fm, int container,
                                    Class<? extends Fragment> newFragmentClass, Bundle bundle) {
        return replaceFragment(fm, container, newFragmentClass, bundle, false);
    }

    public static Fragment replaceFragment(FragmentManager fm, int container,
                                    Class<? extends Fragment> newFragmentClass, Bundle bundle, boolean addToBackStack) {
        Fragment newFragment = null;

        try {
            newFragment = newFragmentClass.newInstance();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if(newFragment != null) {
            if(bundle != null && !bundle.isEmpty()) {
                Bundle bdl = newFragment.getArguments();
                if(bdl != null) {
                    bundle.putAll(bdl);
                }
                newFragment.setArguments(bundle);
            }
            return replaceFragment(fm, container, newFragment, addToBackStack);
        }
        return newFragment;
    }

    public static Fragment replaceFragment(FragmentManager fm, int container, Fragment newFragment) {
        return replaceFragment(fm, container, newFragment, false);
    }

    public static Fragment replaceFragment(FragmentManager fm, int container, Fragment newFragment, boolean addToBackStack) {
        FragmentTransaction transaction = fm.beginTransaction();

        String tag = newFragment.getClass().getSimpleName();
        transaction.replace(container, newFragment, tag);

        if(addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.commitAllowingStateLoss();
        return newFragment;
    }

    public static Fragment switchFragment(FragmentManager fm, int container, Fragment currentFragment,
                                   Class<? extends Fragment> newFragmentClass, Bundle bundle) {
        return switchFragment(fm, container, currentFragment, newFragmentClass, bundle, false);
    }

    static final String TAG = "FragmentUtil";
    public static Fragment switchFragment(FragmentManager fm, int container, Fragment currentFragment,
                                   Class<? extends Fragment> newFragmentClass, Bundle bundle, boolean addToBackStack) {
        FragmentTransaction transaction = fm.beginTransaction();
        String newFragmentTag = newFragmentClass.getSimpleName();
        Fragment newFragment = fm.findFragmentByTag(newFragmentTag);

        String currentFragmentTag = currentFragment.getClass().getSimpleName();
        Log.d(TAG, "current fragment tag is :"+currentFragmentTag);
        Log.d(TAG, "current fragment tag is :"+fm.findFragmentByTag(currentFragmentTag));
        if(fm.findFragmentByTag(currentFragmentTag) == null) {
            Log.d(TAG, "current fragment is null");
            transaction.add(container, currentFragment, currentFragmentTag).commitAllowingStateLoss();
            return currentFragment;
        }

        Log.d(TAG, "newFragment is start");
        if(newFragment != null) {

            if(!currentFragment.getClass().getSimpleName().equals(newFragmentTag)) {
                Log.d(TAG, "newFragment is start 2");
                transaction.hide(currentFragment);
                //fragment已存在，只是先前隐藏掉了，这里是否需要设置args？
                transaction.show(newFragment);
                if(addToBackStack) {
                    transaction.addToBackStack(null);
                }
                transaction.commitAllowingStateLoss();
            } else if(bundle != null){
                newFragment.getArguments().putAll(bundle);
            }
            Log.d(TAG, "new fragment is return");
            return newFragment;
        } else {
            try {
                Log.d(TAG, "created new Fragment");
                newFragment = newFragmentClass.newInstance();
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        if(newFragment != null && bundle != null && !bundle.isEmpty()) {
            Bundle bdl = newFragment.getArguments();
            if(bdl != null) {
                bundle.putAll(bdl);
            }
            newFragment.setArguments(bundle);
        }
        if (currentFragment != null) {
            transaction.hide(currentFragment);
        }
        Log.d(TAG, "new Fragment is added");
        transaction.add(container, newFragment, newFragmentTag);
        if(addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commitAllowingStateLoss();
        Log.d(TAG, "new Fragment is reutrned");
        return newFragment;
    }

    public void addFragment(FragmentManager fm, int container, Fragment currentFragment) {
        if(currentFragment != null)
            fm.beginTransaction().add(container, currentFragment, currentFragment.getClass().getSimpleName());
    }

}
