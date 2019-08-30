package com.github.javiersantos.materialstyleddialogs;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.afollestad.materialdialogs.internal.MDButton;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieOnCompositionLoadedListener;
import com.github.javiersantos.materialstyleddialogs.enums.Duration;
import com.github.javiersantos.materialstyleddialogs.enums.Style;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.RawRes;
import androidx.annotation.StringRes;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;

public class MaterialStyledDialog extends DialogBase {
    protected final Builder mBuilder;

    public final Builder getBuilder() {
        return mBuilder;
    }

    protected MaterialStyledDialog(Builder builder) {
        super(builder.context, R.style.MD_Dark);
        mBuilder = builder;
        mBuilder.dialog = initMaterialStyledDialog(builder);
    }

    public MaterialDialog getBaseDialog() {
        return mBuilder.dialog;
    }

    public ImageView getTitleIconView() {
        View custom = getBaseDialog().getCustomView();
        if (custom != null) {
            return (ImageView) custom.findViewById(R.id.md_styled_header_pic);
        }
        return null;
    }

    public boolean isShowing() {
        return getBaseDialog().isShowing();
    }

    @UiThread
    public void show() {
        if (mBuilder != null && mBuilder.dialog != null)
            mBuilder.dialog.show();
    }

    @UiThread
    public boolean showIfOkay() {
        try {
            show();
            return true;
        } catch (Throwable e) {
        }
        return false;
    }

    @UiThread
    public void dismiss() {
        if (mBuilder != null && mBuilder.dialog != null)
            mBuilder.dialog.dismiss();
    }

