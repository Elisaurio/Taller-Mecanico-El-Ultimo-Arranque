import 'package:flutter/material.dart';
import '../widgets/module_page.dart';

class HomeShell extends StatefulWidget {
  const HomeShell({super.key});

  @override
  State<HomeShell> createState() => _HomeShellState();
}

class _Module {
  final String name;
  final IconData icon;
  final String description;
  const _Module(this.name, this.icon, this.description);
}

class _HomeShellState extends State<HomeShell> {
  static const modules = <_Module>[
    _Module('Inicio', Icons.dashboard_outlined, 'Panel principal del taller'),
    _Module('Clientes', Icons.people_outline, 'Registro y consulta de clientes'),
    _Module('Vehículos', Icons.directions_car_outlined, 'Vehículos e historial por placa'),
    _Module('Mecánicos', Icons.engineering_outlined, 'Personal asignado a las reparaciones'),
    _Module('Repuestos', Icons.inventory_2_outlined, 'Existencias y abastecimiento'),
    _Module('Servicios', Icons.build_outlined, 'Catálogo y costos de mano de obra'),
    _Module('Órdenes', Icons.assignment_outlined, 'Seguimiento de órdenes de trabajo'),
    _Module('Reportes', Icons.picture_as_pdf_outlined, 'Reportes PDF y comprobantes'),
  ];

  int selected = 0;

  void selectModule(int index) {
    setState(() => selected = index);
  }

  @override
  Widget build(BuildContext context) {
    return LayoutBuilder(builder: (context, constraints) {
      final wide = constraints.maxWidth >= 800;
      return Scaffold(
        appBar: AppBar(
          title: const Text('El Último Arranque'),
          backgroundColor: Theme.of(context).colorScheme.primaryContainer,
        ),
        drawer: wide ? null : Drawer(child: Builder(builder: (drawerContext) => SafeArea(child: _menu(drawerContext: drawerContext)))),
        body: Row(children: [
          if (wide)
            SizedBox(width: 245, child: Material(
              color: Theme.of(context).colorScheme.surface,
              child: _menu(),
            )),
          Expanded(child: ModulePage(
            title: modules[selected].name,
            description: modules[selected].description,
            icon: modules[selected].icon,
            isHome: selected == 0,
            onNavigate: selectModule,
            moduleNames: modules.map((e) => e.name).toList(),
            moduleIcons: modules.map((e) => e.icon).toList(),
          )),
        ]),
      );
    });
  }

  Widget _menu({BuildContext? drawerContext}) {
    return ListView(children: [
      const Padding(
        padding: EdgeInsets.fromLTRB(18, 24, 18, 12),
        child: Text('MENÚ DEL TALLER', style: TextStyle(fontWeight: FontWeight.bold)),
      ),
      for (var i = 0; i < modules.length; i++)
        ListTile(
          leading: Icon(modules[i].icon),
          title: Text(modules[i].name),
          selected: selected == i,
          onTap: () {
            if (drawerContext != null) Navigator.of(drawerContext).pop();
            selectModule(i);
          },
        ),
    ]);
  }
}
