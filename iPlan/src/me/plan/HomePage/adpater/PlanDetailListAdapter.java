package me.plan.HomePage.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import me.plan.HomePage.data.element.FeedItem;
import me.plan.R;
import me.plan.comm.Utils;
import me.plan.core.Global;
import me.plan.core.network.BlkNetWorker;

/**
 * Created by tangb4c on 2015/2/8.
 */
public class PlanDetailListAdapter extends ArrayAdapter<FeedItem> {
    protected LayoutInflater mInflater;
    final int layoutId;

    public PlanDetailListAdapter(final Context context, final int resource) {
        super(context, resource);
        layoutId = resource;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        final FeedItem planInfo = getItem(position);
        View view;
        if(convertView == null){
            view = mInflater.inflate(R.layout.plan_detail_item, parent, false);
        }else{
            view = convertView;
        }
        ImageView planCover = (ImageView) view.findViewById(R.id.plan_item_cover);
//        planCover.setImageURI(BlkNetWorker.getImageUrl(planInfo.picurl));
        Global.imageCache().loadRemoteImage(planCover, BlkNetWorker.getImageUrl(planInfo.picurl), R.drawable.cover1);
        //planCover.setImageResource(R.drawable.cover2);
        TextView title = (TextView) view.findViewById(R.id.plan_item_title);
        title.setText(planInfo.content);
        TextView createTime = (TextView) view.findViewById(R.id.plan_item_create_time);
        createTime.setText(Utils.getShortDate((int)planInfo.createTime));
        return view;
    }
}
