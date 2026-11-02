
-- ISY2202 - Semana 2
-- Datos iniciales de recetas
SET NAMES utf8mb4;

USE recetas_db;

INSERT INTO recetas (nombre,tipo_cocina,pais_origen,ingredientes,instrucciones,tiempo_coccion_min,dificultad,fotografia,visitas,fecha_creacion) VALUES
	 ('Pastel de choclo','Tradicional','Chile','Choclo, carne molida, cebolla, pollo, huevo, aceitunas y albahaca','Preparar el pino con carne y cebolla. Cocinar el pollo. Moler el choclo con albahaca. Colocar los ingredientes en una fuente, cubrir con la mezcla de choclo y hornear hasta dorar.',60,'Media','/img/pastel-choclo.jpg',120,'2026-10-31 01:02:23'),
	 ('Lasaña italiana','Italiana','Italia','Pasta para lasaña, carne molida, tomate, cebolla, queso y salsa bechamel','Preparar una salsa de tomate con carne. Alternar capas de pasta, salsa, bechamel y queso. Hornear hasta que la pasta esté cocida y el queso gratinado.',75,'Media','/img/lasana.jpg',95,'2026-10-31 01:02:23'),
	 ('Sushi japonés','Japonesa','Japón','Arroz para sushi, alga nori, salmón, palta, pepino y vinagre de arroz','Cocinar y condimentar el arroz. Extenderlo sobre el alga nori, incorporar el relleno, enrollar cuidadosamente y cortar en porciones.',50,'Alta','/img/sushi.jpg',110,'2026-10-31 01:02:23'),
	 ('Empanadas chilenas','Tradicional','Chile','Harina, carne, cebolla, huevo, aceitunas, comino y manteca','Preparar la masa y el pino de carne. Rellenar los discos de masa, cerrar las empanadas y hornear hasta que estén doradas.',70,'Media','/img/empanadas.jpg',150,'2026-10-31 01:02:23'),
	 ('Pizza margarita','Italiana','Italia','Harina, levadura, tomate, mozzarella, albahaca y aceite de oliva','Preparar y estirar la masa. Agregar salsa de tomate, mozzarella y aceite de oliva. Hornear y terminar con hojas de albahaca.',35,'Baja','/img/pizza.jpg',135,'2026-10-31 01:02:23'),
	 ('Tacos mexicanos','Mexicana','México','Tortillas de maíz, carne, cebolla, tomate, cilantro y limón','Cocinar la carne con condimentos. Calentar las tortillas, agregar la carne y completar con cebolla, tomate, cilantro y limón.',30,'Baja','/img/tacos.jpg',105,'2026-10-31 01:02:23');
