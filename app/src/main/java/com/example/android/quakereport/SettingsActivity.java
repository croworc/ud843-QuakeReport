package com.example.android.quakereport;

import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the activity's xml layout file. Its only element is the
        // static preference fragment, defined below as an inner class.
        setContentView(R.layout.activity_settings);
    } // close onCreate()



    public static class EarthquakePreferenceFragment extends PreferenceFragment
            implements Preference.OnPreferenceChangeListener{

        private static final String LOG_TAG = EarthquakePreferenceFragment.class.getSimpleName();

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Inflate the preference xml resource and add it to the preference hierarchy.
            addPreferencesFromResource(R.xml.preferences);

            // Show the current value of the "Minimum Magnitude" preference as the preference's
            // summary.
            Preference minMagnitude = findPreference(
                    getString(R.string.settings_min_magnitude_key));
            bindPreferenceSummaryToValue(minMagnitude);

            // Show the current value of the "Order-By" preference as the preference's
            // summary.
            Preference orderByPreference = findPreference(
                    getString(R.string.settings_order_by_key));
            bindPreferenceSummaryToValue(orderByPreference);

            // Show the current value of the "Number of events to fetch" preference as the
            // preference's summary.
            Preference numberEventsPreference = findPreference(
                    getString(R.string.settings_number_events_key));
            bindPreferenceSummaryToValue(numberEventsPreference);

        } // close method onCreate()


        /**
         * Update the preference's summary, so the user can see the updated value without
         * opening the preference's list entry.
         *
         * @param preference    The preference that's about to change it's value
         * @param o             The updated value
         * @return              Shall the updated value be accepted?
         *                          - [true] to accept / persist
         *                          - [false] to cancel
         */
        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {
            // Log.d(LOG_TAG, "onPreferenceChange() called...");

            // Fetch the updated value
            String stringValue = o.toString();

            // If its a ListPreference, get the key that belongs to the currently selected
            // list option and retrieve the label that's associated with this option from the
            // ListPreference's "entries" array.
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(stringValue); // get the option's index
                if (prefIndex >= 0) {
                    CharSequence[] labels = listPreference.getEntries(); // get the labels array
                    preference.setSummary(labels[prefIndex]); // fetch the correct label from the array
                }
            } else {
                // Just set the value itself as the preference's summary.
                preference.setSummary(stringValue);
            }
            return true; // Accept/persist the new value.
        } // close method onPreferenceChange()


        private void bindPreferenceSummaryToValue(Preference preference) {

            // Set this PreferenceFragment as the listener on the passed-in preference.
            preference.setOnPreferenceChangeListener(this);

            // Also, read the current/default value of this preference from the SharedPreferences
            // and display that value in the preference summary
            // (so that the user can see the current value of the preference).
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(
                    preference.getContext());

            String preferenceString;

            if( preference instanceof NumberPickerPreference) {
                // The "Number of events to fetch" preference stores its value as an int.
                // So first fetch this int value, then convert it to a String.
                int preferenceValue = sharedPrefs.getInt(preference.getKey(),
                        Integer.parseInt(getString(R.string.settings_number_events_default)));
                preferenceString = String.valueOf(preferenceValue);

            } else { // preference value already is a string...
                preferenceString = sharedPrefs.getString(preference.getKey(), "");
            }

            // Manually fire the preference change event, so the summary gets updated
            // immediately (and the user already sees the summary upon first opening the
            // settings activity).
            onPreferenceChange(preference, preferenceString);
        } // close method bindPreferenceSummaryToValue()

    } // close class EarthquakePreferenceFragment

} // close class SettingsActivity
