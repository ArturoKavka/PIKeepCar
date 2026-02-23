-- KEEPCAR v3.0 — Base de Datos
-- Admins: arturo, amael | 10 usuarios | 21 vehículos
-- ================================================================

INSERT INTO usuario (id, username, password, nombre_completo, email, telefono, rol, activo, avatar_iniciales, fecha_registro) VALUES
-- Administradores
(1,  'arturo',   'Arturo@Admin1',       'Arturo Kavka',             'arturo@keepcar.es',         '+34 612 111 222', 'ADMIN', true, 'AK', DATEADD('DAY',-180,CURRENT_TIMESTAMP)),
(2,  'amael',    'Amael@Admin2',        'Amael Silva',              'amael@keepcar.es',          '+34 613 333 444', 'ADMIN', true, 'AS', DATEADD('DAY',-180,CURRENT_TIMESTAMP)),
-- Usuarios normales
(3,  'norly',    'Norly@2025!',         'Norly García',             'norly.garcia@mail.es',      '+34 614 555 666', 'USER',  true, 'NG', DATEADD('DAY',-175,CURRENT_TIMESTAMP)),
(4,  'juan',     'Juan#Garc1a',         'Juan García Torres',       'juan.garcia@mail.es',       '+34 655 100 200', 'USER',  true, 'JG', DATEADD('DAY',-120,CURRENT_TIMESTAMP)),
(5,  'marcos',   'Marc0s!2025',         'Marcos López Ruiz',        'marcos.lopez@mail.es',      '+34 666 300 400', 'USER',  true, 'ML', DATEADD('DAY',-90, CURRENT_TIMESTAMP)),
(6,  'manu',     'Manu.H3rn',           'Manu Hernández Gil',       'manu.hgil@mail.es',         '+34 677 500 600', 'USER',  true, 'MH', DATEADD('DAY',-60, CURRENT_TIMESTAMP)),
(7,  'rosa',     'Rosa&Fl0res',         'Rosa Martínez Vega',       'rosa.mvega@mail.es',        '+34 688 700 800', 'USER',  true, 'RM', DATEADD('DAY',-45, CURRENT_TIMESTAMP)),
(8,  'lucia',    'Lucia*Cars25',        'Lucía Fernández Ortiz',    'lucia.ford@mail.es',        '+34 699 800 900', 'USER',  true, 'LF', DATEADD('DAY',-30, CURRENT_TIMESTAMP)),
(9,  'pedro',    'P3dro!Motor',         'Pedro Sánchez Díaz',       'pedro.sd@mail.es',          '+34 611 200 300', 'USER',  true, 'PS', DATEADD('DAY',-20, CURRENT_TIMESTAMP)),
(10, 'sofia',    'S0fia@Drive',         'Sofía Ruiz Moreno',        'sofia.rm@mail.es',          '+34 622 400 500', 'USER',  true, 'SR', DATEADD('DAY',-10, CURRENT_TIMESTAMP)),
(11, 'denise',   'D3nise@Garr1do',      'Denise Garrido Tamarit',   'denise.garrido@mail.es',    '+34 633 500 600', 'USER',  true, 'DG', DATEADD('DAY',-5,  CURRENT_TIMESTAMP));

-- ================================================================
-- VEHÍCULOS (21 coches con ITV y estado de neumáticos)
-- ================================================================
INSERT INTO vehiculo (id, marca, modelo, anio, matricula,
  kilometraje_actual, ultimo_cambio_aceite_km, proximo_cambio_aceite_km,
  motor_cc, potencia_cv, combustible, transmision, color, num_puertas, num_bastidor,
  fecha_ultima_itv, fecha_proxima_itv,
  anio_fabricacion_neumaticos, dibujo_neumatico_mm,
  propietario_id, activo, fecha_registro) VALUES

