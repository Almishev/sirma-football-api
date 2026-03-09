package com.sirma.football_api.service.interfaces;

import com.sirma.football_api.dto.UserSummaryDto;

import java.util.List;

public interface UserService {

    List<UserSummaryDto> findAll();

    void updateRoles(Long id, List<String> roleNames);

    void banUser(Long id);
}
