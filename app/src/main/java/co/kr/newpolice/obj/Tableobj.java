package co.kr.newpolice.obj;

import android.os.Parcel;
import android.os.Parcelable;



public class Tableobj implements Parcelable{

	private String keyindex ,part, level , problem , solution , detail ,type,flag;
	public static Creator<Tableobj> getCreator() {
		return CREATOR;
	}
	
	public Tableobj(String keyindex, String part, String level, String problem,
			String solution, String detail, String type, String flag) {
		super();
		this.keyindex = keyindex;
		this.part = part;
		this.level = level;
		this.problem = problem;
		this.solution = solution;
		this.detail = detail;
		this.type = type;
		this.flag = flag;
	}

	public String getKeyindex() {
		return keyindex;
	}
	public void setKeyindex(String keyindex) {
		this.keyindex = keyindex;
	}
	public String getPart() {
		return part;
	}
	public void setPart(String part) {
		this.part = part;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getProblem() {
		return problem;
	}
	public void setProblem(String problem) {
		this.problem = problem;
	}
	public String getSolution() {
		return solution;
	}
	public void setSolution(String solution) {
		this.solution = solution;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public Tableobj(Parcel in) {
		readFromParcel(in);
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
		dest.writeString(keyindex);
		dest.writeString(part);
		dest.writeString(level);
		dest.writeString(problem);
		dest.writeString(solution);
		dest.writeString(detail);
		dest.writeString(type);
		dest.writeString(flag);
	}
	private void readFromParcel(Parcel in){
		
		keyindex = in.readString();
		part = in.readString();
		level = in.readString();
		problem = in.readString();
		solution = in.readString();
		detail = in.readString();
		type = in.readString();
		flag = in.readString();
	}
	@SuppressWarnings("rawtypes")
	public static final Creator<Tableobj> CREATOR = new Creator() {
		public Object createFromParcel(Parcel in) {
			return new Tableobj(in);
		}

		public Object[] newArray(int size) {
			return new Tableobj[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
}