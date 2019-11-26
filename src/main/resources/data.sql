INSERT INTO role (id, name) VALUES
  (1, 'ROLE_ADMIN'),
  (2, 'ROLE_USER');

INSERT INTO user (id, login, password) VALUES
  (1, 'admin', '$2a$10$Xr0hIUi82rIno17jC6rBe.JCp7HfW5WrBQMlRzngIZJSHWqIoxJ76'),
  (2, 'user', '$2a$10$fsbYDvj3S.SVLD3eDiFniONNc6NNEGRoLbVJa7NQwv/oynGdeN7Ay');

INSERT INTO user_role (user_id, role_id) VALUES
  (1, 1),
  (2, 2);
