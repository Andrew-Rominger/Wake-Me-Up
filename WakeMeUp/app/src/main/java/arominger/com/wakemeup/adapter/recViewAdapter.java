package arominger.com.wakemeup.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import arominger.com.wakemeup.MainActivity;
import arominger.com.wakemeup.R;
import arominger.com.wakemeup.Classes.alarmInList;

/**
 * Created by Andrew on 10/6/2016.
 */

public class recViewAdapter extends RecyclerView.Adapter<recViewAdapter.viewHolder>
{
    private LayoutInflater mInflater;
    private List<alarmInList> mdata;
    MainActivity ma;

    public recViewAdapter(Context context, List<alarmInList> data, MainActivity ma)
    {
        this.ma = ma;
        this.mdata = data;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = mInflater.inflate(R.layout.list_item, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position)
    {
        alarmInList al = mdata.get(position);
        holder.setData(al, position);
        holder.setListners();
    }

    @Override
    public int getItemCount()
    {
        return mdata.size();
    }
    public void removeItem(int position)
    {
        ma.helper(position);
        mdata.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mdata.size());
    }
    public void addItem(int position, alarmInList al)
    {
        mdata.add(al);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, mdata.size());
    }





    class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView time, date;
        ImageView delete;
        int position;
        alarmInList current;
        viewHolder(View itemView)
        {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.itemTime);
            date = (TextView) itemView.findViewById(R.id.itemDate);
            delete = (ImageView) itemView.findViewById(R.id.itemDelete);
        }

        void setData(alarmInList al, int position)
        {
            this.date.setText(al.getDate());
            this.time.setText(al.getTime());
            this.position = position;
            this.current = al;

        }

        public void setListners()
        {
            delete.setOnClickListener(viewHolder.this);
        }

        @Override
        public void onClick(View v)
        {
            Log.v("POSITION: ", String.valueOf(position));
            removeItem(position);
        }
    }
}
