package com.ediberto.informes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    private ArrayList<Report> reportList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Report report);
    }

    public ReportAdapter(ArrayList<Report> reportList, OnItemClickListener listener) {
        this.reportList = reportList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_item, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        Report report = reportList.get(position);
        holder.bind(report, listener);
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    static class ReportViewHolder extends RecyclerView .ViewHolder {
        private TextView dateTextView;
        private TextView locationTextView;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
        }

        public void bind(final Report report, final OnItemClickListener listener) {
            dateTextView.setText(report.getDate());
            locationTextView.setText(report.getLocation());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(report);
                }
            });
        }
    }
}
