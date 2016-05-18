package wzp.project.android.elvtmtn.entity;

import java.io.Serializable;
import java.util.Date;

public class ElevatorRecord {
	
	private long id;						// 电梯id
	private String no;						// 电梯编号
	private String address;					// 设备地址
	private String unit;					// 使用单位
	private String buildingNumber;			// 所在楼号
	private String elevatorNumger;			// 电梯中文编号
	private String type;					// 电梯类型
	private String modelNumber;				// 型号
	private Integer maxWeight;				// 载重
	private Float speed;					// 速度
	private Date manufacturingDate;			// 生产日期
	private Date lastMaintainTime;			// 上次维护时间
	private String phone;					// 单位联系电话
	private Group group;					// 维保小组
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getBuildingNumber() {
		return buildingNumber;
	}
	public void setBuildingNumber(String buildingNumber) {
		this.buildingNumber = buildingNumber;
	}
	public String getElevatorNumger() {
		return elevatorNumger;
	}
	public void setElevatorNumger(String elevatorNumger) {
		this.elevatorNumger = elevatorNumger;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getModelNumber() {
		return modelNumber;
	}
	public void setModelNumber(String modelNumber) {
		this.modelNumber = modelNumber;
	}
	public Integer getMaxWeight() {
		return maxWeight;
	}
	public void setMaxWeight(Integer maxWeight) {
		this.maxWeight = maxWeight;
	}
	public Float getSpeed() {
		return speed;
	}
	public void setSpeed(Float speed) {
		this.speed = speed;
	}
	public Date getManufacturingDate() {
		return manufacturingDate;
	}
	public void setManufacturingDate(Date manufacturingDate) {
		this.manufacturingDate = manufacturingDate;
	}
	public Date getLastMaintainTime() {
		return lastMaintainTime;
	}
	public void setLastMaintainTime(Date lastMaintainTime) {
		this.lastMaintainTime = lastMaintainTime;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Group getGroup() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group;
	}
}
