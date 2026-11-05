import 'package:flutter/material.dart';
import '../services/api_service.dart';

class ModulePage extends StatefulWidget {
  final String title;
  final String description;
  final IconData icon;
  final bool isHome;
  final ValueChanged<int> onNavigate;
  final List<String> moduleNames;
  final List<IconData> moduleIcons;

  const ModulePage({
    super.key,
    required this.title,
    required this.description,
    required this.icon,
    required this.isHome,
    required this.onNavigate,
    required this.moduleNames,
    required this.moduleIcons,
  });

  @override
  State<ModulePage> createState() => _ModulePageState();
}

class _ModulePageState extends State<ModulePage> {
  String status = 'Sin comprobar';
  bool checking = false;

  Future<void> checkConnection() async {
    setState(() { checking = true; status = 'Conectando...'; });
    try {
      final message = await ApiService().healthCheck();
      if (!mounted) return;
      setState(() => status = message);
    } catch (e) {
      if (!mounted) return;
      setState(() => status = 'No se pudo conectar. Revisa el backend y la URL.');
    } finally {
      if (mounted) setState(() => checking = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      padding: const EdgeInsets.all(20),
      child: Center(child: ConstrainedBox(
        constraints: const BoxConstraints(maxWidth: 1100),
        child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
          Text(widget.title, style: Theme.of(context).textTheme.headlineMedium),
          const SizedBox(height: 8),
          Text(widget.description),
          const SizedBox(height: 24),
          if (widget.isHome) ...[
            Card(child: Padding(
              padding: const EdgeInsets.all(20),
              child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
                const Text('Conexión con Java', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 18)),
                const SizedBox(height: 10),
                Text(status),
                const SizedBox(height: 12),
                FilledButton.icon(
                  onPressed: checking ? null : checkConnection,
                  icon: const Icon(Icons.wifi_tethering),
                  label: const Text('Probar conexión'),
                ),
              ]),
            )),
            const SizedBox(height: 20),
            LayoutBuilder(builder: (context, c) {
              final cols = c.maxWidth >= 850 ? 4 : c.maxWidth >= 480 ? 2 : 1;
              return GridView.builder(
                shrinkWrap: true,
                physics: const NeverScrollableScrollPhysics(),
                gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
                  crossAxisCount: cols,
                  mainAxisSpacing: 12,
                  crossAxisSpacing: 12,
                  mainAxisExtent: 130,
                ),
                itemCount: widget.moduleNames.length - 1,
                itemBuilder: (context, i) {
                  final index = i + 1;
                  return Card(child: InkWell(
                    borderRadius: BorderRadius.circular(12),
                    onTap: () => widget.onNavigate(index),
                    child: Padding(
                      padding: const EdgeInsets.all(16),
                      child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
                        Icon(widget.moduleIcons[index], size: 30),
                        const Spacer(),
                        Text(widget.moduleNames[index], style: const TextStyle(fontWeight: FontWeight.bold)),
                      ]),
                    ),
                  ));
                },
              );
            }),
          ] else Card(child: Padding(
            padding: const EdgeInsets.all(24),
            child: Row(children: [
              Icon(widget.icon, size: 42),
              const SizedBox(width: 16),
              const Expanded(child: Text('Módulo preparado. Aquí agregaremos el listado, formulario y operaciones de este módulo.')),
            ]),
          )),
        ]),
      )),
    );
  }
}
