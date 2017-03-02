package es.esy.AlarmApp;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by me on 2/11/17.
 */
 
public class AdapterAlarme extends ArrayAdapter<Alarme> {

    Context context;
    int layoutResourceId;
    ArrayList<Alarme> alarmes = new ArrayList<Alarme>();
    PendingIntent pendingIntent;

    public AdapterAlarme(Context context, int layoutResourceId,
                          ArrayList<Alarme> alrms) {
        super(context, layoutResourceId, alrms);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.alarmes = alrms;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View item = convertView;
        AlarmeWrapper AlarmeWrapper = null;
        

        if (item == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            item = inflater.inflate(layoutResourceId, parent, false);
            AlarmeWrapper = new AlarmeWrapper();
            AlarmeWrapper.timeAdd = (TextView) item.findViewById(R.id.timeAdd);
            AlarmeWrapper.dateAdd = (TextView) item.findViewById(R.id.dateAdd);
            AlarmeWrapper.etatAdd = (Button) item.findViewById(R.id.etatAdd);
            AlarmeWrapper.totalActif = (TextView) item.findViewById(R.id.totalActif);
            AlarmeWrapper.totalSave = (TextView) item.findViewById(R.id.totalSave);
            //AlarmeWrapper.edit = (Button) item.findViewById(R.id.btnEdit);
            AlarmeWrapper.delete = (Button) item.findViewById(R.id.btnDelete);
            item.setTag(AlarmeWrapper);
        } else {
            AlarmeWrapper = (AlarmeWrapper) item.getTag();
        }

        final Alarme alarme = alarmes.get(position);
        AlarmeWrapper.timeAdd.setText(alarme.getTime());
        AlarmeWrapper.dateAdd.setText(alarme.getDate());
        AlarmeWrapper.etatAdd.setText(alarme.getEtatAdd());

       /* AlarmeWrapper.edit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Edit", Toast.LENGTH_LONG).show();
            }
        }); */

        final AdapterAlarme.AlarmeWrapper finalAlarmeWrapper = AlarmeWrapper;
        final AdapterAlarme.AlarmeWrapper finalAlarmeWrapper1 = AlarmeWrapper;
        AlarmeWrapper.etatAdd.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(finalAlarmeWrapper1.etatAdd.getText()=="ON"){
                    alarme.setEtatAdd("OFF");
                    finalAlarmeWrapper.etatAdd.setText(alarme.getEtatAdd());
                    Toast.makeText(context, "Enregistrer nouveau Etat: "+alarme.getTime()+" \n Date: "+alarme.getDate()+"\n Etat: "+alarme.getEtatAdd()+"/\n position: "+alarmes.get(position), Toast.LENGTH_LONG).show();
                }else if(finalAlarmeWrapper1.etatAdd.getText()=="OFF"){
                    alarme.setEtatAdd("ON");
                    finalAlarmeWrapper.etatAdd.setText(alarme.getEtatAdd());
                    Toast.makeText(context, "Enregistrer nouveau Etat: "+alarme.getTime()+" \n Date: "+alarme.getDate()+"\n Etat: "+alarme.getEtatAdd()+"/\n position: "+alarmes.get(position), Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(context, "Enregistrer nouveau Etat", Toast.LENGTH_LONG).show();
                }
            }
        });

        AlarmeWrapper.delete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(context, "Delete Time: "+alarme.getTime()+" \n Date: "+alarme.getDate()+"\n Etat: "+alarme.getEtatAdd()+"/\n position: "+alarmes.get(position), Toast.LENGTH_LONG).show();
                alarmes.remove(alarmes.get(position));
                notifyDataSetChanged();
            }
        });

        return item;

    }

    static class AlarmeWrapper {
        TextView timeAdd;
        TextView dateAdd;
        TextView totalActif;
        TextView totalSave;
        Button etatAdd;
        //Button edit;
        Button delete;
    }

}
