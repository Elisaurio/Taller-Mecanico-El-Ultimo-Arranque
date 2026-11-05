import 'package:flutter/material.dart';
import 'screens/home_shell.dart';

void main() {
  runApp(const TallerApp());
}

class TallerApp extends StatelessWidget {
  const TallerApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'El Último Arranque',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        useMaterial3: true,
        colorScheme: ColorScheme.fromSeed(seedColor: const Color(0xFF164E63)),
        scaffoldBackgroundColor: const Color(0xFFF6F8FA),
      ),
      home: const HomeShell(),
    );
  }
}
