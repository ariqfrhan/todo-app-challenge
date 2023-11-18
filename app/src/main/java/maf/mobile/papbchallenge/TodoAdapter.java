package maf.mobile.papbchallenge;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ItemVH> {
    private MainActivity mainActivity;
    private Context context;
    private ArrayList<Todo> todoData;

    public TodoAdapter(Context context, ArrayList<Todo> todoData, MainActivity mainActivity){
        this.context = context;
        this.todoData = todoData;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public TodoAdapter.ItemVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(context)
                .inflate(R.layout.todo_item, parent, false);
        ItemVH viewholder = new ItemVH(card);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull TodoAdapter.ItemVH holder, int position) {
        Todo t = this.todoData.get(position);
        holder.tvTime.setText(t.time);
        holder.tvWhat.setText(t.what);

        holder.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirm Delete");
                builder.setMessage("Do you want to delete this item?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int adapterPosition = holder.getAdapterPosition();
                        if (adapterPosition != RecyclerView.NO_POSITION) {
                            todoData.remove(adapterPosition);
                            notifyItemRemoved(adapterPosition);
                            int jumlah = todoData.size();
                            mainActivity.TaskCount(jumlah);
//                            notifyDataSetChanged();
                            Toast.makeText(context, "Task "+ t.what + " has been deleted", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return todoData.size();
    }

    class ItemVH extends RecyclerView.ViewHolder {
        private TextView tvTime;
        private TextView tvWhat;
        private ImageButton btDelete;

        public ItemVH(@NonNull View itemView) {
            super(itemView);
            this.tvTime = (TextView) itemView.findViewById(R.id.tvTimeA);
            this.tvWhat = (TextView) itemView.findViewById(R.id.tvWhatA);
            this.btDelete = (android.widget.ImageButton) itemView.findViewById(R.id.btDelete);
        }
    }
}

