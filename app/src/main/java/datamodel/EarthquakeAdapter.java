package datamodel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.quakereport.R;

import java.util.List;

import util.DateUtil;

/**
 * A custom ArrayAdapter for handling Earthquake objects
 *
 * Uses our DateUtil class for date formatting.
 *
 * Created by ingo on 11/10/16.
 */

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    /**
     * View lookup cache
     */
    private static class ViewHolder {
        TextView magnitudeTextView;
        TextView locationTextView;
        TextView timeTextView;
    }

    /**
     * Creates a new EarthquakeAdapter, with a context (the activity) and a List of
     * Earthquake objects.
     *
     * @param context           The calling activity
     * @param earthquakeList    The list of Earthquake objects
     */
    public EarthquakeAdapter(Context context, List<Earthquake> earthquakeList) {
        super(context, 0, earthquakeList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        // Get the data item for this position
        Earthquake earthquake = getItem(position);

        // Should we happen not to have a null reference for the Earthquake object, just return
        // the unmodified list item layout
        if (earthquake == null) { return convertView;}

        // Check if an existing view is being reused, otherwise inflate the view.
        ViewHolder viewHolder; // view lookup cache, is being stored in tag
        if (convertView == null) {
            // There's no view to re-use, so we'll inflate a brand new view for this row.
            viewHolder = new ViewHolder();

            // Obtain a LayoutInflater from the Context (activity) that has been passed into this
            // adapter's constructor.
            LayoutInflater inflater = LayoutInflater.from(getContext());

            // Inflate the custom layout for this row.
            convertView = inflater.inflate(R.layout.list_item, parent, false);

            // Fetch references to the TextViews we want to populate and store them away
            // in the ViewHolder for later re-use.
            viewHolder.magnitudeTextView = (TextView) convertView
                    .findViewById(R.id.magnitude_textview);
            viewHolder.locationTextView = (TextView) convertView
                    .findViewById(R.id.location_textview);
            viewHolder.timeTextView = (TextView) convertView
                    .findViewById(R.id.time_textview);

            // Cache the viewHolder object inside the fresh view.
            convertView.setTag(viewHolder);

        } else {
            // View is being recycled, retrieve the viewHolder object from the tag.
            viewHolder = (ViewHolder) convertView.getTag();

        } // close checking for new vs. recycled convertView

        // Populate the data from the Earthquake object via the viewHolder object into the
        // template view for this row.
        viewHolder.magnitudeTextView.setText(String.valueOf(earthquake.getMag()));
        viewHolder.locationTextView.setText(earthquake.getPlace());
        viewHolder.timeTextView.setText(DateUtil.getSimpleDate(earthquake.getTime()) );

        // Return the completed view to be rendered on screen.
        return convertView;
    }
}
