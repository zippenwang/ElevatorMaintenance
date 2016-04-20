package wzp.project.android.elvtmtn.entity;

import java.io.Serializable;

public class Employee {
	
	private Long id;				// 员工id
	private String username;		// 登录用户名
	private String name;			// 姓名
	private String password;		// 登录密码
	private String phone;			// 联系方式
	private Group group;			// 所在小组
	
	
	public Employee() {}

	public Employee(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