    @UiThread
    private MaterialDialog initMaterialStyledDialog(final Builder builder) {
        final MaterialDialog.Builder dialogBuilder = new MaterialDialog.Builder(builder.context).theme(Theme.LIGHT);

        // Set cancelable
        dialogBuilder.cancelable(builder.isCancelable);

        // Set style
        dialogBuilder.customView(initStyle(builder), false);

        // Set positive button
        if (builder.positive != null && builder.positive.length() != 0)
            dialogBuilder.positiveText(builder.positive);

        if (builder.positiveCallback != null)
            dialogBuilder.onPositive(builder.positiveCallback);

        // set negative button
        if (builder.negative != null && builder.negative.length() != 0)
            dialogBuilder.negativeText(builder.negative);
        if (builder.negativeCallback != null)
            dialogBuilder.onNegative(builder.negativeCallback);

        // Set neutral button
        if (builder.neutral != null && builder.neutral.length() != 0)
            dialogBuilder.neutralText(builder.neutral);
        if (builder.neutralCallback != null)
            dialogBuilder.onNeutral(builder.neutralCallback);

        // Set all buttons color to same color as header
        dialogBuilder.positiveColor(builder.primaryColor);
        dialogBuilder.negativeColor(builder.primaryColor);
        dialogBuilder.neutralColor(builder.primaryColor);

        // Set auto dismiss when touching the buttons
        dialogBuilder.autoDismiss(builder.isAutoDismiss);

        // Build the dialog with the previous configuration
        MaterialDialog materialDialog = dialogBuilder.build();

        if (builder.dialogRoundCorner && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            materialDialog.getView().setBackground(ContextCompat.getDrawable(builder.context, R.drawable.md_dialog_round_corner_bg));
            materialDialog.getView().setClipToOutline(true);
            materialDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        // Set dialog animation and animation duration
        if (builder.isDialogAnimation) {
            if (builder.duration == Duration.NORMAL) {
                materialDialog.getWindow().getAttributes().windowAnimations = R.style.MaterialStyledDialogs_DialogAnimationNormal;
            } else if (builder.duration == Duration.FAST) {
                materialDialog.getWindow().getAttributes().windowAnimations = R.style.MaterialStyledDialogs_DialogAnimationFast;
            } else if (builder.duration == Duration.SLOW) {
                materialDialog.getWindow().getAttributes().windowAnimations = R.style.MaterialStyledDialogs_DialogAnimationSlow;
            }
        }

        if (builder.btnActions != null && builder.btnActions.length > 0) {
            //default use header color
            if (builder.btnColor == -1) builder.btnColor = builder.primaryColor;

//            Drawable unselectedDrawable = ResourcesCompat.getDrawable(builder.context.getResources(), R.drawable.md_btn_color_unselected, null);
//            DrawableCompat.setTint(unselectedDrawable, UtilsLibrary.lighter(builder.btnColor, 0.2f));
//
//            Drawable selectedDrawable = ResourcesCompat.getDrawable(builder.context.getResources(), R.drawable.md_btn_color_selected, null);
//            DrawableCompat.setTint(selectedDrawable, builder.btnColor);

//            StateListDrawable bgSelector = new StateListDrawable();
//            bgSelector.addState(new int[]{android.R.attr.state_focused, android.R.attr.state_selected}, selectedDrawable);
//            bgSelector.addState(new int[]{}, unselectedDrawable);

            for (DialogAction btnAction : builder.btnActions) {
                MDButton btn = materialDialog.getActionButton(btnAction);
                if (btn != null) {
                    Drawable unselectedDrawable = ResourcesCompat.getDrawable(builder.context.getResources(), R.drawable.md_btn_color_unselected, null);
                    // DrawableCompat.wrap() to make <21 Drawable can be a "Tintable" drawable
                    DrawableCompat.setTint(DrawableCompat.wrap(unselectedDrawable), UtilsLibrary.lighter(builder.btnColor, 0.2f));

                    Drawable selectedDrawable = ResourcesCompat.getDrawable(builder.context.getResources(), R.drawable.md_btn_color_selected, null);
                    DrawableCompat.setTint(DrawableCompat.wrap(selectedDrawable), builder.btnColor);

                    StateListDrawable bgSelector = new StateListDrawable();
                    bgSelector.addState(new int[]{android.R.attr.state_focused, android.R.attr.state_selected}, selectedDrawable);
                    bgSelector.addState(new int[]{}, unselectedDrawable);
                    btn.setDefaultSelector(bgSelector); //btns in horizontal mode
                    btn.setStackedSelector(bgSelector); //btrs in stacked mode
                    btn.setTextColor(Color.WHITE);
                }
            }
        }

        return materialDialog;
    }

    @UiThread
    @TargetApi(16)
    private View initStyle(final Builder builder) {
        LayoutInflater inflater = LayoutInflater.from(builder.context);
        View contentView;

        switch (builder.style) {
            case HEADER_WITH_ICON:
                contentView = inflater.inflate(R.layout.style_dialog_header_icon, null);
                break;
            case HEADER_WITH_TITLE:
                contentView = inflater.inflate(R.layout.style_dialog_header_title, null);
                break;
            default:
                contentView = inflater.inflate(R.layout.style_dialog_header_icon, null);
                break;
        }

        // Init Views
        RelativeLayout dialogHeaderColor = (RelativeLayout) contentView.findViewById(R.id.md_styled_header_color);
        AppCompatImageView dialogHeader = (AppCompatImageView) contentView.findViewById(R.id.md_styled_header);
        ImageView dialogPic = contentView.findViewById(R.id.md_styled_header_pic);
        LottieAnimationView dialogLottie = contentView.findViewById(R.id.md_styled_header_lottie);
        TextView dialogTitle = (TextView) contentView.findViewById(R.id.md_styled_dialog_title);
        TextView dialogDescription = (TextView) contentView.findViewById(R.id.md_styled_dialog_description);
        FrameLayout dialogCustomViewGroup = (FrameLayout) contentView.findViewById(R.id.md_styled_dialog_custom_view);
        View dialogDivider = contentView.findViewById(R.id.md_styled_dialog_divider);

        // Set header color or drawable
        if (builder.headerDrawable != null) {
            dialogHeader.setImageDrawable(builder.headerDrawable);
            // Apply gray/darker overlay to the header if enabled
            if (builder.isDarkerOverlay) {
                dialogHeader.setColorFilter(Color.rgb(123, 123, 123), PorterDuff.Mode.MULTIPLY);
            }
        }
        dialogHeaderColor.setBackgroundColor(builder.primaryColor);

        if (builder.headerHeightMultiplier != null && builder.headerHeightMultiplier > 0) {
            ViewGroup.LayoutParams params = dialogHeaderColor.getLayoutParams();
            params.height = (int) (params.height * builder.headerHeightMultiplier);
            dialogHeaderColor.setLayoutParams(params);
        }

        dialogHeader.setScaleType(builder.headerScaleType);

        //Set the custom view
        if (builder.customView != null) {
            dialogCustomViewGroup.addView(builder.customView);
            dialogCustomViewGroup.setPadding(builder.customViewPaddingLeft, builder.customViewPaddingTop, builder.customViewPaddingRight, builder.customViewPaddingBottom);
        }

        // Set header icon
        if (builder.iconDrawable != null) {
            if (builder.style == Style.HEADER_WITH_TITLE) {
                Log.e("MaterialStyledDialog", "You can't set an icon to the HEADER_WITH_TITLE style.");
            } else {
                dialogPic.setImageDrawable(builder.iconDrawable);
            }
        }

        // Set header icon lottie
        if (builder.iconRaw != null) {
            if (builder.style == Style.HEADER_WITH_TITLE) {
                Log.e("MaterialStyledDialog", "You can't set an icon to the HEADER_WITH_TITLE style.");
            } else {
                if (builder.isIconAnimation) {
                    final View animView = dialogPic;
                    dialogLottie.addLottieOnCompositionLoadedListener(new LottieOnCompositionLoadedListener() {
                        @Override
                        public void onCompositionLoaded(LottieComposition composition) {
                            //dialogLottie.playAnimation();
                            //Log.d("#", "load lottie completed. animate icon.");
                            UtilsAnimation.popUp(animView, 0.8f);
                        }
                    });
                }

                dialogLottie.setAnimation(builder.iconRaw);

                //dialogLottie.playAnimation();
//                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) dialogPic.getLayoutParams();
//                lp.height = RelativeLayout.LayoutParams.MATCH_PARENT;
//                lp.width = RelativeLayout.LayoutParams.MATCH_PARENT;
//                dialogPic.setLayoutParams(lp);
            }
        }

        // Set dialog title
        if (builder.title != null && builder.title.length() != 0) {
            dialogTitle.setText(builder.title);
        } else {
            dialogTitle.setVisibility(View.GONE);
        }

        // Set dialog description
        if (builder.description != null && builder.description.length() != 0) {
            dialogDescription.setText(builder.description);

            // Set scrollable
            dialogDescription.setVerticalScrollBarEnabled(builder.isScrollable);
            if (builder.isScrollable) {
                dialogDescription.setMaxLines(builder.maxLines);
                dialogDescription.setMovementMethod(new ScrollingMovementMethod());
            } else {
                dialogDescription.setMaxLines(Integer.MAX_VALUE);
            }
        } else {
            dialogDescription.setVisibility(View.GONE);
        }

        // Set icon animation (only no lottie anim set)
        if (builder.isIconAnimation && builder.iconRaw == null) {
            if (builder.style != Style.HEADER_WITH_TITLE) {
                UtilsAnimation.popUp(dialogPic, 0);
            }
        }

        // Show dialog divider if enabled
        if (builder.isDialogDivider) {
            dialogDivider.setVisibility(View.VISIBLE);
        }

        return contentView;
    }

    public static class Builder implements IBuilder {
        protected Context context;

        // build() and show()
        protected MaterialDialog dialog;

        protected Style style; // setStyle()
        protected Duration duration; // withDialogAnimation()
        protected boolean isIconAnimation, isDialogAnimation, isDialogDivider, isCancelable, isScrollable, isDarkerOverlay, isAutoDismiss; // withIconAnimation(), withDialogAnimation(), withDivider(), setCancelable(), setScrollable(), withDarkerOverlay(), autoDismiss()
        protected Drawable headerDrawable, iconDrawable; // setHeaderDrawable(), setIconDrawable()
        protected Integer iconRaw; //setIconLottieRaw()
        protected Integer primaryColor, maxLines; // setHeaderColor(), setScrollable()
        protected Float headerHeightMultiplier; //setHeaderHeightMultiplier()
        protected CharSequence title, description; // setTitle(), setDescription()
        protected View customView; // setCustomView()
        protected int customViewPaddingLeft, customViewPaddingTop, customViewPaddingRight, customViewPaddingBottom;
        protected AppCompatImageView.ScaleType headerScaleType;

        // .setPositive(), setNegative() and setNeutral()
        protected CharSequence positive, negative, neutral;
        protected int btnColor;
        protected DialogAction[] btnActions;
        protected boolean dialogRoundCorner;

        protected MaterialDialog.SingleButtonCallback positiveCallback, negativeCallback, neutralCallback;

        public Builder(Context context) {
            this.context = context;
            this.style = Style.HEADER_WITH_ICON;
            this.isIconAnimation = true;
            this.isDialogAnimation = false;
            this.isDialogDivider = false;
            this.isDarkerOverlay = false;
            this.duration = Duration.NORMAL;
            this.isCancelable = true;
            this.primaryColor = UtilsLibrary.getPrimaryColor(context);
            this.isScrollable = false;
            this.maxLines = 5;
            this.isAutoDismiss = true;
            this.headerScaleType = AppCompatImageView.ScaleType.CENTER_CROP;
            this.btnActions = null;
            this.btnColor = -1;
            this.dialogRoundCorner = false;
        }

        @Override
        public Builder setCustomView(View customView) {
            this.customView = customView;
            this.customViewPaddingLeft = 0;
            this.customViewPaddingRight = 0;
            this.customViewPaddingTop = 0;
            this.customViewPaddingBottom = 0;
            return this;
        }

        @Override
        public Builder setCustomView(View customView, int left, int top, int right, int bottom) {
            this.customView = customView;
            this.customViewPaddingLeft = UtilsLibrary.dpToPixels(context, left);
            this.customViewPaddingRight = UtilsLibrary.dpToPixels(context, right);
            this.customViewPaddingTop = UtilsLibrary.dpToPixels(context, top);
            this.customViewPaddingBottom = UtilsLibrary.dpToPixels(context, bottom);
            return this;
        }

        @Override
        public Builder setStyle(Style style) {
            this.style = style;
            return this;
        }

        @Override
        public Builder withIconAnimation(Boolean withAnimation) {
            this.isIconAnimation = withAnimation;
            return this;
        }

        @Override
        public Builder withDialogAnimation(Boolean withAnimation) {
            this.isDialogAnimation = withAnimation;
            return this;
        }

        @Override
        public Builder withDialogAnimation(Boolean withAnimation, Duration duration) {
            this.isDialogAnimation = withAnimation;
            this.duration = duration;
            return this;
        }

        @Override
        public Builder withDivider(Boolean withDivider) {
            this.isDialogDivider = withDivider;
            return this;
        }

        @Override
        public Builder withDarkerOverlay(Boolean withDarkerOverlay) {
            this.isDarkerOverlay = withDarkerOverlay;
            return this;
        }

        @Override
        public Builder setIcon(@NonNull Drawable icon) {
            this.iconDrawable = icon;
            return this;
        }

        @Override
        public Builder setIcon(@DrawableRes Integer iconRes) {
            this.iconDrawable = ResourcesCompat.getDrawable(context.getResources(), iconRes, null);
            return this;
        }

        @Override
        public Builder setIconLottieRaw(@RawRes Integer iconRaw) {
            this.iconRaw = iconRaw;
            return this;
        }

        @Override
        public Builder setTitle(@StringRes int titleRes) {
            setTitle(this.context.getString(titleRes));
            return this;
        }

        @Override
        public Builder setTitle(@NonNull CharSequence title) {
            this.title = title;
            return this;
        }

        @Override
        public Builder setDescription(@StringRes int descriptionRes) {
            setDescription(this.context.getString(descriptionRes));
            return this;
        }

        @Override
        public Builder setDescription(@NonNull CharSequence description) {
            this.description = description;
            return this;
        }

        @Override
        public Builder setHeaderColor(@ColorRes int color) {
            this.primaryColor = UtilsLibrary.getColor(context, color);
            return this;
        }

        @Override
        public Builder setHeaderHeightMultiplier(Float multi) {
            this.headerHeightMultiplier = multi;
            return this;
        }

        @Override
        public Builder setHeaderColorInt(@ColorInt int color) {
            this.primaryColor = color;
            return this;
        }

        @Override
        public Builder setHeaderDrawable(@NonNull Drawable drawable) {
            this.headerDrawable = drawable;
            return this;
        }

        @Override
        public Builder setHeaderDrawable(@DrawableRes Integer drawableRes) {
            this.headerDrawable = ResourcesCompat.getDrawable(context.getResources(), drawableRes, null);
            return this;
        }

        @Override
        @Deprecated
        public Builder setPositive(String text, MaterialDialog.SingleButtonCallback callback) {
            this.positive = text;
            this.positiveCallback = callback;
            return this;
        }

        @Override
        public Builder setPositiveText(@StringRes int buttonTextRes) {
            setPositiveText(this.context.getString(buttonTextRes));
            return this;
        }

        @Override
        public Builder setHighlightBtns(DialogAction[] actions, @ColorInt int btnColor) {
            this.btnActions = actions;
            this.btnColor = btnColor;
            return this;
        }

        @Override
        public Builder setHighlightBtns(DialogAction[] actions) {
            this.btnActions = actions;
            return this;
        }

        @Override
        public Builder setHighlightBtn(DialogAction action, @ColorInt int btnColor) {
            this.btnActions = new DialogAction[]{action};
            this.btnColor = btnColor;
            return this;
        }

        @Override
        public Builder setHighlightBtn(DialogAction actions) {
            this.btnActions = new DialogAction[]{actions};
            return this;
        }

        @Override
        public Builder setPositiveText(@NonNull CharSequence buttonText) {
            this.positive = buttonText;
            return this;
        }

        @Override
        public Builder onPositive(@NonNull MaterialDialog.SingleButtonCallback callback) {
            this.positiveCallback = callback;
            return this;
        }

        @Override
        @Deprecated
        public Builder setNegative(String text, MaterialDialog.SingleButtonCallback callback) {
            this.negative = text;
            this.negativeCallback = callback;
            return this;
        }

        @Override
        public Builder setNegativeText(@StringRes int buttonTextRes) {
            setNegativeText(this.context.getString(buttonTextRes));
            return this;
        }

        @Override
        public Builder setNegativeText(@NonNull CharSequence buttonText) {
            this.negative = buttonText;
            return this;
        }

        @Override
        public Builder onNegative(@NonNull MaterialDialog.SingleButtonCallback callback) {
            this.negativeCallback = callback;
            return this;
        }

        @Override
        @Deprecated
        public Builder setNeutral(String text, MaterialDialog.SingleButtonCallback callback) {
            this.neutral = text;
            this.neutralCallback = callback;
            return this;
        }

        @Override
        public Builder setNeutralText(@StringRes int buttonTextRes) {
            setNeutralText(this.context.getString(buttonTextRes));
            return this;
        }

        @Override
        public Builder setDialogRoundCorner(boolean isRound) {
            this.dialogRoundCorner = isRound;
            return this;
        }

        @Override
        public Builder setNeutralText(@NonNull CharSequence buttonText) {
            this.neutral = buttonText;
            return this;
        }

        @Override
        public Builder onNeutral(@NonNull MaterialDialog.SingleButtonCallback callback) {
            this.neutralCallback = callback;
            return this;
        }

        @Override
        public Builder setHeaderScaleType(AppCompatImageView.ScaleType scaleType) {
            this.headerScaleType = scaleType;
            return this;
        }

        @Override
        public Builder setCancelable(Boolean cancelable) {
            this.isCancelable = cancelable;
            return this;
        }

        @Override
        public Builder setScrollable(Boolean scrollable) {
            this.isScrollable = scrollable;
            return this;
        }

        @Override
        public Builder setScrollable(Boolean scrollable, Integer maxLines) {
            this.isScrollable = scrollable;
            this.maxLines = maxLines;
            return this;
        }

        @Override
        public Builder autoDismiss(Boolean dismiss) {
            this.isAutoDismiss = dismiss;
            return this;
        }

        @UiThread
        public MaterialStyledDialog build() {
            return new MaterialStyledDialog(this);
        }

        @UiThread
        public MaterialStyledDialog show() {
            MaterialStyledDialog materialStyledDialog = build();
            materialStyledDialog.show();
            return materialStyledDialog;
        }

    }

}
