package wzp.project.android.elvtmtn.entity;

import java.util.List;

/**
 * 保养类型
 * @author Zippen
 *
 */
public class MaintainType {

	private long id;								// 保养类型id
	private String name;							// 保养类型名称
	private List<MaintainItem> maintainItems;		// 包含的保养项目
	
	
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
	public List<MaintainItem> getMaintainItems() {
		return maintainItems;
	}
	public void setMaintainItems(List<MaintainItem> maintainItems) {
		this.maintainItems = maintainItems;
	}
}
