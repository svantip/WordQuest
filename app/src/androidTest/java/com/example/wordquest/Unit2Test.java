package com.example.wordquest;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.annotation.Config;

@RunWith(AndroidJUnit4.class)
@Config(sdk = {Build.VERSION_CODES.O_MR1})
public class Unit2Test {

    @Mock
    private ImageView mockImageBoat;

    @Mock
    private ViewGroup.MarginLayoutParams mockLayoutParamsBoat;

    @Mock
    private ImageView mockImageChest;

    @Mock
    private ViewGroup.MarginLayoutParams mockLayoutParamsChest;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUpdateImagePositions() {
        int guessedLetters = 5;
        int wordLength = 10;

        Context context = ApplicationProvider.getApplicationContext();
        Resources resources = context.getResources();

        when(mockImageBoat.getLayoutParams()).thenReturn(mockLayoutParamsBoat);
        when(mockImageChest.getLayoutParams()).thenReturn(mockLayoutParamsChest);

        updateImagePositions(mockImageBoat, mockLayoutParamsBoat, mockImageChest,
                mockLayoutParamsChest, guessedLetters, wordLength, resources);

        int expectedMarginBoat = convertDpToPx((185 / wordLength) * guessedLetters, resources);
        int expectedMarginChest = convertDpToPx(185 - (185 / wordLength) * guessedLetters, resources);

        verify(mockLayoutParamsBoat).setMarginStart(expectedMarginBoat);
        verify(mockLayoutParamsChest).setMarginStart(expectedMarginChest);
        verify(mockImageBoat).setLayoutParams(mockLayoutParamsBoat);
        verify(mockImageChest).setLayoutParams(mockLayoutParamsChest);
    }

    private void updateImagePositions(ImageView imageBoat, ViewGroup.MarginLayoutParams layoutParamsBoat,
                                      ImageView imageChest, ViewGroup.MarginLayoutParams layoutParamsChest,
                                      int guessedLetters, int wordLength, Resources resources) {
        int convertedMarginBoat = convertDpToPx((185 / wordLength) * guessedLetters, resources);
        layoutParamsBoat.setMarginStart(convertedMarginBoat);
        imageBoat.setLayoutParams(layoutParamsBoat);

        int convertedMarginChest = convertDpToPx(185 - (185 / wordLength) * guessedLetters, resources);
        layoutParamsChest.setMarginStart(convertedMarginChest);
        imageChest.setLayoutParams(layoutParamsChest);
    }

    private int convertDpToPx(int dp, Resources resources) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                resources.getDisplayMetrics()
        );
    }
}