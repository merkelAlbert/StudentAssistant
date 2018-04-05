package com.assistant.albert.studentassistant.studentassistant.homework;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.CheckBox;

import com.assistant.albert.studentassistant.R;

public class HomeWorkItem {
    public static void onSelected(Activity activity) {
        ConstraintLayout item1 = activity.findViewById(R.id.constraintLayout);
        ConstraintLayout item2 = activity.findViewById(R.id.constraintLayout2);
        ConstraintLayout item3 = activity.findViewById(R.id.constraintLayout3);
        item1.setBackgroundColor(activity.getResources().getColor(R.color.greenItem));
        item2.setBackgroundColor(activity.getResources().getColor(R.color.yellowItem));
        item3.setBackgroundColor(activity.getResources().getColor(R.color.redItem));
        CheckBox checkBox = activity.findViewById(R.id.checkBox);
        CheckBox checkBox2 = activity.findViewById(R.id.checkBox2);
        CheckBox checkBox3 = activity.findViewById(R.id.checkBox3);
        HomeWorkItem.handleClick(activity, checkBox, item1,
                ((ColorDrawable) item1.getBackground()).getColor());

        HomeWorkItem.handleClick(activity, checkBox2, item2,
                ((ColorDrawable) item2.getBackground()).getColor());
        HomeWorkItem.handleClick(activity, checkBox3, item3,
                ((ColorDrawable) item3.getBackground()).getColor());
    }

    public static void handleClick(final Activity activity, final CheckBox checkBox, final ConstraintLayout constraintLayout, final int currentColor) {
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) checkBox).isChecked()) {
                    constraintLayout.setBackgroundColor(activity.getResources().getColor(R.color.selectedItem));
                } else {
                    constraintLayout.setBackgroundColor(currentColor);
                }
            }
        });
    }
}
