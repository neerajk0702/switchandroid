package com.kredivation.switchland.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kredivation.switchland.R;
import com.kredivation.switchland.activity.AppTourActivity;
import com.kredivation.switchland.activity.DashboardActivity;
import com.kredivation.switchland.activity.EditProfileActivity;
import com.kredivation.switchland.activity.HelpActivity;
import com.kredivation.switchland.activity.MyChoicesActivity;
import com.kredivation.switchland.activity.MyHomeActivity;
import com.kredivation.switchland.activity.MyLikedChoicesActivity;
import com.kredivation.switchland.activity.NotificationActivity;
import com.kredivation.switchland.activity.SettingActivity;
import com.kredivation.switchland.model.DrawerItem;

import java.util.List;

public class CustomDrawerAdapter extends ArrayAdapter<DrawerItem> {

    Context context;
    List<DrawerItem> drawerItemList;
    int layoutResID;

    public CustomDrawerAdapter(Context context, int layoutResourceID, List<DrawerItem> listItems) {
        super(context, layoutResourceID, listItems);
        this.context = context;
        this.drawerItemList = listItems;
        this.layoutResID = layoutResourceID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        DrawerItemHolder drawerHolder;
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            drawerHolder = new DrawerItemHolder();

            view = inflater.inflate(layoutResID, parent, false);
            drawerHolder.ItemName = view.findViewById(R.id.drawer_itemName);
            drawerHolder.icon = view.findViewById(R.id.drawer_icon);
            drawerHolder.mainLayout = view.findViewById(R.id.mainLayout);
            drawerHolder.selectedView = view.findViewById(R.id.selectedView);

            view.setTag(drawerHolder);

        } else {
            drawerHolder = (DrawerItemHolder) view.getTag();
        }

        DrawerItem dItem = this.drawerItemList.get(position);

        //  drawerHolder.icon.setImageDrawable(view.getResources().getDrawable(dItem.getImgResID()));
        drawerHolder.ItemName.setText(dItem.getItemName());
        if (dItem.getSelectedItem() == dItem.getId()) {
            drawerHolder.mainLayout.setBackgroundColor(Color.parseColor("#DADADC"));
            drawerHolder.selectedView.setVisibility(View.VISIBLE);
        } else {
            drawerHolder.mainLayout.setBackgroundColor(Color.parseColor("#ffffff"));
            drawerHolder.selectedView.setVisibility(View.GONE);
        }
        drawerHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (DrawerItem item : drawerItemList) {
                    if (item.getId() == dItem.getId()) {
                        item.setSelectedItem(item.getId());
                    } else {
                        item.setSelectedItem(0);
                    }
                }
                itemClick(dItem.getId());
                notifyDataSetChanged();
            }
        });
        return view;
    }

    private void itemClick(int id) {
        if (id == 5) {
            context.startActivity(new Intent(context, NotificationActivity.class));
        } else if (id == 8) {
            context.startActivity(new Intent(context, AppTourActivity.class));
        } else if (id == 9) {
            inviteFriend();
        } else if (id == 10) {
            Intent myintent = new Intent(context, HelpActivity.class);
            context.startActivity(myintent);
        } else if (id == 11) {
            // startActivity(new Intent(DashboardActivity.this, MyProfileFilterActivity.class));
        } else if (id == 12) {

        } else if (id == 1) {
            Intent intent = new Intent(context, DashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else if (id == 2) {
            Intent myintent = new Intent(context, MyHomeActivity.class);
            context.startActivity(myintent);
        } else if (id == 3) {
            Intent choicesintent = new Intent(context, MyChoicesActivity.class);
            context.startActivity(choicesintent);
        } else if (id == 4) {
            Intent likeintent = new Intent(context, MyLikedChoicesActivity.class);
            context.startActivity(likeintent);
        } else if (id == 6) {
            Intent editintent = new Intent(context, EditProfileActivity.class);
            context.startActivity(editintent);
        } else if (id == 7) {
            Intent intent = new Intent(context, SettingActivity.class);
            context.startActivity(intent);
        }
        ((DashboardActivity) context).NavHide();
    }

    private void inviteFriend() {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Switch");
            String strShareMessage = "\nLet me recommend you this application\n\n";
            strShareMessage = strShareMessage + "https://play.google.com/store/apps/details?id=" + context.getPackageName();
            Uri screenshotUri = Uri.parse("android.resource://packagename/drawable/ic_switchland_logo");
            i.setType("image/png");
            i.putExtra(Intent.EXTRA_STREAM, screenshotUri);
            i.putExtra(Intent.EXTRA_TEXT, strShareMessage);
            context.startActivity(Intent.createChooser(i, "Share Via"));
        } catch (Exception e) {
            //e.toString();
        }
    }

    private static class DrawerItemHolder {
        TextView ItemName;
        ImageView icon;
        RelativeLayout mainLayout;
        View selectedView;
    }

}
