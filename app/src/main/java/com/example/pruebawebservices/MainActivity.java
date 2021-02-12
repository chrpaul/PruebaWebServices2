package com.example.pruebawebservices;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText edtCodigo,edtPrecio,edtProducto,edtFabricante;

    Button btnagregar, btnBuscar, btnEditar, btnEliminar;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtCodigo= (EditText)findViewById(R.id.editTextCodigo);
        edtPrecio= (EditText)findViewById(R.id.editTextPrecio);
        edtProducto= (EditText)findViewById(R.id.editTextProducto);
        edtFabricante= (EditText)findViewById(R.id.editTextFabricante);
        btnagregar= (Button) findViewById(R.id.buttonInsertar);
        btnBuscar= (Button) findViewById(R.id.buttonBuscar);
        btnEditar= (Button) findViewById(R.id.buttonEditar);
        btnEliminar= (Button) findViewById(R.id.buttonEliminar);

        btnagregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ejecutarServicio("http://192.168.1.7:8080/productoscq/insertar_producto.php");

            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscarProducto("http://192.168.1.7:8080/productoscq/buscar_producto.php?codigo="+edtCodigo.getText()+"");
            }
        });

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ejecutarServicio("http://192.168.1.7:8080/productoscq/editar_producto.php");
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarProducto("http://192.168.1.7:8080/productoscq/eliminar_producto.php");
            }
        });
    }

    private void ejecutarServicio(String URL){
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(),"OPERACION EXITOSA",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros=new HashMap<String, String>();
                parametros.put("codigo",edtCodigo.getText().toString());
                parametros.put("productos",edtProducto.getText().toString());
                parametros.put("precio",edtPrecio.getText().toString());
                parametros.put("fabricante",edtFabricante.getText().toString());
            return parametros;
            }
        };

        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void buscarProducto(String URL){
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        edtProducto.setText(jsonObject.getString("productos"));
                        edtPrecio.setText(jsonObject.getString("precio"));
                        edtFabricante.setText(jsonObject.getString("fabricante"));
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                 Toast.makeText(getApplicationContext(),"ERROR CONEXIÓN", Toast.LENGTH_SHORT).show();            }
        }
        );
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    private void eliminarProducto(String URL){
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(),"EL PRODUCTO FUE ELIMINADO",Toast.LENGTH_SHORT).show();
                limpiarFormulario();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros=new HashMap<String, String>();
                parametros.put("codigo",edtCodigo.getText().toString());
                return parametros;
            }
        };

        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void limpiarFormulario(){
        edtCodigo.setText("");
        edtProducto.setText("");
        edtPrecio.setText("");
        edtFabricante.setText("");
    }
}