-- Arturo
(1,  'BMW',       'Serie 3 320d',       2021,'4521-KAV',  67800, 65000,  75000, 1995,190,'Diésel',    'Manual 6V',      'Negro Carbono',    4,'WBA5C71060G123456','2023-10-15','2025-10-15',2022,4.8, 1,true,DATEADD('DAY',-170,CURRENT_TIMESTAMP)),
(2,  'Porsche',   'Cayenne 3.0 TDI',   2020,'9900-KAV',  42300, 40000,  52000, 2967,231,'Diésel',    'Automático 8G',  'Plata Ártico',     5,'WP1ZZZ9PZLA123456','2023-05-20','2025-05-20',2021,6.1, 1,true,DATEADD('DAY',-80, CURRENT_TIMESTAMP)),

-- Amael
(3,  'Audi',      'A4 2.0 TDI',        2020,'3312-SIL',  89500, 88000,  98000, 1968,150,'Diésel',    'Manual 6V',      'Gris Daytona',     4,'WAUZZZ8K3BA012345','2024-03-10','2026-03-10',2019,3.2, 2,true,DATEADD('DAY',-160,CURRENT_TIMESTAMP)),
(4,  'Volkswagen','Passat 2.0 TDI',    2019,'7754-SIL', 112300,109000, 119000, 1968,150,'Diésel',    'DSG 7V',         'Azul Lapislázuli', 4,'WVWZZZ3CZKE123456','2023-11-01','2025-11-01',2018,2.6, 2,true,DATEADD('DAY',-140,CURRENT_TIMESTAMP)),

-- Norly
(5,  'Tesla',     'Model 3 LR',        2022,'0001-NGA',  38000,     0,      0,    0,358,'Eléctrico', 'Automático',     'Blanco Perla',     4,'5YJ3E1EA8NF123456','2024-07-15','2026-07-15',2022,7.2, 3,true,DATEADD('DAY',-50, CURRENT_TIMESTAMP)),
(6,  'SEAT',      'Ibiza 1.0 TSI',     2021,'3344-NGA',  55200, 52000,  62000,  999, 95,'Gasolina',  'Manual 6V',      'Rojo Desire',      5,'VSSZZZ6JZMR123456','2024-01-20','2026-01-20',2020,5.5, 3,true,DATEADD('DAY',-40, CURRENT_TIMESTAMP)),

-- Juan
(7,  'SEAT',      'León 1.5 TSI',      2019,'1122-JGA',  87500, 85000,  90000, 1498,130,'Gasolina',  'DSG 7V',         'Rojo Desire',      5,'VSSZZZ5FZFR123456','2023-12-05','2025-12-05',2020,4.1, 4,true,DATEADD('DAY',-115,CURRENT_TIMESTAMP)),
(8,  'Ford',      'Focus 1.5 EcoBoost',2018,'4433-JGA', 134600,133000, 138000, 1498,150,'Gasolina',  'Manual 6V',      'Azul Panther',     5,'1FADP3F20FL123456','2024-02-14','2026-02-14',2017,2.3, 4,true,DATEADD('DAY',-115,CURRENT_TIMESTAMP)),
(9,  'Renault',   'Clio 1.0 TCe',      2022,'9987-JGA',  18900, 15000,  25000,  999, 90,'Gasolina',  'Manual 5V',      'Naranja Valencia', 5,'VF15RSN0B62123456','2024-09-01','2026-09-01',2022,6.8, 4,true,DATEADD('DAY',-30, CURRENT_TIMESTAMP)),

-- Marcos
(10, 'Toyota',    'Corolla 1.8 HEV',   2021,'5566-LRZ',  45800, 44000,  54000, 1798,122,'Híbrido',   'Automático CVT', 'Gris Lunar',       4,'SB1Z93BE90E123456','2024-06-10','2026-06-10',2021,5.9, 5,true,DATEADD('DAY',-85, CURRENT_TIMESTAMP)),
(11, 'Kia',       'Sportage 1.6 CRDi', 2020,'3344-LRZ',  78200, 74000,  84000, 1598,136,'Diésel',    'Manual 6V',      'Verde Military',   5,'KNAPB813BK7123456','2023-08-22','2025-08-22',2019,3.8, 5,true,DATEADD('DAY',-85, CURRENT_TIMESTAMP)),

