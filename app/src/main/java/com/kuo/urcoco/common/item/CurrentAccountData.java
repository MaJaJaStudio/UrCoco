package com.kuo.urcoco.common.item;

/*
 * Created by Kuo on 2015/11/16.
 */
public class CurrentAccountData {

    private static AccountItem accountItem;

    public static void setAccountItem(AccountItem accountItem) {
        CurrentAccountData.accountItem = accountItem;
    }

    public static AccountItem getAccountItem() {
        return accountItem;
    }

    public static int getRowId() {
        return accountItem.getRowId();
    }

    public static String getAccountName() {
        return  accountItem.getAccountName();
    }

    public static String getMoneyTableName() {
        return  accountItem.getMoneyTableName();
    }

    public static int getBudget() {
        return  accountItem.getBudget();
    }

    public static boolean isNull() {
        if(accountItem != null) {
            return false;
        } else {
            return  true;
        }
    }
}
