package sg.edu.np.mad.mad2023_team2.ui.Transactions;

import java.io.Serializable;
import java.util.Date;

public class ExpenseModel implements Serializable {
    private String expenseId;
    private String note;
    private String category;
    private  String type;
    private  long amount;
    private Date time;
    public ExpenseModel()
    {}



    public ExpenseModel(String expenseId, String note, String category, String type, long amount, Date time) {
        this.expenseId = expenseId;
        this.note = note;
        this.category = category;
        this.type = type;
        this.amount = amount;
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(String expenseId) {
        this.expenseId = expenseId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