-- Manu
(12, 'Honda',     'Civic 1.5 VTEC',    2020,'2211-HGI',  62400, 60000,  70000, 1498,182,'Gasolina',  'Manual 6V',      'Rojo Rally',       5,'2HGES16503H123456','2024-04-18','2026-04-18',2020,4.7, 6,true,DATEADD('DAY',-55, CURRENT_TIMESTAMP)),
(13, 'Nissan',    'Qashqai 1.3 DIG-T', 2021,'6677-HGI',  41300, 40000,  50000, 1332,140,'Gasolina',  'Manual 6V',      'Blanco Glaciar',   5,'SJNFAAJ11U1123456','2024-08-30','2026-08-30',2021,6.3, 6,true,DATEADD('DAY',-55, CURRENT_TIMESTAMP)),
(14, 'Peugeot',   '308 1.2 PureTech',  2019,'8899-HGI',  97600, 96000, 101000, 1199,130,'Gasolina',  'EAT8 Auto',      'Gris Platinium',   5,'VF3LBBHXHHS123456','2023-07-12','2025-07-12',2018,2.8, 6,true,DATEADD('DAY',-55, CURRENT_TIMESTAMP)),

-- Rosa
(15, 'Hyundai',   'Tucson 1.6 CRDI',   2022,'1155-MVG',  28700, 25000,  35000, 1598,136,'Diésel',    'Manual 6V',      'Azul Abyss',       5,'TMAJ381ABMJ123456','2024-11-05','2026-11-05',2022,6.0, 7,true,DATEADD('DAY',-40, CURRENT_TIMESTAMP)),
(16, 'Opel',      'Corsa 1.2 Turbo',   2023,'4422-MVG',   9200,  5000,  15000, 1199,100,'Gasolina',  'Manual 6V',      'Rosa Blush',       5,'W0L000050T2123456','2025-01-10','2027-01-10',2023,7.5, 7,true,DATEADD('DAY',-40, CURRENT_TIMESTAMP)),

-- Lucía
(17, 'Mazda',     'CX-5 2.2 Skyactiv', 2021,'7788-LFO',  52100, 50000,  60000, 2191,184,'Diésel',    'Manual 6V',      'Azul Polinesia',   5,'JMZKE22L201123456','2024-05-25','2026-05-25',2021,5.1, 8,true,DATEADD('DAY',-25, CURRENT_TIMESTAMP)),
(18, 'Mini',      'Cooper S 2.0',      2022,'9900-LFO',  31800, 30000,  40000, 1998,192,'Gasolina',  'Manual 6V',      'Chili Red',        3,'WMW83DH0X01123456','2024-12-01','2026-12-01',2022,6.9, 8,true,DATEADD('DAY',-25, CURRENT_TIMESTAMP)),

-- Pedro
(19, 'Alfa Romeo','Giulia 2.0 Turbo',  2020,'5544-PSD',  73400, 70000,  80000, 1995,200,'Gasolina',  'Automático 8G',  'Rojo Alfa',        4,'ZAREAM2B001123456','2023-09-17','2025-09-17',2019,3.5, 9,true,DATEADD('DAY',-15, CURRENT_TIMESTAMP)),

-- Sofía
(20, 'Volvo',     'XC40 B4',           2023,'2233-SRM',  19500, 15000,  25000, 1969,197,'Mild-Hybrid','Automático 8G', 'Beige Birch',      5,'YV1XZABH2P1123456','2025-03-20','2027-03-20',2023,7.8,10,true,DATEADD('DAY',-8,  CURRENT_TIMESTAMP)),

-- Denise
(21, 'Renault',   'Megane 1.5 dCi',    2020,'6600-DGT',  68000, 65000,  75000, 1461,115,'Diésel',    'Manual 6V',      'Azul Hierro',      5,'VF1RFBZ0B56123456','2024-06-01','2026-06-01',2019,4.2,11,true,DATEADD('DAY',-3,  CURRENT_TIMESTAMP));

