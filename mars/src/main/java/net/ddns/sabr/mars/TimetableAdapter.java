package net.ddns.sabr.mars;/**/

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

    public class TimetableAdapter extends RecyclerView.Adapter<TimetableAdapter.TimetableViewHolder> {

        private final List<String[]> tableList;

        public TimetableAdapter(List<String[]> tableList) {
            this.tableList = tableList;
        }

        @Override
        public int getItemCount() {
            return tableList.size();
        }

        @Override
        public void onBindViewHolder(TimetableViewHolder holder, int position) {
            String[] t = tableList.get(position);
            holder.groupView.setText(t[0]);
            holder.ncoView.setText(t[3]);
            holder.officerView.setText(t[1]);
            holder.uniformView.setText(t[2]);
            holder.detailView.setText(t[4]);

        }

        @Override
        public TimetableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.timetable_item, parent, false);

            return new TimetableViewHolder(itemView);
        }

        public static class TimetableViewHolder extends RecyclerView.ViewHolder{

            final TextView groupView;
            final TextView ncoView;
            final TextView officerView;
            final TextView uniformView;
            final TextView detailView;

            public TimetableViewHolder(View view){
                super(view);

                groupView = (TextView) view.findViewById(R.id.groupView);
                ncoView = (TextView) view.findViewById(R.id.ncoView);
                officerView = (TextView) view.findViewById(R.id.officerView);
                uniformView = (TextView) view.findViewById(R.id.uniformView);
                detailView = (TextView) view.findViewById(R.id.detailView);

            }

        }

    }

