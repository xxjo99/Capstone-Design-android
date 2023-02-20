package com.delivery.mydelivery.menu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.delivery.mydelivery.R;

import java.util.List;

@SuppressLint("SetTextI18n")
public class OptionContentAdapter extends RecyclerView.Adapter<OptionContentAdapter.ViewHolder> {

    private final List<OptionContentVO> optionContentList; // 옵션 내용 리스트
    // 최소, 최대 선택 개수
    int minimumSelection;
    int maximumSelection;
    int optionPosition; // 옵션 position
    Context context;

    int selectedCount = 0; // 선택한 개수

    // 생성자
    public OptionContentAdapter(List<OptionContentVO> optionContentList, int minimumSelection, int maximumSelection, int optionPosition, Context context) {
        this.optionContentList = optionContentList;
        this.minimumSelection = minimumSelection;
        this.maximumSelection = maximumSelection;
        this.optionPosition = optionPosition;
        this.context = context;
    }

    // 화면생성
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_menu_option_content, parent, false);
        return new ViewHolder(view);
    }

    // 데이터 셋팅
    @Override
    public void onBindViewHolder(@NonNull OptionContentAdapter.ViewHolder holder, int position) {
        OptionContentVO optionContent = optionContentList.get(position);

        String optionContentName = optionContent.getOptionContentName(); // 옵션내용
        int optionPrice = optionContent.getOptionPrice(); // 옵션 가격

        holder.optionCheckBox.setText(optionContentName);
        holder.optionPriceTV.setText("+" + optionPrice + "원");
        holder.optionCheckBox.setChecked(false);

        checkOption();
        // 옵션 선택 이벤트
        holder.optionCheckBox.setOnClickListener(view -> {
            int price = OptionActivity.menuPrice;
            String optionContentId = Integer.toString(optionContent.getMenuOptionContentId());

            if (((CheckBox) view).isChecked()) { // 옵션 선택

                if (maximumSelection <= selectedCount && maximumSelection != 0) {
                    holder.optionCheckBox.setChecked(false);
                    Toast.makeText(context, "최대 " + maximumSelection + "개 선택 가능합니다.", Toast.LENGTH_SHORT).show();
                } else {
                    price += optionPrice;
                    OptionActivity.modifyPrice(price, optionContentId, 1);
                    selectedCount++;
                }

            } else { // 옵션 선택 해제
                price -= optionPrice;
                OptionActivity.modifyPrice(price, optionContentId, 0);
                selectedCount--;
            }

            if (selectedCount >= minimumSelection) {
                if (maximumSelection == 0) {
                    OptionActivity.selectedOptionFlagList.set(optionPosition, true);
                } else if (selectedCount == maximumSelection) {
                    OptionActivity.selectedOptionFlagList.set(optionPosition, true);
                } else {
                    OptionActivity.selectedOptionFlagList.set(optionPosition, false);
                }
            } else {
                OptionActivity.selectedOptionFlagList.set(optionPosition, false);
            }

            checkOption();
        });
    }

    @Override
    public int getItemCount() {
        if (optionContentList != null) {
            return optionContentList.size();
        } else {
            return -1;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox optionCheckBox;
        TextView optionPriceTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            optionCheckBox = itemView.findViewById(R.id.optionCheckBox);
            optionPriceTV = itemView.findViewById(R.id.optionPrice);
        }
    }

    // 모든 값이 true인지 체크
    public void checkOption() {
        boolean result = true;

        for (Boolean bool : OptionActivity.selectedOptionFlagList) {
            if (!bool) {
                result = false;
                break;
            }
        }

        if (result) {
            OptionActivity.addMenuBtn.setBackgroundResource(R.drawable.btn_fill_mint);
            OptionActivity.addMenuBtn.setEnabled(true);
        } else {
            OptionActivity.addMenuBtn.setBackgroundResource(R.drawable.btn_fill_gray);
            OptionActivity.addMenuBtn.setEnabled(false);
        }


    }
}
