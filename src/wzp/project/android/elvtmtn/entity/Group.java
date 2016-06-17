package wzp.project.android.elvtmtn.entity;

import java.util.List;

/**
 * 小组
 * @author Zippen
 *
 */
public class Group {

	private long id;								// 小组id
	private String name;							// 小组名称
	private String description;						// 描述
	private List<Employee> employees;				// 小组中包含的员工集合
	private List<ElevatorRecord> elevatorRecords;	// 小组所负责的电梯集合
	
	
	public Group() {}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	public List<ElevatorRecord> getElevatorRecords() {
		return elevatorRecords;
	}

	public void setElevatorRecords(List<ElevatorRecord> elevatorRecords) {
		this.elevatorRecords = elevatorRecords;
	}
}
