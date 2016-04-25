package wzp.project.android.elvtmtn.entity;

import java.util.Date;

public class MaintainOrder {
	
	private long id;							// 工单id
	private String no;
	private Date finalTime;						// 截止日期
	private Date receivingTime;					// 接单时间
	private Date signInTime;					// 签到时间
	private Date signOutTime;					// 签退时间
	private String signInAddress;				// 签到地址
	private String signOutAddress;				// 签退地址
	private Boolean finished=Boolean.FALSE;		// 是否完成
	private String remark;						// 备注
	private Employee employee;					// 保养人员
	private Group group;						// 保养小组
	private ElevatorRecord elevatorRecord;		// 电梯档案
	private MaintainType maintainType;			// 维保类型
	
	
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
	public Date getFinalTime() {
		return finalTime;
	}
	public void setFinalTime(Date finalTime) {
		this.finalTime = finalTime;
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
	public Boolean getFinished() {
		return finished;
	}
	public void setFinished(Boolean finished) {
		this.finished = finished;
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
	public Group getGroup() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group;
	}
	public ElevatorRecord getElevatorRecord() {
		return elevatorRecord;
	}
	public void setElevatorRecord(ElevatorRecord elevatorRecord) {
		this.elevatorRecord = elevatorRecord;
	}
	public MaintainType getMaintainType() {
		return maintainType;
	}
	public void setMaintainType(MaintainType maintainType) {
		this.maintainType = maintainType;
	}
}