-- ================================================================
-- HISTORIAL DE MANTENIMIENTO
-- ================================================================
INSERT INTO registro_mantenimiento (id, vehiculo_id, tipo_mantenimiento, descripcion, kilometraje_realizado, fecha_mantenimiento, coste, realizado_por) VALUES
(1,  1, 'CAMBIO_ACEITE',    'BMW Genuine 5W-30 + filtro OEM',                65000,DATEADD('DAY',-35,CURRENT_TIMESTAMP),125.00,'BMW Service Valencia'),
(2,  1, 'CAMBIO_FRENOS',    'Pastillas + discos EBC Performance',             58000,DATEADD('DAY',-80,CURRENT_TIMESTAMP),380.00,'BMW Service Valencia'),
(3,  1, 'REVISION_GENERAL', 'Revisión 60.000 km completa',                   60000,DATEADD('DAY',-60,CURRENT_TIMESTAMP),550.00,'BMW Service Valencia'),
(4,  2, 'CAMBIO_ACEITE',    'Porsche Mobil 1 0W-40 + filtro',                40000,DATEADD('DAY',-20,CURRENT_TIMESTAMP),180.00,'Porsche Centre Valencia'),
(5,  3, 'CAMBIO_ACEITE',    'Castrol Edge 5W-40 Audi + filtro',              88000,DATEADD('DAY',-20,CURRENT_TIMESTAMP),109.00,'Audi Center Valencia'),
(6,  3, 'CAMBIO_NEUMATICOS','4x Michelin Pilot Sport 225/45 R17',            82000,DATEADD('DAY',-50,CURRENT_TIMESTAMP),620.00,'Michelin Dealer VLC'),
(7,  4, 'CAMBIO_ACEITE',    'VW Longlife 5W-30 + filtro OEM',               109000,DATEADD('DAY',-15,CURRENT_TIMESTAMP), 95.00,'VW Service Valencia'),
(8,  4, 'REVISION_GENERAL', 'Rev. 100.000 km: correa distribución',         100000,DATEADD('DAY',-60,CURRENT_TIMESTAMP),890.00,'VW Service Valencia'),
(9,  7, 'CAMBIO_ACEITE',    'SEAT 5W-30 + filtro + vaciado',                 85000,DATEADD('DAY',-30,CURRENT_TIMESTAMP), 79.00,'Taller Central VLC'),
(10, 8, 'CAMBIO_ACEITE',    'Ford Motorcraft 5W-20 + filtro',               133000,DATEADD('DAY',-10,CURRENT_TIMESTAMP), 85.00,'Ford Dealer VLC'),
(11, 8, 'REVISION_GENERAL', 'Rev. 130.000 km: bujías, filtros, líquidos',  130000,DATEADD('DAY',-40,CURRENT_TIMESTAMP),320.00,'Ford Dealer VLC'),
(12,10, 'CAMBIO_ACEITE',    'Toyota Genuine 0W-20 SN + filtro',              44000,DATEADD('DAY',-25,CURRENT_TIMESTAMP), 89.00,'Toyota Oficial VLC'),
(13,11, 'CAMBIO_ACEITE',    'Castrol Edge 5W-30 + filtro Kia',               74000,DATEADD('DAY',-35,CURRENT_TIMESTAMP), 92.00,'Kia Service Center'),
(14,11, 'CAMBIO_NEUMATICOS','4x Goodyear EfficientGrip 215/65 R16',          68000,DATEADD('DAY',-70,CURRENT_TIMESTAMP),480.00,'AutoGom Valencia'),
(15,12, 'CAMBIO_ACEITE',    'Honda Genuine HQ 5W-30 + filtro',               60000,DATEADD('DAY',-18,CURRENT_TIMESTAMP), 88.00,'Honda Plus Valencia'),
(16,14, 'CAMBIO_ACEITE',    'Total Quartz 5W-30 + filtro',                   96000,DATEADD('DAY',-12,CURRENT_TIMESTAMP), 80.00,'Peugeot Service VLC'),
(17,14, 'REVISION_GENERAL', 'Rev. 90.000 km: distribución + agua',           90000,DATEADD('DAY',-45,CURRENT_TIMESTAMP),750.00,'Peugeot Service VLC'),
(18,15, 'CAMBIO_ACEITE',    'Hyundai Genuine 5W-30 + filtro',                25000,DATEADD('DAY',-22,CURRENT_TIMESTAMP), 82.00,'Hyundai Motopolis'),
(19,17, 'CAMBIO_ACEITE',    'Mazda Original 5W-30 + filtro',                 50000,DATEADD('DAY',-15,CURRENT_TIMESTAMP), 95.00,'Mazda Premium VLC'),
(20,19, 'CAMBIO_ACEITE',    'Castrol Magnatec 5W-40 + filtro',               70000,DATEADD('DAY',-8, CURRENT_TIMESTAMP), 99.00,'Alfa Romeo Center'),
(21,19, 'CAMBIO_FRENOS',    'Brembo Sport pastillas delanteras + traseras',  65000,DATEADD('DAY',-40,CURRENT_TIMESTAMP),290.00,'Alfa Romeo Center'),
(22,20, 'CAMBIO_ACEITE',    'Volvo 0W-20 Dexos + filtro OEM',               15000,DATEADD('DAY',-5, CURRENT_TIMESTAMP),110.00,'Volvo Official VLC'),
(23, 5, 'CAMBIO_FILTROS',   'Filtro habitáculo HEPA + revisión software',    35000,DATEADD('DAY',-10,CURRENT_TIMESTAMP), 45.00,'Tesla Service Center'),
(24, 6, 'CAMBIO_ACEITE',    'SEAT Longlife 5W-30 + filtro',                  52000,DATEADD('DAY',-20,CURRENT_TIMESTAMP), 69.00,'SEAT Dealer VLC'),
(25,18, 'REVISION_GENERAL', 'Mini 30.000 km: filtros, pastillas, ITV prep.', 30000,DATEADD('DAY',-18,CURRENT_TIMESTAMP),350.00,'Mini Service VLC'),
(26,21, 'CAMBIO_ACEITE',    'Renault Genuine 5W-30 + filtro + revisión',     65000,DATEADD('DAY',-3, CURRENT_TIMESTAMP), 78.00,'Renault Service VLC');

