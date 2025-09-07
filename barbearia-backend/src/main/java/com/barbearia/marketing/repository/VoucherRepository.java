package com.barbearia.marketing.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.barbearia.marketing.model.Voucher;

public interface VoucherRepository extends JpaRepository<Voucher, UUID>{

}
