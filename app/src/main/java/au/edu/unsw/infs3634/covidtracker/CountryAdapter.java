package au.edu.unsw.infs3634.covidtracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder> {
    private ArrayList<Country> mCountries;
    private Listener mListener;

    public CountryAdapter(ArrayList<Country> countries, Listener listener){
        mCountries = countries;
        mListener = listener;
    }
    public interface Listener{
        void onClick(View view, String countryCode);
    }
    @NonNull
    @Override
    public CountryAdapter.CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_list_row, parent, false);
        CountryViewHolder holder = new CountryViewHolder(v, mListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CountryAdapter.CountryViewHolder holder, int position) {
    Country country = mCountries.get(position);
        DecimalFormat df = new DecimalFormat("#,###,###,###");
        holder.country.setText(country.getCountry());
        holder.totalCases.setText(df.format(country.getTotalConfirmed()));
        holder.totalCases.setText(df.format(country.getNewConfirmed()));
        holder.itemView.setTag(country.getCountryCode());
    }

    @Override
    public int getItemCount() {
        return mCountries.size();
    }

    public class CountryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView country, totalCases, newCases;
        private Listener listener;

        public CountryViewHolder(@NonNull View itemView, Listener listener){
            super(itemView);
            this.listener = listener;
            itemView.setOnClickListener(this);
            country = itemView.findViewById(R.id.tvCountry);
            totalCases = itemView.findViewById(R.id.tvTotalCases);
            newCases = itemView.findViewById(R.id.tvNewCases);
        }

        @Override
        public void onClick(View v) {
        listener.onClick(v, (String) v.getTag());
        }
    }
}
