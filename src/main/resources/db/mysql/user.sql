CREATE DATABASE IF NOT EXISTS resify;

ALTER DATABASE resify
  DEFAULT CHARACTER SET utf8
  DEFAULT COLLATE utf8_general_ci;

GRANT ALL PRIVILEGES ON resify.* TO 'resify@%' IDENTIFIED BY 'resify';


-- EJECUTAR LO SIGUIENTE COMO SCRIPT EN MYSQL WORKBENCH (se descomenta seleccionando el bloque y pulsando ctrl+7)--

-- CREATE DATABASE IF NOT EXISTS resify;
-- 
-- ALTER DATABASE resify
--   DEFAULT CHARACTER SET utf8
--   DEFAULT COLLATE utf8_general_ci;
-- 
-- drop user if exists 'resify'@'%';
-- create user 'resify'@'%' identified with mysql_native_password by 'resify';
-- GRANT ALL PRIVILEGES ON resify.* TO 'resify'@'%';