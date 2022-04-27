package com.safi_d.sistemas.safdiscomert.Menu;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.safi_d.sistemas.safdiscomert.AccesoDatos.ArticulosHelper;
import com.safi_d.sistemas.safdiscomert.AccesoDatos.ClientesHelper;
import com.safi_d.sistemas.safdiscomert.AccesoDatos.DataBaseOpenHelper;
import com.safi_d.sistemas.safdiscomert.Auxiliar.variables_publicas;
import com.safi_d.sistemas.safdiscomert.Pedidos.PedidosActivity;
import com.safi_d.sistemas.safdiscomert.AccesoDatos.UsuariosHelper;
import com.safi_d.sistemas.safdiscomert.Principal.MenuActivity;
import com.safi_d.sistemas.safdiscomert.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by usuario on 20/3/2017.
 */

public class PedidosFragment extends Fragment {
    View myView;
    private DataBaseOpenHelper DbOpenHelper;
    private ClientesHelper ClientesH;
    private UsuariosHelper UsuariosH;
    private String TAG = PedidosFragment.class.getSimpleName();
    private String busqueda = "";
    private String tipoBusqueda = "2";
    private ProgressDialog pDialog;
    private ListView lv;
    private TextView lblFooter;
    private EditText txtBusqueda;
    private RadioGroup rgGrupo;
    private Button btnBuscar;
    public static ArrayList<HashMap<String, String>> listaClientes;
    private ArticulosHelper databaseHelper;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        myView= inflater.inflate(R.layout.masterclientes_layout,container,false);
        getActivity().setTitle("Nuevo Pedido");
        lv = (ListView) myView.findViewById(R.id.list);
        registerForContextMenu(lv);
        DbOpenHelper = new DataBaseOpenHelper(getActivity());
        UsuariosH = new UsuariosHelper(DbOpenHelper.database);
        btnBuscar = (Button) myView.findViewById(R.id.btnBuscar);
        lblFooter = (TextView) myView.findViewById(R.id.lblFooter);
        rgGrupo = (RadioGroup) myView.findViewById(R.id.rgGrupo);

        rgGrupo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(group.getCheckedRadioButtonId()== R.id.rbCodigo){
                    txtBusqueda.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
                else {
                    txtBusqueda.setInputType(InputType.TYPE_CLASS_TEXT);
                }
            }
        });
        txtBusqueda = (EditText)myView.findViewById(R.id.txtBusqueda);
        txtBusqueda.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    btnBuscar.performClick();
                }
                return false;
            }
        });
        listaClientes = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= 11) {
            //--post GB use serial executor by default --
            new GetClientesPedidos().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            //--GB uses ThreadPoolExecutor by default--
            new GetClientesPedidos().execute();
        }

        lblFooter.setText("Clientes encontrados: " + String.valueOf(listaClientes.size()));

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(txtBusqueda.getWindowToken(), 0);
                busqueda = txtBusqueda.getText().toString();
                tipoBusqueda = rgGrupo.getCheckedRadioButtonId() == R.id.rbCodigo ? "1" : "2";
                if (Build.VERSION.SDK_INT >= 11) {
                    //--post GB use serial executor by default --
                    new GetClientesPedidos().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    //--GB uses ThreadPoolExecutor by default--
                    new GetClientesPedidos().execute();
                }

                lblFooter.setText("Clientes encontrados: " + String.valueOf(listaClientes.size()));
            }
        });
        // Launching new screen on Selecting Single ListItem
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                variables_publicas.diasCierre = UsuariosH.ObtenerDiasCierre();
                if (variables_publicas.diasCierre != null){
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date fechaActual = new Date();
                    Date fecha1 = new Date();
                    Date fecha2 = new Date();
                    try {
                        fechaActual = dateFormat.parse(variables_publicas.FechaActual);
                        fecha1 = dateFormat.parse(variables_publicas.diasCierre.getFechaInicio());
                        fecha2= dateFormat.parse(variables_publicas.diasCierre.getFechaFin());
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if(fechaActual.after(fecha1) && fechaActual.before(fecha2)){
                        // getting values from selected ListItem
                        String IdCliente = ((TextView) view.findViewById(R.id.IdCliente)).getText().toString();
                        String Nombre = ((TextView) view.findViewById(R.id.Nombre)).getText().toString();
                        // Starting new intent
                        Intent in = new Intent(getActivity().getApplicationContext(), PedidosActivity.class);

                        in.putExtra(variables_publicas.CLIENTES_COLUMN_IdCliente, IdCliente );
                        in.putExtra(variables_publicas.CLIENTES_COLUMN_Nombre, Nombre );
                        in.putExtra(variables_publicas.vVisualizar,"False");
                        startActivity(in);
                    }else{
                        MensajeAviso("No está permitido registrar pedidos en este momento. Se reabre nuevamente los días " + variables_publicas.diasCierre.getDiaInicio() + " a las " + variables_publicas.diasCierre.getHoraInicio()  +" ");
                    }
                }

            }
        });

        if(rgGrupo.getCheckedRadioButtonId()== R.id.rbCodigo){
            txtBusqueda.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        else {
            txtBusqueda.setInputType(InputType.TYPE_CLASS_TEXT);
        }


        return myView;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
//
    private class GetClientesPedidos extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Por favor espere...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

                try {
                    if(getActivity().isFinishing()) return null;
                    DbOpenHelper = new DataBaseOpenHelper(getActivity().getApplicationContext());
                    ClientesH = new ClientesHelper(DbOpenHelper.database);
                    switch (tipoBusqueda){
                        case "1":
                            listaClientes=ClientesH.BuscarClientesCodigo(busqueda);
                            break;
                        case  "2":
                            listaClientes=ClientesH.BuscarClientesNombre(busqueda);
                            break;
                    }
                } catch (final Exception e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    if(getActivity()==null) return null;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
           try{
               // Dismiss the progress dialog
               if (pDialog.isShowing())
                   pDialog.dismiss();
               /**
                * Updating parsed JSON data into ListView
                * */
               ListAdapter adapter = new SimpleAdapter(
                       getActivity(), listaClientes,
                       R.layout.list_cliente, new String[]{variables_publicas.CLIENTES_COLUMN_IdCliente,"CodigoLetra","Ciudad", "Nombre", variables_publicas.CLIENTES_COLUMN_Direccion}, new int[]{R.id.IdCliente,R.id.CodLetra,R.id.Ciudad, R.id.Nombre,
                       R.id.Direccion});
               if(adapter!=null){
                   lv.setAdapter(adapter);
               }
               lblFooter.setText("Cliente Encontrados encontrados: " + String.valueOf(listaClientes.size()));
           }catch (final Exception ex){
               if(getActivity()==null) return ;
               getActivity().runOnUiThread(new Runnable() {
                   @Override
                   public void run() {

                       Toast.makeText(getActivity().getApplicationContext(),
                               "error: " + ex.getMessage(),
                               Toast.LENGTH_LONG)
                               .show();
                   }
               });
           }
        }
    }
    public void MensajeAviso(String texto) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
        dlgAlert.setMessage(texto);
        dlgAlert.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
             /*   if (finalizar) {
                    finish();
                }*/
            }
        });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }
}