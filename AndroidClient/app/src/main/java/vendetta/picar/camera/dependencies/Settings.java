// Copyright © 2016 Shawn Baker using the MIT License.
package vendetta.picar.camera.dependencies;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;


public class Settings implements Parcelable
{
	// public constants
	public final static int MIN_TIMEOUT = 100;
	public final static int MAX_TIMEOUT = 5000;
	public final static int DEFAULT_TIMEOUT = 500;

	// local constants
	private final static String TAG = "Settings";

	// instance variables
	public String cameraName;
	public boolean showAllCameras;
	public int scanTimeout;
	public Source rawTcpIpSource;
	public Source rawHttpSource;
	public Source rawMulticastSource;

	//******************************************************************************
	// Settings
	//******************************************************************************
	public Settings()
	{
		initialize();
		//Log.d(TAG, "init: " + toString());
	}

	//******************************************************************************
	// Settings
	//******************************************************************************
	public Settings(Parcel in)
	{
		readFromParcel(in);
		//Log.d(TAG, "parcel: " + toString());
	}

	//******************************************************************************
	// Settings
	//******************************************************************************
	public Settings(Settings settings)
	{
		cameraName = settings.cameraName;
		showAllCameras = settings.showAllCameras;
		scanTimeout = settings.scanTimeout;
		rawTcpIpSource = new Source(settings.rawTcpIpSource);
		rawHttpSource = new Source(settings.rawHttpSource);
		rawMulticastSource = new Source(settings.rawMulticastSource);
		//Log.d(TAG, "settings: " + toString());
	}

	//******************************************************************************
	// Settings
	//******************************************************************************
	public Settings(JSONObject obj)
	{
		try
		{
			cameraName = obj.getString("cameraName");
			showAllCameras = obj.getBoolean("showAllCameras");
			scanTimeout = obj.getInt("scanTimeout");
			rawTcpIpSource = new Source(obj.getJSONObject("rawTcpIpSource"));
			rawHttpSource = new Source(obj.getJSONObject("rawHttpSource"));
			rawMulticastSource = new Source(obj.getJSONObject("rawMulticastSource"));
		}
		catch (JSONException ex)
		{
			initialize();
		}
		//Log.d(TAG, "json: " + toString());
	}

	//******************************************************************************
	// initialize
	//******************************************************************************
	private void initialize()
	{
		cameraName = "Camera";
		showAllCameras = false;
		scanTimeout = DEFAULT_TIMEOUT;

		rawTcpIpSource = new Source(Source.ConnectionType.RawTcpIp, "", 5001);
		rawTcpIpSource.fps = 15;
		rawTcpIpSource.bps = 1000000;

		rawHttpSource = new Source(Source.ConnectionType.RawHttp, "", 8080);
		rawHttpSource.fps = 15;
		rawHttpSource.bps = 1000000;

		rawMulticastSource = new Source(Source.ConnectionType.RawMulticast, "224.0.0.1",
				5001);
		rawMulticastSource.fps = 15;
		rawMulticastSource.bps = 1000000;
	}

	//******************************************************************************
	// writeToParcel
	//******************************************************************************
	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(cameraName);
		dest.writeInt(showAllCameras ? 1 : 0);
		dest.writeInt(scanTimeout);
		dest.writeParcelable(rawTcpIpSource, flags);
		dest.writeParcelable(rawHttpSource, flags);
		dest.writeParcelable(rawMulticastSource, flags);
	}

	//******************************************************************************
	// readFromParcel
	//******************************************************************************
	private void readFromParcel(Parcel in)
	{
		cameraName = in.readString();
		showAllCameras = in.readInt() != 0;
		scanTimeout = in.readInt();
		rawTcpIpSource = in.readParcelable(Source.class.getClassLoader());
		rawHttpSource = in.readParcelable(Source.class.getClassLoader());
		rawMulticastSource = in.readParcelable(Source.class.getClassLoader());
	}

	//******************************************************************************
	// describeContents
	//******************************************************************************
	public int describeContents()
	{
		return 0;
	}

	//******************************************************************************
	// Parcelable.Creator
	//******************************************************************************
	public static final Creator CREATOR = new Creator()
	{
		public Settings createFromParcel(Parcel in)
		{
			return new Settings(in);
		}
		public Settings[] newArray(int size)
		{
			return new Settings[size];
		}
	};

	//******************************************************************************
	// getSource
	//******************************************************************************
	public Source getSource(Source.ConnectionType type)
	{
		switch (type)
		{
			case RawTcpIp:
				return rawTcpIpSource;
			case RawHttp:
				return rawHttpSource;
			case RawMulticast:
				return rawMulticastSource;
		}
		return null;
	}

	//******************************************************************************
	// toString
	//******************************************************************************
	@Override
	public String toString()
	{
		return cameraName + "," + showAllCameras + "," + scanTimeout + "," + rawTcpIpSource.toString() +
				"," + rawHttpSource.toString() + "," + rawMulticastSource.toString();
	}

	//******************************************************************************
	// toJson
	//******************************************************************************
	public JSONObject toJson()
	{
		try
		{
			JSONObject obj = new JSONObject();
			obj.put("cameraName", cameraName);
			obj.put("showAllCameras", showAllCameras);
			obj.put("scanTimeout", scanTimeout);
			obj.put("rawTcpIpSource", rawTcpIpSource.toJson());
			obj.put("rawHttpSource", rawHttpSource.toJson());
			obj.put("rawMulticastSource", rawMulticastSource.toJson());
			return obj;
		}
		catch(JSONException ex)
		{
			ex.printStackTrace();
		}
		return null;
	}
}
