package net.media.silk.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;


public class Date {
	public static final String TIME_SERVER = "time-c.nist.gov";
	private static Calendar calendar;
	private static SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:SS");

	public static java.util.Date getCurrentDate() {
		calendar = Calendar.getInstance();
		return calendar.getTime();
	}
	public static java.util.Date getNHourAfterDate(String date, int hours) {		
		calendar = Calendar.getInstance();
		try {
			calendar.setTime(formatter.parse(date));
		} catch (ParseException e) {
		}
		calendar.add(Calendar.HOUR, hours);
		return calendar.getTime();
	}

	private class LocationStateListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			calendar = Calendar.getInstance();
			calendar.setTimeInMillis(location.getTime());
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}
	}
}
