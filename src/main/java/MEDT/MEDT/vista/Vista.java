package MEDT.MEDT.vista;

import MEDT.MEDT.controlador.ControladorArticulos;
import MEDT.MEDT.controlador.ControladorClientes;
import MEDT.MEDT.controlador.ControladorPedidos;
import MEDT.MEDT.modelo.Articulo;
import MEDT.MEDT.modelo.Cliente;
import MEDT.MEDT.modelo.Pedido;
import MEDT.MEDT.modelo.excepciones.PedidoNoCancelableException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Vista {
    private final Scanner sc = new Scanner(System.in);
    private final ControladorPedidos controladorPedidos;
    private final ControladorArticulos controladorArticulos;
    private final ControladorClientes controladorClientes;

    public Vista(ControladorPedidos cp, ControladorArticulos ca, ControladorClientes cc) {
        this.controladorArticulos = ca;
        this.controladorClientes = cc;
        this.controladorPedidos = cp;
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

                if (this.controladorArticulos.addArticulo(codigo, descripcion, precio, gastosEnvio, tiempoPrep)) {
                    System.out.println("Artículo añadido correctamente.");
                } else {
                    System.out.println("Error: el artículo ya existe o no se pudo añadir.");
                }
            }
            case 2 -> {
                List<Articulo> articulos = this.controladorArticulos.getArticulos();
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
                        ? this.controladorClientes.addClienteEstandar(nombre, domicilio, nif, email)
                        : this.controladorClientes.addClientePremium(nombre, domicilio, nif, email);
                if (ok)
                    System.out.println("Cliente añadido correctamente.");
                else
                    System.out.println("Error: el cliente ya existe o no se pudo añadir.");
            }
            case 2 -> {
                this.subMenuClientes();
            }
            default -> System.out.println("Opción no válida.");
        }
    }

    private void subMenuClientes() {
        System.out.println("--Mostrar Clientes--");
        System.out.println("1. Mostrar clientes estandar");
        System.out.println("2. Mostrar clientes premium");
        System.out.println("3. Mostrar todos los clientes");
        System.out.print("Elige opción: ");
        int opcion = Integer.parseInt(sc.nextLine());

        switch (opcion) {
            case 1 -> {
                List<Cliente> clientes = this.controladorClientes.getClientesEstandar();
                if (clientes.isEmpty()) {
                    System.out.println("No hay clientes estandar registrados.");
                } else {
                    clientes.forEach(System.out::println);
                }
            }
            case 2 -> {
                List<Cliente> clientes = this.controladorClientes.getClientesPremium();
                if (clientes.isEmpty()) {
                    System.out.println("No hay clientes premium registrados.");
                } else {
                    clientes.forEach(System.out::println);
                }
            }
            case 3 -> {
                List<Cliente> clientes = this.controladorClientes.getClientes();
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

        String filtro = "";

        if (opcion == 3 || opcion == 4){
            String userOption = "";
            System.out.print("Desea filtrar? S/N:");
            userOption = sc.nextLine();
            if (userOption.equals("S")){
                System.out.print("Introduzca el DNI:");
                filtro = sc.nextLine();
            }
        }

        switch (opcion) {
            case 1 -> addPedido();
            case 2 -> eliminarPedido();
            case 3 -> mostrarPedidosPendientes(filtro);
            case 4 -> mostrarPedidosEnviados(filtro);
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

        String resultado;
        boolean existeCliente = this.controladorClientes.existeCliente(nif);

        if (!existeCliente){
            System.out.println("El cliente no existe, debe darlo de alta primero.");

            System.out.print("Nombre: ");
            String nombre = sc.nextLine();
            System.out.print("Domicilio: ");
            String domicilio = sc.nextLine();
            System.out.print("Email: ");
            String email = sc.nextLine();
            System.out.print("Tipo de cliente (1=Estandar, 2=Premium): ");
            int tipo = Integer.parseInt(sc.nextLine());

            /// TODO: TRANSACCIÓN AGREGAR CLIENTE Y PEDIDO AL MISMO TIEMPO
            resultado = "";

            /*
            boolean ok = (tipo == 1)
                    ? this.controladorClientes.addClienteEstandar(nombre, domicilio, nif, email)
                    : this.controladorClientes.addClientePremium(nombre, domicilio, nif, email);
            if (ok){
                System.out.println("Cliente añadido correctamente.");
            }
            else{
                System.out.println("Error: No se ha podido registrar el cliente.");
                return;
            }
            */
            if (this.controladorPedidos.addPedidoYClienteAtomico(numPedido, cantidad, fecha, codigoArticulo, nif, nombre, domicilio, email, tipo)){
                System.out.println("Pedido y cliente agregados correctamente.");
            }
            else{
                System.out.println("Error al agregar el pedido y clientes.");
            }
        }
        else{
            resultado = this.controladorPedidos.addPedido(numPedido, cantidad, fecha, codigoArticulo, nif);
        }
        System.out.println(resultado);
    }

    private void eliminarPedido() {
        System.out.print("Número del pedido a eliminar: ");
        int num = Integer.parseInt(sc.nextLine());

        try {
            this.controladorPedidos.eliminarPedido(num);
            System.out.println("Pedido eliminado correctamente.");
        } catch (PedidoNoCancelableException e) {
            System.out.println("No se puede eliminar el pedido: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void mostrarPedidosPendientes(String filtro) {
        List<Pedido> pedidos = this.controladorPedidos.getPedidosPendientes(filtro);
        if (pedidos.isEmpty()) System.out.println("No hay pedidos pendientes.");
        else pedidos.forEach(System.out::println);
    }

    private void mostrarPedidosEnviados(String filtro) {
        List<Pedido> pedidos = this.controladorPedidos.getPedidosEnviados(filtro);
        if (pedidos.isEmpty()) System.out.println("No hay pedidos enviados.");
        else pedidos.forEach(System.out::println);
    }
}
