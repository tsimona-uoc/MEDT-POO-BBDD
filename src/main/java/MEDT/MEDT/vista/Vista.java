package MEDT.MEDT.vista;

import MEDT.MEDT.controlador.Controlador;
import MEDT.MEDT.modelo.excepciones.PedidoNoCancelableException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Vista {
    private final Scanner sc = new Scanner(System.in);
    private final Controlador controlador;

    public Vista(Controlador controlador) {
        this.controlador = controlador;
    }

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
        System.out.println("--Gestor de Artículos--");
        System.out.println("1. Añadir Artículo");
        System.out.println("2. Mostrar Artículos");
        System.out.print("Elige opción: ");
        int opcion = Integer.parseInt(sc.nextLine());

        switch (opcion) {
            case 1 -> {
                System.out.print("Código: ");
                String codigo = sc.nextLine();
                System.out.print("Descripción: ");
                String descripcion = sc.nextLine();
                System.out.print("Precio: ");
                double precio = Double.parseDouble(sc.nextLine());
                System.out.print("Tiempo de preparación (min): ");
                int tiempoPrep = Integer.parseInt(sc.nextLine());
                System.out.print("Gastos de envío: ");
                double gastosEnvio = Double.parseDouble(sc.nextLine());

                if (controlador.addArticulo(codigo, descripcion, precio, gastosEnvio, tiempoPrep)) {
                    System.out.println("Artículo añadido correctamente.");
                } else {
                    System.out.println("Error: el artículo ya existe o no se pudo añadir.");
                }
            }
            case 2 -> {
                List<String> articulos = controlador.getArticulosStr();
                if (articulos.isEmpty()) {
                    System.out.println("No hay artículos registrados.");
                } else {
                    articulos.forEach(System.out::println);
                }
            }
            default -> System.out.println("Opción no válida.");
        }
    }

    private void menuClientes() {
        System.out.println("--Gestor de Clientes--");
        System.out.println("1. Añadir Cliente");
        System.out.println("2. Mostrar Clientes");
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
                System.out.print("Tipo de cliente (1=Estandar, 2=Premium): ");
                int tipo = Integer.parseInt(sc.nextLine());

                boolean ok = (tipo == 1)
                        ? controlador.addClienteEstandar(nombre, domicilio, nif, email)
                        : controlador.addClientePremium(nombre, domicilio, nif, email);

                if (ok)
                    System.out.println("Cliente añadido correctamente.");
                else
                    System.out.println("Error: el cliente ya existe o no se pudo añadir.");
            }
            case 2 -> {
                List<String> clientes = controlador.getClientesStr();
                if (clientes.isEmpty()) {
                    System.out.println("No hay clientes registrados.");
                } else {
                    clientes.forEach(System.out::println);
                }
            }
            default -> System.out.println("Opción no válida.");
        }
    }

    private void menuPedidos() {
        System.out.println("--Gestor de Pedidos--");
        System.out.println("1. Añadir Pedido");
        System.out.println("2. Eliminar Pedido");
        System.out.println("3. Mostrar Pedidos Pendientes");
        System.out.println("4. Mostrar Pedidos Enviados");
        System.out.print("Elige opción: ");
        int opcion = Integer.parseInt(sc.nextLine());

        switch (opcion) {
            case 1 -> addPedido();
            case 2 -> eliminarPedido();
            case 3 -> mostrarPedidosPendientes();
            case 4 -> mostrarPedidosEnviados();
            default -> System.out.println("Opción no válida.");
        }
    }

    private void addPedido() {
        System.out.print("Número del pedido: ");
        int numPedido = Integer.parseInt(sc.nextLine());
        System.out.print("Cantidad de unidades: ");
        int cantidad = Integer.parseInt(sc.nextLine());
        System.out.print("Fecha del pedido (yyyy-MM-dd HH:mm): ");
        LocalDateTime fecha = LocalDateTime.parse(sc.nextLine(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        System.out.print("Código del artículo: ");
        String codigoArticulo = sc.nextLine();
        System.out.print("NIF del cliente: ");
        String nif = sc.nextLine();

        String resultado = controlador.addPedido(numPedido, cantidad, fecha, codigoArticulo, nif);
        System.out.println(resultado);
    }

    private void eliminarPedido() {
        System.out.print("Número del pedido a eliminar: ");
        int num = Integer.parseInt(sc.nextLine());

        try {
            controlador.eliminarPedido(num);
            System.out.println("Pedido eliminado correctamente.");
        } catch (PedidoNoCancelableException e) {
            System.out.println("No se puede eliminar el pedido: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void mostrarPedidosPendientes() {
        List<String> pedidos = controlador.getPedidosPendientesStr();
        if (pedidos.isEmpty()) System.out.println("No hay pedidos pendientes.");
        else pedidos.forEach(System.out::println);
    }

    private void mostrarPedidosEnviados() {
        List<String> pedidos = controlador.getPedidosEnviadosStr();
        if (pedidos.isEmpty()) System.out.println("No hay pedidos enviados.");
        else pedidos.forEach(System.out::println);
    }
}
