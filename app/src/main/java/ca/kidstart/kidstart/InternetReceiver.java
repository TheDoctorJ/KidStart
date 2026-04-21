package ca.kidstart.kidstart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Objects;

import ca.kidstart.kidstart.activity.LoginActivity;

public class InternetReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        if(!Objects.equals(intent.getAction(), Intent.ACTION_AIRPLANE_MODE_CHANGED))
            return;

        boolean isAirplaneModeOn = intent.getBooleanExtra("state", false);

        if(isAirplaneModeOn){
            Intent airplaneOnIntent = new Intent(context, LoginActivity.class);
            airplaneOnIntent.setAction("AIRPLANE_MODE_ON");
            context.startActivity(airplaneOnIntent);
        }else{
            Intent airplaneOffIntent = new Intent(context, LoginActivity.class);
            airplaneOffIntent.setAction("AIRPLANE_MODE_OFF");
            context.startActivity(airplaneOffIntent);
        }
    }
}