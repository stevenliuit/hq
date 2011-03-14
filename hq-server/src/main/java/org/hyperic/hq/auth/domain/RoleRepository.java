package org.hyperic.hq.auth.domain;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);

    List<Role> findBySystem(boolean system, Sort sort);
}
