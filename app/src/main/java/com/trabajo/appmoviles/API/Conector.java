package com.trabajo.appmoviles.API;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import com.trabajo.appmoviles.Modelos.Comida;
import com.trabajo.appmoviles.Modelos.Direccion;
import com.trabajo.appmoviles.Modelos.DireccionDTO;
import com.trabajo.appmoviles.Modelos.EstadoPedido;
import com.trabajo.appmoviles.Modelos.Filtro;
import com.trabajo.appmoviles.Modelos.MetodoPago;
import com.trabajo.appmoviles.Modelos.Pedido;
import com.trabajo.appmoviles.Modelos.TipoEntrega;
import com.trabajo.appmoviles.Modelos.UsuarioDTO;
import com.trabajo.appmoviles.Modelos.Usuarios;

public interface Conector {

    @GET("usuarios/email/{email}")
    Call<Usuarios> obtenerUsuarioemail(@Path("email") String email);

    @POST("/usuarios")
    Call<Usuarios> crearUsuario(@Body Usuarios usuario);

    @POST("/usuarios/login")
    Call<Map<String, Object>> login(@Body Usuarios usuario);

    @PUT("usuarios/email/{email}")
    Call<Usuarios> actualizarUsuarioPorEmail(@Path("email") String email, @Body Usuarios usuario);

    @GET("/comida")
    Call<List<Comida>> obtenerTodasLasComidas(@Header("Cookie") String cookie);

    @GET("/filtro")
    Call<List<Filtro>> ObtenerFiltro(@Header("Cookie") String cookie);

    @GET("/comida/filtro/{filtroId}")
    Call<List<Comida>> obtenerComidasPorFiltro(@Path("filtroId") int filtroId);

    @PUT("/direccion/{id}")
    Call<Direccion> actualizarDireccion(@Path("id") int id, @Body Direccion direccion);

    @GET("/direccion/usuarios/{usuariosId}")
    Call<List<Direccion>> obtenerDireccionesPorUsuarioId(@Path("usuariosId") int usuariosId);

    @POST("/direccion/usuarios/{usuariosId}/direccion")
    Call<Direccion> agregarDireccion(@Path("usuariosId") Integer usuariosId, @Body Direccion direccion);

    @GET("/metodo_pago")
    Call<List<MetodoPago>> ObtenerMetodos(@Header("Cookie") String cookie);

    @GET("/estado_pedido")
    Call<List<EstadoPedido>> obtenerTodosLosEstados(@Header("Cookie") String cookie);

    @GET("/tipo_entrega")
    Call<List<TipoEntrega>> ObtenerTipoEntrega(@Header("Cookie") String cookie);

    @POST("/pedido")
    Call<Pedido> registrarPedido(@Body Pedido pedido);

    @GET("/direccion/usuarios/{usuariosId}/primera")
    Call<Direccion> obtenerPrimeraDireccionPorUsuarioId(@Path("usuariosId") int usuariosId);

    @GET("/direccion/usuarios/{usuariosId}/primera/dir")
    Call<DireccionDTO> obtenerPrimeraDireccionDTOPorUsuarioId(@Path("usuariosId") int usuariosId);

    // Obtener UsuarioDTO por email
    @GET("/usuarios/perfil/{email}")
    Call<UsuarioDTO> obtenerPerfilUsuario(@Path("email") String email);

}
