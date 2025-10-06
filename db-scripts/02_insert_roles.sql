ALTER SESSION SET CONTAINER = XEPDB1;
ALTER SESSION SET CURRENT_SCHEMA = PHARMACY_LOCAL;

DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count FROM ROL WHERE NOMBRE_ROL = 'Administrador';
  IF v_count = 0 THEN
    INSERT INTO ROL (NOMBRE_ROL) VALUES ('Administrador');
    DBMS_OUTPUT.PUT_LINE('Inserted Administrador role');
  END IF;
  
  SELECT COUNT(*) INTO v_count FROM ROL WHERE NOMBRE_ROL = 'Empleado';
  IF v_count = 0 THEN
    INSERT INTO ROL (NOMBRE_ROL) VALUES ('Empleado');
    DBMS_OUTPUT.PUT_LINE('Inserted Empleado role');
  END IF;
  
  SELECT COUNT(*) INTO v_count FROM ROL WHERE NOMBRE_ROL = 'Paciente';
  IF v_count = 0 THEN
    INSERT INTO ROL (NOMBRE_ROL) VALUES ('Paciente');
    DBMS_OUTPUT.PUT_LINE('Inserted Paciente role');
  END IF;
  
  SELECT COUNT(*) INTO v_count FROM ROL WHERE NOMBRE_ROL = 'Usuario de Interconexión';
  IF v_count = 0 THEN
    INSERT INTO ROL (NOMBRE_ROL) VALUES ('Usuario de Interconexión');
    DBMS_OUTPUT.PUT_LINE('Inserted Usuario de Interconexión role');
  END IF;
END;
/

DECLARE
  v_count NUMBER;
  v_admin_id NUMBER;
  v_admin_role_id NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count FROM USUARIO WHERE CORREO = 'admin@pharmacy.com';
  
  IF v_count = 0 THEN
    INSERT INTO USUARIO (
      NOMBRE, 
      CORREO, 
      PASSWORD_HASH, 
      ACTIVO, 
      FECHA_CREACION,
      PERFIL_COMPLETO,
      PRIMER_LOGIN
    ) VALUES (
      'Administrator', 
      'admin@pharmacy.com', 
      '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', -- password: 'admin'
      'Y', 
      SYSTIMESTAMP,
      'Y',
      'N'
    ) RETURNING ID_USUARIO INTO v_admin_id;
    
    SELECT ID_ROL INTO v_admin_role_id FROM ROL WHERE NOMBRE_ROL = 'Administrador';
    
    INSERT INTO USUARIOROL (ID_USUARIO, ID_ROL) VALUES (v_admin_id, v_admin_role_id);
    
    DBMS_OUTPUT.PUT_LINE('Created default admin user: admin@pharmacy.com with password: admin');
  END IF;
END;
/

COMMIT; 
