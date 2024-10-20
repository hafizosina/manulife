package com.manulife.id.repository;

import com.manulife.id.model.MasterUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<MasterUser, Long> {
}
