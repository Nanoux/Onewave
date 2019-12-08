package com.nanowheel.nanoux.nanowheel.TripDatabase;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.nanowheel.nanoux.nanowheel.R;
import com.nanowheel.nanoux.nanowheel.util.SharedPreferencesUtil;
import com.nanowheel.nanoux.nanowheel.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TripListAdapter extends RecyclerView.Adapter<TripListAdapter.TripViewHolder> implements OnChartValueSelectedListener {

    class TripViewHolder extends RecyclerView.ViewHolder{
        private final TextView tripItemView;
        private final TextView tripItemView2;
        private final RelativeLayout layout;

        Details details;

        private TripViewHolder(View itemView) {
            super(itemView);
            tripItemView = itemView.findViewById(R.id.textView);
            tripItemView2 = itemView.findViewById(R.id.textView2);
            layout = itemView.findViewById(R.id.layout);
            details = new Details();
        }

        Details getItemDetails(){
            return details;
        }

        void bind(int position){
            details.position = position;
            if (mTrips != null) {
                final Trip current = mTrips.get(mTrips.size() - 1 - position);
                final long key = (long)details.getSelectionKey();
                final boolean selected = FragmentMap.mSelectionTracker.isSelected(key);
                this.itemView.setActivated(selected);

                final int pos = position;
                this.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!fragMap.mSelectionTracker.hasSelection()) {
                            if (current.getRevs() == null || current.getLevels() == null || current.getRevs().size() == 0 || current.getLevels().size() == 0) {
                                return;
                            }

                            showGragh(current);
                            showLines(pos, pos);
                            focusLine(mTrips.size() - 1 - pos);
                        }
                    }
                });

                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (!FragmentMap.mSelectionTracker.hasSelection()) {
                            FragmentMap.mSelectionTracker.select(key);
                            itemView.setActivated(true);
                        }
                        return true;
                    }
                });

                int revs;
                if (current.getRevs() == null || current.getRevs().size() == 0){
                    revs = 0;
                }else {
                    revs = current.getRevs().get(current.getRevs().size() - 1) - current.getRevStamp();
                }

                String res;
                if (SharedPreferencesUtil.getPrefs(context).isMetric()){
                    res = String.format(Locale.getDefault(),"%.1f",Util.revolutionsToKilometers(revs,0)) + " Km   ";
                    tripItemView.setText(convertDate(current.getTimeStamp(),"dd/MM  hh:mm"));
                }else{
                    res = String.format(Locale.getDefault(),"%.1f",Util.revolutionsToMiles(revs,0)) + " Mi   ";
                    tripItemView.setText(convertDate(current.getTimeStamp(),"MM/dd  hh:mm"));
                }

                if (current.getTimes() == null || current.getTimes().size() == 0) {
                    res += "0 Min";
                }else {
                    res += Math.round((double) current.getTimes().get(current.getTimes().size() - 1) / 1000d / 60d) + " Min";
                }

                tripItemView2.setText(res);

                int selectColor;
                int accentColor;
                if (Util.isDark(context)){
                    selectColor = Color.parseColor("#8880d8ff");
                    accentColor = Color.parseColor("#80d8ff");
                }else{
                    selectColor = Color.parseColor("#880277bd");
                    accentColor = Color.parseColor("#0277bd");
                }

                if (selected){
                    layout.setBackgroundColor(selectColor);
                }else{
                    layout.setBackgroundColor(Color.TRANSPARENT);
                }

                if (lines == null){
                    lines = new SparseArray<>();
                }
                Iterable<LatLng> lats = current.getPositions();

                if (lats != null) {
                    PolylineOptions opt = (new PolylineOptions()
                            .clickable(false)
                            .addAll(lats)
                            .visible(false)
                            .color(accentColor)
                    );

                    Polyline line = FragmentMap.googleMap.addPolyline(opt);

                    lines.put(position, line);
                }
            } else {
                // Covers the case of data not being ready yet.
                tripItemView.setText("No Time Stamp");
                tripItemView2.setText("RIP");
            }
        }

    }

    private final LayoutInflater mInflater;
    private List<Trip> mTrips; // Cached copy of words
    private static SparseArray<Polyline> lines;
    private FragmentMap fragMap;
    private Context context;

    TripListAdapter(Context context, FragmentMap map) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        fragMap = map;
    }

    @Override
    @NonNull
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType) {
        View itemView = mInflater.inflate(R.layout.recycler_view, parent, false);
        return new TripViewHolder(itemView);
    }

    @Override
    public long getItemId(int position) {
        return position;
        //return super.getItemId(position);
        //return mTrips.get(mTrips.size() - 1 - position).getTimeStamp();
    }

    @Override
    public void onBindViewHolder(final TripViewHolder holder, int position) {
        holder.bind(position);
    }

    void setTrips(List<Trip> trips){
        mTrips = trips;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mTrips != null)
            return mTrips.size();
        else return 0;
    }

    private static String convertDate(long dateInMilliseconds,String dateFormat) {
        return DateFormat.format(dateFormat, dateInMilliseconds).toString();
    }

    static RecyclerView.OnScrollListener scrollListen = new RecyclerView.OnScrollListener(){
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy){
            super.onScrolled(recyclerView,dx,dy);

            LinearLayoutManager man = (LinearLayoutManager)recyclerView.getLayoutManager();
            if (man != null) {
                int first = man.findFirstCompletelyVisibleItemPosition();
                int last = man.findLastCompletelyVisibleItemPosition();
                showLines(first, last);
            }
        }
    };


    private static void showLines(int first, int last){
        if (lines != null){
            for(int i = 0; i < lines.size(); i++){
                if (i >= first && i <= last){
                    lines.get(i).setVisible(true);
                }else{
                    lines.get(i).setVisible(false);
                }
            }
        }
    }

    public static void showLines(RecyclerView recyclerView){
        LinearLayoutManager man = (LinearLayoutManager)recyclerView.getLayoutManager();
        if (man != null) {
            int first = man.findFirstCompletelyVisibleItemPosition();
            int last = man.findLastCompletelyVisibleItemPosition();
            showLines(first, last);
        }
    }

    private void focusLine (int index){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        List<LatLng> lats = mTrips.get(index).getPositions();
        if (lats == null){
            return;
        }
        for (int i = 0; i < lats.size(); i += 5){
            builder.include(lats.get(i));
        }
        builder.include(lats.get(lats.size()-1));
        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,24);
        FragmentMap.googleMap.animateCamera(cu);
    }

    private static LineChart chart;
    private void showGragh(Trip tripp){

        ConstraintLayout layout = fragMap.mView.findViewById(R.id.chart_layout);
        if (layout.getVisibility() == View.INVISIBLE){
            Animation in = AnimationUtils.loadAnimation(context, R.anim.fade_in);
            layout.startAnimation(in);
        }
        layout.setVisibility(View.VISIBLE);

        if (!SharedPreferencesUtil.getPrefs(context).isMetric()){
            TextView text = fragMap.mView.findViewById(R.id.distance_units);
            text.setText("Distance (Mi)");
        }

        final Trip trip = tripp;
        chart = fragMap.mView.findViewById(R.id.chart);
        chart.setOnChartValueSelectedListener(this);
        TextView text = fragMap.mView.findViewById(R.id.selected_units);
        text.setText("");
        new Thread(new Runnable() {
            @Override
            public void run() {
                chart.clear();
                chart.invalidate();

                final int colorText;
                final int colorAccent;
                //final int colorHighlight;
                if (Util.isDark(context)){
                    colorText = Color.parseColor("#ffffff");
                    //colorHighlight = Color.parseColor("#8880d8ff");
                    colorAccent = Color.parseColor("#80d8ff");
                }else{
                    colorText = Color.parseColor("#000000");
                    //colorHighlight = Color.parseColor("#880277bd");
                    colorAccent = Color.parseColor("#0277bd");
                }

                List<Integer> batteryList = trip.getLevels();
                List<Integer> revList = trip.getRevs();
                List<Long> timeList = trip.getTimes();

                List<Entry> batteryVal = new ArrayList<>();
                for (int i = 0; i < batteryList.size(); i++){
                    batteryVal.add(new Entry(timeList.get(i),batteryList.get(i)));
                }

                List<Entry> revVal = new ArrayList<>();
                for (int i = 0; i < revList.size(); i++){
                    revVal.add(new Entry(timeList.get(i),revList.get(i) - trip.getRevStamp()));
                }

                LineDataSet setBat = new LineDataSet(batteryVal, "Battery Percentage");
                setBat.setAxisDependency(YAxis.AxisDependency.LEFT);
                setBat.setValueTextSize(0);
                setBat.setColor(colorText);
                setBat.setCircleColor(colorText);
                setBat.setCircleRadius(2);
                setBat.setCircleColorHole(colorText);
                setBat.setHighLightColor(colorAccent);

                LineDataSet setRev = new LineDataSet(revVal, "Distance");
                setRev.setAxisDependency(YAxis.AxisDependency.RIGHT);
                setRev.setValueTextSize(0);
                setRev.setColor(colorAccent);
                setRev.setCircleColor(colorAccent);
                setRev.setCircleRadius(2);
                setRev.setCircleColorHole(colorAccent);
                setRev.setHighLightColor(colorAccent);

                List<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(setBat);
                dataSets.add(setRev);

                XAxis right = chart.getXAxis();
                right.setValueFormatter(new MyXAxisValueFormatter());
                right.setTextColor(colorText);
                right.setPosition(XAxis.XAxisPosition.BOTTOM);
                right.setTextSize(12);
                right.setGranularity(1);

                YAxis left = chart.getAxisLeft();
                left.setTextColor(colorText);
                left.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
                left.setTextSize(12);
                left.setGranularity(1);
                left.setLabelCount(6,true);

                YAxis rightY = chart.getAxisRight();
                rightY.setValueFormatter(new MyYAxisValueFormatter());
                rightY.setTextColor(colorAccent);
                rightY.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
                rightY.setTextSize(12);
                rightY.setGranularity(1);
                rightY.setLabelCount(6,true);


                LineData data = new LineData(dataSets);
                chart.setData(data);
                Description desc = new Description();
                desc.setText("");
                chart.getLegend().setEnabled(false);
                chart.setDescription(desc);
                chart.invalidate(); // refresh
            }
        }).start();

    }

    public class MyXAxisValueFormatter implements IAxisValueFormatter {

        MyXAxisValueFormatter() {
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            // "value" represents the position of the label on the axis (x or y)
            return String.format(Locale.getDefault(),"%.1f",value / 1000d / 60d);
        }

    }

    public class MyYAxisValueFormatter implements IAxisValueFormatter {

        MyYAxisValueFormatter() {
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            // "value" represents the position of the label on the axis (x or y)
            if (SharedPreferencesUtil.getPrefs(context).isMetric()) {
                return String.format(Locale.getDefault(), "%.1f", Util.revolutionsToKilometers(value,0)) + "";
            }else{
                return String.format(Locale.getDefault(), "%.1f", Util.revolutionsToMiles(value,0)) + "";
            }
        }

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        TextView text = fragMap.mView.findViewById(R.id.selected_units);
        String time = String.format(Locale.getDefault(),"%.1f",e.getX()/ 1000d / 60d) + " Min";
        String battery;
        String distance;
        if (h.getDataSetIndex() == 0){
            //text.setText("Time: " + e.getX() + " Min, Battery: " + e.getY() + " %, Distance: " + chart.getLineData().getDataSets().get(1).getEntryForXValue(e.getX(),0).getY() + " Mi");
            battery = ((int)e.getY()) + "%";
            if (SharedPreferencesUtil.getPrefs(context).isMetric()){
                distance = String.format(Locale.getDefault(), "%.1f", Util.revolutionsToKilometers(chart.getLineData().getDataSets().get(1).getEntryForXValue(e.getX(),0).getY(),0)) + "Km";
            }else{
                distance = String.format(Locale.getDefault(), "%.1f", Util.revolutionsToMiles(chart.getLineData().getDataSets().get(1).getEntryForXValue(e.getX(),0).getY(),0)) + "Mi";
            }
        }else{
            battery = ((int)chart.getLineData().getDataSets().get(0).getEntryForXValue(e.getX(),0).getY()) + "%";
            if (SharedPreferencesUtil.getPrefs(context).isMetric()){
                distance = String.format(Locale.getDefault(), "%.1f", Util.revolutionsToKilometers(e.getY(),0)) + "Km";
            }else{
                distance = String.format(Locale.getDefault(), "%.1f", Util.revolutionsToMiles(e.getY(),0)) + "Mi";
            }
        }
        text.setText("Time: " + time + ", Battery: " + battery + ", Distance: " + distance);
    }

    @Override
    public void onNothingSelected() {
        TextView text = fragMap.mView.findViewById(R.id.selected_units);
        text.setText("");
    }



    static class Details extends ItemDetailsLookup.ItemDetails{
        long position;

        Details(){}

        @Override
        public int getPosition() {
            return (int) position;
        }

        @Nullable
        @Override
        public Object getSelectionKey() {
            return position;
        }

        @Override
        public boolean inSelectionHotspot(@NonNull MotionEvent e) {
            return true;
        }
    }
    static class TripDetailsLookup extends ItemDetailsLookup<Long> {
        RecyclerView recyclerView;

        TripDetailsLookup (RecyclerView rec){
            recyclerView = rec;
        }

        @Override
        public ItemDetails<Long> getItemDetails(MotionEvent e) {
            if (FragmentMap.mSelectionTracker.hasSelection()) {
                View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (view != null) {
                    final RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(view);
                    if (holder instanceof TripListAdapter.TripViewHolder) {
                        return ((TripListAdapter.TripViewHolder) holder).getItemDetails();
                    }
                }
            }
            return null;
        }
    }
    static class Predicate extends SelectionTracker.SelectionPredicate{
        @Override
        public boolean canSetStateForKey(@NonNull Object key, boolean nextState) {
            return true;
        }

        @Override
        public boolean canSetStateAtPosition(int position, boolean nextState) {
            return true;
        }

        @Override
        public boolean canSelectMultiple() {
            return true;
        }
    }
}
