import 'dart:convert';
import 'package:flutter/foundation.dart';
import 'package:http/http.dart' as http;

class ApiService {
  // En Android Emulator, 10.0.2.2 apunta al equipo anfitrión.
  // En Chrome local se utiliza localhost.
  String get baseUrl => kIsWeb ? 'http://localhost:8080' : 'http://10.0.2.2:8080';

  Future<String> healthCheck() async {
    final response = await http.get(Uri.parse('$baseUrl/api/health'))
        .timeout(const Duration(seconds: 5));
    if (response.statusCode != 200) {
      throw Exception('HTTP ${response.statusCode}');
    }
    final json = jsonDecode(response.body) as Map<String, dynamic>;
    return json['message'] as String? ?? 'Backend conectado';
  }
}
