INSERT INTO `users` (
    `login`,
    `password`,
    `is_active`,
    `role_id`
) VALUES (
             'Anna12',
             md5('Anna12'), /* MD5 хэш пароля "admin" */
             true,
             2
         );