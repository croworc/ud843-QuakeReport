package com.example.android.quakereport;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import java.lang.reflect.Member;

import static com.example.android.quakereport.R.id.picker;


/**
 * A DialogPreference showing a NumberPicker for selecting the number of earthquake
 * events to fetch from USGS per web request.
 *
 * Created by ingo on 11/24/16.
 */

public class NumberPickerPreference extends DialogPreference
        implements NumberPicker.OnValueChangeListener {

    private static final String LOG_TAG = NumberPickerPreference.class.getSimpleName();

    private final int DEFAULT_VALUE = Integer.valueOf(getContext()
            .getString(R.string.settings_number_events_default));

    /** holds a reference to the NumberPicker wrapped by this preference */
    private NumberPicker mNumberPicker;

    private int mCurrentValue;  // holds the currently persisted value
    private int mNewValue;      // holds the settings current value, as is displayed

    /**
     * Create a custom NumberPickerPreference
     *
     * @param context   The SettingsActivity
     * @param attrs
     */
    public NumberPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(R.layout.dialog_numberpicker); // doing it myself in onCreateDialogView()
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
        setDialogIcon(null);

    } // close constructor


    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        mNumberPicker = (NumberPicker) view.findViewById(picker);
        mNumberPicker.setMinValue(5);
        mNumberPicker.setMaxValue(100);
        // Set the number picker to the currently persited preference value.
        mNumberPicker.setValue(mCurrentValue);
        // Attach an onValueChanged listener to this Preference.
        mNumberPicker.setOnValueChangedListener(this);
    }


    @Override
    protected void onDialogClosed(boolean positiveResult) {
        // If the user clicked [OK], then save the new value to the preferences
        // and update the preference summary.
        if (positiveResult) {
            // Log.d(LOG_TAG, "onDialogClosed() called...");
            persistInt(mNewValue);
            getOnPreferenceChangeListener().onPreferenceChange(this, mNewValue);
        }
    } // close method onDialogClosed()


    /**
     * If this setting has already been persisted, retrieve and display it as the initial value.
     * Otherwise,
     * @param restorePersistedValue
     * @param defaultValue
     */
    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            // The setting's value has already been persisted, so retrieve it.
            mCurrentValue = this.getPersistedInt(10);
        } else {
            // We don't have a persisted value yet, so set the default state from XML attribute.
            mCurrentValue = (Integer) defaultValue;
            persistInt(mCurrentValue);
        }

    } // close method onSetInitialValue()

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index, DEFAULT_VALUE);
    }


    private static class SavedState extends BaseSavedState {

        /**  Member that holds the setting's value */
        int value; // the number of earthquake events to fetch from USGS

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public SavedState(Parcel source) {
            super(source);
            // Get the preference's current value
            value = source.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            // Write the preference's value
            dest.writeInt(value);
        }

        // Standard creator object using an instance of this class
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {

                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    } // close inner class SavedState


    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        // Check whether this Preference is persistent (continually saved)
        if (isPersistent()) {
            // No need to save instance state since it's persistent,
            // use superclass state
            return superState;
        }

        // Create instance of custom BaseSavedState
        final SavedState myState = new SavedState(superState);
        // Set the state's value with the class member that holds current
        // setting value
        myState.value = mNewValue;
        return myState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        // Check whether we saved the state in onSaveInstanceState
        if (state == null || !state.getClass().equals(SavedState.class)) {
            // Didn't save the state, so call superclass
            super.onRestoreInstanceState(state);
            return;
        }

        // Cast state to custom BaseSavedState and pass to superclass
        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());

        // Set this Preference's widget to reflect the restored state
        mNumberPicker.setValue(myState.value);
    }


    @Override
    public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
        mNewValue = newValue; // store the selected value in the instance variable
        // Log.d(LOG_TAG, "onValueChanged() called. mNewValue = " + mNewValue);
    }
} // close class NumberPickerPreference
