package com.safi_d.sistemas.safdiscomert.Auxiliar;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;
import com.safi_d.sistemas.safdiscomert.AccesoDatos.ArticulosHelper;
import com.safi_d.sistemas.safdiscomert.AccesoDatos.CartillasBcDetalleHelper;
import com.safi_d.sistemas.safdiscomert.AccesoDatos.CartillasBcHelper;
import com.safi_d.sistemas.safdiscomert.AccesoDatos.CategoriasClienteHelper;
import com.safi_d.sistemas.safdiscomert.AccesoDatos.ClientesHelper;
import com.safi_d.sistemas.safdiscomert.AccesoDatos.ClientesSucursalHelper;
import com.safi_d.sistemas.safdiscomert.AccesoDatos.ConfiguracionSistemaHelper;
import com.safi_d.sistemas.safdiscomert.AccesoDatos.DataBaseOpenHelper;
import com.safi_d.sistemas.safdiscomert.AccesoDatos.EscalaPreciosHelper;
import com.safi_d.sistemas.safdiscomert.AccesoDatos.FacturasPendientesHelper;
import com.safi_d.sistemas.safdiscomert.AccesoDatos.FormaPagoHelper;
import com.safi_d.sistemas.safdiscomert.AccesoDatos.InformesDetalleHelper;
import com.safi_d.sistemas.safdiscomert.AccesoDatos.InformesHelper;
import com.safi_d.sistemas.safdiscomert.AccesoDatos.PedidosDetalleHelper;
import com.safi_d.sistemas.safdiscomert.AccesoDatos.PedidosHelper;
//import com.safi_d.sistemas.safiapp.AccesoDatos.PreciosHelper;
import com.safi_d.sistemas.safdiscomert.AccesoDatos.PromocionesHelper;
import com.safi_d.sistemas.safdiscomert.AccesoDatos.RutasHelper;
import com.safi_d.sistemas.safdiscomert.AccesoDatos.TPreciosHelper;
import com.safi_d.sistemas.safdiscomert.AccesoDatos.UsuariosHelper;
import com.safi_d.sistemas.safdiscomert.AccesoDatos.VendedoresHelper;
//import com.safi_d.sistemas.safiapp.AccesoDatos.ZonasHelper;
import com.safi_d.sistemas.safdiscomert.Entidades.Cliente;
import com.safi_d.sistemas.safdiscomert.Entidades.EscalaPrecios;
import com.safi_d.sistemas.safdiscomert.Entidades.Vendedor;
import com.safi_d.sistemas.safdiscomert.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import static com.safi_d.sistemas.safdiscomert.Auxiliar.Funciones.Codificar;

/**
 * Created by usuario on 5/5/2017.
 */

public class SincronizarDatos {



    private String urlClientes = variables_publicas.direccionIp + "/ServicioClientes.svc/BuscarClientes";
    private String urlDptoMuniBarrios = variables_publicas.direccionIp + "/ServicioClientes.svc/ObtenerDptoMuniBarrios";
   // private String urlZonas = variables_publicas.direccionIp + "/ServicioClientes.svc/GetZonas";
    private String urlRutas = variables_publicas.direccionIp + "/ServicioClientes.svc/GetRutas/";
    private String urlArticulos2 = variables_publicas.direccionIp + "/ServicioTotalArticulos.svc/BuscarTotalArticulo";
    private String urlArticulos = variables_publicas.direccionIp + "/ServicioTotalArticulos.svc/BuscarTotalArticuloVendedor";
    final String urlVendedores = variables_publicas.direccionIp + "/ServicioPedidos.svc/ListaVendedores/";
    final String urlCartillasBc = variables_publicas.direccionIp + "/ServicioPedidos.svc/GetCartillasBC/";
    final String urlDetalleCartillasBc = variables_publicas.direccionIp + "/ServicioPedidos.svc/GetDetalleCartillasBC/";
    final String urlFormasPago = variables_publicas.direccionIp + "/ServicioPedidos.svc/FormasPago/";
    final String urlGetConfiguraciones = variables_publicas.direccionIp + "/ServicioPedidos.svc/GetConfiguraciones/";
    final String urlGetClienteSucursales = variables_publicas.direccionIp + "/ServicioPedidos.svc/GetClienteSucursales/";
    final String url = variables_publicas.direccionIp + "/ServicioLogin.svc/BuscarUsuario/";
    static final String urlConsultarExistencias = variables_publicas.direccionIp + "/ServicioPedidos.svc/ObtenerInventarioArticulo/";
    final String urlGetFacturasPendientes = variables_publicas.direccionIp + "/ServicioRecibos.svc/SpObtieneFacturasSaldoPendiente/";
    final String urlGetCategorias = variables_publicas.direccionIp + "/ServicioClientes.svc/GetListaCategorias";
    static final String urlPrecios = variables_publicas.direccionIp + "/ServicioPedidos.svc/GetPreciosArticulos/1";
    static final String urlDiasCierre= variables_publicas.direccionIp + "/ServicioLogin.svc/GetDiasCierre/";
    static final String urlDiasCierreNew= variables_publicas.direccionIp + "/ServicioLogin.svc/GetDiasCierreNew/";
    final String urlPromociones = variables_publicas.direccionIp + "/ServicioPedidos.svc/GetPromociones/";
    final String urlEscalaPrecios = variables_publicas.direccionIp + "/ServicioPedidos.svc/GetEscalaPrecios";
    private String TAG = SincronizarDatos.class.getSimpleName();
    private DataBaseOpenHelper DbOpenHelper;
    private ClientesHelper ClientesH;
    private VendedoresHelper VendedoresH;
    private ArticulosHelper ArticulosH;
    private UsuariosHelper UsuariosH;
    private PedidosHelper PedidosH;
    private PedidosDetalleHelper PedidosDetalleH;
    private CartillasBcHelper CartillasBcH;
    //private ZonasHelper ZonasH;
    private RutasHelper RutasH;
    private CategoriasClienteHelper CategoriaH;
    private CartillasBcDetalleHelper CartillasBcDetalleH;
    private FormaPagoHelper FormaPagoH;
    private ConfiguracionSistemaHelper ConfigSistemasH;
    private ClientesSucursalHelper ClientesSucH;
    private InformesHelper InformesH;
    private InformesDetalleHelper InformesDetalleH;
    private FacturasPendientesHelper FacturasPendientesH;
    private PromocionesHelper PromocionesH;
    //private PreciosHelper PreciosH;
    private TPreciosHelper TPreciosH;
    private boolean isOnline=false;
    private EscalaPreciosHelper EscalaPreciosH;

    public SincronizarDatos(DataBaseOpenHelper dbh, ClientesHelper Clientesh,
                            VendedoresHelper Vendedoresh, PromocionesHelper Promocionesh,CartillasBcHelper CatillasBch,
                            CartillasBcDetalleHelper CartillasBcDetalleh, FormaPagoHelper FormaPagoh,
                            ConfiguracionSistemaHelper ConfigSistemah,
                            ClientesSucursalHelper ClientesSuch, ArticulosHelper Articulosh, UsuariosHelper usuariosH,
                            PedidosHelper pedidoH, PedidosDetalleHelper pedidosDetalleH ,TPreciosHelper tpreciosH,RutasHelper rutasH,EscalaPreciosHelper escalaPreciosH) {
        DbOpenHelper = dbh;
        ClientesH = Clientesh;
        VendedoresH = Vendedoresh;
        PromocionesH = Promocionesh;
        CartillasBcH = CatillasBch;
        CartillasBcDetalleH = CartillasBcDetalleh;
        FormaPagoH = FormaPagoh;
        ConfigSistemasH = ConfigSistemah;
        ClientesSucH = ClientesSuch;
        ArticulosH = Articulosh;
        UsuariosH = usuariosH;
        PedidosH = pedidoH;
        PedidosDetalleH = pedidosDetalleH;
       // PreciosH = preciosH;
        RutasH = rutasH;
        TPreciosH = tpreciosH;
        EscalaPreciosH = escalaPreciosH;
    }

    public SincronizarDatos(DataBaseOpenHelper dbh, ClientesHelper Clientesh,
                            VendedoresHelper Vendedoresh, PromocionesHelper Promocionesh, CartillasBcHelper CatillasBch,
                            CartillasBcDetalleHelper CartillasBcDetalleh, FormaPagoHelper FormaPagoh,
                            ConfiguracionSistemaHelper ConfigSistemah,
                            ClientesSucursalHelper ClientesSuch, ArticulosHelper Articulosh, UsuariosHelper usuariosH,
                            PedidosHelper pedidoH, PedidosDetalleHelper pedidosDetalleH, InformesHelper Informesh,
                            InformesDetalleHelper InformesDetalleh,FacturasPendientesHelper FacturasPendientesh,
                            CategoriasClienteHelper categoriasH,TPreciosHelper tpreciosH,RutasHelper rutasH,EscalaPreciosHelper escalaPreciosH) {
        DbOpenHelper = dbh;
        ClientesH = Clientesh;
        VendedoresH = Vendedoresh;
        PromocionesH = Promocionesh;
        CartillasBcH = CatillasBch;
        CartillasBcDetalleH = CartillasBcDetalleh;
        FormaPagoH = FormaPagoh;
        ConfigSistemasH = ConfigSistemah;
        ClientesSucH = ClientesSuch;
        ArticulosH = Articulosh;
        UsuariosH = usuariosH;
        PedidosH = pedidoH;
        PedidosDetalleH = pedidosDetalleH;
        InformesH=Informesh;
        InformesDetalleH=InformesDetalleh;
        FacturasPendientesH=FacturasPendientesh;
        //ZonasH=zonasH;
        //PreciosH = preciosH;
        TPreciosH = tpreciosH;
        CategoriaH=categoriasH;
        RutasH =rutasH;
        EscalaPreciosH = escalaPreciosH;
    }

    public SincronizarDatos(DataBaseOpenHelper dbh , ClientesHelper Clientesh, CategoriasClienteHelper Categoriah,TPreciosHelper tpreciosH,RutasHelper rutasH) {
        DbOpenHelper = dbh;
        ClientesH = Clientesh;
        //Zonash = Zonash;
        CategoriaH = Categoriah;
        //PreciosH=preciosH;
        TPreciosH=tpreciosH;
        RutasH= rutasH;
    }

