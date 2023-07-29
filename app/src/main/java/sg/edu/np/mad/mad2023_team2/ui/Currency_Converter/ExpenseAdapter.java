package sg.edu.np.mad.mad2023_team2.ui.Currency_Converter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import sg.edu.np.mad.mad2023_team2.R;
import sg.edu.np.mad.mad2023_team2.ui.Transactions.ExpenseModel;
import sg.edu.np.mad.mad2023_team2.ui.Transactions.OnItemsClick;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.MyViewHolder> {
    private Context context;
    private OnItemsClick onItemsClick;
    private List<ExpenseModel> expenseModelList;

    private String Currency_Code;


    private double conversion_Rate;

    public ExpenseAdapter(Context context,OnItemsClick onItemsClick,ArrayList<ExpenseModel> expenseModel) {
        this.context = context;
        this.expenseModelList = expenseModel;
        this.onItemsClick=onItemsClick;
    }
    public void add(ExpenseModel expenseModel){
        expenseModelList.add(expenseModel);
        notifyDataSetChanged();
    }

    public void clear()
    {

        expenseModelList.clear();
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_row,parent,false);
       return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
         ExpenseModel expenseModel=expenseModelList.get(position);
        //get the currency details to be set
        Currency_Code=Get_Currency_Of_App.getcountrycodesharedprefs(context);
        conversion_Rate=Get_Currency_Of_App.getconversionratesharedprefs(context);
         holder.note.setText(expenseModel.getNote());
         holder.category.setText(expenseModel.getCategory());
         holder.amount.setText(String.valueOf(expenseModel.getAmount()*conversion_Rate));
         holder.er_currency_code.setText(Currency_Code);

         holder.itemView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 onItemsClick.onClick(expenseModel);
             }
         });
    }

    @Override
    public int getItemCount() {
        return expenseModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
    private TextView note,category,amount,date,er_currency_code;


     public MyViewHolder(@NonNull View itemView){
         super(itemView);
         note=itemView.findViewById(R.id.note);
         category=itemView.findViewById(R.id.category);
         amount=itemView.findViewById(R.id.amount);
         date=itemView.findViewById(R.id.date);
         er_currency_code=itemView.findViewById(R.id.er_currency_code);
     }




 }
}
