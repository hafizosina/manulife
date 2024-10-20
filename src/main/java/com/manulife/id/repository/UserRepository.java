package com.manulife.id.repository;

import com.manulife.id.model.MasterUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<MasterUser, Long> {


    boolean existsByEmailAndIsDeletedFalse(String email);
    boolean existsByUsernameAndIsDeletedFalse(String username);
    Optional<MasterUser> findByUsernameAndIsDeletedFalse(String username);

    List<MasterUser> findAllByIsDeletedFalse();
    Page<MasterUser> findAllByIsDeletedFalse(Pageable pageable);
}
