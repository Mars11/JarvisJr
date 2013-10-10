package me.arthurtucker.jarvisjr;

import android.app.ActionBar;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import me.arthurtucker.jarvisjr.utils.Global;
import me.arthurtucker.jarvisjr.utils.Prefs;

/**
 * Prove HER wrong.
 */

public class TheActivity extends Activity implements CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "TheActivity";
    private static Switch mActionBarSwitch;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PackageManager pkgMngr = this.getPackageManager();
        assert pkgMngr != null;
        if (pkgMngr.checkSignatures("me.arthurtucker.jarvisjr", "me.arthurtucker.jarvisjrdonationkey") == PackageManager.SIGNATURE_MATCH) {
            Global.hasDonated = true;
        }

        Prefs.startPrefs(this);
        setContentView(R.layout.activity_layout);
        TheFragment.updateSwitches();
        ActionBar mActionBar = getActionBar();
        assert mActionBar != null;
        mActionBar.setDisplayShowTitleEnabled(false);
        /**
         * Making ActonBar Switch
         */
        mActionBarSwitch = new Switch(getBaseContext());

        // setting up switch for actionbar
        final int padding = getResources().getDimensionPixelSize(R.dimen.action_bar_switch_padding);
        mActionBarSwitch.setPadding(0, 0, padding, 0);
        // Jelly Bean and up get custom switch
        if (Build.VERSION.SDK_INT >= 16) {
            mActionBarSwitch.setThumbResource(R.drawable.switch_inner);
            mActionBarSwitch.setTrackResource(R.drawable.switch_track);
        }
        mActionBarSwitch.setSwitchTextAppearance(getBaseContext(), R.style.SwitchTextAppearance);

        // adding switch to actionbar
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionBar.setCustomView(mActionBarSwitch, new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER_VERTICAL | Gravity.END));

        // Checking if controls are enabled
        mActionBarSwitch.setChecked(Global.isSpeechEnabled);
        // Starting Switch listener
        mActionBarSwitch.setOnCheckedChangeListener(this);
        /**
         * Switch made
         */

        if (Global.hasDonated) {
            makeToast("Thanks for donating!", false);
        } else {
            // Create the adView
            adView = new AdView(this, AdSize.SMART_BANNER, "a151f83af4d6472");

            adView.setGravity(Gravity.CENTER_HORIZONTAL);
            LinearLayout layout = (LinearLayout) findViewById(R.id.mainLayout);
            // Add the adView to it
            layout.addView(adView);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!Global.hasDonated) {
            AdRequest request = new AdRequest();
            request.addTestDevice(AdRequest.TEST_EMULATOR);
            request.addTestDevice("738A7A857DB379FD6814583956ADE5A6");    // My Galaxy Nexus
            adView.loadAd(request);
        }
    }

    public void makeToast(String says, Boolean isLong) {
        Toast.makeText(this, says, (isLong ? Toast.LENGTH_LONG : Toast.LENGTH_LONG)).show();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean bool) {
        if (compoundButton.equals(mActionBarSwitch)) {
            Global.isSpeechEnabled = bool;       //updating global var
            Prefs.pushPrefs();            //update prefs
            TheFragment.updateSwitches();   //enable/disable switches
            makeToast((bool ? "Voice enabled" : "Voice disabled"), false);
        } else {
            makeToast("Uh, what other switch was flipped..?", false);
        }
    }


    @Override
    protected void onStop() {
        Prefs.pushPrefs();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Prefs.pushPrefs();
        super.onDestroy();
        if (adView != null) {
            adView.destroy();
        }
    }

}
