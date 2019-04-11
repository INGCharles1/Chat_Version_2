package com.example.chatproyecto.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chatproyecto.Entidades.Firebase.Usuario;
import com.example.chatproyecto.Entidades.Logica.LUsuario;
import com.example.chatproyecto.Holder.UsuarioViewHolder;
import com.example.chatproyecto.R;
import com.example.chatproyecto.Utilidades.Constantes;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class VerUsuariosActivity extends AppCompatActivity {

    private RecyclerView rvUsuarios;
    private  FirebaseRecyclerAdapter adapter;
    private Button btnCerrarSesion;
    private Button btnChatGrupo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_usuarios);
        rvUsuarios = findViewById(R.id.rvUsuarios);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnChatGrupo = findViewById(R.id.btnGrupo);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvUsuarios.setLayoutManager(linearLayoutManager);

        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                returnLogin();
            }
        });

        btnChatGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VerUsuariosActivity.this, MensajeriaGrupoActivity.class));
            }
        });

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child(Constantes.NODO_USUARIOS);

        FirebaseRecyclerOptions<Usuario> options =
                new FirebaseRecyclerOptions.Builder<Usuario>()
                        .setQuery(query, Usuario.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Usuario, UsuarioViewHolder>(options) {
            @Override
            public UsuarioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_usuarios, parent, false);
                return new UsuarioViewHolder(view);

            }

            @Override
            protected void onBindViewHolder(UsuarioViewHolder holder, int position, Usuario model) {

                Glide.with(VerUsuariosActivity.this).load(model.getFotoPerfilURL()).into(holder.getCivFotoPerfil());
                holder.getTxtNombreUsuario().setText(model.getNombre());

                final LUsuario lUsuario = new LUsuario(getSnapshots().getSnapshot(position).getKey(),model);

                holder.getLayoutPrincipal().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(VerUsuariosActivity.this, MensajeriaActivity.class);
                        intent.putExtra("key_receptor",lUsuario.getKey());
                        startActivity(intent);
                    }
                });

            }
        };
        rvUsuarios.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void returnLogin(){
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

}
