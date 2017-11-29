package com.strategy.intecom.vtc.vtctracking.interfaces;

/**
 * Created by Thuy Chi on 7/7/16.
 */
public interface RequestConnection {
    void onPostStarting(int key);
    void onPostError(int key);
    void onPostExecute(int key);
}
