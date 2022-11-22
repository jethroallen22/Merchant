package com.example.merchant.activities.ui.editproduct;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EditProductViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public EditProductViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}