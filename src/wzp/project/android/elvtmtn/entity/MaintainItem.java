package wzp.project.android.elvtmtn.entity;

/**
 * 保养类型所包含的保养项目
 * @author Zippen
 *
 */
public class MaintainItem {

	private long id;						// 维保项目id
	private String name;					// 单个Item的名称
	private MaintainType maintainType;		// 对应的维保类型
	
	
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
	public MaintainType getMaintainType() {
		return maintainType;
	}
	public void setMaintainType(MaintainType maintainType) {
		this.maintainType = maintainType;
	}
}
