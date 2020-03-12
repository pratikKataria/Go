package com.tricky_tweaks.go;

import java.util.ArrayList;
import java.util.List;

public interface FirebaseCallback {
    void isExist(boolean value);
    void getList(ArrayList<GatePassData> listData);
}