-- ================================================================
-- NOTIFICACIONES
-- ================================================================
INSERT INTO notificacion (id, vehiculo_id, usuario_id, tipo, mensaje, leida, fecha) VALUES
-- Arturo
(1,  1,1,'CAMBIO_ACEITE_URGENTE','🔴 BMW Serie 3: aceite VENCIDO en 2.800 km. Cambio urgente.',true, DATEADD('DAY',-40,CURRENT_TIMESTAMP)),
(2,  1,1,'SISTEMA_INFO',        '✅ BMW Serie 3: cambio de aceite a los 65.000 km.',          true, DATEADD('DAY',-35,CURRENT_TIMESTAMP)),
(3,  1,1,'CAMBIO_ACEITE_PROXIMO','🟡 BMW Serie 3: quedan 700 km para próximo cambio.',         false,DATEADD('DAY',-3, CURRENT_TIMESTAMP)),
(4,  1,1,'ITV_PROXIMA',         '📋 BMW Serie 3: ITV vence el 15/10/2025.',                   false,DATEADD('DAY',-5, CURRENT_TIMESTAMP)),
(5,  2,1,'SISTEMA_INFO',        '✅ Porsche Cayenne registrado correctamente.',                true, DATEADD('DAY',-80,CURRENT_TIMESTAMP)),
(6,  2,1,'CAMBIO_ACEITE_PROXIMO','🟡 Porsche Cayenne: próximo cambio a 52.000 km.',           false,DATEADD('DAY',-2, CURRENT_TIMESTAMP)),
-- Amael
(7,  3,2,'CAMBIO_ACEITE_URGENTE','🔴 Audi A4: aceite vencido. Km actual 89.500.',             true, DATEADD('DAY',-25,CURRENT_TIMESTAMP)),
(8,  3,2,'SISTEMA_INFO',        '✅ Cambio de aceite Audi A4 a los 88.000 km.',               true, DATEADD('DAY',-20,CURRENT_TIMESTAMP)),
(9,  4,2,'CAMBIO_ACEITE_URGENTE','🔴 VW Passat: aceite URGENTE. Superado 3.300 km.',          true, DATEADD('DAY',-20,CURRENT_TIMESTAMP)),
(10, 4,2,'NEUMATICOS',          '⚠️ VW Passat: neumáticos con dibujo 2.6mm. ¡Cambiar!',      false,DATEADD('DAY',-4, CURRENT_TIMESTAMP)),
(11, 4,2,'ITV_PROXIMA',         '📋 VW Passat: ITV vence el 01/11/2025.',                     false,DATEADD('DAY',-3, CURRENT_TIMESTAMP)),
-- Norly
(12, 5,3,'SISTEMA_INFO',        '✅ Tesla Model 3 registrado. Sin cambios de aceite.',         true, DATEADD('DAY',-50,CURRENT_TIMESTAMP)),
(13, 6,3,'CAMBIO_ACEITE_PROXIMO','🟡 SEAT Ibiza: quedan 800 km para el cambio.',              false,DATEADD('DAY',-1, CURRENT_TIMESTAMP)),
-- Juan
(14, 7,4,'CAMBIO_ACEITE_PROXIMO','🟡 SEAT León: quedan 500 km para el cambio.',               false,DATEADD('DAY',-2, CURRENT_TIMESTAMP)),
(15, 7,4,'ITV_PROXIMA',         '📋 SEAT León: ITV vence dic/2025. Pide cita.',               false,DATEADD('DAY',-7, CURRENT_TIMESTAMP)),
(16, 8,4,'CAMBIO_ACEITE_PROXIMO','🟡 Ford Focus: quedan 400 km para el cambio. Urgente.',     false,DATEADD('DAY',-1, CURRENT_TIMESTAMP)),
(17, 8,4,'NEUMATICOS',          '🔧 Ford Focus: neumáticos de 2017 (8 años). ¡Cambiar!',     false,DATEADD('DAY',-2, CURRENT_TIMESTAMP)),
-- Marcos
(18,10,5,'SISTEMA_INFO',        '✅ Toyota Corolla: cambio de aceite a los 44.000 km.',        true, DATEADD('DAY',-25,CURRENT_TIMESTAMP)),
(19,11,5,'CAMBIO_ACEITE_URGENTE','🔴 Kia Sportage: aceite VENCIDO 4.200 km.',                 true, DATEADD('DAY',-40,CURRENT_TIMESTAMP)),
(20,11,5,'NEUMATICOS',          '⚠️ Kia Sportage: neumáticos 3.8mm. Revisa pronto.',          false,DATEADD('DAY',-3, CURRENT_TIMESTAMP)),
(21,11,5,'ITV_PROXIMA',         '📋 Kia Sportage: ITV vence ago/2025. ¡Muy urgente!',         false,DATEADD('DAY',-1, CURRENT_TIMESTAMP)),
-- Manu
(22,12,6,'SISTEMA_INFO',        '✅ Honda Civic: cambio de aceite a 60.000 km.',               true, DATEADD('DAY',-18,CURRENT_TIMESTAMP)),
(23,14,6,'CAMBIO_ACEITE_URGENTE','🔴 Peugeot 308: aceite VENCIDO. Km actual 97.600.',         false,DATEADD('DAY',-3, CURRENT_TIMESTAMP)),
(24,14,6,'NEUMATICOS',          '⚠️ Peugeot 308: neumáticos 2.8mm. Cambiar pronto.',          false,DATEADD('DAY',-2, CURRENT_TIMESTAMP)),
(25,14,6,'ITV_PROXIMA',         '📋 Peugeot 308: ITV vence jul/2025. ¡URGENTE!',              false,DATEADD('DAY',-1, CURRENT_TIMESTAMP)),
-- Denise
(26,21,11,'SISTEMA_INFO',       '🚗 Renault Megane registrado. ¡Bienvenida a KeepCar!',       true, DATEADD('DAY',-3, CURRENT_TIMESTAMP)),
(27,21,11,'CAMBIO_ACEITE_PROXIMO','🟡 Renault Megane: quedan 700 km para el cambio de aceite.',false,DATEADD('DAY',-1,CURRENT_TIMESTAMP));

-- Reiniciar secuencia de ID de usuario por encima del último insertado
ALTER TABLE usuario ALTER COLUMN id RESTART WITH 200;

-- Asegurar que arturo y amael tienen rol ADMIN
UPDATE usuario SET rol = 'ADMIN' WHERE username IN ('arturo', 'amael');
