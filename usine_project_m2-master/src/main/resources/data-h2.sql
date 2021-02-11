SET MODE MYSQL; 

CREATE SCHEMA IF NOT EXISTS "public";


INSERT INTO `categorie` (`id`, `name`)
VALUES (1,'Sport'),
       (2,'Livre'),
       (3,'Jeux De Filles'),
       (4,'Reflexion');


INSERT INTO `role` (`id`, `name`)
VALUES (1,'ADMIN'),
       (2,'USER');


INSERT INTO `competence` (`id`, `name`)
VALUES (1,'CREATIVITE'),
       (2,'ELECTRONIQUE'),
       (3,'CHARPENTERIE'),
       (4,'L’AUTONOMIE'),
       (5,'POLYVALENCE'),
       (6,'MACONNERIE');


INSERT INTO `user` (`id`, `created_by`, `creation_date`, `email`, `name`, `password`, `role_id`)
VALUES (1,NULL,'2021-01-31 15:12:25.721000','papa@admin.fr','PAPA NOEL','$2a$10$pEPLOctpddhrWlY9uZpiiOTdnCa783ps6kaOHsx.8rGt84jvqbYCq',1),
       (2,NULL,'2021-01-31 15:12:25.820000','mere@admin.fr','MAMA NOEL','$2a$10$J5GpCvynLxd7sAn9FqOpf.gN9eQSPbbUVaHG4Vjav4axEEMYxh2qe',1),
       (3,1,'2021-01-31 15:12:25.913000','user1@user.fr','USER 1','$2a$10$q/tf.kk/q8WEE.sIG8oVv.NAZ7h5LW/27VJr/K09frFKEINNFQl2C',2),
       (4,2,'2021-01-31 15:12:26.006000','user2@user.fr','USER 2','$2a$10$clWH6G.FtOCMkrzVzKYujOEOVCgIXij4406utZPQYEwX7Y95j5C7K',2),
       (5,NULL,'2021-01-31 15:12:26.098000','user3@admin.fr','USER 2','$2a$10$Z29Oeq7nSFoES997R3/AG.qbtHZNJOGLsSo95pb0iDLcGmnRtE5Y.',2),
       (6,1,'2021-01-31 15:12:46.294000','merouan@merouan.fr','merouan','$2a$10$JoH/FHv2YryvvDb1O2vraeHwnkf8Fag.7hSgvY3Ouapagp6LvER9e',2),
       (7,1,'2021-01-31 15:13:30.939000','sofiane@sofiane.fr','sofiane','$2a$10$hQbj7sioySFV8owVD7SLO.urqzLRppAJZeVoVjs1d81j9o/CAN3Nm',2),
       (8,1,'2021-01-31 15:32:37.159000','anis@anis.fr','anis','$2a$10$MYEoczS5xSJxAXGdLarsfeofY/1xjDrDZXxwjiQE14.0kc0XQ0A0G',2),
       (9,1,'2021-01-31 15:36:25.750000','ramzi@ramzi.fr','ramzi','$2a$10$H46BOI.fv/f5VuDei2i1VuQeZSn3csPIiinlwNsozNaUnoA/3POZS',2);


INSERT INTO `commande` (`id`, `comment`, `date_creation`)
VALUES (1,'Jeux Pour Deux Frére Et Soeur','2021-01-31 15:29:26.204000'),
       (2,'Jeux d Electronique Pour Le Fils D un Client','2021-01-31 15:34:35.351000'),
       (3,'Jeux Pour Une Petite Fille D un Client','2021-01-31 15:37:23.329000'),
       (4,'Jeux De Reflexion Pour Un Enfant','2021-01-31 15:42:15.161000'),
       (5,'Jeux De Foot Pour Un Enfant Sportif','2021-01-31 15:44:18.184000'),
       (6,'Jeux De police Pour Un Enfant','2021-01-31 15:48:02.106000'),
       (7,'Jeux De Reflexion Pour Deux Frères','2021-01-31 15:53:34.815000'),
       (8,'Méga Commande Pour une famille','2021-01-31 16:01:11.043000'),
       (9,'Commande Simple','2021-01-31 16:12:51.758000'),
       (10,'Commande Rapide','2021-01-31 16:18:50.581000');


INSERT INTO `jeux` (`id`, `duree`, `is_archived`, `name`, `statut`, `categorie_id`)
VALUES (1,10,FALSE,'raquette de tennis ',NULL,1),
       (2,3,FALSE,'Poupée Barbie ',NULL,3),
       (3,10,FALSE,'Puzzle ',NULL,4),
       (4,10,FALSE,'jeu de dominos ',NULL,4),
       (5,20,FALSE,'Corde à Sauter',NULL,3),
       (6,100,FALSE,'Game Boy',NULL,1),
       (7,2,FALSE,'Chess',NULL,4),
       (8,2,FALSE,'Ballon De Foot',NULL,1),
       (9,2,FALSE,'Jeux De Police',NULL,1),
       (10,2,FALSE,'Rubiks Cube',NULL,4),
       (11,8,FALSE,'Rubiks Cube',NULL,4);


INSERT INTO `jeux_competence` (`jeux_id`, `comp_id`)
VALUES (1,3),
       (1,4),
       (2,1),
       (2,5),
       (3,1),
       (3,2),
       (3,6),
       (4,1),
       (4,2),
       (4,3),
       (4,6),
       (5,5),
       (6,1),
       (6,2),
       (7,1),
       (7,2),
       (8,4),
       (8,5),
       (9,4),
       (9,5),
       (9,6),
       (10,1),
       (10,6),
       (11,1);


INSERT INTO `tache` (`id`, `date_affectation`, `date_fin`, `commande_id`, `jeux_id`, `lutin_id`)
VALUES (1,'2021-01-31 15:29:26.211000','2021-01-31 15:41:26.211000',1,1,6),
       (2,'2021-01-31 15:29:26.220000','2021-01-31 15:32:26.220000',1,2,7),
       (3,'2021-01-31 15:34:35.354000','2021-01-31 17:14:35.354000',2,6,8),
       (4,'2021-01-31 15:37:23.332000','2021-01-31 15:59:23.332000',3,5,9),
       (5,'2021-01-31 15:42:15.165000','2021-01-31 15:44:15.165000',4,7,6),
       (6,'2021-01-31 15:44:18.186000','2021-01-31 15:46:18.186000',5,8,7),
       (7,'2021-01-31 15:48:02.110000','2021-01-31 15:51:02.110000',6,9,6),
       (8,'2021-01-31 15:53:34.818000','2021-01-31 15:55:34.818000',7,7,6),
       (9,'2021-01-31 15:53:34.823000','2021-01-31 15:55:34.823000',7,10,7),
       (10,'2021-01-31 16:01:11.045000','2021-01-31 16:04:11.045000',8,2,6),
       (11,'2021-01-31 16:01:11.048000','2021-01-31 16:14:11.048000',8,4,7),
       (12,'2021-01-31 16:01:11.051000','2021-01-31 16:03:11.051000',8,8,9),
       (13,'2021-01-31 16:12:51.760000','2021-01-31 16:15:51.760000',9,2,6),
       (14,'2021-01-31 16:12:51.764000','2021-01-31 16:14:51.764000',9,8,8),
       (15,'2021-01-31 16:18:50.583000','2021-01-31 16:21:50.583000',10,2,9);


INSERT INTO `user_competence` (`user_id`, `compt_id`)
VALUES (6,1),
       (7,1),
       (8,1),
       (8,2),
       (9,1),
       (9,2);