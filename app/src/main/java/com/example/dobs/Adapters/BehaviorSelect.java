package com.example.dobs.Adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dobs.Classes.Behavior;
import com.example.dobs.R;

import java.util.List;

public class BehaviorSelect extends ArrayAdapter<Behavior> {
    private static final String TAG = "BehaviorSelectAdapter";

    Context context;
    int layoutResourceId;
    List<Behavior> data = null;

    public BehaviorSelect(Context context, int layoutResourceId, List<Behavior> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        BehaviorHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((AppCompatActivity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new BehaviorHolder();
            holder.txtBehavior = (TextView) row.findViewById(R.id.labelBehavior);
            holder.iconBehavior = (ImageView) row.findViewById(R.id.iconBehavior);
            row.setTag(holder);
        } else {
            holder = (BehaviorHolder) row.getTag();
        }

        Behavior behavior = data.get(position);
        holder.txtBehavior.setText(behavior.name);
        holder.txtBehavior.setTextColor(getContext().getResources().getColor(behavior.color));
        holder.iconBehavior.setImageResource(getBehaviorIcon(behavior.name, holder.iconBehavior));

        return (row);
    }

    private int getBehaviorIcon(String name, ImageView image) {
        switch (name) {
            case "Sleeping in chair":
                image.setColorFilter(0xff00ddff, PorterDuff.Mode.SRC_ATOP);
                return (R.drawable.sleeping_in_chair);
            case "Sleeping in bed":
                image.setColorFilter(0xff008000, PorterDuff.Mode.SRC_ATOP);
                return (R.drawable.sleeping_in_bed);
            case "Awake & calm":
                image.setColorFilter(0xffffff00, PorterDuff.Mode.SRC_ATOP);
                return (R.drawable.awake_and_calm);
            case "Noisy":
                image.setColorFilter(0xffff5500, PorterDuff.Mode.SRC_ATOP);
                return (R.drawable.noisy);
            case "Restless, pacing":
                image.setColorFilter(0xffffC0cb, PorterDuff.Mode.SRC_ATOP);
                return (R.drawable.restless_pacing);
            case "Aggressive-verbal":
                image.setColorFilter(0xffc0c0c0, PorterDuff.Mode.MULTIPLY);
                return (R.drawable.aggressive_verbal);
            case "Aggressive-physical":
                image.setColorFilter(0xffff0000, PorterDuff.Mode.SRC_ATOP);
                return (R.drawable.aggressive_physical);
            default:
                image.setColorFilter(0xffbd96e0, PorterDuff.Mode.SRC_ATOP);
                return (R.drawable.behavior_default);
        }
    }

    static class BehaviorHolder {
        TextView txtBehavior;
        ImageView iconBehavior;
    }
}