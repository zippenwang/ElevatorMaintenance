package wzp.project.android.elvtmtn.helper.contant;

public interface ProjectContants {

	String basePath = "http://192.168.1.103:8080/ElevatorMaintainSystem/api";		// Web服务器的域名
	
	int ORDER_IS_NULL = 0x50;				// 符合要求的工单不存在
	int ORDER_SHOW_COMPLETE = 0x51;			// 工单已经全部被显示出来了
	int ORDER_SHOW_UNCOMPLETE = 0X52;		// 工单还未全部显示出来
	
	int PAGE_SIZE = 10;
	
}
