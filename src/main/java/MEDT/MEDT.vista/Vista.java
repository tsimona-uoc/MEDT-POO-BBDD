package MEDT.MEDT.vista;

import MEDT.MEDT.controlador.Controlador;

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
        System.out.println("1. Añadir Clientes");
        System.out.println("2. Mostrar Clientes");
        System.out.println("3. Mostrar Clientes Estándar");
        System.out.println("4. Mostrar Clientes Premium");
        System.out.print("Elige opción: ");
        int opcion = Integer.parseInt(sc.nextLine());

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
                System.out.print("Tipo de cliente: (E)stándar / (P)remium");
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
            case 2 -> controlador. getClientes().forEach(System.out::println);
            case 3 -> controlador. getClientesEstandar().forEach(System.out::println);
            case 4 -> controlador. getClientesPremium().forEach(System.out::println);
        }
    }

    private void menuPedidos() {
        // opciones 3.1 a 3.4 según enunciado
        System.out.println("1. Añadir Pedido");
        System.out.println("2. Eliminar Pedido");
        System.out.println("3. Mostrar Pedidos pendientes de envío (con opción de filtrado por cliente)");
        System.out.println("4. Mostrar Pedidos enviados (con opción de filtrado por cliente)");
        System.out.print("Elige opción: ");
        int opcion = Integer.parseInt(sc.nextLine());

        switch (opcion) {
            case 1 -> {

                
    }

}

