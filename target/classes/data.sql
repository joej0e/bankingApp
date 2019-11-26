INSERT INTO role (id, name) VALUES
  (1, 'ROLE_ADMIN'),
  (2, 'ROLE_USER');

INSERT INTO user (id, login, password) VALUES
  (1, 'admin', '$2a$10$Xr0hIUi82rIno17jC6rBe.JCp7HfW5WrBQMlRzngIZJSHWqIoxJ76'),
  (2, 'user', '$2a$10$fsbYDvj3S.SVLD3eDiFniONNc6NNEGRoLbVJa7NQwv/oynGdeN7Ay');

INSERT INTO user_role (user_id, role_id) VALUES
  (1, 1),
  (2, 2);

INSERT INTO account (id, balance, user_id) VALUES
  (1, 1000., 1),
  (2, 3000., 1),
  (3, 5000., 1);

INSERT INTO operation (id, account_id, sum, balance) VALUES
  (1, 1, -200., 800.),
  (2, 1, 300., 1100.),
  (3, 1, -90., 1010.),
  (4, 1, 150., 1160.);
