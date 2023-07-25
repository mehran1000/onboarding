package com.candidate.mastcheshmi.onboarding.repository;

import com.candidate.mastcheshmi.onboarding.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

}
