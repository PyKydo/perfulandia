package com.duoc.msvc.pedido.models;

public class Cliente {
    private Long idUsuario;

    private String nombre;

    private String apellido;

    private String contrasena;

    private String rol;

    private String correo;

    private String telefono;

    public Cliente(String telefono, String correo, String contrasena, String apellido, String nombre, Long idUsuario) {
        this.telefono = telefono;
        this.correo = correo;
        this.rol = "Cliente";
        this.contrasena = contrasena;
        this.apellido = apellido;
        this.nombre = nombre;
        this.idUsuario = idUsuario;
    }

    public Cliente() {
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
