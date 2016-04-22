package wzp.project.android.elvtmtn.entity;

import java.util.List;

public class MaintainType {

	private long id;
	private String name;
	private List<MaintainItem> maintainItems;
	
	
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
