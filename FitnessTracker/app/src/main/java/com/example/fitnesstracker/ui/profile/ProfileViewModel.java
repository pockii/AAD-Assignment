package com.example.fitnesstracker.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<String> name;

    public ProfileViewModel() {
        name = new MutableLiveData<>();
        name.setValue("GO");
    }

    public LiveData<String> getName() {
        return name;
    }
}

