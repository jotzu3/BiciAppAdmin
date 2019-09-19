package app.josueburbano.com.biciapp_admin.datos;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;

import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import app.josueburbano.com.biciapp_admin.R;
import app.josueburbano.com.biciapp_admin.datos.modelos.Estacion;
import app.josueburbano.com.biciapp_admin.ui.ViewModels.EstacionViewModel;
import app.josueburbano.com.biciapp_admin.ui.ViewModels.EstacionViewModelFactory;

public class MyCustomAdapter<T> extends BaseAdapter implements ListAdapter {
    private List<T> list;
    private Context context;
    private FragmentActivity activity;
    private Class<T> typeKey;
    List<String> list1;
    List<String> list2;
    List<String> list3;
    List<String> list4;

    public void setList(List<T> t){
        this.list = t;
    }
    public void setTypeKey(Class<T> typeKey){
        this.typeKey = typeKey;
    }
    public void prepareLists() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        list1 = new ArrayList<String>(list.size());
        list2 = new ArrayList<String>(list.size());
        list3 = new ArrayList<String>(list.size());
        list4 = new ArrayList<String>(list.size());

        if(typeKey == Estacion.class){
            for (T item : list) {
                list1.add(item.toString());
                Method addInfo1 = Estacion.class.getMethod("addInfo1");
                Method addInfo2 = Estacion.class.getMethod("addInfo2");
                Method getId = Estacion.class.getMethod("getId");
                list2.add((String) addInfo1.invoke(item));
                list3.add((String) getId.invoke(item));
                list4.add((String) addInfo2.invoke(item));
            }
        }
    }

    public <T> MyCustomAdapter(Context context, FragmentActivity activity) {
        this.context = context;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        //return list.get(pos).getId();
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }


    private ImageButton editBtn;
    private ImageButton deleteBtn;
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.cstm_lstview_crud, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        listItemText.setText(String.valueOf(list.get(position)));

        TextView listItemText2 = (TextView)view.findViewById(R.id.list_item_string2);
        listItemText2.setText(String.valueOf(list2.get(position)));

        TextView listItemText3 = (TextView)view.findViewById(R.id.list_item_string3);
        listItemText3.setText(String.valueOf(list4.get(position)));

        //Handle buttons and add onClickListeners
        ImageButton deleteBtn = (ImageButton)view.findViewById(R.id.delete_btn);
        ImageButton editBtn = (ImageButton)view.findViewById(R.id.edit_btn);

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showConfirmationBox("¿Seguro que desea eliminar el item?",context,list3.get(position), position);
                notifyDataSetChanged();
            }
        });
        editBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                notifyDataSetChanged();
            }
        });

        return view;
    }

    public void showConfirmationBox(String messageToShow, final Context context, final String item, final int position) {

        // prepare the alert box
        AlertDialog.Builder alertbox = new AlertDialog.Builder(context);

        // set the message to display
        alertbox.setMessage(messageToShow);

        // set a positive/yes button and create a listener
        alertbox.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(context,
                                "'Yes' button clicked", Toast.LENGTH_SHORT)
                                .show();
                        if (typeKey.equals(Estacion.class)){
                            RemoveEstacion(item, position);
                        }
                    }
                });

        // set a negative/no button and create a listener
        alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {

            // do something when the button is clicked
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(context, "'No' button clicked",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // display box
        alertbox.show();
    }

    private void RemoveEstacion(String idItem, final int position) {
        EstacionViewModel viewModel= ViewModelProviders.of(activity, new EstacionViewModelFactory())
                .get(EstacionViewModel.class);
        viewModel.EliminarEstacion(idItem);
        viewModel.ObservarConfirmacion().observe(activity, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean confirmacion) {
                if(confirmacion != null){
                    if(confirmacion){
                    Toast.makeText(context, "Estación eliminada",
                            Toast.LENGTH_SHORT).show();
                    list.remove(position);
                    list2.remove(position);
                    list3.remove(position);
                    notifyDataSetChanged();

                }
            }
            }
        });


    }
}
