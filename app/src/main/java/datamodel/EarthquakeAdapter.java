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
import util.TextUtil;

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
        TextView offsetTextView;
        TextView locationTextView;
        TextView dateTextView;
        TextView timeTextView;
    }

    /**
     * An {@link EarthquakeAdapter} knows how to create a list item layout for each earthquake
     * in the data source (a list of {@link Earthquake} objects).
     *
     * These list item layouts will be provided to an adapter view like ListView
     * to be displayed to the user.
     *
     * @param context        the calling activity
     * @param earthquakeList is the list of Earthquake objects, which is the data source
     *                       of the adapter
     */
    public EarthquakeAdapter(Context context, List<Earthquake> earthquakeList) {
        super(context, 0, earthquakeList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        // Get the data item (earthquake object) for this position
        Earthquake earthquake = getItem(position);

        // Should we happen to have a null reference for the Earthquake object, just return
        // the unmodified list item layout
        if (earthquake == null) { return convertView;}

        // Check if an existing view is being reused, otherwise inflate the view.
        ViewHolder viewHolder; // view lookup cache, is being stored in convertView's tag
        if (convertView == null) {
            // There's no view to re-use, so we'll inflate a brand new view for this row.
            viewHolder = new ViewHolder();

            // Obtain a LayoutInflater from the Context object (activity) that has been
            // passed into this adapter's constructor.
            LayoutInflater inflater = LayoutInflater.from(getContext());

            // Inflate the custom layout for this row (list_item.xml).
            convertView = inflater.inflate(R.layout.list_item, parent, false);

            // Fetch references to the TextViews we want to populate and store them away
            // in the ViewHolder for later re-use.
            viewHolder.magnitudeTextView = (TextView) convertView
                    .findViewById(R.id.magnitude_textview);

            viewHolder.offsetTextView = (TextView) convertView
                    .findViewById(R.id.offset_textview);

            viewHolder.locationTextView = (TextView) convertView
                    .findViewById(R.id.location_textview);

            viewHolder.dateTextView = (TextView) convertView
                    .findViewById(R.id.date_textview);

            viewHolder.timeTextView = (TextView) convertView
                    .findViewById(R.id.time_textview);

            // Cache the viewHolder object inside the fresh view.
            convertView.setTag(viewHolder);

        } else {
            // View is being recycled, retrieve the viewHolder object from the tag.
            viewHolder = (ViewHolder) convertView.getTag();

        } // close checking for new vs. recycled convertView

        // Populate the data from the Earthquake object via the ViewHolder object into the
        // template view for this row.
        viewHolder.magnitudeTextView.setText(String.valueOf(earthquake.getMag()));

        // The earthquake's location (place) will be used twice: for the location and its offset.
        String place = earthquake.getPlace();
        viewHolder.offsetTextView.setText(TextUtil.getLocationOffset(getContext(), place));
        viewHolder.locationTextView.setText(TextUtil.getLocation(place));


        // Analogously, the earthquake's Unix timestamp will be used twice: for the
        // date component as well as the time component.
        long timestamp = earthquake.getTimeInMilliseconds();
        viewHolder.dateTextView.setText(DateUtil.getSimpleDate(timestamp) );
        viewHolder.timeTextView.setText(DateUtil.getSimpleTime(timestamp) );

        // Return the completed view to be rendered on screen.
        return convertView;
    } // close getView()

} // close class EarthquakeAdapter
