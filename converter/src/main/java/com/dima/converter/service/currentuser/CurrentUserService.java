package com.dima.converter.service.currentuser;

import com.dima.converter.model.CurrentUser;

public interface CurrentUserService {

    boolean canAccessUser(CurrentUser currentUser, Long userId);

}
