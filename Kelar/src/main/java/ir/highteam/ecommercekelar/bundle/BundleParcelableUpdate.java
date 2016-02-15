package ir.highteam.ecommercekelar.bundle;

import android.os.Parcel;
import android.os.Parcelable;

public class BundleParcelableUpdate implements Parcelable {
	
	private String title = "";
	private String urlAddress = "";
	private String fileName = "";
	private int version = 0;
	private int fileSizeByte = 0;
	private String note = "";

    /* everything below here is for implementing Parcelable */

	public int getVersion(){return version;}

	public void setVersion(int version){this.version = version;}

    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrlAddress() {
		return urlAddress;
	}

	public void setUrlAddress(String urlAddress) {
		this.urlAddress = urlAddress;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getFileSizeByte() {
		return fileSizeByte;
	}

	public void setFileSizeByte(int fileSizeByte) {
		this.fileSizeByte = fileSizeByte;
	}

    public void setNote(String note){this.note = note;}

    public String getNote(){return this.note;}

	// 99.9% of the time you can just ignore this
    @Override
    public int describeContents() {
        return 0;
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Creator<BundleParcelableUpdate> CREATOR = new Creator<BundleParcelableUpdate>() {
        public BundleParcelableUpdate createFromParcel(Parcel in) {
            return new BundleParcelableUpdate(in);
        }

        public BundleParcelableUpdate[] newArray(int size) {
            return new BundleParcelableUpdate[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    public BundleParcelableUpdate(Parcel in) {
        title = in.readString();
        urlAddress = in.readString();
        fileName = in.readString();
        fileSizeByte = in.readInt();
		version = in.readInt();
        note = in.readString();
    }
    
    public BundleParcelableUpdate() {}

    // write your object's data to the passed-in Parcel
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(title);
		dest.writeString(urlAddress);
		dest.writeString(fileName);
		dest.writeInt(fileSizeByte);
		dest.writeInt(version);
        dest.writeString(note);
	}
}