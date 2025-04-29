package com.truecodes.WalletServiceApplication.repository;
import com.truecodes.WalletServiceApplication.model.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TxnHistoryRepository extends JpaRepository<TransactionHistory, Integer> {

}

