package com.kuo.urcoco.model.sql;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.kuo.urcoco.R;
import com.kuo.urcoco.SQLiteManager;
import com.kuo.urcoco.view.OnCreateSQLiteListener;

/**
 * Created by Kuo on 2015/12/14.
 */
public class CreateSQLiteInteractorImpl implements CreateSQLiteInteractor {

    @Override
    public void createSQLite(OnCreateSQLiteListener onCreateSQLiteListener, Context context) {
        onCreateSQLiteListener.message("Creating...", false);
        onCreateMoneyTypeData(context);
        onCreateSQLiteListener.message("Created.", true);
    }

    private void onCreateMoneyTypeData(Context context) {

        SQLiteManager sqLiteManager = new SQLiteManager(context);
        sqLiteManager.onOpen(sqLiteManager.getWritableDatabase());

        String[] typeCostPaths = {"ic_restaurant_menu_white_48dp", "ic_restaurant_menu_white_48dp", "ic_room_service_white_48dp", "ic_local_cafe_white_48dp",
                "ic_local_drink_white_48dp", "ic_shopping_cart_white_48dp", "ic_directions_subway_white_48dp", "ic_card_travel_white_48dp", "ic_hotel_white_48dp", "ic_accessibility_white_48dp",
                "ic_videogame_asset_white_48dp", "ic_content_cut_white_48dp", "ic_local_atm_white_48dp", "ic_card_giftcard_white_48dp", "ic_local_hospital_white_48dp",
                "ic_subtitles_white_48dp", "ic_language_white_48dp", "ic_style_white_48dp"};

        String[] typeIncomePaths = {"ic_account_balance_white_48dp", "ic_language_white_48dp", "ic_local_atm_white_48dp", "ic_style_white_48dp"};

        String[] choloseTexts = {"早餐", "午餐", "晚餐", "下午茶", "飲料", "購物",
                "交通", "旅行", "房租","衣服", "娛樂", "髮型",
                "帳單", "禮物", "醫療", "信用卡", "投資", "其他"};

        String[] chooseIncomeTexts = {"私房錢", "投資", "薪水", "其他"};

        int[] typeColors = {R.color.Red_500, R.color.Pink_500, R.color.Purple_500, R.color.DeepPurple_500,
                R.color.Indigo_500, R.color.Blue_500, R.color.LightBlue_500, R.color.Teal_500,
                R.color.Green_500, R.color.LightGreen_500, R.color.Lime_500, R.color.Yellow_500, R.color.Amber_500,
                R.color.Orange_500, R.color.DeepOrange_500, R.color.Brown_500, R.color.Grey_500, R.color.BlueGrey_500};


        int[] typeDarkColors = {R.color.Red_700, R.color.Pink_700, R.color.Purple_700, R.color.DeepPurple_700,
                R.color.Indigo_700, R.color.Blue_700, R.color.LightBlue_700, R.color.Teal_700,
                R.color.Green_700, R.color.LightGreen_700, R.color.Lime_700, R.color.Yellow_700, R.color.Amber_700,
                R.color.Orange_700, R.color.DeepOrange_700, R.color.Brown_700, R.color.Grey_700, R.color.BlueGrey_700};

        for(int i = 0 ; i < typeCostPaths.length ; i++) {
            sqLiteManager.insterTypeData("expense", choloseTexts[i], "" + typeCostPaths[i], ContextCompat.getColor(context, typeColors[i]), ContextCompat.getColor(context, typeDarkColors[i]));
        }

        for(int i = 0 ; i < typeIncomePaths.length ; i++) {
            sqLiteManager.insterTypeData("income", chooseIncomeTexts[i], "" + typeIncomePaths[i], ContextCompat.getColor(context, typeColors[i]), ContextCompat.getColor(context, typeDarkColors[i]));
        }

        sqLiteManager.close();
    }
}
