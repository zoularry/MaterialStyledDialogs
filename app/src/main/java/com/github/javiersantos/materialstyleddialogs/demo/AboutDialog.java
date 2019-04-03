package com.github.javiersantos.materialstyleddialogs.demo;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;

import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class AboutDialog extends DialogFragment {

    public static void show(AppCompatActivity context) {
        AboutDialog dialog = new AboutDialog();
        dialog.show(context.getSupportFragmentManager(), "[ABOUT_DIALOG]");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new MaterialStyledDialog.Builder(getActivity())
                .setIcon(new IconicsDrawable(getActivity()).icon(MaterialDesignIconic.Icon.gmi_comment_alt).color(Color.WHITE))
                .setTitle(R.string.app_name)
                .setDescription(R.string.app_description)
                .setPositiveText(android.R.string.ok)
                .build();
    }

}