    public SincronizarDatos(DataBaseOpenHelper dbh, InformesHelper Informessh,InformesDetalleHelper InformesDetallesh ,ClientesHelper Clientesh, FacturasPendientesHelper FacturasPendientesh,RutasHelper rutasH) {
        DbOpenHelper = dbh;
        InformesH = Informessh;
        InformesDetalleH = InformesDetallesh;
        ClientesH = Clientesh;
        FacturasPendientesH = FacturasPendientesh;
        RutasH= rutasH;
    }

    private boolean SincronizarArticulos() throws JSONException {
        HttpHandler shC = new HttpHandler();
        String urlStringC="";
        if (variables_publicas.usuario.getTipo().equalsIgnoreCase("Cliente")){
             urlStringC = urlArticulos2 ;
        }else {
             urlStringC = urlArticulos + "/" + variables_publicas.usuario.getCodigo() + "/1";
        }
        String jsonStrC = shC.makeServiceCall(urlStringC);

        if (jsonStrC == null) {
            isOnline = Funciones.TestServerConectivity();
            if (isOnline) {
                new Funciones().SendMail("Ha ocurrido un error al sincronizar Articulos, Respuesta nula GET", variables_publicas.info + urlStringC, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
            }
            return false;
        }
        //Log.e(TAG, "Response from url: " + jsonStrC);
        DbOpenHelper.database.beginTransaction();

        ArticulosH.EliminaArticulos();
        JSONObject jsonObjC = new JSONObject(jsonStrC);
        // Getting JSON Array node
        JSONArray articulos=null;
        if (variables_publicas.usuario.getTipo().equalsIgnoreCase("Cliente")){
            articulos = jsonObjC.getJSONArray("BuscarTotalArticuloResult");
        }else {
            articulos = jsonObjC.getJSONArray("BuscarTotalArticuloVendedorResult");
        }

        try {
            // looping through All Contacts
            for (int i = 0; i < articulos.length(); i++) {
                JSONObject c = articulos.getJSONObject(i);

                String Codigo = c.getString("CODIGO_ARTICULO");
                String Nombre = c.getString("NOMBRE");
                String COSTO = c.getString("COSTO");
                String UNIDAD = c.getString("UNIDAD");
                String UnidadCaja = c.getString("UnidadCaja");
                String Precio = c.getString("Precio");
                String Precio2 = c.getString("Precio2");
                String Precio3 = c.getString("Precio3");
                String Precio4 = c.getString("Precio4");
                String Precio5 = c.getString("Precio5");
                String Precio6 = c.getString("Precio6");
                String CodUM = c.getString("codUM");
                String PorIVA = c.getString("PorIVA");
                String DESCUENTO_MAXIMO = c.getString("DESCUENTO_MAXIMO");
                String existencia = c.getString("Existencia");
                String UnidadCajaVenta = c.getString("UnidadCajaVenta");
                String UnidadCajaVenta2 = c.getString("UnidadCajaVenta2");
                String UnidadCajaVenta3 = c.getString("UnidadCajaVenta3");
                String IdProveedor = c.getString("IdProveedor");
                String UnidadMinima = c.getString("UnidadMinima");
                ArticulosH.GuardarTotalArticulos(Codigo, Nombre, COSTO, UNIDAD, UnidadCaja, Precio,Precio2,Precio3,Precio4,Precio5,Precio6,CodUM, PorIVA, DESCUENTO_MAXIMO,  existencia, UnidadCajaVenta,UnidadCajaVenta2,UnidadCajaVenta3, IdProveedor,UnidadMinima);
            }
            DbOpenHelper.database.setTransactionSuccessful();
            return true;
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al sincronizar Articulos, Excepcion controlada", variables_publicas.info + ex.getMessage(), "dlunasistemas@gmail.com", variables_publicas.correosErrores);
            return false;
        } finally {
            DbOpenHelper.database.endTransaction();
        }

    }

   private boolean SincronizarTPrecios() throws JSONException {
       HttpHandler shC = new HttpHandler();
       String urlStringC = urlPrecios;
       String jsonStrC = shC.makeServiceCall(urlStringC);

       if (jsonStrC == null) {
           isOnline = Funciones.TestServerConectivity();
           if (isOnline) {
               new Funciones().SendMail("Ha ocurrido un error al sincronizar TiposPrecios, Respuesta nula GET", variables_publicas.info + urlStringC, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
           }

           return false;
       }
       //Log.e(TAG, "Response from url: " + jsonStrC);
       DbOpenHelper.database.beginTransaction();

       TPreciosH.EliminaTPrecios();
       JSONObject jsonObjC = new JSONObject(jsonStrC);
       // Getting JSON Array node
       JSONArray precios = jsonObjC.getJSONArray("GetPreciosArticulosResult");


       try {
           // looping through All Contacts
           for (int i = 0; i < precios.length(); i++) {
               JSONObject c = precios.getJSONObject(i);

               String CodTipoPrecio = c.getString("COD_TIPO_PRECIO");
               String TipoPrecio = c.getString("TIPO_PRECIO");
               TPreciosH.GuardarTPrecios(CodTipoPrecio, TipoPrecio);
           }
           DbOpenHelper.database.setTransactionSuccessful();
           return true;
       } catch (Exception ex) {
           new Funciones().SendMail("Ha ocurrido un error al sincronizar TiposPrecios, Excepcion controlada", variables_publicas.info + ex.getMessage(), "dlunasistemas@gmail.com", variables_publicas.correosErrores);
           return false;
       } finally {
           DbOpenHelper.database.endTransaction();
       }

   }
    private boolean SincronizarCategorias() throws JSONException {
        HttpHandler shC = new HttpHandler();
        String urlStringC = urlGetCategorias;
        String jsonStrC = shC.makeServiceCall(urlStringC);

        if (jsonStrC == null) {
            isOnline = Funciones.TestServerConectivity();
            if (isOnline) {
                new Funciones().SendMail("Ha ocurrido un error al sincronizar lista de Categorias, Respuesta nula GET", variables_publicas.info + urlStringC, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
            }
            return false;
        }
        //Log.e(TAG, "Response from url: " + jsonStrC);
        DbOpenHelper.database.beginTransaction();

        CategoriaH.EliminarCategoria();
        JSONObject jsonObjC = new JSONObject(jsonStrC);
        // Getting JSON Array node
        JSONArray precios = jsonObjC.getJSONArray("GetListaCategoriasResult");


        try {
            // looping through All Contacts
            for (int i = 0; i < precios.length(); i++) {
                JSONObject c = precios.getJSONObject(i);

                String Codigo = c.getString("Cod_Cat");
                String Categoria = c.getString("Categoria");
                CategoriaH.GuardarCategoria(Codigo, Categoria);
            }
            DbOpenHelper.database.setTransactionSuccessful();
            return true;
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al sincronizar lista de Categorias, Excepcion controlada", variables_publicas.info + ex.getMessage(), "dlunasistemas@gmail.com", variables_publicas.correosErrores);
            return false;
        } finally {
            DbOpenHelper.database.endTransaction();
        }

    }
    //Cliente
    public boolean SincronizarClientes() throws JSONException {
        /*******************************CLIENTES******************************/
        //************CLIENTES
        DbOpenHelper.database.beginTransaction();

        ObtenerDptosMuniBarrios();
        String urlStringC="";
        HttpHandler shC = new HttpHandler();
        if (variables_publicas.usuario.getTipo().equals("Supervisor") || variables_publicas.usuario.getTipo().equals("User")){
            urlStringC  = urlClientes + "/" + variables_publicas.usuario.getCodigo() + "/" + variables_publicas.rutacargada + "/" + 4;
        }else if (variables_publicas.usuario.getTipo().equals("Cliente")){
            urlStringC = urlClientes + "/" + variables_publicas.usuario.getCodigo()  + "/" + variables_publicas.usuario.getCodigo() + "/" + 5;
        }else{
            urlStringC = urlClientes + "/" + variables_publicas.rutacargada + "/" + variables_publicas.usuario.getCodigo() + "/" + 3;
        }
        String jsonStrC = shC.makeServiceCall(urlStringC);

        if (jsonStrC == null) {
            isOnline = Funciones.TestServerConectivity();
            if (isOnline) {
                new Funciones().SendMail("Ha ocurrido un error al sincronizar clientes, Respuesta nula GET", variables_publicas.info + urlStringC, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
            }
            return false;
        }
        //Log.e(TAG, "Response from url: " + jsonStrC);

        ClientesH.EliminaClientes();
        JSONObject jsonObjC = new JSONObject(jsonStrC);
        // Getting JSON Array node
        JSONArray clientes = jsonObjC.getJSONArray("BuscarClientesResult");


        try {
            // looping through All Contacts
            for (int i = 0; i < clientes.length(); i++) {
                JSONObject c = clientes.getJSONObject(i);

                String IdCliente = c.getString("IdCliente");
                String Nombre = c.getString("Nombre");
                String FechaCreacion = c.getString("FechaCreacion");
                String Telefono = c.getString("Telefono");
                String Direccion = c.getString("Direccion");
                String IdDepartamento = c.getString("IdDepartamento");
                String IdMunicipio = c.getString("IdMunicipio");
                String Ciudad = c.getString("Ciudad");
                String Ruc = c.getString("Ruc");
                String Cedula = c.getString("Cedula");
                String LimiteCredito = c.getString("LimiteCredito");
                String IdFormaPago = c.getString("IdFormaPago");
                String IdVendedor = c.getString("IdVendedor");
                String Excento = c.getString("Excento");
                String CodigoLetra = c.getString("CodigoLetra");
                String Ruta = c.getString("Ruta");
                String NombreRuta = c.getString("NombreRuta");
                String Frecuencia = c.getString("Frecuencia");
                String PrecioEspecial = c.getString("PrecioEspecial");
                String FechaUltimaCompra = c.getString("FechaUltimaCompra");
                String Tipo = c.getString("Tipo");
                String TipoPrecio = c.getString("TipoPrecio");
                String Descuento = c.getString("Descuento");
                String Empleado = c.getString("Empleado");
                String IdSupervisor = c.getString("IdSupervisor");
                String Empresa = c.getString("EMPRESA");
                String Cod_Zona = c.getString("COD_ZONA");
                String Cod_SubZona = c.getString("COD_SUBZONA");
                String Pais_Id = c.getString("Pais_Id");
                String Pais_Nombre = c.getString("Pais_Nombre");
                String IdTipoNegocio = c.getString("IdTipoNegocio");
                String TipoNegocio = c.getString("TipoNegocio");
                String Barrio = c.getString("Barrio");
                ClientesH.GuardarClientes(IdCliente, Nombre,  FechaCreacion, Telefono, Direccion, IdDepartamento, IdMunicipio,
                        Ciudad, Ruc, Cedula, LimiteCredito, IdFormaPago, IdVendedor, Excento, CodigoLetra, Ruta,NombreRuta,
                        Frecuencia, PrecioEspecial, FechaUltimaCompra, Tipo,TipoPrecio, Descuento, Empleado, IdSupervisor,Empresa,
                        Cod_Zona,Cod_SubZona,Pais_Id,Pais_Nombre,IdTipoNegocio,TipoNegocio,Barrio);
            }
            DbOpenHelper.database.setTransactionSuccessful();
            return true;
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al sincronizar clientes, Excepcion controlada ", variables_publicas.info + ex.getMessage(), "dlunasistemas@gmail.com", variables_publicas.correosErrores);
            return false;
        } finally {
            DbOpenHelper.database.endTransaction();
        }

    }

    //DiasCierre
    public boolean SincronizarDiasCierre() throws JSONException {
        DbOpenHelper.database.beginTransaction();

        String urlStringC="";
        HttpHandler shC = new HttpHandler();

        urlStringC = urlDiasCierreNew ;

        String jsonStrC = shC.makeServiceCall(urlStringC);

        if (jsonStrC == null) {
            isOnline = Funciones.TestServerConectivity();
            if (isOnline) {
                new Funciones().SendMail("Ha ocurrido un error al sincronizar la tabla DiasCierre, Respuesta nula GET", variables_publicas.info + urlStringC, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
            }
            return false;
        }

        UsuariosH.EliminaDiasCierre();
        JSONObject jsonObjC = new JSONObject(jsonStrC);
        JSONArray dias = jsonObjC.getJSONArray("GetDiasCierreNewResult");

        try {
            for (int i = 0; i < dias.length(); i++) {
                JSONObject c = dias.getJSONObject(i);

                String diaInicio = c.getString("DiaInicio");
                String diaFin = c.getString("DiaFin");
                String horaInicio = c.getString("HoraInicio");
                String horaFin = c.getString("HoraFin");
                String fechaInicio = c.getString("FechaInicio");
                String fechaFin = c.getString("FechaFin");
                UsuariosH.GuardarDiasCierre(diaInicio, diaFin,  horaInicio, horaFin,fechaInicio,fechaFin);
            }
            DbOpenHelper.database.setTransactionSuccessful();
            return true;
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al sincronizar la tabla de DiasCierre, Excepcion controlada ", variables_publicas.info + ex.getMessage(), "dlunasistemas@gmail.com", variables_publicas.correosErrores);
            return false;
        } finally {
            DbOpenHelper.database.endTransaction();
        }

    }
    //Vendedor
    public boolean SincronizarVendedores() throws JSONException {
        //************VENDEDORES
        HttpHandler shV = new HttpHandler();
        String urlStringV = urlVendedores;
        String jsonStrV = shV.makeServiceCall(urlStringV);

        if (jsonStrV == null) {
            isOnline = Funciones.TestServerConectivity();
            if (isOnline) {
                new Funciones().SendMail("Ha ocurrido un error: al sincronizar vendedores, Respuesta Nula GET", variables_publicas.info + urlStringV, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
            }
            return false;
        }
        //Log.e(TAG, "Response from url: " + jsonStrC);
        DbOpenHelper.database.beginTransaction();
        VendedoresH.EliminaVendedores();
        JSONObject jsonObjV = new JSONObject(jsonStrV);
        // Getting JSON Array node
        JSONArray vendedores = jsonObjV.getJSONArray("ListaVendedoresResult");


        try {
            // looping through All Contacts
            for (int i = 0; i < vendedores.length(); i++) {
                JSONObject c = vendedores.getJSONObject(i);

                String CODIGO = c.getString("CODIGO");
                String NOMBRE = c.getString("NOMBRE");
                String IDRUTA = c.getString("IDRUTA");
                String RUTA = c.getString("RUTA");
                String EMPRESA = c.getString("EMPRESA");
                String codsuper = c.getString("codsuper");
                String Status = c.getString("Status");
                String Supervisor = c.getString("Supervisor");

                VendedoresH.GuardarTotalVendedores(CODIGO, NOMBRE, IDRUTA, RUTA,EMPRESA, codsuper,Supervisor, Status);
            }
            DbOpenHelper.database.setTransactionSuccessful();
            return true;
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al sincronizar vendedores, Excepcion controlada", variables_publicas.info + urlStringV, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
            return false;
        } finally {
            DbOpenHelper.database.endTransaction();
        }

    }

    //CartillasBc
    public boolean SincronizarCartillasBc() throws JSONException {
        HttpHandler shCartillas = new HttpHandler();
        String urlStringCartillas = urlCartillasBc;
        String jsonStrCartillas = shCartillas.makeServiceCall(urlStringCartillas);

        if (jsonStrCartillas == null) {
            isOnline = Funciones.TestServerConectivity();
            if (isOnline) {
                new Funciones().SendMail("Ha ocurrido un error al sincronicar CartillasBC, Respuesta nula GET", variables_publicas.info + urlStringCartillas, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
            }
            return false;
        }

        DbOpenHelper.database.beginTransaction();
        CartillasBcH.EliminaCartillasBc();
        JSONObject jsonObjCartillas = new JSONObject(jsonStrCartillas);
        // Getting JSON Array node
        JSONArray cartillas = jsonObjCartillas.getJSONArray("GetCartillasBCResult");


        try {
            // looping through All Contacts
            for (int i = 0; i < cartillas.length(); i++) {
                JSONObject c = cartillas.getJSONObject(i);

                String id = c.getString("id");
                String codigo = c.getString("codigo");
                String fechaini = c.getString("fechaini");
                String fechafinal = c.getString("fechafinal");
                String tipo = c.getString("tipo");
                String aprobado = c.getString("aprobado");

                CartillasBcH.GuardarCartillasBc(id, codigo, fechaini, fechafinal, tipo, aprobado);
            }
            DbOpenHelper.database.setTransactionSuccessful();
            return true;
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al sincronicar CartillasBC, Excepcion controlada", variables_publicas.info + ex.getMessage(), "dlunasistemas@gmail.com", variables_publicas.correosErrores);
            return false;
        } finally {
            DbOpenHelper.database.endTransaction();
        }

    }
    public boolean SincronizarPromociones() throws JSONException {
        HttpHandler shPromociones = new HttpHandler();
        String urlStringPromociones= urlPromociones;
        String jsonStrPromociones = shPromociones.makeServiceCall(urlStringPromociones);

        if (jsonStrPromociones == null) {
            new Funciones().SendMail("Ha ocurrido un error al sincronicar CartillasBC, Respuesta nula GET", variables_publicas.info + urlStringPromociones, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
            return false;
        }

        DbOpenHelper.database.beginTransaction();
        PromocionesH.EliminaPromociones();
        JSONObject jsonObjPromociones = new JSONObject(jsonStrPromociones);
        // Getting JSON Array node
        JSONArray promociones = jsonObjPromociones.getJSONArray("GetPromocionesResult");


        try {
            // looping through All Contacts
            for (int i = 0; i < promociones.length(); i++) {
                JSONObject c = promociones.getJSONObject(i);

                String codpromo = c.getString("CodPromo");
                String itemv = c.getString("itemV");
                String cantv = c.getString("cantV");
                String itemb = c.getString("itemB");
                String cantb = c.getString("cantB");

                PromocionesH.GuardarPromociones(codpromo, itemv, cantv, itemb, cantb);
            }
            DbOpenHelper.database.setTransactionSuccessful();
            return true;
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al sincronicar Promociones, Excepcion controlada", variables_publicas.info + ex.getMessage(), "dlunasistemas@gmail.com", variables_publicas.correosErrores);
            return false;
        } finally {
            DbOpenHelper.database.endTransaction();
        }

    }
    private boolean SincronizarEscalaPrecios() throws JSONException {
        HttpHandler shC = new HttpHandler();
        String urlStringC = urlEscalaPrecios;
        String jsonStrC = shC.makeServiceCall(urlStringC);

        if (jsonStrC == null) {
            new Funciones().SendMail("Ha ocurrido un error al sincronizar las Escalas de Precios Combinados, Respuesta nula GET", variables_publicas.info + urlStringC, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
            return false;
        }
        //Log.e(TAG, "Response from url: " + jsonStrC);
        DbOpenHelper.database.beginTransaction();

        EscalaPreciosH.EliminaEscalaPrecios();
        JSONObject jsonObjC = new JSONObject(jsonStrC);
        // Getting JSON Array node
        JSONArray precios = jsonObjC.getJSONArray("GetEscalaPreciosResult");


        try {
            // looping through All Contacts
            for (int i = 0; i < precios.length(); i++) {
                JSONObject c = precios.getJSONObject(i);

                String codEscala = c.getString("CodEscala");
                String lista = c.getString("ListaArticulos");
                String escala1 = c.getString("Escala1");
                String escala2 = c.getString("Escala2");
                String escala3 = c.getString("Escala3");
                String precio1 = c.getString("Precio1");
                String precio2 = c.getString("Precio2");
                String precio3 = c.getString("Precio3");
                EscalaPreciosH.GuardarEscalaPrecios(codEscala,lista,escala1,escala2,escala3,precio1,precio2,precio3);
            }
            DbOpenHelper.database.setTransactionSuccessful();
            return true;
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al sincronizar las Escalas de Precios Combinados, Excepcion controlada", variables_publicas.info + ex.getMessage(), "dlunasistemas@gmail.com", variables_publicas.correosErrores);
            return false;
        } finally {
            DbOpenHelper.database.endTransaction();
        }

    }
    private boolean ObtenerMotivosNoVenta() {

        HttpHandler sh = new HttpHandler();
        String urlString = variables_publicas.direccionIp + "/ServicioClientes.svc/ObtenerMotivosNoVisita";
        String encodeUrl = "";
        try {
            URL Url = new URL(urlString);
            URI uri = new URI(Url.getProtocol(), Url.getUserInfo(), Url.getHost(), Url.getPort(), Url.getPath(), Url.getQuery(), Url.getRef());
            encodeUrl = uri.toURL().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String jsonStr = sh.makeServiceCall(encodeUrl);

        /**********************************BANCOS**************************************/
        if (jsonStr != null) {
            DbOpenHelper.database.beginTransactionNonExclusive();
            try {
                //DbOpenHelper.database.beginTransactionNonExclusive();
                JSONObject jsonObj = new JSONObject(jsonStr);
                // Getting JSON Array node
                JSONArray motivos = jsonObj.getJSONArray("ObtenerMotivosNoVisitaResult");
                if (motivos.length() == 0) {
                    return false;
                }
                ClientesH.EliminarMotivosNoVenta();
                // looping through All Contacts

                for (int i = 0; i < motivos.length(); i++) {
                    JSONObject c = motivos.getJSONObject(i);
                    ClientesH.GuardarMotivosNoVenta(c.get("Codigo").toString(),c.get("Motivo").toString());
                }
                DbOpenHelper.database.setTransactionSuccessful();
                return true;
            } catch (Exception ex) {
                Log.e("Error", ex.getMessage());
                new Funciones().SendMail("Ha ocurrido un error al obtener el listado de Motivos No Venta,Excepcion controlada", variables_publicas.info + ex.getMessage(), "dlunasistemas@gmail.com", variables_publicas.correosErrores);
                return false;
            }  finally {
                DbOpenHelper.database.endTransaction();
            }

        } else {
            new Funciones().SendMail("Ha ocurrido un error al obtener el Listado de Motivos No Venta,Respuesta nula", variables_publicas.info + urlString, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
            return false;
        }
    }
   public boolean SincronizarRutas() throws JSONException {
       HttpHandler shRutas= new HttpHandler();
       String urlStringRutas= urlRutas + variables_publicas.usuario.getCodigo();
       String jsonStrRutas = shRutas.makeServiceCall(urlStringRutas);

       if (jsonStrRutas == null) {
           isOnline = Funciones.TestServerConectivity();
           if (isOnline) {
               new Funciones().SendMail("Ha ocurrido un error al sincronicar las Rutas, Respuesta nula GET", variables_publicas.info + urlStringRutas, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
           }
           return false;
       }

       DbOpenHelper.database.beginTransaction();
       RutasH.EliminaRutas();
       JSONObject jsonObjRutas = new JSONObject(jsonStrRutas);
       // Getting JSON Array node
       JSONArray rutas = jsonObjRutas.getJSONArray("GetRutasResult");


       try {
           // looping through All Contacts
           for (int i = 0; i < rutas.length(); i++) {
               JSONObject c = rutas.getJSONObject(i);

               String idruta = c.getString("IDRUTA");
               String ruta = c.getString("RUTA");
               String vendedor = c.getString("VENDEDOR");

               RutasH.GuardarRutas(idruta, ruta, vendedor);
           }
           DbOpenHelper.database.setTransactionSuccessful();
           return true;
       } catch (Exception ex) {
           new Funciones().SendMail("Ha ocurrido un error al sincronicar las Rutas, Excepcion controlada", variables_publicas.info + ex.getMessage(), "dlunasistemas@gmail.com", variables_publicas.correosErrores);
           return false;
       } finally {
           DbOpenHelper.database.endTransaction();
       }

   }
    //CartillasBcDetalle
    public boolean SincronizarCartillasBcDetalle() throws JSONException {
        HttpHandler shCartillasD = new HttpHandler();
        String urlStringCartillasD = urlDetalleCartillasBc;
        String jsonStrCartillasD = shCartillasD.makeServiceCall(urlStringCartillasD);

        if (jsonStrCartillasD == null) {
            isOnline = Funciones.TestServerConectivity();
            if (isOnline) {
                new Funciones().SendMail("Ha ocurrido un error DetallaCartillaBC", variables_publicas.info + urlStringCartillasD, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
            }
            return false;
        }
        DbOpenHelper.database.beginTransaction();
        CartillasBcDetalleH.EliminaCartillasBcDetalle();
        JSONObject jsonObjCartillasD = new JSONObject(jsonStrCartillasD);
        // Getting JSON Array node
        JSONArray cartillasD = jsonObjCartillasD.getJSONArray("GetDetalleCartillasBCResult");


        try {
            // looping through All Contacts
            for (int i = 0; i < cartillasD.length(); i++) {
                JSONObject c = cartillasD.getJSONObject(i);
                String id = c.getString("id");
                String itemV = c.getString("itemV");
                String descripcionV = c.getString("descripcionV");
                String cantidad = c.getString("cantidad");
                String itemB = c.getString("itemB");
                String descripcionB = c.getString("descripcionB");
                String cantidadB = c.getString("cantidadB");
                String codigo = c.getString("codigo");
                String tipo = c.getString("tipo");
                String activo = c.getString("activo");
                String codUMV = c.getString("CODUMV");
                String codUMB = c.getString("CODUMB");
                String unidadesV = c.getString("unidadesV");
                String unidadesB = c.getString("unidadesB");
                String umb = c.getString("UMB");
                CartillasBcDetalleH.GuardarCartillasBcDetalle(id, itemV, descripcionV, cantidad, itemB, descripcionB, cantidadB, codigo, tipo, activo,codUMV,codUMB,unidadesV,unidadesB,umb);
            }
            DbOpenHelper.database.setTransactionSuccessful();
            return true;
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al sincronizar DetallaCartillaBC, Excepcion controlada", variables_publicas.info + ex.getMessage(), "dlunasistemas@gmail.com", variables_publicas.correosErrores);
            return false;
        } finally {
            DbOpenHelper.database.endTransaction();
        }
    }

    //FormaPago
    public boolean SincronizarFormaPago() throws JSONException {
        HttpHandler shFormaPago = new HttpHandler();
        String urlStringFormaPago = urlFormasPago;
        String jsonStrFormaPago = shFormaPago.makeServiceCall(urlStringFormaPago);

        if (jsonStrFormaPago == null) {
            isOnline = Funciones.TestServerConectivity();
            if (isOnline) {
                new Funciones().SendMail("Ha ocurrido un error al sincronizar Forma de pago, Respuesta nula GET", variables_publicas.info + urlStringFormaPago, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
            }
            return false;
        }
        DbOpenHelper.database.beginTransaction();
        FormaPagoH.EliminaFormaPago();
        JSONObject jsonObjFormaPago = new JSONObject(jsonStrFormaPago);
        // Getting JSON Array node
        JSONArray FormaPago = jsonObjFormaPago.getJSONArray("FormasPagoResult");


        try {
            // looping through All Contacts
            for (int i = 0; i < FormaPago.length(); i++) {
                JSONObject c = FormaPago.getJSONObject(i);

                String CODIGO = c.getString("CODIGO");
                String NOMBRE = c.getString("NOMBRE");
                String DIAS = c.getString("DIAS");
                String EMPRESA = c.getString("EMPRESA");

                FormaPagoH.GuardarTotalFormaPago(CODIGO, NOMBRE, DIAS, EMPRESA);
            }
            DbOpenHelper.database.setTransactionSuccessful();
            return true;
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al sincronizar Forma de pago, Excepcion controlada", variables_publicas.info + ex.getMessage(), "dlunasistemas@gmail.com", variables_publicas.correosErrores);
            return false;
        } finally {
            DbOpenHelper.database.endTransaction();
        }

    }
    //ConfiguracionSistema
    public boolean SincronizarConfiguracionSistema() throws JSONException {
        HttpHandler shConfiguracionSistema = new HttpHandler();
        String urlStringConfiguracionSistema = urlGetConfiguraciones;
        String jsonStrConfiguracionSistema = shConfiguracionSistema.makeServiceCall(urlStringConfiguracionSistema);

        if (jsonStrConfiguracionSistema == null) {
            isOnline = Funciones.TestServerConectivity();
            if (isOnline) {
                new Funciones().SendMail("Ha ocurrido un error al Sincronizar ConfiguracionSistema, Respuesta nula GET", variables_publicas.info + urlStringConfiguracionSistema, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
            }
            return false;
        }

        DbOpenHelper.database.beginTransaction();
        ConfigSistemasH.EliminaConfigSistema();
        JSONObject jsonObjConfiguracionSistema = new JSONObject(jsonStrConfiguracionSistema);
        // Getting JSON Array node
        JSONArray FormaPago = jsonObjConfiguracionSistema.getJSONArray("GetConfiguracionesResult");


        try {
            // looping through All Contacts
            for (int i = 0; i < FormaPago.length(); i++) {
                JSONObject c = FormaPago.getJSONObject(i);

                String Id = c.getString("Id");
                String Sistema = c.getString("Sistema");
                String Configuracion = c.getString("Configuracion");
                String Valor = c.getString("Valor");
                String Activo = c.getString("Activo");

                if (Configuracion.equalsIgnoreCase("VersionDatos")) {
                    variables_publicas.ValorConfigServ = Valor;
                }
                if (Configuracion.equalsIgnoreCase("AplicarPrecioMayoristaXCaja")) {
                    variables_publicas.AplicarPrecioMayoristaXCaja = Valor;
                }
                if (Configuracion.equalsIgnoreCase("PermitirVentaDetAMayoristaXCaja")) {
                    variables_publicas.PermitirVentaDetAMayoristaXCaja = Valor;
                }
                if (Configuracion.equalsIgnoreCase("lstDepartamentosForaneo1")) {
                    variables_publicas.lstDepartamentosForaneo1 = Valor.split(",");
                }


                ConfigSistemasH.GuardarConfiguracionSistema(Id, Sistema, Configuracion, Valor, Activo);
                variables_publicas.Configuracion = ConfigSistemasH.BuscarValorConfig("VersionDatos");
            }
            DbOpenHelper.database.setTransactionSuccessful();
            return true;
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al Sincronizar ConfiguracionSistema, Excepcion controlada", variables_publicas.info + ex.getMessage(), "dlunasistemas@gmail.com", variables_publicas.correosErrores);
            return false;
        } finally {
            DbOpenHelper.database.endTransaction();
        }
    }

    public String ObtenerValorConfigDatos() throws JSONException {
        HttpHandler shConfigSistema = new HttpHandler();
        String urlStringConfigSistema = urlGetConfiguraciones;
        String jsonStrConfiguracionSistema = shConfigSistema.makeServiceCall(urlStringConfigSistema);

        if (jsonStrConfiguracionSistema == null)
            return null;

        JSONObject jsonObjConfiguracionSistema = new JSONObject(jsonStrConfiguracionSistema);
        JSONArray ValorConfig = jsonObjConfiguracionSistema.getJSONArray("GetConfiguracionesResult");

        for (int i = 0; i < ValorConfig.length(); i++) {
            JSONObject c = ValorConfig.getJSONObject(i);
            String Valor = c.getString("Valor");
            String Configuracion = c.getString("Configuracion");
            if (Configuracion == "VersionDatos") {
                variables_publicas.ValorConfigServ = Valor;
            }
            if (Configuracion == "AplicarPrecioMayoristaXCaja") {
                variables_publicas.AplicarPrecioMayoristaXCaja = Valor;
            }
            if (Configuracion == "PermitirVentaDetAMayoristaXCaja") {
                variables_publicas.PermitirVentaDetAMayoristaXCaja = Valor;
            }
            if (Configuracion == "lstDepartamentosForaneo1") {
                variables_publicas.lstDepartamentosForaneo1 = Valor.split(",");
            }
        }
        return jsonStrConfiguracionSistema;
    }

    //ClientesSucursal
    public boolean SincronizarClientesSucursal() throws JSONException {
        HttpHandler shClientesSucursal = new HttpHandler();
        String urlStringClientesSucursal = urlGetClienteSucursales;
        String jsonStrClientesSucursal = shClientesSucursal.makeServiceCall(urlStringClientesSucursal);

        if (jsonStrClientesSucursal == null) {
            isOnline = Funciones.TestServerConectivity();
            if (isOnline) {
                new Funciones().SendMail("Ha ocurrido un error al SincronizarClientesSucursal, Respuesta nula", variables_publicas.info + urlStringClientesSucursal, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
            }
            return false;
        }

        DbOpenHelper.database.beginTransaction();
        ClientesSucH.EliminaClientesSucursales();
        JSONObject jsonObjClientesSucursal = new JSONObject(jsonStrClientesSucursal);
        // Getting JSON Array node
        JSONArray PrecioEspecial = jsonObjClientesSucursal.getJSONArray("GetClienteSucursalesResult");



        try {
            // looping through All Contacts
            for (int i = 0; i < PrecioEspecial.length(); i++) {
                JSONObject c = PrecioEspecial.getJSONObject(i);

                String CodSuc = c.getString("CodSuc");
                String CodCliente = c.getString("CodCliente");
                String Sucursal = c.getString("Sucursal");
                String Ciudad = c.getString("Ciudad");
                String DeptoID = c.getString("DeptoID");
                String Direccion = c.getString("Direccion");
                String FormaPagoID = c.getString("FormaPagoID");
                String Descuento = c.getString("Descuento");

                ClientesSucH.GuardarTotalClientesSucursal(CodSuc, CodCliente, Sucursal, Ciudad, DeptoID, Direccion, FormaPagoID,Descuento);
            }
            DbOpenHelper.database.setTransactionSuccessful();
            return true;
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al SincronizarClientesSucursal,Excepcion controlada", variables_publicas.info + ex.getMessage(), "dlunasistemas@gmail.com", variables_publicas.correosErrores);
            return false;
        } finally {
            DbOpenHelper.database.endTransaction();
        }
    }

    public boolean SincronizarTodo() throws JSONException {

        if (SincronizarDiasCierre()) {
            if (SincronizarArticulos()) {
                 if (SincronizarClientes()) {
                     if (ObtenerMotivosNoVenta()) {
                        if (SincronizarTPrecios()) {
                            if (SincronizarEscalaPrecios()) {
                                if (SincronizarCategorias()) {
                                    if (SincronizarVendedores()) {
                                        if (SincronizarPromociones()) {
                                            if (SincronizarCartillasBc()) {
                                                if (SincronizarCartillasBcDetalle()) {
                                                    if (SincronizarFormaPago()) {
                                                        if (SincronizarRutas()) {
                                                            if (SincronizarClientesSucursal()) {
                                                                if (SincronizarConfiguracionSistema()) {
                                                                    if (ActualizarUsuario()) {
                                                                        SincronizarPedidosLocales();
                                                                        return true;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean SincronizarConsulta() throws JSONException {
        if (SincronizarConfiguracionSistema()) {
             if (ActualizarUsuario()) {
                //SincronizarPedidosLocales();
                    return true;
                }
             }
        return false;
    }
    public void SincronizarTablas() throws JSONException {
        SincronizarDiasCierre();
        SincronizarArticulos();
        SincronizarClientes();
        ObtenerMotivosNoVenta();
        SincronizarRutas();
        SincronizarEscalaPrecios();
        SincronizarPromociones();
        SincronizarCartillasBc();
        SincronizarCartillasBcDetalle();
        SincronizarClientesSucursal();
        SincronizarConfiguracionSistema();
    }

    public void SincronizarTablaClientes() throws JSONException {
        SincronizarClientes();
    }

    public boolean SincronizarPedidosLocales() {

        boolean guardadoOK = true;
        List<HashMap<String, String>> PedidosLocal = PedidosH.ObtenerPedidosLocales(Funciones.GetLocalDateTime(), "");
        for (HashMap<String, String> item : PedidosLocal) {
            if (guardadoOK == false) {
                break;
            }
            Gson gson = new Gson();
            Vendedor vendedor = VendedoresH.ObtenerVendedor(item.get(variables_publicas.PEDIDOS_COLUMN_IdVendedor));
            Cliente cliente = ClientesH.BuscarCliente(item.get(variables_publicas.PEDIDOS_COLUMN_IdCliente));
            String jsonPedido = gson.toJson(PedidosH.ObtenerPedido(item.get(variables_publicas.PEDIDOS_COLUMN_CodigoPedido)));
            guardadoOK = Boolean.parseBoolean(SincronizarDatos.SincronizarPedido(PedidosH, PedidosDetalleH, vendedor, cliente, item.get(variables_publicas.PEDIDOS_COLUMN_CodigoPedido), jsonPedido, false).split(",")[0]);
        }
        return guardadoOK;
    }

    private boolean ActualizarUsuario() {

        HttpHandler sh = new HttpHandler();
        String urlString = url + variables_publicas.usuario.getUsuario() + "/" + Funciones.Codificar(variables_publicas.usuario.getContrasenia());
        String encodeUrl = "";
        try {
            URL Url = new URL(urlString);
            URI uri = new URI(Url.getProtocol(), Url.getUserInfo(), Url.getHost(), Url.getPort(), Url.getPath(), Url.getQuery(), Url.getRef());
            encodeUrl = uri.toURL().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String jsonStr = sh.makeServiceCall(encodeUrl);

        /**********************************USUARIOS**************************************/
        if (jsonStr != null) {

            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                // Getting JSON Array node
                JSONArray Usuarios = jsonObj.getJSONArray("BuscarUsuarioResult");
                if (Usuarios.length() == 0) {
                    return false;
                }
                UsuariosH.EliminaUsuarios();
                // looping through All Contacts

                for (int i = 0; i < Usuarios.length(); i++) {
                    JSONObject c = Usuarios.getJSONObject(i);
                    variables_publicas.CodigoVendedor = c.getString("Codigo");
                    variables_publicas.NombreVendedor = c.getString("Nombre");
                    variables_publicas.UsuarioLogin = c.getString("Usuario");
                    variables_publicas.TipoUsuario = c.getString("Tipo");
                    String Contrasenia = c.getString("Contrasenia");
                    String Tipo = c.getString("Tipo");
                    variables_publicas.RutaCliente = c.getString("Ruta");
                    variables_publicas.Canal = c.getString("Canal");
                    String TasaCambio = c.getString("TasaCambio");
                    String RutaForanea = c.getString("RutaForanea");
                    String FechaActualiza = Funciones.getDatePhone();
                    //String EsVendedor = c.getString("EsVendedor");
                    String Empresa_ID = c.getString("Empresa_ID");
                    UsuariosH.GuardarUsuario(variables_publicas.CodigoVendedor, variables_publicas.NombreVendedor,
                            variables_publicas.UsuarioLogin, Contrasenia, Tipo, variables_publicas.RutaCliente, variables_publicas.Canal, TasaCambio, RutaForanea, FechaActualiza,"",Empresa_ID);

                    variables_publicas.usuario = UsuariosH.BuscarUsuarios(variables_publicas.usuario.getUsuario(), Contrasenia);
                    return true;
                }
            } catch (Exception ex) {
                Log.e("Error", ex.getMessage());
                new Funciones().SendMail("Ha ocurrido un error al obtener los datos del usuario,Excepcion controlada", variables_publicas.info + ex.getMessage(), "dlunasistemas@gmail.com", variables_publicas.correosErrores);
                return false;
            }

        } else {
            isOnline = Funciones.TestServerConectivity();
            if (isOnline) {
                new Funciones().SendMail("Ha ocurrido un error al obtener los datos del usuario,Respuesta nula", variables_publicas.info + urlString, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
            }
            return false;
        }
       return false;
    }

    private boolean ObtenerDptosMuniBarrios() {

        HttpHandler sh = new HttpHandler();
        String urlString = urlDptoMuniBarrios;
        String encodeUrl = "";
        try {
            URL Url = new URL(urlString);
            URI uri = new URI(Url.getProtocol(), Url.getUserInfo(), Url.getHost(), Url.getPort(), Url.getPath(), Url.getQuery(), Url.getRef());
            encodeUrl = uri.toURL().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String jsonStr = sh.makeServiceCall(encodeUrl);

        /**********************************DEPARTAMENTOS**************************************/
        if (jsonStr != null) {

            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                // Getting JSON Array node
                JSONArray dptosMunBarr = jsonObj.getJSONArray("ObtenerDptoMuniBarriosResult");
                if (dptosMunBarr.length() == 0) {
                    return false;
                }
                ClientesH.EliminarDptosMuniBarrios();
                // looping through All Contacts

                for (int i = 0; i < dptosMunBarr.length(); i++) {
                    JSONObject c = dptosMunBarr.getJSONObject(i);
                    ClientesH.GuardarDptosMuniBarrios(c.get("CodDpto").toString(),c.get("Dpto").toString(),c.get("CodMuni").toString(),c.get("Muni").toString());
                }
            } catch (Exception ex) {
                Log.e("Error", ex.getMessage());
                new Funciones().SendMail("Ha ocurrido un error al obtener los Departamentos y Municipios,Excepcion controlada", variables_publicas.info + ex.getMessage(), "dlunasistemas@gmail.com", variables_publicas.correosErrores);
                return false;
            }

        } else {
            isOnline = Funciones.TestServerConectivity();
            if (isOnline) {
                new Funciones().SendMail("Ha ocurrido un error al obtener los Departamentos y Municipios,Respuesta nula", variables_publicas.info + urlString, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
            }
            return false;
        }
        return false;
    }

    public static String SincronizarPedido(PedidosHelper PedidoH, PedidosDetalleHelper PedidoDetalleH, Vendedor vendedor, Cliente cliente, String IdPedido, String jsonPedido, boolean Editar) {

        HttpHandler sh = new HttpHandler();
        String encodeUrl = "";
        Gson gson = new Gson();
        List<HashMap<String, String>> pedidoDetalle = PedidoDetalleH.ObtenerPedidoDetalle(IdPedido);
        for (HashMap<String, String> item : pedidoDetalle) {
            item.put("SubTotal", item.get("SubTotal").replace(",", ""));
            item.put("Costo", item.get("Costo").replace(",", ""));
            item.put("Total", item.get("Total").replace(",", ""));
            item.put("Iva", item.get("Iva").replace(",", ""));
            item.put("Precio", item.get("Precio").replace(",", ""));
            item.put("Descuento", item.get("Descuento").replace(",", ""));
            item.put("Descripcion", Codificar(item.get("Descripcion")));
        }
        String jsonPedidoDetalle = gson.toJson(pedidoDetalle);
        final String urlDetalle = variables_publicas.direccionIp + "/ServicioPedidos.svc/SincronizarPedidoTotalPromoNew/";
        final String urlStringDetalle = urlDetalle + cliente.getIdCliente() + "/" + String.valueOf(Editar) + "/" + vendedor.getCODIGO() + "/" + jsonPedido + "/" + jsonPedidoDetalle+ "/" + cliente.getEmpresa();

        HashMap<String,String> postData = new HashMap<>();
        postData.put("CodigoCliente",cliente.getIdCliente());
        postData.put("Editar",String.valueOf(Editar));
        postData.put("IdVendedor",vendedor.getCODIGO());
        postData.put("pedido",jsonPedido);
        postData.put("Detalle",jsonPedidoDetalle)   ;
        postData.put("Empresa",cliente.getEmpresa())   ;

        String jsonStrPedido = sh.performPostCall(urlDetalle,postData);

      //  String jsonStrPedido = sh.makeServiceCallPost(encodeUrl);
        if (jsonStrPedido == null || jsonPedido.isEmpty()) {
            if (Funciones.TestServerConectivity()) {
                new Funciones().SendMail("Ha ocurrido un error al sincronizar el pedido,Respuesta nula POST", variables_publicas.info + urlStringDetalle, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
            }
            return "false,Ha ocurrido un error al sincronizar el detalle del pedido,Respuesta nula";
        } else {
            try {
                JSONObject result = new JSONObject(jsonStrPedido);
                String resultState = (String) ((String) result.get("SincronizarPedidoTotalPromoNewResult")).split(",")[0];
                String NoPedido = (String) ((String) result.get("SincronizarPedidoTotalPromoNewResult")).split(",")[1];
                if (resultState.equals("false")) {

                    if (NoPedido.equalsIgnoreCase("Pedido ya existe en base de datos")) {
                        NoPedido =  ((String) result.get("SincronizarPedidoTotalPromoNewResult")).split(",")[1];
                    } else {
                        new Funciones().SendMail("Ha ocurrido un error al sincronizar el pedido ,Respuesta false", variables_publicas.info + NoPedido +" *** "+urlStringDetalle, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
                        return "false," + NoPedido;
                    }
                }
                PedidoH.ActualizarPedido(IdPedido, NoPedido);
                PedidoDetalleH.ActualizarCodigoPedido(IdPedido, NoPedido);
                return "true";
            } catch (Exception ex) {
                new Funciones().SendMail("Ha ocurrido un error al sincronizar el pedido, Excepcion controlada ", variables_publicas.info + ex.getMessage(), "dlunasistemas@gmail.com", variables_publicas.correosErrores);
                Log.e("Error", ex.getMessage());
                return "false," + ex.getMessage() + "";
            }

        }

    }

    public static String ConsultarExistencias(final Activity activity, PedidosHelper PedidoH, ArticulosHelper ArticulosH, String CodigoArticulo) {
        HttpHandler sh = new HttpHandler();
        String encodeUrl = "";

        final String urlConsulta = urlConsultarExistencias + CodigoArticulo;

        try {
            URL Url = new URL(urlConsulta);
            URI uri = new URI(Url.getProtocol(), Url.getUserInfo(), Url.getHost(), Url.getPort(), Url.getPath(), Url.getQuery(), Url.getRef());
            encodeUrl = uri.toURL().toString();
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            new Funciones().SendMail("Ha ocurrido un error al obtener las existencias, Codificar URL", variables_publicas.info + e.getMessage(), "dlunasistemas@gmail.com", variables_publicas.correosErrores);
            e.printStackTrace();
            return "N/A";
        }

        String jsonExistencia = sh.makeServiceCall(encodeUrl);
        if (jsonExistencia == null) {
            return "N/A";
        } else {
            try {
                JSONObject result = new JSONObject(jsonExistencia);
                String resultState = (String) ((String) result.get("ObtenerInventarioArticuloResult")).split(",")[0];
                final String existencia = (String) ((String) result.get("ObtenerInventarioArticuloResult")).split(",")[1];
                if (resultState.equals("false")) {

                    new Funciones().SendMail("Ha ocurrido un error al obtener las existencias ,Respuesta false", variables_publicas.info + " --- " + existencia, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
                    return "N/A";
                }
                /*Si no hubo ningun problema procedemos a actualizar las existencias locales*/
                ArticulosH.ActualizarExistencias(CodigoArticulo, existencia);
                return existencia;
            } catch (Exception ex) {
                new Funciones().SendMail("Ha ocurrido un error al obtener las existencias, Excepcion controlada ", variables_publicas.info + ex.getMessage() + " ---json: " + urlConsulta + " ---Response: " + jsonExistencia, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
                Log.e("Error", ex.getMessage());
                return "N/A";
            }

        }

    }

    public static String ConsultarExistencia2(ArticulosHelper ArticulosH, String CodigoArticulo) {
        HttpHandler sh = new HttpHandler();
        String encodeUrl = "";

        final String urlConsulta = urlConsultarExistencias + CodigoArticulo;

        try {
            URL Url = new URL(urlConsulta);
            URI uri = new URI(Url.getProtocol(), Url.getUserInfo(), Url.getHost(), Url.getPort(), Url.getPath(), Url.getQuery(), Url.getRef());
            encodeUrl = uri.toURL().toString();
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            new Funciones().SendMail("Ha ocurrido un error al obtener las existencias, Codificar URL", variables_publicas.info + e.getMessage(), "dlunasistemas@gmail.com", variables_publicas.correosErrores);
            e.printStackTrace();
            return "N/A";
        }

        String jsonExistencia = sh.makeServiceCall(encodeUrl);
        if (jsonExistencia == null) {
            return "0";
        } else {
            try {
                JSONObject result = new JSONObject(jsonExistencia);
                String resultState = (String) ((String) result.get("ObtenerInventarioArticuloResult")).split(",")[0];
                final String existencia = (String) ((String) result.get("ObtenerInventarioArticuloResult")).split(",")[1];
                if (resultState.equals("false")) {

                    new Funciones().SendMail("Ha ocurrido un error al obtener las existencias ,Respuesta false", variables_publicas.info + " --- " + existencia, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
                    return "0";
                }
                /*Si no hubo ningun problema procedemos a actualizar las existencias locales*/
                ArticulosH.ActualizarExistencias(CodigoArticulo, existencia);
                return existencia;
            } catch (Exception ex) {
                new Funciones().SendMail("Ha ocurrido un error al obtener las existencias, Excepcion controlada ", variables_publicas.info + ex.getMessage() + " ---json: " + urlConsulta + " ---Response: " + jsonExistencia, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
                Log.e("Error", ex.getMessage());
                return "0";
            }

        }

    }
    private boolean ObtenerBancos() {

        HttpHandler sh = new HttpHandler();
        String urlString = variables_publicas.direccionIp + "/ServicioRecibos.svc/ObtenerListaBancos";
        String encodeUrl = "";
        try {
            URL Url = new URL(urlString);
            URI uri = new URI(Url.getProtocol(), Url.getUserInfo(), Url.getHost(), Url.getPort(), Url.getPath(), Url.getQuery(), Url.getRef());
            encodeUrl = uri.toURL().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String jsonStr = sh.makeServiceCall(encodeUrl);

        /**********************************BANCOS**************************************/
        if (jsonStr != null) {

            try {
                //DbOpenHelper.database.beginTransaction();
                JSONObject jsonObj = new JSONObject(jsonStr);
                // Getting JSON Array node
                JSONArray bancos = jsonObj.getJSONArray("ObtenerListaBancosResult");
                if (bancos.length() == 0) {
                    return false;
                }
                InformesH.EliminarBancos();
                // looping through All Contacts

                for (int i = 0; i < bancos.length(); i++) {
                    JSONObject c = bancos.getJSONObject(i);
                    InformesH.GuardarBancos(c.get("CODIGO").toString(),c.get("NOMBRE").toString());
                }
                return true;
               // DbOpenHelper.database.setTransactionSuccessful();
            } catch (Exception ex) {
                Log.e("Error", ex.getMessage());
                new Funciones().SendMail("Ha ocurrido un error al obtener el listado de Bancos,Excepcion controlada", variables_publicas.info + ex.getMessage(), "dlunasistemas@gmail.com", variables_publicas.correosErrores);
                return false;
            }

          /*  finally {
                DbOpenHelper.database.endTransaction();
            }*/

        } else {
            isOnline = Funciones.TestServerConectivity();
            if (isOnline) {
                new Funciones().SendMail("Ha ocurrido un error al obtener el Listado de bancos,Respuesta nula", variables_publicas.info + urlString, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
            }
            return false;
        }
        //return false;
    }

    private boolean ObtenerSerieRecibos() {

        HttpHandler sh = new HttpHandler();
        String urlString = variables_publicas.direccionIp + "/ServicioRecibos.svc/ObtenerSerieRecibos";
        String encodeUrl = "";
        try {
            URL Url = new URL(urlString);
            URI uri = new URI(Url.getProtocol(), Url.getUserInfo(), Url.getHost(), Url.getPort(), Url.getPath(), Url.getQuery(), Url.getRef());
            encodeUrl = uri.toURL().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String jsonStr = sh.makeServiceCall(encodeUrl);

        /**********************************SERIE RECIBOS**************************************/
        if (jsonStr != null) {

            try {
                //DbOpenHelper.database.beginTransaction();
                JSONObject jsonObj = new JSONObject(jsonStr);
                // Getting JSON Array node
                JSONArray series = jsonObj.getJSONArray("ObtenerSerieRecibosResult");
                if (series.length() == 0) {
                    return false;
                }
                InformesH.EliminarSeries();
                // looping through All Contacts

                for (int i = 0; i < series.length(); i++) {
                    JSONObject c = series.getJSONObject(i);
                    InformesH.GuardarSeries(c.get("id").toString(),c.get("vendedor").toString(),c.get("ninicial").toString(),c.get("nfinal").toString(),c.get("numero").toString());
                }
                return true;
                // DbOpenHelper.database.setTransactionSuccessful();
            } catch (Exception ex) {
                Log.e("Error", ex.getMessage());
                new Funciones().SendMail("Ha ocurrido un error al obtener las Series de Recibos, Excepcion controlada", variables_publicas.info + ex.getMessage(), "dlunasistemas@gmail.com", variables_publicas.correosErrores);
                return false;
            }

          /*  finally {
                DbOpenHelper.database.endTransaction();
            }*/

        } else {
            isOnline = Funciones.TestServerConectivity();
            if (isOnline) {
                new Funciones().SendMail("Ha ocurrido un error al obtener las Series de Recibos, Respuesta nula", variables_publicas.info + urlString, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
            }
            return false;
        }
        //return false;
    }

    public static String SincronizarClientesTotal(Cliente cliente, String jsonCliente) {
        boolean Editar=false;
        if (variables_publicas.vEditando){
            Editar=true;
        }else {
            Editar=false;
        }
        HttpHandler sh = new HttpHandler();
        String encodeUrl = "";
        Gson gson = new Gson();

        final String urlCliente = variables_publicas.direccionIp + "/ServicioClientes.svc/SincronizarClienteTotal2/";
        final String urlStringDetalle = urlCliente + cliente.getIdCliente() + "/" + String.valueOf(Editar) + "/" + cliente.getEmpresa() + "/" + jsonCliente;


        HashMap<String,String> postData = new HashMap<>();
        postData.put("Editar",String.valueOf(Editar));
        postData.put("IdCliente",cliente.getIdCliente());
        postData.put("Empresa",cliente.getEmpresa());
        postData.put("cliente",jsonCliente);


        String jsonStrCliente = sh.performPostCall(urlCliente,postData);

        //  String jsonStrPedido = sh.makeServiceCallPost(encodeUrl);
        if (jsonStrCliente == null || jsonCliente.isEmpty()) {
            if (Funciones.TestServerConectivity()) {
                new Funciones().SendMail("Ha ocurrido un error al sincronizar el Cliente,Respuesta nula POST", variables_publicas.info + urlStringDetalle, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
            }
            return "false,Ha ocurrido un error al sincronizar el cliente,Respuesta nula";
        } else {
            try {
                JSONObject result = new JSONObject(jsonStrCliente);
                String resultState = (String) ((String) result.get("SincronizarClienteTotal2Result")).split(",")[0];
                String NoCliente = (String) ((String) result.get("SincronizarClienteTotal2Result")).split(",")[1];
                if (resultState.equals("false")) {

                    if (NoCliente.equalsIgnoreCase("Cliente ya existe en base de datos")) {
                        NoCliente =  ((String) result.get("SincronizarClienteTotal2Result")).split(",")[1];
                    } else {
                        new Funciones().SendMail("Ha ocurrido un error al sincronizar el Cliente ,Respuesta false", variables_publicas.info + NoCliente +" *** "+urlStringDetalle, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
                        return "false," + NoCliente;
                    }
                }
               // PedidoH.ActualizarPedido(IdPedido, NoPedido);
//
                return "true";
            } catch (Exception ex) {
                new Funciones().SendMail("Ha ocurrido un error al sincronizar el Cliente, Excepcion controlada ", variables_publicas.info + ex.getMessage(), "dlunasistemas@gmail.com", variables_publicas.correosErrores);
                Log.e("Error", ex.getMessage());
                return "false," + ex.getMessage() + "";
            }

        }

    }

    public static String SincronizarClientesInactivo(Cliente cliente, String jsonCliente) {
        HttpHandler sh = new HttpHandler();
        String encodeUrl = "";
        Gson gson = new Gson();

        final String urlCliente = variables_publicas.direccionIp + "/ServicioClientes.svc/SincronizarClienteInactivo/";
        final String urlStringDetalle = urlCliente + cliente.getIdCliente() + "/" + cliente.getEmpresa() + "/" + jsonCliente;


        HashMap<String,String> postData = new HashMap<>();
        postData.put("IdCliente",cliente.getIdCliente());
        postData.put("Empresa",cliente.getEmpresa());
        postData.put("cliente",jsonCliente);

        String jsonStrCliente = sh.performPostCall(urlCliente,postData);

        //  String jsonStrPedido = sh.makeServiceCallPost(encodeUrl);
        if (jsonStrCliente == null || jsonCliente.isEmpty()) {
            if (Funciones.TestServerConectivity()) {
                new Funciones().SendMail("Ha ocurrido un error al activar al cliente. Respuesta nula POST", variables_publicas.info + urlStringDetalle, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
            }
            return "false,Ha ocurrido un error al activar al cliente. Respuesta nula";
        } else {
            try {
                JSONObject result = new JSONObject(jsonStrCliente);
                String resultState = (String) ((String) result.get("SincronizarClienteInactivoResult")).split(",")[0];
                String NoCliente = (String) ((String) result.get("SincronizarClienteInactivoResult")).split(",")[1];
                if (resultState.equals("false")) {

                    if (NoCliente.equalsIgnoreCase("Cliente ya existe en base de datos")) {
                        NoCliente =  ((String) result.get("SincronizarClienteInactivoResult")).split(",")[1];
                    } else {
                        new Funciones().SendMail("Ha ocurrido un error al activar el Cliente. Respuesta false", variables_publicas.info + NoCliente +" *** "+urlStringDetalle, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
                        return "false," + NoCliente;
                    }
                }
                // PedidoH.ActualizarPedido(IdPedido, NoPedido);
//
                return "true";
            } catch (Exception ex) {
                new Funciones().SendMail("Ha ocurrido un error al activar el Cliente. Excepcion controlada ", variables_publicas.info + ex.getMessage(), "dlunasistemas@gmail.com", variables_publicas.correosErrores);
                Log.e("Error", ex.getMessage());
                return "false," + ex.getMessage() + "";
            }

        }

    }

    private boolean SincronizarFacturasPendientes(String vVendedor, String vCliente) throws JSONException {
        HttpHandler shC = new HttpHandler();
        String urlStringC = urlGetFacturasPendientes + vVendedor + "/" + vCliente;
        String jsonStrC = shC.makeServiceCall(urlStringC);

        if (jsonStrC == null) {
            isOnline = Funciones.TestServerConectivity();
            if (isOnline) {
                new Funciones().SendMail("Ha ocurrido un error al sincronizar las Facturas Pendientes, Respuesta nula GET", variables_publicas.info + urlStringC, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
            }
            return false;
        }
        //Log.e(TAG, "Response from url: " + jsonStrC);
        DbOpenHelper.database.beginTransaction();

        FacturasPendientesH.EliminaFacturasPendientes();
        JSONObject jsonObjC = new JSONObject(jsonStrC);
        // Getting JSON Array node
        JSONArray articulos = jsonObjC.getJSONArray("SpObtieneFacturasSaldoPendienteResult");


        try {
            // looping through All Contacts
            for (int i = 0; i < articulos.length(); i++) {
                JSONObject c = articulos.getJSONObject(i);

                String codvendedor = c.getString("codvendedor");
                String No_Factura = c.getString("No_Factura");
                String Cliente = c.getString("Cliente");
                String CodigoCliente = c.getString("CodigoCliente");
                String Fecha = c.getString("Fecha");
                String IVA = c.getString("IVA");
                String Tipo = c.getString("Tipo");
                String SubTotal = c.getString("SubTotal");
                String Descuento = c.getString("Descuento");
                String Total = c.getString("Total");
                String Abono = c.getString("Abono");
                String Saldo = c.getString("Saldo");
                String Guardada = c.getString("Guardada");
                FacturasPendientesH.GuardarFacturasPendientes(codvendedor,Fecha, No_Factura, Cliente, CodigoCliente, IVA, Tipo, SubTotal, Descuento, Total, Abono, Saldo, Guardada);
            }
            DbOpenHelper.database.setTransactionSuccessful();
            return true;
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al sincronizar las Facturas Pendientes, Excepcion controlada", variables_publicas.info + ex.getMessage(), "dlunasistemas@gmail.com", variables_publicas.correosErrores);
            return false;
        } finally {
            DbOpenHelper.database.endTransaction();
        }

    }

    public static String SincronizarInforme(InformesHelper InformesH, InformesDetalleHelper InformesDetalleH, String vvendedor, String CodInforme, String jsonInforme, boolean Editar) {

        HttpHandler sh = new HttpHandler();
        String encodeUrl = "";
        Gson gson = new Gson();
        List<HashMap<String, String>> informeDetalle = InformesDetalleH.ObtenerInformeDetalle(CodInforme);
        for (HashMap<String, String> item : informeDetalle) {
            item.put("CodInforme", item.get("CodInforme"));
            item.put("Recibo", item.get("Recibo"));
            item.put("Idvendedor", item.get("Idvendedor"));
            item.put("IdCliente", item.get("IdCliente"));
            item.put("Factura", item.get("Factura"));
            item.put("Saldo", item.get("Saldo").replace(",", ""));
            item.put("Monto", item.get("Monto").replace(",", ""));
            item.put("Abono", item.get("Abono").replace(",", ""));
            item.put("NoCheque", item.get("NoCheque"));
            item.put("BancoE", item.get("BancoE"));
            item.put("BancoR", item.get("BancoR"));
            item.put("FechaCK", item.get("FechaCK"));
            item.put("FechaDep", item.get("FechaDep"));
            item.put("Efectivo", item.get("Efectivo"));
            item.put("Moneda", item.get("Moneda"));
            item.put("Aprobado", item.get("Aprobado"));
            item.put("Posfechado", item.get("Posfechado"));
            item.put("Procesado", item.get("Procesado"));
            item.put("Usuario", item.get("Usuario"));
            item.put("Vendedor", item.get("Vendedor"));
            item.put("Cliente", item.get("Cliente"));
            item.put("CodigoLetra", item.get("CodigoLetra"));
            item.put("CantLetra", item.get("CantLetra"));
            item.put("Observacion", item.get("Observacion"));
            item.put("Concepto", item.get("Concepto"));
            item.put("DepPendiente", item.get("DepPendiente"));
        }
        String jsonInformeDetalle = gson.toJson(informeDetalle);
        final String urlDetalle = variables_publicas.direccionIp + "/ServicioRecibos.svc/SincronizarInformeTotal/";
        final String urlStringDetalle = urlDetalle + String.valueOf(Editar) + "/" + vvendedor + "/" + jsonInforme + "/" + jsonInformeDetalle;

        HashMap<String,String> postData = new HashMap<>();
        postData.put("Editar",String.valueOf(Editar));
        postData.put("IdVendedor",vvendedor);
        postData.put("informe",jsonInforme);
        postData.put("Detalle",jsonInformeDetalle)   ;

        String jsonStrInforme= sh.performPostCall(urlDetalle,postData);

        //  String jsonStrPedido = sh.makeServiceCallPost(encodeUrl);
        if (jsonStrInforme == null || jsonInforme.isEmpty()) {
            if (Funciones.TestServerConectivity()) {
                new Funciones().SendMail("Ha ocurrido un error al sincronizar el informe, Respuesta nula POST", variables_publicas.info + urlStringDetalle, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
            }
            return "false,Ha ocurrido un error al sincronizar el detalle del informe, Respuesta nula";
        } else {
            try {
                JSONObject result = new JSONObject(jsonStrInforme);
                String resultState = (String) ((String) result.get("SincronizarInformeTotalResult")).split(",")[0];
                String NoInforme = (String) ((String) result.get("SincronizarInformeTotalResult")).split(",")[1];
                if (resultState.equals("false")) {

                    if (NoInforme.equalsIgnoreCase("Informe ya existe en base de datos")) {
                        NoInforme =  ((String) result.get("SincronizarInformeTotalResult")).split(",")[1];
                    } else {
                        new Funciones().SendMail("Ha ocurrido un error al sincronizar el Informe ,Respuesta false", variables_publicas.info + NoInforme +" *** "+urlStringDetalle, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
                        return "false," + NoInforme;
                    }
                }
                InformesH.ActualizarInforme(CodInforme, NoInforme);
                InformesDetalleH.ActualizarCodigoInforme(CodInforme, NoInforme);
                variables_publicas.noInforme=NoInforme;
                return "true";
            } catch (Exception ex) {
                new Funciones().SendMail("Ha ocurrido un error al sincronizar el Informe, Excepcion controlada ", variables_publicas.info + ex.getMessage(), "dlunasistemas@gmail.com", variables_publicas.correosErrores);
                Log.e("Error", ex.getMessage());
                return "false," + ex.getMessage() + "";
            }

        }

    }

       public static boolean ObtenerPedidoGuardado(String vPedido, PedidosHelper vpedidoh) {

        HttpHandler sh = new HttpHandler();
        String urlString = variables_publicas.direccionIp + "/ServicioPedidos.svc/ObtenerPedidoCabecera/" + vPedido;
        String encodeUrl = "";
        try {
            URL Url = new URL(urlString);
            URI uri = new URI(Url.getProtocol(), Url.getUserInfo(), Url.getHost(), Url.getPort(), Url.getPath(), Url.getQuery(), Url.getRef());
            encodeUrl = uri.toURL().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String jsonStr = sh.makeServiceCall(encodeUrl);

        /**********************************PEDIDOS**************************************/
        if (jsonStr != null) {

            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                JSONArray pedido = jsonObj.getJSONArray("ObtenerPedidoCabeceraResult");
                if (pedido.length() == 0) {
                    return false;
                }
                vpedidoh.EliminaPedido(vPedido);
                for (int i = 0; i < pedido.length(); i++) {
                    JSONObject c = pedido.getJSONObject(i);
                    vpedidoh.GuardarPedido(c.get("CodigoPedido").toString(),c.get("IdVendedor").toString(),c.get("IdCliente").toString(),c.get("Tipo").toString(),c.get("Observacion").toString(),c.get("IdFormaPago").toString(),c.get("IdSucursal").toString(),c.get("Fecha").toString(),c.get("Usuario").toString(),c.get("IMEI").toString(),c.get("SubTotal").toString(),c.get("Total").toString(),c.get("TCambio").toString(),c.get("Empresa").toString());
                }
                return true;
            } catch (Exception ex) {
                Log.e("Error", ex.getMessage());
                new Funciones().SendMail("Ha ocurrido un error al obtener el Pedido. Excepcion controlada", variables_publicas.info + ex.getMessage(), "dlunasistemas@gmail.com", variables_publicas.correosErrores);
                return false;
            }

        } else {
            if (Funciones.TestServerConectivity()) {
                new Funciones().SendMail("Ha ocurrido un error al obtener el Pedido. Respuesta nula", variables_publicas.info + urlString, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
            }
            return false;
        }
    }

    public static boolean ObtenerPedidoGuardadoDetalle(String vPedido,PedidosDetalleHelper vpedidodetalleh) {

        HttpHandler sh = new HttpHandler();
        String urlString = variables_publicas.direccionIp + "/ServicioPedidos.svc/ObtenerPedidoDetalleNew/" + vPedido;
        String encodeUrl = "";
        try {
            URL Url = new URL(urlString);
            URI uri = new URI(Url.getProtocol(), Url.getUserInfo(), Url.getHost(), Url.getPort(), Url.getPath(), Url.getQuery(), Url.getRef());
            encodeUrl = uri.toURL().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String jsonStr = sh.makeServiceCall(encodeUrl);

        /**********************************PEDIDOS DETALLE**************************************/
        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                JSONArray pedido = jsonObj.getJSONArray("ObtenerPedidoDetalleNewResult");
                if (pedido.length() == 0) {
                    return false;
                }
                vpedidodetalleh.EliminarDetallePedido(vPedido);
                for (int i = 0; i < pedido.length(); i++) {
                    JSONObject c = pedido.getJSONObject(i);
                    vpedidodetalleh.GuardarDetallePedido(c.get("CodigoPedido").toString(),c.get("CodigoArticulo").toString(),c.get("Descripcion").toString(),c.get("Cantidad").toString().substring(0,c.get("Cantidad").toString().indexOf(".")),c.get("BonificaA").toString(),c.get("TipoArt").toString(),c.get("PorDescuento").toString(),c.get("Descuento").toString(),c.get("CodUM").toString(),c.get("Unidades").toString(),c.get("Costo").toString(),c.get("Precio").toString(),c.get("TipoPrecio").toString(),c.get("PorcentajeIva").toString(),c.get("Iva").toString(),c.get("Um").toString(),"1",c.get("Subtotal").toString(),c.get("Total").toString(),c.get("Bodega").toString(),c.get("codPromo").toString());
                }
                return true;
            } catch (Exception ex) {
                Log.e("Error", ex.getMessage());
                new Funciones().SendMail("Ha ocurrido un error al obtener el detalle del pedido. Excepcion controlada", variables_publicas.info + ex.getMessage(), "dlunasistemas@gmail.com", variables_publicas.correosErrores);
                return false;
            }

        } else {
            if (Funciones.TestServerConectivity()) {
                new Funciones().SendMail("Ha ocurrido un error al obtener el detalle del pedido. Respuesta nula", variables_publicas.info + urlString, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
            }
            return false;
        }
    }

}