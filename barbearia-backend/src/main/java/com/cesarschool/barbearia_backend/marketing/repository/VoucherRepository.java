package com.cesarschool.barbearia_backend.marketing.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cesarschool.barbearia_backend.marketing.model.Voucher;

public interface VoucherRepository extends JpaRepository<Voucher, UUID>{

}
