package com.example.demo.helper;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.dao.ExpenseRepository;
import com.example.demo.dao.IncomeChangeRepository;
import com.example.demo.dao.SavingsHistoryRepository;
import com.example.demo.entity.SavingsHistory;
import com.example.demo.entity.User;


public class GetSaveablePercentage {
	public double getSaveablePercentageForCurrentMonth(User user, ExpenseRepository expenseRepository,
			IncomeChangeRepository incomeChangeRepository, SavingsHistoryRepository savingsHistoryRepository) {
		String selectedMonth = DateHelper.getCurrentDate();
		SavingsHistory savingsHistory = savingsHistoryRepository.getSavingsForMonth(user.getId(),
				selectedMonth + "-01");
		Double savingsAmount = savingsHistory.getAmount();
		// getting total expense amount and total income amount for the current month
		Double totalExpenseAmtCM = expenseRepository.getTotalExpenseAmountByMonth(user, selectedMonth);
		if (totalExpenseAmtCM == null) {
			totalExpenseAmtCM = 0.0;
		}
		Double totalEarningsCM = incomeChangeRepository.getTotalEarnings(user.getId(), selectedMonth + "-01");
		if (totalEarningsCM == null) {
			totalEarningsCM = 0.0;
		}
		Double saveable = totalEarningsCM - totalExpenseAmtCM;
		Integer saveablePercent;
		if (saveable > savingsAmount) {
			saveablePercent = 100;
			saveable = savingsAmount;
		} else {
			saveablePercent = (int) ((saveable / savingsAmount) * 100);
		}
		return saveablePercent;
	}
}
