package wzp.project.android.elvtmtn.biz;

import wzp.project.android.elvtmtn.entity.User;

public interface IUserBiz {

	void login(User user, IUserLoginListener listener);
}
