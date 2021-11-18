package com.youwu.shouyinxitong.ui.setup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;


import com.youwu.shouyinxitong.R;
import com.youwu.shouyinxitong.app.AppApplication;

import java.util.List;

public class SwitchingTimeListAdapter extends BaseAdapter {
    Context context;
    LayoutInflater mInflater;
    ViewHolder holder;
    List<SwitchingTime> switchingTimes;
    SwitchingTime switchingTime;

    public SwitchingTimeListAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    public void setList(List<SwitchingTime> switchingTimes) {
        this.switchingTimes = switchingTimes;
    }

    // 选中当前选项时，让其他选项不被选中
    public void select(int position) {
        if (!switchingTimes.get(position).isSelected()) {
            switchingTimes.get(position).setSelected(true);
            for (int i = 0; i < switchingTimes.size(); i++) {
                if (i != position) {
                    switchingTimes.get(i).setSelected(false);
                }
            }
        }
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return switchingTimes.size();
    }

    @Override
    public Object getItem(int i) {
        return switchingTimes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.system_model_item_layout, null);
            holder.radioBtn = (RadioButton) convertView
                    .findViewById(R.id.system_model_radiobutton);
            holder.radioBtn.setClickable(false);
            holder.textView = (TextView) convertView
                    .findViewById(R.id.system_model_textview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        switchingTime = (SwitchingTime) getItem(position);
        holder.radioBtn.setChecked(switchingTime.isSelected());
        holder.textView.setText(switchingTime.getTimeName() );
        AppApplication.spUtils.put("switchingTime",switchingTime.getTimeName()+"");
        return convertView;
    }

    class ViewHolder {
        TextView textView;
        RadioButton radioBtn;
    }


}
