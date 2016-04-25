package wzp.project.android.elvtmtn.entity;

import java.io.Serializable;
import java.util.Date;

public class FaultOrder {

	private long id;
	private String no;
	private Date occuredTime;				// 故障发生时间
	private String description;				// 故障描述
	private String reason;					// 故障原因
	private Date receivingTime;				// 接单时间
	private Date signInTime;				// 签到时间
	private Date signOutTime;				// 签退时间
	private String signInAddress;			// 签到地点
	private String signOutAddress;			// 签退地点
	private Boolean fixed = Boolean.FALSE;	// 是否修好
	private String remark;					// 备注
	private Employee employee;				// 维修人员，记录接单的工作人员
	private ElevatorRecord elevatorRecord;	// 电梯档案
	
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Date getOccuredTime() {
		return occuredTime;
	}
	public void setOccuredTime(Date occuredTime) {
		this.occuredTime = occuredTime;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Date getReceivingTime() {
		return receivingTime;
	}
	public void setReceivingTime(Date receivingTime) {
		this.receivingTime = receivingTime;
	}
	public Date getSignInTime() {
		return signInTime;
	}
	public void setSignInTime(Date signInTime) {
		this.signInTime = signInTime;
	}
	public Date getSignOutTime() {
		return signOutTime;
	}
	public void setSignOutTime(Date signOutTime) {
		this.signOutTime = signOutTime;
	}
	public String getSignInAddress() {
		return signInAddress;
	}
	public void setSignInAddress(String signInAddress) {
		this.signInAddress = signInAddress;
	}
	public String getSignOutAddress() {
		return signOutAddress;
	}
	public void setSignOutAddress(String signOutAddress) {
		this.signOutAddress = signOutAddress;
	}
	public Boolean getFixed() {
		return fixed;
	}
	public void setFixed(Boolean fixed) {
		this.fixed = fixed;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	public ElevatorRecord getElevatorRecord() {
		return elevatorRecord;
	}
	public void setElevatorRecord(ElevatorRecord elevatorRecord) {
		this.elevatorRecord = elevatorRecord;
	}
}
