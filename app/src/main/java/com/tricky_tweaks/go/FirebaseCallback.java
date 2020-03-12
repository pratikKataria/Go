package com.tricky_tweaks.go;

import com.tricky_tweaks.go.DataModel.GatePassData;

import java.util.ArrayList;

public interface FirebaseCallback {
    void isExist(boolean value);
    void getList(ArrayList<GatePassData> listData);
}
