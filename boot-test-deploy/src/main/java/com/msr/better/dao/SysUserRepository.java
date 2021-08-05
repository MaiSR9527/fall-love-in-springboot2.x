package com.msr.better.dao;

import com.msr.better.domain.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-08-05 18:45:38
 */
@Repository
public interface SysUserRepository extends JpaRepository<SysUser, Long> {
}
