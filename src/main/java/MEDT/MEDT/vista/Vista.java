package MEDT.MEDT.vista;

import MEDT.MEDT.controlador.Controlador;
import MEDT.MEDT.modelo.Cliente;
import MEDT.MEDT.modelo.Pedido;

import java.util.Collection;
import java.util.Scanner;


public class Vista {
    private final Scanner sc = new Scanner(System.in);
    private final Controlador controlador = new Controlador();

    public void menu() {
        int opcion;
        do {
            System.out.println("===== MENÚ PRINCIPAL =====");
            System.out.println("1. Gestión de Artículos");
            System.out.println("2. Gestión de Clientes");
            System.out.println("3. Gestión de Pedidos");
            System.out.println("0. Salir");
            System.out.print("Elige una opción: ");
            opcion = Integer.parseInt(sc.nextLine());

            switch (opcion) {
                case 1 -> menuArticulos();
                case 2 -> menuClientes();
                case 3 -> menuPedidos();
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    private void menuArticulos() {
        System.out.println("1. Añadir Artículo");
        System.out.println("2. Mostrar Artículos");
        System.out.print("Elige opción: ");
        int opcion = Integer.parseInt(sc.nextLine());

        switch (opcion) {
            case 1 -> {
                System.out.print("Codigo: ");
                String codigo = sc.nextLine();
                System.out.print("Descripción: ");
                String descripcion = sc.nextLine();
                System.out.print("Precio: ");
                double precio = Double.parseDouble(sc.nextLine());
                System.out.print("Tiempo de preparación (min): ");
                double gastosEnvio = Double.parseDouble(sc.nextLine());
                System.out.print("Gastos de envío: ");
                int tiempoPrep = Integer.parseInt(sc.nextLine());
                controlador.addArticulo(codigo, descripcion, precio, gastosEnvio, tiempoPrep);
                System.out.println("Artículo añadido correctamente.");
            }
            case 2 -> controlador.getArticulos().forEach(System.out::println);
        }
    }

    private void menuClientes() {
        // similar a artículos (añadir, mostrar, etc.)
        System.out.println("--- GESTION DE CLIENTES ---");
        System.out.println("1. Añadir Clientes");
        System.out.println("2. Mostrar Clientes");
        System.out.println("3. Mostrar Clientes Estándar");
        System.out.println("4. Mostrar Clientes Premium");
        System.out.print("Elige opción: ");
        int opcion = Integer.parseInt(sc.nextLine());

        Collection<Cliente> listaClientes;

        switch (opcion) {
            case 1 -> {
                System.out.print("Nombre: ");
                String nombre = sc.nextLine();
                System.out.print("Domicilio: ");
                String domicilio = sc.nextLine();
                System.out.print("NIF: ");
                String nif = sc.nextLine();
                System.out.print("Email: ");
                String email = sc.nextLine();

                // TENGO DUDAS
                System.out.print("Tipo de cliente: (E)stándar / (P)remium: ");
                String tipoCliente = sc.nextLine().trim().toUpperCase();

                if (tipoCliente.equals("E")) {
                    controlador.addClienteEstandar(nombre, domicilio, nif, email);
                    System.out.println("Cliente Estándar añadido correctamente.");
                } else if (tipoCliente.equals("P")) {
                    controlador.addClientePremium(nombre, domicilio, nif, email);
                    System.out.println("Cliente Premium añadido correctamente.");
                } else {
                    System.out.println("Tipo de cliente no válido. No se añadieron clientes. Repita el proceso.");
                }
            }
            case 2 -> {
                listaClientes = controlador.getClientes();
                if (listaClientes.isEmpty()) {
                    System.out.println("No existen clientes.");
                } else {
                    listaClientes.forEach(System.out::println);
                }
            }
                    //controlador. getClientes().forEach(System.out::println);
            case 3 -> {
                listaClientes = controlador.getClientesEstandar();
                if (listaClientes.isEmpty()) {
                    System.out.println("No existen clientes Estándar.");
                } else {
                    listaClientes.forEach(System.out::println);
                }
            }

            case 4 -> {
                listaClientes = controlador.getClientesPremium();
                if (listaClientes.isEmpty()) {
                    System.out.println("No existen clientes Premium.");
                } else {
                    listaClientes.forEach(System.out::println);
                }
            }
        }
    }

    private void menuPedidos() {
        // opciones 3.1 a 3.4 según enunciado
        System.out.println("--- GESTION DE PEDIDOS ---");
        System.out.println("1. Añadir Pedido");
        System.out.println("2. Eliminar Pedido");
        System.out.println("3. Mostrar Pedidos pendientes de envío (con opción de filtrado por cliente)");
        System.out.println("4. Mostrar Pedidos enviados (con opción de filtrado por cliente)");
        System.out.print("Elige opción: ");

        try {
            int opcion = Integer.parseInt(sc.nextLine());
            switch (opcion) {
                case 1 -> addPedido();
                case 2 -> eliminarPedido();
                case 3 -> mostrarPedidos(false); // false = Pendientes
                case 4 -> mostrarPedidos(true); // true = Enviados
                default -> System.out.println("Opción no válida.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ERROR: Debes introducir un número.");
        }
    }

    private void addPedido() {
        System.out.println("Email del cliete: ");
        String emailCliente = sc.nextLine();

        System.out.print("Codigo del articulo: ");
        String codigoArticulo = sc.nextLine();

        int cantidad = 0;
        try {
            System.out.print("Cantidad: ");
            cantidad = Integer.parseInt(sc.nextLine());
            if (cantidad <= 0) {
                System.out.println("La cantidad debe ser mayor que cero.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("ERROR: Cantidad no válida.");
            return;
        }

        try {
            controlador.addPedido(emailCliente, codigoArticulo, cantidad);
            System.out.println("Pedido añadido correctamente.");
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("Cliente no existe")) {
                System.out.println("Cliente no encontrado. Se solicitarán datos para añadirlo.");
                addClienteAux(emailCliente);

                try {
                    controlador.addPedido(emailCliente, codigoArticulo, cantidad);
                    System.out.println("Cliente creado y pedido añadido exitosamente.");
                } catch (IllegalArgumentException ex) {
                    System.out.println("ERROR al reintentar pedido: " + ex.getMessage());
                }
            } else {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

    private void eliminarPedido() {
        System.out.println("Numero de pedido a eliminar: ");
        try {
            int numPedido = Integer.parseInt(sc.nextLine());

            if (controlador.eliminarPedido(numPedido)) {
                System.out.println("Pedido " + numPedido + " eliminado correctamente.");
            } else {
                System.out.println("ERROR: El pedido no existe o ya fue enviado y no puede ser cancelado.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ERROR: Introduzca el número de pedido entero.");
        }
    }

    private void mostrarPedidos(boolean esEnviado) {
        String tipo = esEnviado ? "ENVIADOS" : "PENDIENTES";
        System.out.print("Email del cliente para filtrar (Dejar vacío para ver todos los " + tipo + "): ");
        String emailFiltro = sc.nextLine().trim();

        Collection<Pedido> pedidos;
        if (esEnviado) {
            pedidos = controlador.getPedidosEnviados(emailFiltro);
        } else {
            pedidos = controlador.getPedidosPendientes(emailFiltro);
        }
        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos " + tipo + " que cumplan el criterio.");
        } else {
            System.out.println("\n--- LISTA DE PEDIDOS " + tipo + " ---");
            pedidos.forEach(System.out::println);
        }
    }

    private void addClienteAux(String email) {
        System.out.println("\n--- CREACION DE NUEVO CLIENTE ---");
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Domicilio: ");
        String domicilio = sc.nextLine();
        System.out.print("NIF: ");
        String nif = sc.nextLine();

        System.out.print("Tipo de cliente: (E)standar / (P)remium: ");
        String tipoCliente = sc.nextLine().trim().toUpperCase();

        if (tipoCliente.equals("E")) {
            controlador.addClienteEstandar(nombre, domicilio, nif, email);
            System.out.println("Cliente Estandar con email: " + email + " añadido.");
        } else if (tipoCliente.equals("P")) {
            controlador.addClientePremium(nombre, domicilio, nif, email);
            System.out.println("Cliente Premium con email: " + email + " añadido.");
        } else {
            System.out.println("Tipo de cliente no valido. No se añadio el cliente. Reintente la operación.");
        }
    }
}